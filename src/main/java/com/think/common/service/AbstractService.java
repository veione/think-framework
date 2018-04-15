package com.think.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 一个实现了的服务组件接口的抽象类
 *
 * @author Gavin
 */
public abstract class AbstractService implements Service {
    protected final Logger logger = LoggerFactory.getLogger(AbstractService.class);
    // 服务组件默认是空闲未运行状态
    private ServiceStatus status = ServiceStatus.IDLE;

    @Override
    public void start() {
        startService();
        this.status = ServiceStatus.STARTING;
        logger.debug("Service {} has starting.", this);
    }

    protected abstract void startService();

    @Override
    public void ready() {
        // 服务组件正确初始化完成,切换状态为运行状态
        this.status = ServiceStatus.RUNNING;
        logger.debug("Service {} has been ready.", this);
    }

    @Override
    public void pause() {
        this.status = ServiceStatus.PUASE;
        logger.debug("Service {} has been paused.", this);
    }


    public void resume() {
        this.status = ServiceStatus.RESUME;
        logger.debug("Service {} has been resume.", this);
    }

    @Override
    public void stop() {
        this.status = ServiceStatus.STOP;
        logger.debug("Service {} has been stop.", this);
    }
}
