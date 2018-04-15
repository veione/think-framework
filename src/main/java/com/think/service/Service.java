package com.think.service;

public interface Service {
    String getName();

    void ready() throws Exception;

    void shutdown();
}
