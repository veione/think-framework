package com.think.common.timer;

/**
 * 定时轮是一种数据结构，其主体是一个循环列表(circular buffer),每个列表中包含了一个称之为槽(slot)的结构。
 * 至于slot的具体结构依赖于具体应用场景。
 *
 * 定时轮的工作原理可以类比于始终,某一个方向按固定频率轮动，每一次跳动称之为一个tick。
 * 这样可以看出定时轮由3个重要的属性参数，ticksPerWheel(一轮的tick数),tickDuration(一个tick的持续时间)
 * 以及timeUnit(时间单位),例如 当ticksPerWheel=60, tickDuration=1,timeUnit=秒,这就和现实的秒钟完成类似了。
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A timing-wheel optimized for approximated I/O timeout scheduling.<br>
 * {@link TimingWheel} creates a new thread whenever it is instantiated and started, so don't create many instances.
 * <p>
 * <b>The classic usage as follows:</b><br>
 * <li>using timing-wheel manage any object timeout</li>
 * <pre>
 *    // Create a timing-wheel with 60 ticks, and every tick is 1 second.
 *    private static final TimingWheel<CometChannel> TIMING_WHEEL = new TimingWheel<CometChannel>(1, 60, TimeUnit.SECONDS);
 *
 *    // Add expiration listener and start the timing-wheel.
 *    static {
 *      TIMING_WHEEL.addExpirationListener(new YourExpirationListener());
 *      TIMING_WHEEL.start();
 *    }
 *
 *    // Add one element to be timeout approximated after 60 seconds
 *    TIMING_WHEEL.add(e);
 *
 *    // Anytime you can cancel count down timer for element e like this
 *    TIMING_WHEEL.remove(e);
 * </pre>
 *
 * After expiration occurs, the {@link ExpirationListener} interface will be invoked and the expired object will be
 * the argument for callback method {@link ExpirationListener#expired(Object)}
 * <p>
 * {@link TimingWheel} is based on <a href="http://cseweb.ucsd.edu/users/varghese/">George Varghese</a> and Tony Lauck's paper,
 * <a href="http://cseweb.ucsd.edu/users/varghese/PAPERS/twheel.ps.Z">'Hashed and Hierarchical Timing Wheels: data structures
 * to efficiently implement a timer facility'</a>.  More comprehensive slides are located <a href="http://www.cse.wustl.edu/~cdgill/courses/cs6874/TimingWheels.ppt">here</a>.
 */
public class TimingWheel<E> {
    /**
     * 每次走动的精度 tickDuration(一个tick的持续时间)
     */
    private long tickDuration;
    /**
     * 每次需要转多少圈1(1轮的tick数)
     */
    private int ticksPerWheel;
    /**
     * 当前指针的位置
     */
    private volatile int currentTickIndex = 0;
    /**
     * 写时复制任务超时监听器列表
     */
    private CopyOnWriteArrayList<ExpirationListener<E>> expirationListeners = new CopyOnWriteArrayList<>();
    /**
     * 时间轮
     */
    private ArrayList<TimeSlot<E>> wheel;
    /**
     * 指示器
     */
    private Map<E, TimeSlot<E>> indicator = new ConcurrentHashMap<>();
    /**
     * 当前TimingWheel的运行状态
     */
    private AtomicBoolean status = new AtomicBoolean(false);
    /**
     * 读写锁
     */
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     * 工作线程
     */
    private Thread workThread;

    public TimingWheel(int ticksPerWheel, int tickDuration, TimeUnit timeUnit) {
        Objects.requireNonNull(timeUnit);

        if (ticksPerWheel <= 0 || tickDuration <= 0) {
            throw new IllegalArgumentException("Invalid ticksPerWheel or tickDuration, must be greater than 0");
        }

        this.wheel = new ArrayList<>();
        this.ticksPerWheel = ticksPerWheel;
        this.tickDuration = TimeUnit.MILLISECONDS.convert(tickDuration, timeUnit);

        for (int i = 0; i < this.ticksPerWheel; i++) {
            wheel.add(new TimeSlot<>(i));
        }

        wheel.trimToSize();

        workThread = new Thread(new TickWorker(), "wheel-timer");
    }

    public void start() {
        if (status.get()) {
            throw new IllegalStateException("The timer wheel is stopped.");
        }

        if (!workThread.isAlive()) {
            workThread.start();
        }
    }

    public boolean stop() {
        if (!status.compareAndSet(false, true)) {
            return false;
        }
        boolean interrupted = false;
        while (workThread.isAlive()) {
            workThread.interrupt();
            try {
                workThread.join(1000);
            } catch (Exception e) {
                interrupted = false;
            }
        }

        if (interrupted) {
            Thread.currentThread().interrupt();
        }

        return true;
    }

    public void addExpirationListener(ExpirationListener<E> listener) {
        expirationListeners.add(listener);
    }

    public void removeExpirationListener(ExpirationListener<E> listener) {
        expirationListeners.remove(listener);
    }

    public long add(E e) {
        synchronized (e) {
            checkAdd(e);
            int previousTickIndex = getPreviousTickIndex();
            System.out.println("放入索引 = [" + previousTickIndex + "]");
            TimeSlot<E> timeSlot = wheel.get(previousTickIndex);
            timeSlot.add(e);
            indicator.put(e, timeSlot);
            return (ticksPerWheel - 1) * tickDuration;
        }
    }

    public boolean remove(E e) {
        synchronized (e) {
            TimeSlot<E> timeSlot = indicator.get(e);
            if (timeSlot == null) {
                return false;
            }

            indicator.remove(e);
            return timeSlot.remove(e);
        }
    }

    public void notifyExpired(int index) {
        TimeSlot<E> timeSlot = wheel.get(index);
        Set<E> elements = timeSlot.getElements();
        for (E e : elements) {
            timeSlot.remove(e);
            synchronized (e) {
                TimeSlot<E> latestSlot = indicator.get(e);
                if (latestSlot.equals(timeSlot)) {
                    indicator.remove(e);
                }
            }

            for (ExpirationListener<E> listener : expirationListeners) {
                System.out.println("过期索引 = [" + index + "]");
                listener.expired(e);
            }
        }
    }

    private int getPreviousTickIndex() {
        lock.readLock().lock();
        try {
            int curIndex = currentTickIndex;
            if (curIndex == 0) {
                return ticksPerWheel - 1;
            }
            return curIndex - 1;
        } catch (Exception e) {

        } finally {
            lock.readLock().unlock();
        }

        return currentTickIndex - 1;
    }

    private void checkAdd(E e) {
        TimeSlot<E> slot = indicator.get(e);
        if (slot != null) {
            slot.remove(e);
        }
    }

    private class TickWorker implements Runnable {
        /**
         * 启动时间
         */
        private long startTime;
        /**
         * 运行次数
         */
        private long tick = 1L;

        @Override
        public void run() {
            startTime = System.currentTimeMillis();
            tick = 1;
            for (int i = 0; !status.get(); i++) {
                if (i == wheel.size()) {
                    i = 0;
                }

                lock.writeLock().lock();
                try {
                    currentTickIndex = i;
                } finally {
                    lock.writeLock().unlock();
                }

                notifyExpired(currentTickIndex);
                waitForNextTick();
            }
        }

        private void waitForNextTick() {
            for (; ; ) {
                long currentTime = System.currentTimeMillis();
                long sleepTime = tickDuration * tick - (currentTime - startTime);
                if (sleepTime <= 0) {
                    break;
                }

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {

                } finally {
                    break;
                }
            }
        }
    }
}
