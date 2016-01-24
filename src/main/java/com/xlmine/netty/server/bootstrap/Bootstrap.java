package com.xlmine.netty.server.bootstrap;

import com.xlmine.netty.server.channel.SimpleNioServerSocketChannel;
import com.xlmine.netty.server.init.ServerInitParams;
import com.xlmine.netty.server.initializer.DefaultChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;

public class Bootstrap {

	protected static ServerInitParams serverInitParams = new ServerInitParams();

	public static void main(String[] args) throws Exception {

		EventLoopGroup bossGroup = new NioEventLoopGroup(serverInitParams.getBossThreadPoolSize(), new DefaultThreadFactory("Netty-Boss"));
		EventLoopGroup workerGroup = new NioEventLoopGroup(serverInitParams.getWorkerThreadPoolSize(), new DefaultThreadFactory("Netty-Worker"));

		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workerGroup)//
				.channel(SimpleNioServerSocketChannel.class)//
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,500)
                .option(ChannelOption.SO_TIMEOUT,500)
				.option(ChannelOption.TCP_NODELAY, true)//
				.option(ChannelOption.SO_REUSEADDR, true)//
				.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)//
				.option(ChannelOption.SO_BACKLOG, 512)//
				.childOption(ChannelOption.SO_SNDBUF, 8096)//
				.childOption(ChannelOption.SO_RCVBUF, 8096)//
				.localAddress(serverInitParams.getPort())//
				.childHandler(new DefaultChannelInitializer(serverInitParams));

		ChannelFuture channelFuture = serverBootstrap.bind().sync();
		channelFuture.channel().closeFuture().sync();
	}

}
