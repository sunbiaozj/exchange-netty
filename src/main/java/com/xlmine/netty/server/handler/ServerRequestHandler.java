package com.xlmine.netty.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xlmine.netty.server.channel.SimpleNioSocketChannel;
import com.xlmine.netty.server.message.ServerRequestMessage;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class ServerRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerRequestHandler.class);

    public void handle(ServerRequestMessage serverRequestMessage) {

        try {


            //TODO 处理业务逻辑


            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/json;charset=UTF-8");
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, "success".getBytes().length);
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            response.content().writeBytes("success".getBytes());

            SimpleNioSocketChannel channel = serverRequestMessage.getChannel();
            channel.writeAndFlush(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

}