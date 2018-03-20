package com.think.core.net;

import com.google.protobuf.MessageLite;

import com.think.test.protobuf.PersonProtos;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProtobufServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PersonProtos.Person person = (PersonProtos.Person) msg;
        //经过pipeline的各个decoder，到此Person类型已经可以断定
        System.out.println(person.getEmail());
        ChannelFuture future = ctx.writeAndFlush(build());
        //发送数据之后，我们手动关闭channel，这个关闭是异步的，当数据发送完毕后执行。
        future.addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 构建一个Protobuf实例，测试
     */
    public MessageLite build() {
        PersonProtos.Person.Builder personBuilder = PersonProtos.Person.newBuilder();
        personBuilder.setEmail("zhangsan@gmail.com");
        personBuilder.setId(1000);
        PersonProtos.Person.PhoneNumber.Builder phone = PersonProtos.Person.PhoneNumber.newBuilder();
        phone.setNumber("18610000000");

        personBuilder.setName("张三");
        personBuilder.addPhone(phone.build());

        return personBuilder.build();
    }

}