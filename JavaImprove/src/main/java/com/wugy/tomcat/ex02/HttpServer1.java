package com.wugy.tomcat.ex02;

import java.net.Socket;

import com.wugy.tomcat.utils.ConsTomcat;

import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class HttpServer1 {

	/**
	 * WEB_ROOT is the directory where our HTML and other files reside. For this
	 * package, WEB_ROOT is the "webroot" directory under the working directory.
	 * The working directory is the location in the file system from where the
	 * java command was invoked.
	 */
	// the shutdown command received
	private boolean shutdown = false;

	public static void main(String[] args) {
		HttpServer1 server = new HttpServer1();
		server.await();
	}

	public void await() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(ConsTomcat.port, 1, InetAddress.getByName(ConsTomcat.host));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Loop waiting for a request
		while (!shutdown) {
			Socket socket = null;
			InputStream input = null;
			OutputStream output = null;
			try {
				socket = serverSocket.accept();
				input = socket.getInputStream();
				output = socket.getOutputStream();

				// create Request object and parse
				Request request = new Request(input);
				request.parse();

				// create Response object
				Response response = new Response(output);
				response.setRequest(request);

				// check if this is a request for a servlet or a static resource
				// a request for a servlet begins with "/servlet/"
				if (request.getUri().startsWith("/servlet/")) {
					ServletProcessor1 processor = new ServletProcessor1();
					processor.process(request, response);
				} else {
					StaticResourceProcessor processor = new StaticResourceProcessor();
					processor.process(request, response);
				}

				// Close the socket
				socket.close();
				// check if the previous URI is a shutdown command
				shutdown = ConsTomcat.shutDownCmd.equals(request.getUri());
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
