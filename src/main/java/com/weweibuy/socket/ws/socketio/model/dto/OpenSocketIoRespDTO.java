package com.weweibuy.socket.ws.socketio.model.dto;

import com.weweibuy.socket.ws.socketio.model.SocketIoTransportType;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 开启Socket(客户端第一次请求) 响应
 *
 * @author durenhao
 * @date 2021/12/17 20:13
 **/
@Data
public class OpenSocketIoRespDTO {

    /**
     * 客户端id
     */
    private String sid;

    /**
     * 升级协议类型 {@link SocketIoTransportType}
     */
    private List<String> upgrades;

    /**
     * ping 间隔;  V4 EIO 由服务端 ping, 长时间没有ping,客户端将自动断开连接
     */
    private Integer pingInterval;

    /**
     * ping 超时;  V4 EIO 由服务端 ping
     */
    private Integer pingTimeout;

    public static OpenSocketIoRespDTO websocket(String sid) {
        OpenSocketIoRespDTO openSocketIoRespDTO = new OpenSocketIoRespDTO();
        openSocketIoRespDTO.setSid(sid);
        openSocketIoRespDTO.setUpgrades(Collections.singletonList(SocketIoTransportType.WEBSOCKET.getTransport()));
        openSocketIoRespDTO.setPingInterval(10000);
        openSocketIoRespDTO.setPingTimeout(5000);
        return openSocketIoRespDTO;
    }


}
