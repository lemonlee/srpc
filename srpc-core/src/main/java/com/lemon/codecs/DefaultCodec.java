package com.lemon.codecs;

import com.lemon.core.DefaultRequest;
import com.lemon.core.DefaultResponse;
import com.lemon.util.Constants;

import java.io.IOException;

/**
 * Created by lihuihua on 2019/1/27.
 */
public class DefaultCodec implements Codec{
    private Serializer serializer;

    public DefaultCodec (Serializer serializer){
        this.serializer = serializer;
    }

    @Override
    public <T> byte[] encode(T message) throws IOException {
        return serializer.serialize(message);
    }

    @Override
    public Object decode(byte messageType, byte[] data) throws IOException {
        if(messageType == Constants.FLAG_REQUEST){
            return  serializer.deserialize(data,DefaultRequest.class);
        }else{
            if(messageType == Constants.FLAG_RESPONSE){
                return serializer.deserialize(data, DefaultResponse.class);
            }
        }
        return null;
    }
}
