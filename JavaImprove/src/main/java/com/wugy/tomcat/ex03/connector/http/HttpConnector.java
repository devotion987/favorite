package com.wugy.tomcat.ex03.connector.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.wugy.tomcat.utils.ConsTomcat;

public class HttpConnector implements Runnable {

	boolean stopped;
	private String scheme = "http";

	public String getScheme() {
		return scheme;
	}

	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(ConsTomcat.port, 1, InetAddress.getByName(ConsTomcat.host));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		while (!stopped) {
			// Accept the next incoming connection from the server socket
			Socket socket = null;
			try {
				socket = serverSocket.accept();
			} catch (Exception e) {
				continue;
			}
			// Hand this socket off to an HttpProcessor
			HttpProcessor processor = new HttpProcessor(this);
			processor.process(socket);
		}
	}

	public void start() {
		Thread thread = new Thread(this);
		thread.start();
	}
}