package com.wugy.java.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Test;

import com.wugy.java.utils.CloseUtil;
import com.wugy.java.utils.CustomConst;

public class Server2 {

	@Test
	public void testServer() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(CustomConst.port);
			Socket socket = serverSocket.accept();
			BufferedReader sysReader = new BufferedReader(
					new InputStreamReader(System.in));
			BufferedReader socketReader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			String line = "";
			while ((line = sysReader.readLine()) != CustomConst.end) {
				writer.println(line);
				writer.flush();
				System.out.println("Client line = " + line);
				System.out.println("Server line = " + socketReader.readLine());
			}
			writer.close();
			socketReader.close();
			sysReader.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CloseUtil.closeServerSocket(serverSocket);
		}
	}
}
