package com.wugy.tomcat.ex13.startup;

import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;

import com.wugy.tomcat.utils.ConsTomcat;

public class Stopper {

	public static void main(String[] args) {
		// the following code is taken from the Stop method of
		// the org.apache.catalina.startup.Catalina class
		try {
			Socket socket = new Socket(ConsTomcat.host, ConsTomcat.port);
			OutputStream stream = socket.getOutputStream();
			String shutdown = "SHUTDOWN";
			for (int i = 0; i < shutdown.length(); i++)
				stream.write(shutdown.charAt(i));
			stream.flush();
			stream.close();
			socket.close();
			System.out.println("The server was successfully shut down.");
		} catch (IOException e) {
			System.out.println("Error. The server has not been started.");
		}
	}
}