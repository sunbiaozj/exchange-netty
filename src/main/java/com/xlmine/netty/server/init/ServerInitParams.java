package com.xlmine.netty.server.init;

public class ServerInitParams {

    private int port = 9090;

    private int bossThreadPoolSize = 8;

    private int workerThreadPoolSize = 500;

    /**
     * queue size for request to wait
     */
    private int queueSize = 10000;

    /**
     * unit is ms,max time that a connection wait to begin process
     */
    private int maxConnectionTime = 200;

    /**
     * max idle time,if idle time which not yet process is gratter than this , then return
     */
    private int idleTime = 300;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getBossThreadPoolSize() {
        return bossThreadPoolSize;
    }

    public void setBossThreadPoolSize(int bossThreadPoolSize) {
        this.bossThreadPoolSize = bossThreadPoolSize;
    }

    public int getWorkerThreadPoolSize() {
        return workerThreadPoolSize;
    }

    public void setWorkerThreadPoolSize(int workerThreadPoolSize) {
        this.workerThreadPoolSize = workerThreadPoolSize;
    }


    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public int getMaxConnectionTime() {
        return maxConnectionTime;
    }

    public void setMaxConnectionTime(int maxConnectionTime) {
        this.maxConnectionTime = maxConnectionTime;
    }

    public void setIdleTime(int idleTime) {
        this.idleTime = idleTime;
    }

    public int getIdleTime() {
        return idleTime;
    }
}
