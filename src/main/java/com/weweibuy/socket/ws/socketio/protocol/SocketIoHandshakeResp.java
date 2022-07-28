package com.weweibuy.socket.ws.socketio.protocol;

import lombok.Data;

/**
 * @author durenhao
 * @date 2021/12/16 23:41
 **/
@Data
public class SocketIoHandshakeResp {

    private Integer type;

    private HandshakeRespData data;

    @Data
    public static class HandshakeRespData{

        private String sid;

    }

}
