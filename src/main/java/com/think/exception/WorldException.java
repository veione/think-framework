package com.think.exception;

/**
 * 游戏世界异常基类
 *
 * @author Gavin
 */
public class WorldException extends Exception {
    public WorldException() {
    }

    public WorldException(String message) {
        super(message);
    }

    public WorldException(String message, Throwable cause) {
        super(message, cause);
    }
}