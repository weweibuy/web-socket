package com.weweibuy.socket.ws.socketio.protocol;

import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.Data;

import java.util.Map;

/**
 * socket io 请求参数
 *
 * @author durenhao
 * @date 2021/12/16 22:38
 **/
@Data
public class SocketIoQueryParam {

    /**
     * eio 版本
     */
    private String eio;

    /**
     * 传输类型
     */
    private String transport;

    /**
     * 客户端id
     */
    private String sid;


    public static SocketIoQueryParam fromRequest(FullHttpRequest fullHttpRequest) {
        String uri = fullHttpRequest.uri();
        Map<String, String> queryMap = HttpRequestUtils.parseQueryParamsToMap(uri);
        SocketIoQueryParam socketIoQueryParam = new SocketIoQueryParam();
        socketIoQueryParam.setEio(queryMap.get("EIO"));
        socketIoQueryParam.setTransport(queryMap.get("transport"));
        socketIoQueryParam.setSid(queryMap.get("sid"));
        return socketIoQueryParam;
    }

}
