package com.think.core.net;

import com.google.protobuf.Message;

public class ResponseWrapper {
    private int responseType;
    private Message response;
    private long startTime;

    public ResponseWrapper() {
        this.startTime = System.currentTimeMillis();
    }

    public ResponseWrapper(int responseType, Message response) {
        this.responseType = responseType;
        this.response = response;
        this.startTime = System.currentTimeMillis();
    }

    public void setResponse(Message response) {
        this.response = response;
    }

    public Message getResponse() {
        return response;
    }

    public void setResponseType(int responseType) {
        this.responseType = responseType;
    }

    public int getResponseType() {
        return responseType;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getMsgSize() {
        if (this.response != null) {
            return this.response.getSerializedSize();
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Type=").append(this.responseType).append(" Content=").append(this.response);
        return builder.toString();
    }

    public static ResponseWrapper newBuilder() {
        return new ResponseWrapper();
    }

    public byte[] getResponseBody() {
        if (this.response != null) {
            return this.response.toByteArray();
        } else {
            return new byte[0];
        }
    }
}
