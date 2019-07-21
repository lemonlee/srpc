package com.lemon.transport;

import com.lemon.codecs.Codec;
import com.lemon.config.ConnectPoolConfig;
import com.lemon.core.Request;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lihuihua on 2019/1/29.
 */
public class DefaultCluster implements Cluster  {

    private List<Server> servers;
    private boolean async;

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    @Override
    public List<Server> getServers() {
        return servers;
    }

    @Override
    public boolean isAsync() {
        return async;
    }


    public Server getServer() {
        return servers.get(0);
    }

    @Override
    public Object call(Request request) throws InterruptedException {
        if(!isAsync()){
            return getServer().getClient().invokeSync(request);
        }else{
            return null;
        }

    }
}
