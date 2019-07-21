package com.lemon.codecs;

import java.io.IOException;

/**
 * Created by lihuihua on 2019/1/26.
 */
public interface Serializer {
    <T> byte[]  serialize(T msg) throws IOException;

    <T> T deserialize(byte[] data, Class<T> type) throws IOException;
}
