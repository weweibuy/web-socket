package com.weweibuy.socket.ws.socketio.protocol;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.ArrayList;
import java.util.List;

public class ChannelFutureList implements GenericFutureListener<Future<Void>> {

    private List<ChannelFuture> futureList = new ArrayList<ChannelFuture>();
    private ChannelPromise promise = null;

    private void cleanup() {
        promise = null;
        for (ChannelFuture f : futureList) f.removeListener(this);
    }

    private void validate() {
        boolean allSuccess = true;
        for (ChannelFuture f : futureList) {
            if (f.isDone()) {
                if (!f.isSuccess()) {
                    promise.tryFailure(f.cause());
                    cleanup();
                    return;
                }
            } else {
                allSuccess = false;
            }
        }
        if (allSuccess) {
            promise.trySuccess();
            cleanup();
        }
    }

    public void add(ChannelFuture f) {
        futureList.add(f);
        f.addListener(this);
    }

    public void setChannelPromise(ChannelPromise p) {
        promise = p;
        validate();
    }

    @Override
    public void operationComplete(Future<Void> voidFuture) throws Exception {
        if (promise != null) validate();
    }
}