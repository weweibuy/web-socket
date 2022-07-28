package com.weweibuy.socket.ws.socketio.namespace;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author durenhao
 * @date 2021/12/19 16:43
 **/
public class NameSpaceHelper {

    private static Map<String, Integer> clientState = new ConcurrentHashMap<>();


    public static void setStat(String sid, Integer stat) {
        clientState.put(sid, stat);
    }

    public static boolean hasApprovalNamespace(String sid) {
        return clientState.get(sid) != null;
    }


}
