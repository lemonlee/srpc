package com.lemon.transport;

import com.lemon.codecs.Codec;
import com.lemon.core.DefaultRequest;
import com.lemon.core.DefaultResponse;
import com.lemon.core.Exception.RpcFrameworkException;
import com.lemon.core.MessageRouter;
import com.lemon.util.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by lihuihua on 2019/1/29.
 */
public class NettyServer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private ServerBootstrap serverBootstrap = new ServerBootstrap();
    private MessageRouter router;
    private Codec codec;
    private int port;

    public NettyServer(MessageRouter router,Codec codec,int port){
        this.router = router;
        this.codec = codec;
        this.port = port;
        this.serverBootstrap.group(bossGroup, workerGroup)
                .localAddress(new InetSocketAddress("localhost",port))
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                            throws IOException {

                        ch.pipeline().addLast(new NettyDecoder(NettyServer.this.codec, 1024*1024, Constants.HEADER_SIZE, 4),
                                new NettyEncoder(NettyServer.this.codec),new NettyServerHandler());
                    }
                });
        try {
            ChannelFuture channelFuture = this.serverBootstrap.bind().sync();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture f) throws Exception {

                    if(f.isSuccess()){
                        logger.info("Rpc Server bind port:{} success");
                    } else {
                        logger.error("Rpc Server bind port:{} failure");
                    }
                }
            });
        } catch (InterruptedException e) {
            logger.error("NettyServer bind to address:%s failure", e);
            throw new RpcFrameworkException("NettyClient connect to address:%s failure", e);
        }
    }

    private void processRpcRequest(ChannelHandlerContext context, DefaultRequest request) {
        DefaultResponse response = (DefaultResponse) this.router.handle(request);//;
        logger.info("Rpc server process request:{} end...", request.getRequestId());
        context.writeAndFlush(response);
    }

    class NettyServerHandler extends SimpleChannelInboundHandler<DefaultRequest> {

        @Override
        protected void channelRead0(ChannelHandlerContext context, DefaultRequest request) throws Exception {

            logger.info("Rpc server receive request id:{}", request.getRequestId());
            //处理请求
            processRpcRequest(context, request);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            logger.error("NettyServerHandler exceptionCaught: remote=" + ctx.channel().remoteAddress()
                    + " local=" + ctx.channel().localAddress(), cause);
            ctx.channel().close();
        }
    }

}
