package com.xlmine.netty.server.channel;

import java.nio.channels.SocketChannel;
import java.util.List;

import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 这个类和SimpleNioSocketChannel是有区别的
 * SimpleNioServerSocketChannel是用来接受请求的，SimpleNioSocketChannel是用来在程序内部传递消息的
 */
public class SimpleNioServerSocketChannel extends NioServerSocketChannel {

	@Override
	protected int doReadMessages(List<Object> obj) throws Exception {
		SocketChannel ch = javaChannel().accept();
		try {
			if (ch != null) {
				//封装了一个NioSocketChannel，增加id属性
				obj.add(new SimpleNioSocketChannel(this, ch));
				return 1;
			}
		} catch (Throwable t) {
			t.printStackTrace();
			try {
				ch.close();
			} catch (Throwable t2) {
				t2.printStackTrace();
			}
		}
		return 0;
	}
}
