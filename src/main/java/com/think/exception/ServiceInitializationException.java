package com.think.exception;

/**
 * 服务初始化异常
 *
 * @author Gavin
 */
public class ServiceInitializationException extends RuntimeException {
    public ServiceInitializationException() {
    }

    public ServiceInitializationException(String message) {
        super(message);
    }

    public ServiceInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
