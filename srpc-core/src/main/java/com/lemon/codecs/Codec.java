package com.lemon.codecs;

import java.io.IOException;

/**
 * Created by lihuihua on 2019/1/27.
 */
public interface Codec {

    <T> byte[] encode(T message) throws IOException;

    <T> T decode(byte messageType, byte[] data) throws IOException;

}
