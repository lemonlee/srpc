package com.lemon.core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class MessageRouter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private ConcurrentHashMap<String, Provider<?>> providers = new ConcurrentHashMap<String, Provider<?>>();

    public MessageRouter() {}

    public Response handle(Request request){
        String service = request.getInterfaceName();
        Provider provider = providers.get(service);
        return provider.invoke(request);
    }

    public void addProvider(Provider<?> provider) {
        providers.put(provider.getInterface().getName(),provider);
    }


}
