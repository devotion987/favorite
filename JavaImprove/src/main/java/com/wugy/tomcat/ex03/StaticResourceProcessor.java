package com.wugy.tomcat.ex03;

import java.io.IOException;

import com.wugy.tomcat.ex03.connector.http.HttpRequest;
import com.wugy.tomcat.ex03.connector.http.HttpResponse;

public class StaticResourceProcessor {

	public void process(HttpRequest request, HttpResponse response) {
		try {
			response.sendStaticResource();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
