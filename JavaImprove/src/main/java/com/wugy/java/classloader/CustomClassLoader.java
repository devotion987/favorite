package com.wugy.java.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

public class CustomClassLoader {

}

class PathClassLoader extends ClassLoader {

	private String classPath;
	private String packeageName = "";

	public PathClassLoader(String classPath) {
		this.classPath = classPath;
	}

	@Override
	protected Class<?> findClass(String className) throws ClassNotFoundException {
		if (packeageName.startsWith(className)) {
			byte[] classData = getData(className);
			if (null == classData || classData.length == 0) {
				throw new ClassCastException();
			}
			return defineClass(className, classData, 0, classData.length);
		}
		return super.loadClass(className);
	}

	private byte[] getData(String className) {
		StringBuilder path = new StringBuilder();
		path.append(classPath).append(File.separatorChar).append(className.replace('.', File.separatorChar))
				.append(".class");
		try {
			InputStream inStream = new FileInputStream(path.toString());
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[2048];
			int num = 0;
			while ((num = inStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, num);
			}
			inStream.close();
			return outputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

class URLPathClassLoader extends URLClassLoader {

	private String packeageName = "";

	public URLPathClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		Class<?> clazz = findLoadedClass(name);
		if (null != clazz)
			return clazz;
		if (!packeageName.startsWith(name))
			return super.loadClass(name);
		return super.findClass(name);
	}

}
