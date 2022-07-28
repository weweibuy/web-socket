package com.weweibuy.socket.ws.socketio.handshake;

import com.weweibuy.socket.ws.socketio.model.SocketIoTransportType;
import com.weweibuy.socket.ws.socketio.protocol.SocketIoQueryParam;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import org.apache.commons.lang3.StringUtils;

/**
 * socket io polling请求握手处理
 * <p>
 * 包括开启连接(获取客户端id)
 * 名称空间连接
 * 名称空间批准
 *
 * @author durenhao
 * @date 2021/12/19 20:42
 **/
public class SocketIoPollHandshakeHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private SocketIoPollingReqHandler socketIoPollingReqHandler;

    @Override
    protected synchronized void channelRead0(ChannelHandlerContext ctx, FullHttpRequest httpRequest) throws Exception {
        SocketIoQueryParam socketIoQueryParam = SocketIoQueryParam.fromRequest(httpRequest);
        SocketIoTransportType socketIoTransport = SocketIoTransportType.fromTransport(socketIoQueryParam.getTransport());
        if (socketIoTransport == null || SocketIoTransportType.WEBSOCKET.equals(socketIoTransport)) {
            ctx.fireChannelRead(httpRequest.retain());
            return;
        }
        String clientId = socketIoQueryParam.getSid();
        HttpMethod method = httpRequest.method();
        Channel channel = ctx.channel();
        if (StringUtils.isBlank(clientId)) {
            String sid = socketIoPollingReqHandler.firstConnection(channel, httpRequest);
        }
    }
}
