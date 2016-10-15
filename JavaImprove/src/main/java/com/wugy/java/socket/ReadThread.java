package com.wugy.java.socket;

import java.io.DataInputStream;
import java.net.Socket;

import com.wugy.java.utils.CloseUtil;

public class ReadThread implements Runnable {

	private Socket socket;

	public ReadThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		DataInputStream dataInputStream = null;
		try {
			while (true) {
				dataInputStream = new DataInputStream(socket.getInputStream());
				String recieve = dataInputStream.readUTF();
				System.out.println("读取的数据：" + recieve);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CloseUtil.closeInputStream(dataInputStream);
		}
	}

}
