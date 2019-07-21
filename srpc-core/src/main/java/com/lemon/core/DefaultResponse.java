package com.lemon.core;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class DefaultResponse implements Serializable, Response {

    private static final long serialVersionUID = -7432143972263049268L;

    private Long requestId;
    private Exception exception;
    private Object result;
    private long processTime;

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    @Override
    public Long getRequestId() {
        return requestId;
    }

    @Override
    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public Object getResult() {
        return result;
    }


    public void setProcessTime(long processTime) {
        this.processTime = processTime;
    }

    public long getProcessTime() {
        return processTime;
    }
}
