package com.wugy.tomcat.ex01;

import java.io.InputStream;

import com.wugy.tomcat.utils.ConsTomcat;

public class Request {

	private InputStream inStream;
	private String uri;

	public Request(InputStream inStream) {
		this.inStream = inStream;
	}

	public void parse() {
		StringBuilder request = new StringBuilder(ConsTomcat.size);
		int i;
		byte[] buffer = new byte[ConsTomcat.size];
		try {
			i = inStream.read(buffer);
		} catch (Exception e) {
			e.printStackTrace();
			i = -1;
		}
		for (int j = 0; j < i; j++) {
			request.append((char) buffer[j]);
		}
		System.out.println("request = " + request.toString());
		uri = parseUri(request.toString());
	}

	private String parseUri(String request) {
		int inx1, inx2;
		inx1 = request.indexOf(' ');
		if (inx1 != -1) {
			inx2 = request.indexOf(' ', inx1 + 1);
			if (inx2 > inx1) {
				return request.substring(inx1 + 1, inx2);
			}
		}
		return null;
	}

	public String getUri() {
		return uri;
	}

}
