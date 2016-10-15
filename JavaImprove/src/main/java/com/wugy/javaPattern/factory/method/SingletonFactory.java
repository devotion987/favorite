package com.wugy.javaPattern.factory.method;

import java.lang.reflect.Constructor;

/**
 * 单例模式的工厂类
 * 
 * @author wugy 2016年9月18日
 *
 */
public class SingletonFactory {

	private static Singleton singleton;
	static {
		try {
			Class<?> clazz = Class.forName(Singleton.class.getName());
			Constructor<?>[] constructors = clazz.getDeclaredConstructors();
			constructors[0].setAccessible(true);
			singleton = (Singleton) constructors[0].newInstance(new Object[] { null });
			// singleton = (Singleton)
			// Class.forName(Singleton.class.getName()).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Singleton getSingleton() {
		return singleton;
	}
}

class Singleton {

	private Singleton() {
	}
}
