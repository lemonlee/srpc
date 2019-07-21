package com.lemon.core;

import java.io.Serializable;
import java.util.Map;


public class DefaultRequest implements Serializable, Request {

    private static final long serialVersionUID = 7478520607109127572L;

    private Long requestId;
    private String interfaceName;
    private String methodName;
    private Object[] arguments;
    private Class<?>[] parameterTypes;
    private byte type;  //请求类型

    @Override
    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    @Override
    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }


}
