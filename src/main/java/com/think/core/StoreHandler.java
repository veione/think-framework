package com.think.core;

import com.think.core.annotation.Handler;
import com.think.core.annotation.Mapping;
import com.think.protocol.GPSDataProto.gps_data;


@Handler(desc = "商店处理器")
public class StoreHandler {

    @Mapping(requestId = 100, msg = gps_data.class, desc = "处理GPS")
    public void handleGPSData(RequestContext context, gps_data request) {
        System.out.println("context = [" + context + "], request = [" + request + "]");
        System.out.println("context = [" + request.getDataTime() + "], request = [" + request.getDirection() + "]");
    }
}
