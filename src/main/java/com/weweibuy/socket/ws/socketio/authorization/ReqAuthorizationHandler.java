package com.weweibuy.socket.ws.socketio.authorization;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * 请求权限处理
 *
 * @author durenhao
 * @date 2021/12/19 21:31
 **/
public class ReqAuthorizationHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest httpRequest) {

        } else {
            super.channelRead(ctx, msg);
        }
    }
}
