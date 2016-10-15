package com.wugy.tomcat.ex01;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import com.wugy.java.utils.CloseUtil;
import com.wugy.tomcat.utils.ConsTomcat;

public class Response {

	private OutputStream outStream;
	private Request request;

	public Response(OutputStream outStream) {
		this.outStream = outStream;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public void sendStaticResource() {
		byte[] bytes = new byte[ConsTomcat.size];
		File file = new File(ConsTomcat.webRoot, request.getUri());
		FileInputStream fileInputStream = null;

		try {
			if (file.exists()) {
				fileInputStream = new FileInputStream(file);
				int ch = fileInputStream.read(bytes, 0, ConsTomcat.size);
				while (ch != -1) {
					outStream.write(bytes, 0, ch);
					ch = fileInputStream.read(bytes, 0, ConsTomcat.size);
				}
			} else {
				StringBuilder errorMessage = new StringBuilder();
				errorMessage.append("HTTP/1.1 404 File Not Found\r\n").append("Content-Type: text/html\r\n")
						.append("Content-Length: 23\r\n").append("\r\n").append("<h1>File Not Found</h1>");
				outStream.write(errorMessage.toString().getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fileInputStream) {
				CloseUtil.closeInputStream(fileInputStream);
			}
		}
	}

}
