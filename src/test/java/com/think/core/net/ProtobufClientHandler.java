package com.think.core.net;

import com.google.protobuf.MessageLite;

import com.think.core.net.message.RequestWrapper;
import com.think.core.net.message.ResponseWrapper;
import com.think.core.net.security.EncryptionDecryption;
import com.think.protocol.Gps;
import com.think.test.protobuf.PersonProtos;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProtobufClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当channel就绪后，我们首先通过client发送一个数据。
        ctx.writeAndFlush(build());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();;
        ctx.close();
    }

    public ResponseWrapper build() {
        Gps.gps_data.Builder builder = Gps.gps_data.newBuilder();
        builder.setAltitude(1);
        builder.setDataTime("2017-12-17 16:21:44");
        builder.setGpsStatus(1);
        builder.setLat(39.123);
        builder.setLon(120.112);
        builder.setDirection(30.2F);

        short msgId = 100;
        ResponseWrapper wrapper = ResponseWrapper.newBuilder();
        wrapper.setResponseId(msgId);
        wrapper.setReal(true);
        wrapper.setPayload(builder.build());
        wrapper.setAccessTime(System.currentTimeMillis());

        return wrapper;
    }

}