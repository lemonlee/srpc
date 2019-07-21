package com.lemon.server;

import com.lemon.model.Message;
import com.lemon.service.EchoService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by lihuihua on 2019/1/30.
 */
@Service("echoServiceImpl")
public class EchoServiceImpl implements EchoService {
    public String sendString(String message) {
        return "server"+message;
    }

    public Message sendMessage(Message message) {
        message.setContent("server"+message.getContent());
        return message;
    }
}
