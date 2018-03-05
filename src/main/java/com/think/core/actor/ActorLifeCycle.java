package com.think.core.actor;

/**
 * Actor生命周期接口类
 * 
 * @author veione
 *
 */
public interface ActorLifeCycle {
  /**
   * 在Actor启动之前触发
   */
  default void onBeforeStart() {}

  /**
   * 在Actor启动之后触发
   */
  default void onAfterStart() {}

  /**
   * 在Actor结束之前触发
   */
  default void onBeforeStop() {}

  /**
   * 在Actor结束之后触发
   */
  default void onAfterStop() {}

  /**
   * 在Actor重启之前触发
   */
  default void onPreBeforeReStart() {}

  /**
   * 在Actor重启之后触发
   */
  default void onPreAfterReStart() {}

  /**
   * 触发消息接口
   * 
   * @param msg
   */
  void onMessage(Object msg);
}
