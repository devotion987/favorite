package com.wugy.java.socket;

import java.net.Socket;

import org.junit.Test;

import com.wugy.java.utils.CloseUtil;
import com.wugy.java.utils.CustomConst;

public class Client {

	@Test
	public void testClient() {
		Socket socket = null;
		try {
			socket = new Socket(CustomConst.ip, CustomConst.port);
			new Thread(new ReadThread(socket)).start();
			new Thread(new WriteThread(socket)).start();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CloseUtil.closeSocket(socket);
		}
	}
}
