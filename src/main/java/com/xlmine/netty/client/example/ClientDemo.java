package com.xlmine.netty.client.example;

import com.xlmine.netty.client.init.ClientInitParams;

import com.xlmine.netty.client.message.HostConfig;
import com.xlmine.netty.client.client.HttpClient;
import com.xlmine.netty.client.message.HttpRequestMessage;
import com.xlmine.netty.client.message.HttpResponseFuture;
import com.xlmine.netty.client.message.HttpResponseMessage;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

public class ClientDemo {

    public static void main(String[] args) throws InterruptedException {

        //TODO 请求百度会报错
        ClientInitParams clientInitParams = new ClientInitParams();
        HostConfig hostConfig = new HostConfig("infoq.com");
        HttpClient client = new HttpClient(clientInitParams, hostConfig);

        HttpRequestMessage httpRequestMessage = new HttpRequestMessage(HttpVersion.HTTP_1_1, HttpMethod.GET, "/redis-event");
        httpRequestMessage.headers().set(HttpHeaders.Names.HOST, hostConfig.getHost() + ":" + hostConfig.getPort());
        httpRequestMessage.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        httpRequestMessage.setHostConfig(hostConfig);
        httpRequestMessage.setHttpResponseFuture(new HttpResponseFuture() {
            @Override
            public void complete(HttpResponseMessage httpResponseMessage) {
                if (httpResponseMessage != null) {
                    byte[] content = httpResponseMessage.getContent();
                    if (content != null) {
                        System.out.println(new String(content));
                    }
                }
            }
        });

        client.asyncRequest(httpRequestMessage);

    }
}
