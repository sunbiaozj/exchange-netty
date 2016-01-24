package com.xlmine.netty.client.initializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;

import java.util.List;

import com.xlmine.netty.client.message.HttpRequestMessage;

public class HttpRequestEncoder extends MessageToMessageEncoder<HttpRequestMessage> {

	private HttpRequestMessage httpRequestMessage;

	@Override
	protected void encode(final ChannelHandlerContext ctx, final HttpRequestMessage msg, final List<Object> out) throws Exception {

		setHttpRequestMessage(msg);//将HttpRequestMessage和channel绑定，通过channel可以获得RequestMessage

		if (msg.getMethod().equals(HttpMethod.GET)) {
			FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, msg.getMethod(), msg.getUri());
			request.headers().add(msg.headers());
			out.add(request);
		} else if (msg.getMethod().equals(HttpMethod.POST)) {
            HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, msg.getMethod(), msg.getUri());
            byte[] contentBytes = new byte[0];
            if (msg.getContent() != null) {
                contentBytes = msg.getContent();
            }
            ByteBuf buf = ctx.alloc().directBuffer();
            buf.writeBytes(contentBytes);
            request.headers().add(msg.headers());
            request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, contentBytes.length);
            final HttpContent content = new DefaultLastHttpContent(buf);
            out.add(request);
			out.add(content);
		}
		ctx.flush();
	}

	public HttpRequestMessage getHttpRequestMessage() {
		return httpRequestMessage;
	}

	public void setHttpRequestMessage(HttpRequestMessage httpRequestMessage) {
		this.httpRequestMessage = httpRequestMessage;
	}

}
