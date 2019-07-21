package com.lemon.server;

import com.lemon.codecs.*;
import com.lemon.core.MessageRouter;
import com.lemon.core.Provider;
import com.lemon.service.EchoService;
import com.lemon.transport.NettyServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by lihuihua on 2019/1/30.
 */
@Configuration
public class App {
    public static void main(String[] args) throws IOException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:srpc-server.xml");
        Serializer serializer = new ProtostuffSerializer();
        MessageRouter messageRouter = new MessageRouter();
        Codec codec = new DefaultCodec(serializer);
        messageRouter.addProvider((Provider<?>) ctx.getBean("provider"));
        NettyServer nettyServer = new NettyServer(messageRouter,codec,9000);

    }



}
