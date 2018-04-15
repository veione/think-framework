package com.think.common.service;

/**
 * 服务组件接口
 *
 * @author Gavin
 */
public interface Service {
    /**
     * 启动事件
     */
    void start();

    /**
     * 当服务正确启动时通知事件
     */
    void ready();

    /**
     * 服务暂停运行
     */
    void pause();

    /**
     * 服务继续运行
     */
    void resume();

    /**
     * 停止服务
     */
    void stop();

    /**
     * 服务组件状态
     */
    enum ServiceStatus {
        IDLE,   //空闲未运行状态
        STARTING, //启动中状态,服务暂时不可用
        RUNNING, // 服务运行状态,此时服务可用
        PUASE, // 服务暂停状态,不可用
        RESUME, // 服务继续运行
        STOP; //服务停止状态,不可用
    }
}
