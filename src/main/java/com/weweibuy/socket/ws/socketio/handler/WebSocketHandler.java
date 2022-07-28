package com.weweibuy.socket.ws.socketio.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;

/**
 * socket.io 握手处理
 *
 * @author durenhao
 * @date 2021/12/16 22:27
 **/
@Slf4j
public class WebSocketHandler extends WebSocketServerProtocolHandler {

    private WebSocketServerProtocolConfig socketServerProtocolConfig;

    public WebSocketHandler(WebSocketServerProtocolConfig serverConfig) {
        super(serverConfig);
        this.socketServerProtocolConfig = serverConfig;
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        ChannelPipeline cp = ctx.pipeline();
        if (cp.get(CustomWebSocketServerProtocolHandshakeHandler.class) == null) {
            // Add the WebSocketHandshakeHandler before this one.
            cp.addBefore(ctx.name(), CustomWebSocketServerProtocolHandshakeHandler.class.getName(),
                    new CustomWebSocketServerProtocolHandshakeHandler(socketServerProtocolConfig));
        }
        if (socketServerProtocolConfig.decoderConfig().withUTF8Validator() && cp.get(Utf8FrameValidator.class) == null) {
            // Add the UFT8 checking before this one.
            cp.addBefore(ctx.name(), Utf8FrameValidator.class.getName(),
                    new Utf8FrameValidator());
        }
    }

    /**
     * Gets called if an user event was triggered.
     */
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        // websocket 握手完成
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            log.info("webSocket 握手完成");
            // 保证过长的消息可以整合和一个完整的消息
            ctx.pipeline().addBefore(WebSocketHandler.class.getName(),
                    WebSocketFrameAggregator.class.getName(),
                    new WebSocketFrameAggregator(Integer.MAX_VALUE));
        }

    }

}
