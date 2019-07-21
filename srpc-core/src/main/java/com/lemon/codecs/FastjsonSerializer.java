package com.lemon.codecs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lemon.core.DefaultRequest;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.springframework.objenesis.Objenesis;
import org.springframework.objenesis.ObjenesisStd;


import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//这个类在反序列化时，如果类内部存在自定义的类型变量，会出现问题，内部的自定义类型变量反序列化后为JsonObject 类型
public class FastjsonSerializer implements Serializer {


    @Override
    public<T> byte[] serialize(T msg) throws IOException {
        String jsonString = JSON.toJSONString(msg);
        return jsonString.getBytes();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> type) throws IOException {
        String jsonString = new String(data);
        T serialization = JSON.parseObject(jsonString, type);
        return serialization;
    }
}
