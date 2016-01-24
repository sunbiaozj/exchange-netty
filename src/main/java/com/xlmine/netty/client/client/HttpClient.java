package com.xlmine.netty.client.client;

import com.xlmine.netty.client.initializer.HttpRequestEncoder;
import com.xlmine.netty.client.initializer.HttpResponseDecoder;
import com.xlmine.netty.client.init.ClientInitParams;
import com.xlmine.netty.client.message.HostConfig;
import com.xlmine.netty.client.message.HttpRequestMessage;
import com.xlmine.netty.client.message.HttpResponseMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

//尽量一个路由(域名)一个client
public class HttpClient {

    // 保存ResponseMessage
    public static final ConcurrentHashMap<String, HttpResponseMessage> RESPONSES = new ConcurrentHashMap<String, HttpResponseMessage>();

    private final Bootstrap bootstrap = new Bootstrap();

    public HttpClient(ClientInitParams clientInitParams, HostConfig hostConfig) {
        EventLoopGroup workerGroup = new NioEventLoopGroup(clientInitParams.getWorkerThreadPoolSize());
        bootstrap.group(workerGroup)//
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)//
                .option(ChannelOption.SO_REUSEADDR, true)//
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)//
                .option(ChannelOption.SO_SNDBUF, 8096)//
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, hostConfig.getConnectionTimeout())
                .option(ChannelOption.SO_TIMEOUT,hostConfig.getSocketTimeout())
                .remoteAddress(hostConfig.getHost(), hostConfig.getPort())
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("heartbeat", new IdleStateHandler(hostConfig.getIdleTime(), hostConfig.getIdleTime(), hostConfig.getIdleTime(), TimeUnit.MILLISECONDS)); //超时自动断开连接
                        pipeline.addLast("HttpObjectAggregator", new HttpObjectAggregator(8192));
                        pipeline.addLast("HttpClientCodec",new HttpClientCodec());
                        pipeline.addLast("HttpRequestEncoder",new HttpRequestEncoder());
                        pipeline.addLast("HttpResponseDecoder",new HttpResponseDecoder());
                    }
                });
    }

    /**
     * 异步发送请求
     */
    public void asyncRequest(HttpRequestMessage httpRequestMessage) {

        ChannelFuture channelFuture = bootstrap.connect().syncUninterruptibly();
        if (channelFuture != null && httpRequestMessage != null) {
            channelFuture.channel().writeAndFlush(httpRequestMessage).syncUninterruptibly();
        }

    }

}
