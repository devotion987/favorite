package com.wugy.tomcat.utils;

import java.io.File;

public interface ConsTomcat {

	String webRoot = System.getProperty("user.dir") + File.separator + "webRoot";

	String shutDownCmd = "/shutdown";

	String host = "127.0.0.1";
	int port = 8080;
	int size = 1024;

	String packageEx03 = "com.wugy.tomcat.ex03.connector.http";

}
