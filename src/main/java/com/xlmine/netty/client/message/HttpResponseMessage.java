package com.xlmine.netty.client.message;

import io.netty.handler.codec.http.HttpResponseStatus;

public class HttpResponseMessage {

    private int statuscode;
    private byte[] content;

    public HttpResponseMessage() {
    }

    public HttpResponseMessage(int statuscode, byte[] content) {
        this.statuscode = statuscode;
        this.content = content;
    }

    public int getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(int statuscode) {
        this.statuscode = statuscode;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

}
