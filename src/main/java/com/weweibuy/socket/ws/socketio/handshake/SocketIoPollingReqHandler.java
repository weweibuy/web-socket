package com.weweibuy.socket.ws.socketio.handshake;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author durenhao
 * @date 2021/12/19 21:55
 **/
public interface SocketIoPollingReqHandler {

    String firstConnection(Channel channel, FullHttpRequest fullHttpRequest);

    String namespaceRequest(String namespace, Channel channel, FullHttpRequest fullHttpRequest);

    String namespaceApproval(String sid, Channel channel, FullHttpRequest fullHttpRequest);


}
