package com.xlmine.netty.server.initializer;

import com.xlmine.netty.server.channel.SimpleNioSocketChannel;
import com.xlmine.netty.server.handler.ServerRequestHandler;
import com.xlmine.netty.server.init.ServerInitParams;
import com.xlmine.netty.server.message.ServerRequestMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class ServerChannelInitializer extends ChannelInboundHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(ServerChannelInitializer.class);

    private final ServerInitParams serverInitParams;

    private final ConcurrentHashMap<String, SimpleNioSocketChannel> channels;

    private final ServerRequestHandler serverRequestHandler;

    public ServerChannelInitializer(ServerInitParams serverInitParams, ConcurrentHashMap<String, SimpleNioSocketChannel> channels) {
        this.serverInitParams = serverInitParams;
        this.channels = channels;
        this.serverRequestHandler = new ServerRequestHandler();
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        SimpleNioSocketChannel channel = (SimpleNioSocketChannel) ctx.channel();
        channels.put(channel.getId(), channel);

        //这个channels的作用是控制同时处理请求的数量,避免负载太高
        if (channels.size() >= serverInitParams.getQueueSize()) {
            logger.warn("current connections is [{}]", channels.size());
            throw new RuntimeException("too mangy connection");
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            SimpleNioSocketChannel channel = (SimpleNioSocketChannel) ctx.channel();
            logger.debug("channel [{}] channelRead", channel.getId());

            if (msg != null && msg instanceof HttpRequest) {

                //如果已经超时了
                long time = System.currentTimeMillis();
                if (time - channel.getConnectTime() >= serverInitParams.getMaxConnectionTime()) {
                    FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NO_CONTENT);
                    response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, 0);
                    response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
                    channel.writeAndFlush(response);
                } else {
                    serverRequestHandler.handle(new ServerRequestMessage(channel, (HttpRequest) msg));
                }

            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        try {
            SimpleNioSocketChannel channel = (SimpleNioSocketChannel) ctx.channel();
            channels.remove(channel.getId());
            channel.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        try {
            SimpleNioSocketChannel channel = (SimpleNioSocketChannel) ctx.channel();
            channels.remove(channel.getId());
            channel.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE || event.state() == IdleState.WRITER_IDLE) {
                try {
                    SimpleNioSocketChannel channel = (SimpleNioSocketChannel) ctx.channel();
                    channels.remove(channel.getId());
                    channel.close();
                } catch (Exception e) {
                    logger.error("channel userEventTriggered:", e.getMessage());
                }
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        try {
            SimpleNioSocketChannel channel = (SimpleNioSocketChannel) ctx.channel();
            channels.remove(channel.getId());
            channel.close();
        } catch (Exception e) {
            logger.error("channel exceptionCaught:", e.getMessage());
        }
    }
}
