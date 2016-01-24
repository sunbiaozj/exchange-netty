package com.xlmine.netty.server.channel;

import java.nio.channels.SocketChannel;

import com.xlmine.netty.util.StringUtils;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class SimpleNioSocketChannel extends NioSocketChannel {

	/**
	 * 该连接的唯一标识
	 */
	private String id = StringUtils.uuid();

	/**
	 * 连接时间，channel的创建时间
	 */
	private long connectTime = System.currentTimeMillis();

	public SimpleNioSocketChannel(SocketChannel socket) {
		super(socket);
	}

	public SimpleNioSocketChannel(Channel parent, SocketChannel socket) {
		super(parent, socket);
	}

	public String getId() {
		return id;
	}

	public long getConnectTime() {
		return connectTime;
	}

	public void setConnectTime(long connectTime) {
		this.connectTime = connectTime;
	}

}
