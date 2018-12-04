package cn.amaging.encology.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

/**
 * Created by DuQiyu on 2018/11/19 9:58.
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        byte[] reader = new byte[in.readableBytes()];
        in.readBytes(reader);
        String message = new String(reader, StandardCharsets.UTF_8);
        System.out.println(message);
        ctx.writeAndFlush(handle(message));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private ByteBuf handle(String message) {
        // handle client message here
        return Unpooled.copiedBuffer("[Server] Hi, i'm server. i received your message.".getBytes());
    }
}
