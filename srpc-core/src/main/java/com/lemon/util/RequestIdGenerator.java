package com.lemon.util;

import java.util.concurrent.atomic.AtomicLong;


public class RequestIdGenerator {
    private static final AtomicLong idGenerator = new AtomicLong(1);

    public static long getRequestId() {
        return idGenerator.getAndIncrement();
    }
}
