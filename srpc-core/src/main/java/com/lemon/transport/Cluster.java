package com.lemon.transport;

import com.lemon.codecs.Codec;
import com.lemon.config.ConnectPoolConfig;
import com.lemon.core.Request;

import java.util.List;

/**
 * Created by lihuihua on 2019/1/26.
 */
public interface Cluster {
    List<Server> getServers();
    boolean isAsync();
    Object call(Request request) throws InterruptedException;

}
