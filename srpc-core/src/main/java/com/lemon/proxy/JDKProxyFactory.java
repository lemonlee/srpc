package com.lemon.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by lihuihua on 2019/1/25.
 */
public class JDKProxyFactory  {

    public <T> T getProxy(ClassLoader loader, Class<T> interfaces,InvocationHandler invocationHandler) {
        return (T) Proxy.newProxyInstance(loader, new Class[]{interfaces}, invocationHandler);
    }
}
