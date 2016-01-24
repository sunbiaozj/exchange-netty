package com.xlmine.netty.server.initializer;

import com.xlmine.netty.server.channel.SimpleNioSocketChannel;
import com.xlmine.netty.server.init.ServerInitParams;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class DefaultChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final ConcurrentHashMap<String, SimpleNioSocketChannel> channels;

    private final ServerInitParams serverInitParams;

    public DefaultChannelInitializer(ServerInitParams serverInitParams) {
        this.serverInitParams = serverInitParams;
        this.channels = new ConcurrentHashMap<String, SimpleNioSocketChannel>(serverInitParams.getQueueSize());
    }

    @Override
    public void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("heartbeat", new IdleStateHandler(serverInitParams.getIdleTime(), serverInitParams.getIdleTime(), serverInitParams.getIdleTime(), TimeUnit.MILLISECONDS)); //超时自动断开连接
        pipeline.addLast("HttpRequestDecoder", new HttpRequestDecoder());//这个地方和client有区别
        pipeline.addLast("HttpObjectAggregator", new HttpObjectAggregator(8192));
        pipeline.addLast("HttpResponseEncoder", new HttpResponseEncoder());
        pipeline.addLast("ServerChannelInitializer", new ServerChannelInitializer(serverInitParams, channels));
    }

}
