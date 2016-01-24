package com.xlmine.netty.client.init;

import com.xlmine.netty.util.ResourceHolder;

public class ClientInitParams {

	private int workerThreadPoolSize = 500;

	public void setWorkerThreadPoolSize(int workerThreadPoolSize) {
		this.workerThreadPoolSize = workerThreadPoolSize;
	}

	public int getWorkerThreadPoolSize() {
		return workerThreadPoolSize;
	}

}
