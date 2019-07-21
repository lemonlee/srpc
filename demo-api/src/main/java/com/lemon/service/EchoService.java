package com.lemon.service;

import com.lemon.model.Message;

/**
 * Created by lihuihua on 2019/1/25.
 */
public interface EchoService {
    String sendString(String message);
    Message sendMessage(Message message);
}
