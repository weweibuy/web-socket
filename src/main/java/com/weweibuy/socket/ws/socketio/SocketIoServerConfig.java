package com.weweibuy.socket.ws.socketio;

import lombok.Data;

/**
 * @author durenhao
 * @date 2021/12/16 22:07
 **/
@Data
public class SocketIoServerConfig {

    private Integer port = 9092;

    private Integer bossThreadNum = 4;

    private Integer workThreadNum = 4;




}
