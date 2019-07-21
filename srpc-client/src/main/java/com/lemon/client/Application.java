package com.lemon.client;

import com.alibaba.fastjson.JSON;
import com.lemon.codecs.Codec;
import com.lemon.codecs.DefaultCodec;
import com.lemon.codecs.FastjsonSerializer;
import com.lemon.codecs.ProtostuffSerializer;
import com.lemon.config.ConnectPoolConfig;
import com.lemon.core.Request;
import com.lemon.model.Message;
import com.lemon.proxy.SrpcClientProxy;
import com.lemon.service.EchoService;
import com.lemon.transport.Cluster;
import com.lemon.transport.DefaultCluster;
import com.lemon.transport.NettyClient;
import com.lemon.transport.Server;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by lihuihua on 2019/1/25.
 */
@Configuration
public class Application {
    public static void main(String[] args) throws IOException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:srpc-client.xml");
        EchoService service = (EchoService) ctx.getBean("echoService");
        String echo = service.sendString("hello");
        System.out.println("get echo string:"+echo);
        Message msg = new Message();
        msg.setContent("hhhh");
        msg.setId(1);
        Message echoMessage = service.sendMessage(msg);
        System.out.println("get echo msg:"+echoMessage.getContent());
    }


    @Bean(name = "cluster")
    public Cluster getCluster() {
        DefaultCluster cluster = new DefaultCluster();
        cluster.setAsync(false);
        ConnectPoolConfig connectPoolConfig = new ConnectPoolConfig();
        connectPoolConfig.setMaxConnections(5);

        Server server = new Server();
        server.setIp("127.0.0.1");
        server.setPort(9000);
        server.setServiceName("srpc");
        cluster.setServers(new LinkedList<Server>());
        cluster.getServers().add(server);
        NettyClient nettyClient = new NettyClient(server,connectPoolConfig,new DefaultCodec(new ProtostuffSerializer()),100);
        server.setClient(nettyClient);

         return cluster;
    }
}
