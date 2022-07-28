package com.weweibuy.socket.ws.socketio.handshake;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.websocketx.CustomWebSocketServerProtocolHandshakeHandler;
import io.netty.handler.codec.http.websocketx.Utf8FrameValidator;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolConfig;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * websocket 握手处理
 *
 * @author durenhao
 * @date 2021/12/16 22:27
 **/
public class SocketIoHandshakeWebSocketServerProtocolHandler extends WebSocketServerProtocolHandler {

    private WebSocketServerProtocolConfig socketServerProtocolConfig;

    public SocketIoHandshakeWebSocketServerProtocolHandler(WebSocketServerProtocolConfig serverConfig) {
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
}
