package com.think.handler;

import com.think.common.annotation.Handler;
import com.think.common.annotation.Mapping;
import com.think.core.net.message.ResponseWrapper;
import com.think.core.net.session.Session;
import com.think.protocol.GPSDataProto.gps_data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Handler(desc = "商店处理器")
public class StoreHandler {
    private final Logger logger = LoggerFactory.getLogger(StoreHandler.class);

    @Mapping(requestId = 100, msg = gps_data.class, desc = "处理GPS")
    public void handleGPSData(Session session, gps_data request) {
        logger.info("session = [{}], request = [{}]", session, request);

        ResponseWrapper response = ResponseWrapper.newBuilder();
        response.setAccessTime(System.currentTimeMillis());
        response.setPayload(request);
        response.setReal(true);
        response.setSession(session);
        response.setResponseId((short) 100);

        session.send(response);
    }
}
