package com.think.core.actor;

import java.util.Optional;

import akka.actor.UntypedAbstractActor;

/**
 * 线程安全Actor抽象类
 * 
 * @author veione
 *
 */
public abstract class ThreadSafeActor extends UntypedAbstractActor implements ActorLifeCycle {

  @Override
  public void onReceive(Object msg) throws Throwable {
    onMessage(msg);
  }

  @Override
  public void preStart() throws Exception {
    onBeforeStart();
    super.preStart();
    onAfterStart();
  }

  @Override
  public void preRestart(Throwable reason, Optional<Object> msg) throws Exception {
    onPreAfterReStart();
    super.preRestart(reason, msg);
    onPreBeforeReStart();
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
