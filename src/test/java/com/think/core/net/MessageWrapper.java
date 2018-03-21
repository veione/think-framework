package com.think.core.net;

import akka.protobuf.MessageLite;
import com.think.core.Session;

import java.io.Serializable;

public class MessageWrapper implements Serializable{
    public MessageLite request;
    public Session session;

}
