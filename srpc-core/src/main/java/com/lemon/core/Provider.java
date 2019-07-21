package com.lemon.core;

import com.lemon.core.Exception.RpcBizException;
import com.lemon.core.Exception.RpcFrameworkException;

import java.lang.reflect.Method;

/**
 * Created by lihuihua on 2019/1/30.
 */
public class Provider <T>{


    private Class<T> clz;
    private Object proxyImpl;

    public Provider(){

    }
    public Provider(Object proxyImpl, Class<T> clz) {
        this.clz = clz;
        this.proxyImpl = proxyImpl;
    }
    public Class<T> getClz() {
        return clz;
    }

    public void setClz(Class<T> clz) {
        this.clz = clz;
    }

    public Object getProxyImpl() {
        return proxyImpl;
    }

    public void setProxyImpl(Object proxyImpl) {
        this.proxyImpl = proxyImpl;
    }
    public Class<T> getInterface() {
        return clz;
    }

    public Method getMethed(Request request) {
        try {
            return clz.getMethod(request.getMethodName(), request.getParameterTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Response invoke(Request request) {

        DefaultResponse response = new DefaultResponse();
        response.setRequestId(request.getRequestId());

        Method method = getMethed(request);
        if (method == null) {
            RpcFrameworkException exception =
                    new RpcFrameworkException("Service method not exist: " + request.getInterfaceName() + "." + request.getMethodName());

            response.setException(exception);
            return response;
        }
        try {
            method.setAccessible(true);
            Object result = method.invoke(proxyImpl, request.getArguments());
            response.setResult(result);
        } catch (Exception e) {
            response.setException(new RpcBizException("invoke failure", e));
        }
        return response;
    }
}
