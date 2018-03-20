package com.think.core.actor;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import akka.actor.Cancellable;
import akka.actor.UntypedAbstractActor;
import scala.concurrent.duration.Duration;

/**
 * 线程安全Actor抽象类
 * 
 * @author veione
 *
 */
public abstract class ThreadSafeActor extends UntypedAbstractActor implements ActorLifeCycle {
	protected Cancellable tickManager;
	protected Duration initialDelay = Duration.create(0, TimeUnit.SECONDS);
	protected Duration interval = Duration.create(20, TimeUnit.MICROSECONDS);
	
	@Override
	public void onReceive(Object msg) throws Throwable {
		onMessage(msg);
	}

	@Override
	public void preStart() throws Exception {
		onBeforeStart();
		
		onAfterStart();
	}

	@Override
	public void preRestart(Throwable reason, Optional<Object> msg) throws Exception {
		super.preRestart(reason, msg);
	}

	@Override
	public void postRestart(Throwable reason) throws Exception {
		super.postRestart(reason);
	}

	@Override
	public void postStop() throws Exception {
		onBeforeStop();
		super.postStop();
		onAfterStop();
	}
}
