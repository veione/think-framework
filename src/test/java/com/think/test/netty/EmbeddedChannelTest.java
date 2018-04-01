package com.think.test.netty;

import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;

import com.think.core.net.codec.ProtocolDecoder;
import com.think.core.net.codec.ProtocolEncoder;
import com.think.core.net.handler.SocketMessageHandler;
import com.think.core.net.message.RequestWrapper;
import com.think.core.net.message.ResponseWrapper;
import com.think.core.net.security.EncryptionDecryption;
import com.think.protocol.Gps;
import com.think.service.HandlerManager;
import com.think.service.MessageManager;

import org.junit.Test;

import java.util.List;
import java.util.zip.Adler32;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class EmbeddedChannelTest {

    @Test
    public void testEmbeddedChannel() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 3; i++) {
            buf.writeInt(i);
        }

        EmbeddedChannel channel = new EmbeddedChannel();

        // 获取channelPipeLine
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new SimpleChannelInBoundHandlerTest());
        pipeline.addFirst(new DecoderTest());


        // 写入测试数据
        channel.writeInbound(buf);

        System.out.println("embeddedChannel readInbound:" + channel.readInbound());
        System.out.println("embeddedChannel readInbound:" + channel.readInbound());
        System.out.println("embeddedChannel readInbound:" + channel.readInbound());
    }

    // 解码器
    class DecoderTest extends ByteToMessageDecoder {
        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
            if (in.readableBytes() >= 4) {
                out.add(in.readInt());
            }
        }
    }

    // channelHandler
    class SimpleChannelInBoundHandlerTest extends SimpleChannelInboundHandler {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
            System.out.println("Received message = [" + msg + "]");
            System.out.println("Received finished!");
            ctx.fireChannelRead(msg);
        }
    }

    /***
     * writeInbound(Object...msgs) : 将入站消息写入到EmbeddedChannel中
     * readInbound() : 从EmbeddedChannel中读取一个入站消息,任何返回的消息都穿过了整个ChannelPipeline
     * writeOutbound(Object...msgs) : 将出站消息写入到EmbeddedChannel中
     * readOutbound() : 从EmbeddedChannel中读取一个出站消息,任何返回的消息都穿过了整个ChannelPipeline
     */
    @Test
    public void testProtobufProtocol() throws Exception {
        HandlerManager.load("com.think");
        MessageManager.load("com.think");
        EmbeddedChannel channel = new EmbeddedChannel();
        ChannelPipeline pipeline = channel.pipeline();
        //pipeline.addLast(new SimpleProtobufChannel());
        //pipeline.addFirst(new SimpleProtobufDecoder());
//        pipeline.addLast(new ProtocolDecoder(true));
//        pipeline.addLast(new ProtocolEncoder(true));
//        pipeline.addLast(new SimpleProtobufChannel());

        // Decoders
        //pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
        pipeline.addLast("protobufDecoder", new ProtocolDecoder(true));
        // Encoder
        //pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
        pipeline.addLast("protobufEncoder", new ProtocolEncoder(true));
        // Handler
        pipeline.addLast(new SimpleProtobufChannel());

        Gps.gps_data.Builder builder = Gps.gps_data.newBuilder();
        builder.setAltitude(1);
        builder.setDataTime("2017-12-17 16:21:44");
        builder.setGpsStatus(1);
        builder.setLat(39.123);
        builder.setLon(120.112);
        builder.setDirection(30.2F);

        EncryptionDecryption dec = new EncryptionDecryption();
        ByteBuf buf = Unpooled.buffer();
        short msgId = 100;
        byte[] msg = dec.encrypt(builder.build().toByteArray());
        //byte[] msg = builder.build().toByteArray();
        buf.writeShort(RequestWrapper.MAGIC_WORD);
        buf.writeShort(1);
        buf.writeShort(msgId);
        buf.writeInt(msg.length);
        buf.writeBytes(msg);

        ByteBuf buf2 = Unpooled.buffer();
        buf2.writeShort(RequestWrapper.MAGIC_WORD);
        buf2.writeShort(1);
        buf2.writeShort(msgId);
        buf2.writeInt(msg.length);
        buf2.writeBytes(msg);


        ByteBuf buf3 = Unpooled.buffer();
        buf3.writeShort(RequestWrapper.MAGIC_WORD);
        buf3.writeShort(1);
        buf3.writeShort(msgId);
        buf3.writeInt(msg.length);
        buf3.writeBytes(msg);
        channel.writeInbound(buf);
        channel.writeInbound(buf2);
        channel.writeInbound(buf3);

        ResponseWrapper response = ResponseWrapper.newBuilder();
        response.setPayload(builder.build());
        response.setResponseId((short) 100);
        //channel.writeOutbound(response);

    }

    class SimpleProtobufChannel extends SimpleChannelInboundHandler<List<RequestWrapper>> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, List<RequestWrapper> msg) {
            msg.stream().forEach(m -> {

                MessageLite message = m.getPayload();
                HandlerManager.execute(m.getRequestId(), null, message);
                // System.out.println("Received message = [" + message + "]");
                System.out.println("Received finished!");
            });
        }
    }

    @Test
    public void testChecksum() {
        ByteBuf buf = Unpooled.buffer();
        Gps.gps_data.Builder builder = Gps.gps_data.newBuilder();
        builder.setAltitude(1);
        builder.setDataTime("2017-12-17 16:21:44");
        builder.setGpsStatus(1);
        builder.setLat(39.123);
        builder.setLon(120.112);
        builder.setDirection(30.2F);
        buf.writeBytes(builder.build().toByteArray());

        boolean flag = checksum(buf);
        System.out.println("flag = " + flag);

        long checksum = calcAdler32CheckSum("hello", ByteString.copyFromUtf8("hello"));
        System.out.println("checksum = " + checksum);
        Adler32 adler32 = new Adler32();
        adler32.update(buf.nioBuffer());
        long sum = adler32.getValue();
        System.out.println("sum = " + sum);
    }

    private boolean checksum(ByteBuf buf) {
        Adler32 adler32 = new Adler32();
        adler32.update(buf.array(), buf.arrayOffset() + buf.readerIndex(), buf.readableBytes() - 4);
        buf.markReaderIndex();
        buf.readerIndex(buf.writerIndex() - 4);
        int checksum = buf.readInt();
        buf.resetReaderIndex();
        return checksum == (int) adler32.getValue();
    }

    public long calcAdler32CheckSum(String name, ByteString body) {
        Adler32 checksum = new Adler32();
        checksum.update(name.getBytes());
        checksum.update(body.toByteArray());
        return checksum.getValue();
    }
}
