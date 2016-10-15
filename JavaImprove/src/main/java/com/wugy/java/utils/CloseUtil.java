package com.wugy.java.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

public class CloseUtil {

	public static void closeSocket(Socket socket) {
		try {
			if (null != socket) {
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void closeInputStream(InputStream inputStream) {
		try {
			if (null != inputStream) {
				inputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void closeOutputStream(OutputStream outputStream) {
		try {
			if (null != outputStream) {
				outputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void closeServerSocket(ServerSocket serverSocket) {
		try {
			if (null != serverSocket) {
				serverSocket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void closeReader(Reader reader) {
		try {
			if (null != reader) {
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void closeWriter(Writer writer) {
		try {
			if (null != writer) {
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
