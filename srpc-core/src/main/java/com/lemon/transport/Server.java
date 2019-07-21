package com.lemon.transport;

import java.net.InetSocketAddress;

/**
 * Created by lihuihua on 2019/1/26.
 */
public class Server {
    private String serviceName;
    private String ip;
    private int port;
    private InetSocketAddress remoteAddress;
    private NettyClient client;

    public NettyClient getClient() {
        return client;
    }

    public void setClient(NettyClient client) {
        this.client = client;
    }



    public InetSocketAddress getRemoteAddress() {
        if(remoteAddress == null){
            remoteAddress = new InetSocketAddress(ip, port);
        }
        return remoteAddress;
    }

    public void setRemoteAddress(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
