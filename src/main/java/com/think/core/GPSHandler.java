package com.think.core;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.think.protocol.Gps;

import java.util.HashMap;
import java.util.Map;

public class GPSHandler extends AbstractHandler<Gps.gps_data> {

    @Override
    public void onHandler() {
        this.request.getDataTime();
        System.out.println(this.request);
        System.out.println(this.session);
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        Map<Integer, AbstractHandler> handlerMap = new HashMap<>(1);
        handlerMap.put(100, new GPSHandler());

        Session session = new Session();
        session.nickName = "peter";
        session.userId = 10001;
        session.sessionId = 5327572893L;

        Gps.gps_data.Builder gps_builder = Gps.gps_data.newBuilder();
        gps_builder.setAltitude(1);
        gps_builder.setDataTime("2017-12-17 16:21:44");
        gps_builder.setGpsStatus(1);
        gps_builder.setLat(39.123);
        gps_builder.setLon(120.112);
        gps_builder.setDirection(30.2F);
        gps_builder.setId(100L);


        byte[] datas = gps_builder.build().toByteArray();
        GeneratedMessage message = null;



        if (100 == 100) {
            message = Gps.gps_data.parseFrom(datas);
        }
        AbstractHandler handler = handlerMap.get(100);
        handler.init(message, session);


        handler.onHandler();
        System.out.println();
    }
}
