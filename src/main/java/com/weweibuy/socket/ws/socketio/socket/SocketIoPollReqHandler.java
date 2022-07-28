package com.weweibuy.socket.ws.socketio.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weweibuy.framework.common.core.utils.IdWorker;
import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import com.weweibuy.socket.ws.socketio.model.EioMessageType;
import com.weweibuy.socket.ws.socketio.model.SocketIoTransportType;
import com.weweibuy.socket.ws.socketio.model.dto.OpenSocketIoRespDTO;
import com.weweibuy.socket.ws.socketio.namespace.NameSpaceHelper;
import com.weweibuy.socket.ws.socketio.protocol.SocketIoQueryParam;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author durenhao
 * @date 2021/12/18 11:20
 **/
@Slf4j
public class SocketIoPollReqHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {

        SocketIoQueryParam socketIoQueryParam = SocketIoQueryParam.fromRequest(fullHttpRequest);
        SocketIoTransportType socketIoTransport = SocketIoTransportType.fromTransport(socketIoQueryParam.getTransport());
        if (socketIoTransport == null || SocketIoTransportType.WEBSOCKET.equals(socketIoTransport)) {
            ctx.fireChannelRead(fullHttpRequest.retain());
            return;
        }
        HttpMethod method = fullHttpRequest.method();
        String sid = socketIoQueryParam.getSid();
        Channel channel = ctx.channel();
        FullHttpResponse response = null;

        if (StringUtils.isNotBlank(sid) && NameSpaceHelper.hasApprovalNamespace(sid)) {
            // polling 获取数据请求
            response = noopResponse(channel);
        }else if (StringUtils.isBlank(sid)) {
            sid = IdWorker.nextStringId();
            // 第一次 建立连接
            response = openSocketResponse(sid, channel);
        } else if (StringUtils.isNotBlank(sid) && HttpMethod.POST.equals(method)) {
            // 第二次 请求连接Namespace
            response = requestNameSpaceResponse(channel);
        } else if (StringUtils.isNotBlank(sid) && HttpMethod.GET.equals(method)) {
            // 第三次  Namespace 建立批准
            response = approvalNameSpaceResponse(fullHttpRequest, channel);
            NameSpaceHelper.setStat(sid, 1);
        } else {
            pingResponse(channel);
        }
        log.info("poll 请求");
        channel.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private FullHttpResponse pingResponse(Channel channel) {
        ByteBuf byteBuf = byteBuf(channel);
        byteBuf.writeByte(toChar(EioMessageType.NOOP.getCode()));
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, HttpResponseStatus.OK, byteBuf);
        responseHeader(response, channel);
        return response;
    }

    private FullHttpResponse approvalNameSpaceResponse(FullHttpRequest fullHttpRequest, Channel channel) throws IOException {
        String namespace = fullHttpRequest.content().toString(StandardCharsets.UTF_8);

        ByteBuf byteBuf = byteBuf(channel);
        byteBuf.writeByte(toChar(EioMessageType.MESSAGE.getCode()));
        byteBuf.writeByte(toChar(EioMessageType.OPEN.getCode()));

        Map<String, String> map = new HashMap<>();
        map.put("sid", IdWorker.nextStringId());

        ByteBufOutputStream out = new ByteBufOutputStream(byteBuf);
        ObjectMapper camelCaseMapper = JackJsonUtils.getCamelCaseMapper();

        camelCaseMapper.writeValue((OutputStream) out, map);
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, HttpResponseStatus.OK, byteBuf);
        responseHeader(response, channel);
        return response;
    }


    private ByteBuf byteBuf(Channel channel) {
        ByteBufAllocator alloc = channel.alloc();
        return allocateBuffer(alloc);
    }

    private FullHttpResponse noopResponse(Channel channel) {
        ByteBuf byteBuf = byteBuf(channel);
        byteBuf.writeBytes("6".getBytes(StandardCharsets.UTF_8));
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, HttpResponseStatus.OK, byteBuf);
        responseHeader(response, channel);
        return response;
    }


    private FullHttpResponse requestNameSpaceResponse(Channel channel) {
        ByteBuf byteBuf = byteBuf(channel);
        byteBuf.writeBytes("ok".getBytes(StandardCharsets.UTF_8));
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, HttpResponseStatus.OK, byteBuf);
        responseHeader(response, channel);
        return response;
    }


    private FullHttpResponse openSocketResponse(String sid, Channel channel) throws IOException {
        ByteBuf byteBuf = byteBuf(channel);
        byteBuf.writeByte(toChar(EioMessageType.OPEN.getCode()));
        OpenSocketIoRespDTO socketIoRespDTO = OpenSocketIoRespDTO.websocket(sid);

        ObjectMapper camelCaseMapper = JackJsonUtils.getCamelCaseMapper();
        ByteBufOutputStream out = new ByteBufOutputStream(byteBuf);
        camelCaseMapper.writeValue((OutputStream) out, socketIoRespDTO);

        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, HttpResponseStatus.OK, byteBuf);
        responseHeader(response, channel);
        return response;
    }

    private void responseHeader(FullHttpResponse response, Channel channel) {
        response.headers().add(HttpHeaderNames.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                .add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

        AttributeKey<String> originAttrKey = AttributeKey.valueOf("origin");

        String origin = channel.attr(originAttrKey).get();

        addOriginHeaders(origin, response);
    }


    private void addOriginHeaders(String origin, HttpResponse res) {
        if (origin != null) {
            res.headers().add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
            res.headers().add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, Boolean.TRUE);
        } else {
            res.headers().add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        }
    }

    public ByteBuf allocateBuffer(ByteBufAllocator allocator) {
        // TODO 根据配置选择堆外还是堆内
        return allocator.heapBuffer();
    }

    private byte toChar(int number) {
        return (byte) (number ^ 0x30);
    }

}
