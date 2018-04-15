package com.think.common.timer;

import akka.actor.Cancellable;
import scala.concurrent.duration.FiniteDuration;

/**
 * 时间调度器
 *
 * @author Gavin
 */
public interface TimeManager {

    Cancellable schedule(FiniteDuration initialyDelay, FiniteDuration interval, Runnable task);


}
