package com.wugy.java.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;

public final class ClassUtil {

	public static final String CLASS_SUFFIX = ".class";

	/**
	 * 获取类加载器
	 */
	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * 加载类，默认初始化
	 */
	public static Class<?> loadClass(String className) {
		return loadClass(className, true);
	}

	public static Class<?> loadClass(String className, boolean isInitialized) {
		Class<?> clazz = null;
		try {
			Class.forName(className, isInitialized, getClassLoader());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clazz;
	}

	/**
	 * 获取指定包名下所有类
	 */
	public static List<Class<?>> getClassSet(String packageName) {
		List<Class<?>> classSet = new ArrayList<>();
		try {
			Enumeration<URL> urls = getClassLoader().getResources(packageName);
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				if (null != url) {
					String protocol = url.getProtocol();
					if (protocol.endsWith("file")) {
						String packagePath = url.getPath().replaceAll("%20", " ");
						addClass(classSet, packagePath, packageName);
					} else if (protocol.endsWith("jar")) {
						JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
						if (null != jarURLConnection) {
							JarFile jarFile = jarURLConnection.getJarFile();
							if (null != jarFile) {
								Enumeration<JarEntry> jarEntries = jarFile.entries();
								while (jarEntries.hasMoreElements()) {
									JarEntry jarEntry = jarEntries.nextElement();
									String jarEntryName = jarEntry.getName();
									if (jarEntryName.endsWith(CLASS_SUFFIX)) {
										String className = jarEntryName.substring(0, jarEntryName.lastIndexOf("."))
												.replaceAll(File.separator, ".");
										doAddClass(classSet, className);
									}
								}
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classSet;
	}

	public static void doAddClass(List<Class<?>> classSet, String className) {
		Class<?> clazz = loadClass(className);
		classSet.add(clazz);
	}

	public static void addClass(List<Class<?>> classSet, String packagePath, String packageName) {
		File[] files = new File(packagePath).listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				return file.isFile() && file.getName().endsWith(CLASS_SUFFIX) || file.isDirectory();
			}
		});
		for (File file : files) {
			String fileName = file.getName();
			if (file.isFile()) {
				String className = fileName.substring(0, fileName.lastIndexOf("."));
				if (StringUtils.isNotEmpty(packageName)) {
					className = packageName + "." + className;
				}
				doAddClass(classSet, className);
			} else {
				String subPackagePath = fileName;
				if (StringUtils.isNotEmpty(packagePath)) {
					subPackagePath += File.separator + packageName;
				}
				String subPackageName = fileName;
				if (StringUtils.isNotEmpty(packageName)) {
					subPackageName += "." + packageName;
				}
				addClass(classSet, subPackagePath, subPackageName);
			}
		}
	}

	/**
	 * 通过反射创建实例
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String className) {
		T instance;
		try {
			Class<?> commandClass = ClassUtil.loadClass(className);
			instance = (T) commandClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return instance;
	}

	/**
	 * 获取类路径
	 */
	public static String getClassPath() {
		String classpath = "";
		URL resource = getClassLoader().getResource("");
		if (resource != null) {
			classpath = resource.getPath();
		}
		return classpath;
	}

	/**
	 * 是否为 int 类型（包括 Integer 类型）
	 */
	public static boolean isInt(Class<?> type) {
		return type.equals(int.class) || type.equals(Integer.class);
	}

	/**
	 * 是否为 long 类型（包括 Long 类型）
	 */
	public static boolean isLong(Class<?> type) {
		return type.equals(long.class) || type.equals(Long.class);
	}

	/**
	 * 是否为 double 类型（包括 Double 类型）
	 */
	public static boolean isDouble(Class<?> type) {
		return type.equals(double.class) || type.equals(Double.class);
	}

	/**
	 * 是否为 String 类型
	 */
	public static boolean isString(Class<?> type) {
		return type.equals(String.class);
	}

}
