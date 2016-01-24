package com.xlmine.netty.client.message;

/**
 * 在handler中接收到相应结果后调用的方法
 */
public interface HttpResponseFuture {

	public void complete(HttpResponseMessage responseMsg);

}
