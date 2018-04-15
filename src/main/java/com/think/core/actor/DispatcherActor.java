package com.think.core.actor;

import com.think.core.Session;
import com.think.core.net.message.RequestWrapper;
import com.think.service.HandlerManager;
import com.think.service.SessionManager;

import java.util.List;

import akka.actor.Props;

public class DispatcherActor extends ThreadSafeActor {
    @Override
    public void onMessage(Object msg) {
        if (msg instanceof List) {
            List<RequestWrapper> requestList = (List<RequestWrapper>) msg;
            handleMessage(requestList);
        }
    }

    /**
     * 处理来自客户端的消息
     */
    private void handleMessage(List<RequestWrapper> requestList) {
        requestList.stream().forEach(m -> {
            Session session = SessionManager.getInstance().getSession(m.getChannel());
            HandlerManager.execute(m.getRequestId(), session, m.getPayload());
        });
    }

    public static Props props() {
        return Props.create(DispatcherActor.class);
    }
}
