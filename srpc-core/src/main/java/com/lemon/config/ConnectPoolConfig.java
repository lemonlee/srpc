package com.lemon.config;

import java.net.InetSocketAddress;

/**
 * Created by lihuihua on 2019/1/26.
 */
public class ConnectPoolConfig {

    private int maxConnections;

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }
}
