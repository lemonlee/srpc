package com.lemon.transport;


import com.lemon.codecs.Codec;
import com.lemon.config.ConnectPoolConfig;
import com.lemon.core.*;
import com.lemon.core.Exception.TransportException;
import com.lemon.util.Constants;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by lihuihua on 2019/1/27.
 */
public class NettyClient {
    private  Logger logger = LoggerFactory.getLogger(this.getClass());
    private EventLoopGroup group = new NioEventLoopGroup();
    private Bootstrap b = new Bootstrap();
    private SimpleChannelPool channelPool;
    private final ConcurrentHashMap<Long, ResponseFuture> responseFutureMap = new ConcurrentHashMap<Long, ResponseFuture>(256);
    private int timeout;

    public NettyClient(Server remoteServer, ConnectPoolConfig connectPoolConfig, Codec codec, int timeout){
        b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true) .option(ChannelOption.SO_KEEPALIVE, true);
        ChannelPoolHandler channelPoolHandler = new NettyChannelPoolHandler(codec);
        this.channelPool = new FixedChannelPool(b.remoteAddress(remoteServer.getRemoteAddress()), channelPoolHandler,connectPoolConfig.getMaxConnections());
        this.timeout = timeout;
    }


    public Response invokeSync(final Request request) throws InterruptedException, TransportException {
        Future<Channel> f = channelPool.acquire();
        Thread thread = Thread.currentThread();
        f.addListener( (FutureListener<Channel>) ff ->{
            LockSupport.unpark(thread);
            if(ff.isSuccess()){
                logger.info("channel active, request id:{}", request.getRequestId());
            }
        });
        LockSupport.park();
        final Channel channel = f.getNow();
        if (channel != null && channel.isActive()) {
            final ResponseFuture<Response> rpcFuture = new DefaultResponseFuture<Response>(timeout);
            this.responseFutureMap.put(request.getRequestId(), rpcFuture);
            //写数据
            channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {

                    if (future.isSuccess()) {
                        logger.info("send success, request id:{}", request.getRequestId());

                    } else {
                        logger.info("send failure, request id:{}", request.getRequestId());
                        responseFutureMap.remove(request.getRequestId());
                        rpcFuture.setFailure(future.cause());
                    }
                    channelPool.release(channel);
                }
            });
            return rpcFuture.get();
        } else {
            throw new TransportException("channel not active. request id:"+request.getRequestId());
        }
    }

    private class NettyClientHandler extends ChannelInboundHandlerAdapter {
        private Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
                throws Exception {

            logger.info("client read msg:{}, ", msg);
            if(msg instanceof Response) {
                DefaultResponse response = (DefaultResponse) msg;

                ResponseFuture<Response> rpcFuture =responseFutureMap.get(response.getRequestId());
                if(rpcFuture!=null) {
                    responseFutureMap.remove(response.getRequestId());
                    rpcFuture.setResult(response);
                }

            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
                throws Exception {
            logger.error("client caught exception", cause);
            ctx.close();
        }
    }

    class NettyChannelPoolHandler implements ChannelPoolHandler {
        private  Codec codec;

        public NettyChannelPoolHandler( Codec codec){
            this.codec= codec;
        }

        @Override
        public void channelReleased(Channel ch) throws Exception {
            System.out.println("channelReleased. Channel ID: " + ch.id());
        }
        @Override
        public void channelAcquired(Channel ch) throws Exception {
            System.out.println("channelAcquired. Channel ID: " + ch.id());
        }

        @Override
        public void channelCreated(Channel ch) throws Exception {
            System.out.println("channelCreated. Channel ID: " + ch.id());
            SocketChannel channel = (SocketChannel) ch; channel.config().setKeepAlive(true);
            NettyDecoder decoder = new NettyDecoder(codec, 1024*1024, Constants.HEADER_SIZE, 4);
            NettyEncoder encoder = new NettyEncoder(codec);
            NettyClientHandler clientHandler = new NettyClientHandler();
            ChannelHandler[] channelHandlers = new ChannelHandler[]{decoder,encoder,clientHandler};
            channel.config().setTcpNoDelay(true); channel.pipeline().addLast(channelHandlers);
        }

    }

}




