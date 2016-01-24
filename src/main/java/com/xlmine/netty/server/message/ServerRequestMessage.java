package com.xlmine.netty.server.message;

import com.xlmine.netty.server.channel.SimpleNioSocketChannel;

import io.netty.handler.codec.http.HttpRequest;

public class ServerRequestMessage {

	private SimpleNioSocketChannel channel;

	private String channelId;

	private long connectTime;

	private HttpRequest request;

	public ServerRequestMessage(SimpleNioSocketChannel channel, HttpRequest request) {
		this.channel = channel;
		this.channelId = channel.getId();
		this.connectTime = channel.getConnectTime();
		this.request = request;
	}

	public long getConnectTime() {
		return connectTime;
	}

	public String getChannelId() {
		return channelId;
	}

	public SimpleNioSocketChannel getChannel() {
		return channel;
	}

	public HttpRequest getRequest() {
		return request;
	}

}
