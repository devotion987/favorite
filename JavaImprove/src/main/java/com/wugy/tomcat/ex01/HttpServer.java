package com.wugy.tomcat.ex01;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.wugy.tomcat.utils.ConsTomcat;

public class HttpServer {

	private boolean shutdown = false;

	public static void main(String[] args) {
		HttpServer server = new HttpServer();
		server.await();
	}

	private void await() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(ConsTomcat.port, 1, InetAddress.getByName(ConsTomcat.host));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		Socket socket = null;
		InputStream inStream = null;
		OutputStream outStream = null;
		Request request = null;
		Response response = null;
		while (!shutdown) {
			try {
				socket = serverSocket.accept();
				inStream = socket.getInputStream();
				outStream = socket.getOutputStream();

				request = new Request(inStream);
				request.parse();

				response = new Response(outStream);
				response.setRequest(request);
				response.sendStaticResource();

				socket.close();
				shutdown = request.getUri().equals(ConsTomcat.shutDownCmd);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}

	}
}
