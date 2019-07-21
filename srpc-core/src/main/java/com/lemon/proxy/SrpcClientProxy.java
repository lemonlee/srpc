package com.lemon.proxy;

import com.lemon.invoker.SrpcInvocationHandler;;
import com.lemon.transport.Cluster;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by lihuihua on 2019/1/25.
 */
public class SrpcClientProxy implements FactoryBean<Object>, ApplicationContextAware, InitializingBean {

    private Class<?> serviceInterface;
    private Object serviceProxy;
    private int timeout;
    private int connTimeout;
    private int serverPort;
    private ApplicationContext applicationContext;
    private Cluster cluster;

    public SrpcClientProxy(){

        System.out.println(timeout);
    }


    public Object getObject() throws Exception {
        return serviceProxy;
    }

    public Class<?> getObjectType() {
        return serviceInterface;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        SrpcInvocationHandler invocationHandler = new SrpcInvocationHandler(this.cluster);
        JDKProxyFactory pf = new JDKProxyFactory();
        serviceProxy = pf.getProxy(this.getClass().getClassLoader(),serviceInterface,invocationHandler);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Class<?> getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public Object getServiceProxy() {
        return serviceProxy;
    }

    public void setServiceProxy(Object serviceProxy) {
        this.serviceProxy = serviceProxy;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getConnTimeout() {
        return connTimeout;
    }

    public void setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }


}
