package com.xlmine.netty.client.message;

public class HostConfig {

	private String host;
	private int port = 80;
    private int idleTime;
	private int connectionTimeout = 100;
	private int socketTimeout = 300;

	public HostConfig(final String host) {
		this(host, 80);
	}

	public HostConfig(final String host, final int port) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(final String host) { this.host = host; }

	public int getPort() {
		return this.port;
	}

	public void setPort(final int port) {
		this.port = port;
	}

    public int getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(int idleTime) {
        this.idleTime = idleTime;
    }

    public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(final int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	@Override
	public int hashCode() {
		return System.identityHashCode(this.host);
	}

	@Override
	public String toString() {
		return String.format(String.format("[Host: %s, Port: %s]", host, port));
	}

}
