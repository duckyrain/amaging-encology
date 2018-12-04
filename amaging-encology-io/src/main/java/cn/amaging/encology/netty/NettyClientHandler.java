package cn.amaging.encology.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

/**
 * Created by DuQiyu on 2018/11/19 9:59.
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private String message;

    public NettyClientHandler(String message) {
        this.message = message;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (null == message || message.trim().length() == 0) {
            return;
        }
        byte[] req = message.getBytes();
        ByteBuf writeBuffer = Unpooled.buffer(req.length);
        writeBuffer.writeBytes(req);
        ctx.writeAndFlush(writeBuffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        byte[] reader = new byte[in.readableBytes()];
        in.readBytes(reader);
        String message = new String(reader, StandardCharsets.UTF_8);
        System.out.println(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
