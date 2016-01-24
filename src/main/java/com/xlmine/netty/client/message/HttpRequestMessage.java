package com.xlmine.netty.client.message;

import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

public class HttpRequestMessage extends DefaultFullHttpRequest {

	private HttpResponseFuture httpResponseFuture;

	private byte[] content;
	private HostConfig hostConfig;

	public HttpRequestMessage(HttpVersion httpVersion, HttpMethod method, String uri) {
		super(httpVersion, method, uri);
	}

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

	public HttpResponseFuture getHttpResponseFuture() {
		return httpResponseFuture;
	}

	public void setHttpResponseFuture(HttpResponseFuture httpResponseFuture) {
		this.httpResponseFuture = httpResponseFuture;
	}

	public HostConfig getHostConfig() {
		return hostConfig;
	}

	public void setHostConfig(HostConfig hostConfig) {
		this.hostConfig = hostConfig;
	}

}
