package com.wugy.spring.utils;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * JSON 操作工具类
 *
 * @author devotion
 */
public class JsonUtil {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 将 Java 对象转为 JSON 字符串
	 */
	public static <T> String toJSON(T obj) {
		String jsonStr;
		try {
			jsonStr = objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return jsonStr;
	}

	/**
	 * 将 JSON 字符串转为 Java 对象
	 */
	public static <T> T fromJSON(String json, Class<T> type) {
		T obj;
		try {
			obj = objectMapper.readValue(json, type);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return obj;
	}
}
