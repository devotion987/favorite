package com.wugy.javaPattern.factory.method;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ProductFactory {

	private static final ConcurrentMap<String, Object> typeMap = new ConcurrentHashMap<>();

	public static Object createObject(String type) {
		Object obj = null;
		if (typeMap.containsKey(type)) {
			obj = typeMap.get(type);
		} else {
			if (type.equalsIgnoreCase("String")) {
				obj = new String();
			} else {
				obj = new Integer(0);
			}
			typeMap.put(type, obj);
		}
		return obj;
	}
}
