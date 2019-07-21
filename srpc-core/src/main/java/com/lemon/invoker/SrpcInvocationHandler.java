package com.lemon.invoker;

import com.lemon.codecs.Serializer;
import com.lemon.core.DefaultRequest;
import com.lemon.core.Response;
import com.lemon.transport.Cluster;
import com.lemon.util.Constants;
import com.lemon.util.RequestIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by lihuihua on 2019/1/25.
 */
public class SrpcInvocationHandler implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(SrpcInvocationHandler.class);
    private Cluster cluster;

    public SrpcInvocationHandler(Cluster cluster){
        this.cluster = cluster;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("SrpcInvocationHandler: "+ method.getName());

        DefaultRequest request = new DefaultRequest();
        request.setRequestId(RequestIdGenerator.getRequestId());
        request.setInterfaceName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setArguments(args);
        request.setType(cluster.isAsync()?Constants.REQUEST_ASYNC:Constants.REQUEST_SYNC);

        Response response = (Response) cluster.call(request);
        return response.getResult();
    }

}
