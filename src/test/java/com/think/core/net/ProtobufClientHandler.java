package com.think.core.net;

import com.google.protobuf.MessageLite;

import com.think.test.protobuf.PersonProtos;

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

        PersonProtos.Person person = (PersonProtos.Person)msg;
        System.out.println(person.getEmail());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();;
        ctx.close();
    }

    public MessageLite build() {
        PersonProtos.Person.Builder personBuilder = PersonProtos.Person.newBuilder();
        personBuilder.setEmail("lisi@gmail.com");
        personBuilder.setId(1000);
        PersonProtos.Person.PhoneNumber.Builder phone = PersonProtos.Person.PhoneNumber.newBuilder();
        phone.setNumber("18610000000");

        personBuilder.setName("李四");
        personBuilder.addPhone(phone.build());

        return personBuilder.build();
    }

}