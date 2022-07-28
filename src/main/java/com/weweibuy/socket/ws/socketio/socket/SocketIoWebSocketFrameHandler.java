package com.weweibuy.socket.ws.socketio.socket;

import com.weweibuy.framework.common.core.concurrent.LogExceptionThreadFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author durenhao
 * @date 2021/12/17 21:25
 **/
@Slf4j
public class SocketIoWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final ScheduledExecutorService pingExecutorService = new ScheduledThreadPoolExecutor(1,
            new LogExceptionThreadFactory("ping-schedule-"),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        Channel channel = ctx.channel();
        String text = msg.text();
        TextWebSocketFrame textWebSocketFrame = null;
        if ("2probe".equals(text)) {
            textWebSocketFrame = new TextWebSocketFrame("3probe");
        } else if ("2".equals(text)) {
            textWebSocketFrame = new TextWebSocketFrame("3");
        } else if ("5".equals(text)) {
            pingExecutorService.scheduleAtFixedRate(() -> ping(ctx), 2, 5, TimeUnit.SECONDS);
            return;
        } else if ("3".equals(text)) {
            return;
        } else if (text.startsWith("42")) {
            textWebSocketFrame = new TextWebSocketFrame("42[\"chatevent\",{\"userName\":\"user109\",\"message\":\"111\"}]");
        } else {
            textWebSocketFrame = new TextWebSocketFrame("6");
        }
        ChannelFuture channelFuture = channel.writeAndFlush(textWebSocketFrame);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("websocket, 通道关闭");
    }

    private void ping(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(new TextWebSocketFrame("2"));
    }

}
