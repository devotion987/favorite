package com.wugy.java.socket;

import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Test;

import com.wugy.java.utils.CloseUtil;
import com.wugy.java.utils.CustomConst;

public class Server {

	@Test
	public void testServer() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(CustomConst.port);
			System.out.println("服务端正在接收。。。");
			while (true) {
				Socket socket = serverSocket.accept();
				new Thread(new ReadThread(socket)).start();
				new Thread(new WriteThread(socket)).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CloseUtil.closeServerSocket(serverSocket);
		}
	}
}
