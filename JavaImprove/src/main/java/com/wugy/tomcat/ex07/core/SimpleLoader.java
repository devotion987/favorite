package com.wugy.tomcat.ex07.core;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import org.apache.catalina.Container;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Loader;

import com.wugy.tomcat.utils.ConsTomcat;

import org.apache.catalina.DefaultContext;

public class SimpleLoader implements Loader, Lifecycle {

	ClassLoader classLoader = null;
	Container container = null;

	public SimpleLoader() {
		try {
			URL[] urls = new URL[1];
			URLStreamHandler streamHandler = null;
			File classPath = new File(ConsTomcat.webRoot);
			String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
			urls[0] = new URL(null, repository, streamHandler);
			classLoader = new URLClassLoader(urls);
		} catch (IOException e) {
			System.out.println(e.toString());
		}

	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	public DefaultContext getDefaultContext() {
		return null;
	}

	public void setDefaultContext(DefaultContext defaultContext) {
	}

	public boolean getDelegate() {
		return false;
	}

	public void setDelegate(boolean delegate) {
	}

	public String getInfo() {
		return "A simple loader";
	}

	public boolean getReloadable() {
		return false;
	}

	public void setReloadable(boolean reloadable) {
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
	}

	public void addRepository(String repository) {
	}

	public String[] findRepositories() {
		return null;
	}

	public boolean modified() {
		return false;
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
	}

	// implementation of the Lifecycle interface's methods
	public void addLifecycleListener(LifecycleListener listener) {
	}

	public LifecycleListener[] findLifecycleListeners() {
		return null;
	}

	public void removeLifecycleListener(LifecycleListener listener) {
	}

	public synchronized void start() throws LifecycleException {
		System.out.println("Starting SimpleLoader");
	}

	public void stop() throws LifecycleException {
	}

}