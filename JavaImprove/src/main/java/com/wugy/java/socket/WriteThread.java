package com.wugy.java.socket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.wugy.java.utils.CloseUtil;

public class WriteThread implements Runnable {

	private Socket socket;

	public WriteThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		DataOutputStream outputStream = null;
		BufferedReader reader = null;
		try {
			while (true) {
				outputStream = new DataOutputStream(socket.getOutputStream());
				System.out.print("请输入：");
				reader = new BufferedReader(new InputStreamReader(System.in));
				String send = reader.readLine();
				outputStream.writeUTF(send);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CloseUtil.closeOutputStream(outputStream);
			CloseUtil.closeReader(reader);
		}
	}

}
