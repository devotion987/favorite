package com.devotion.blue.web.core.addon;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.jfinal.log.Log;

public class AddonClassLoader extends URLClassLoader {
	private static final Log log = Log.getLog(AddonClassLoader.class);
	private String path;

	public AddonClassLoader(String path) {
		super(new URL[] {}, Thread.currentThread().getContextClassLoader());
		this.path = path;
	}

	public void init() {
		File jarFile = new File(path);
		try {
			addURL(jarFile.toURI().toURL());
		} catch (MalformedURLException e) {
			log.error("AddonClassLoader init error", e);
		}
	}

	public void autoLoadClass(JarFile jarfile) {
		Enumeration<JarEntry> entries = jarfile.entries();

		while (entries.hasMoreElements()) {
			JarEntry jarEntry = entries.nextElement();
			String entryName = jarEntry.getName();
			if (!jarEntry.isDirectory() && entryName.endsWith(".class")) {
				String className = entryName.replace("/", ".").substring(0, entryName.length() - 6);
				try {
					loadClass(className);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
