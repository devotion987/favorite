package com.wugy.java.utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 属性文件操作工具类
 *
 * @author devotion
 */
public class PropsUtil {

	/**
	 * 加载属性文件
	 */
	public static Properties loadProps(String propsPath) {
		Properties props = new Properties();
		InputStream is = null;
		try {
			if (StringUtils.isEmpty(propsPath)) {
				throw new IllegalArgumentException();
			}
			String suffix = ".properties";
			if (propsPath.lastIndexOf(suffix) == -1) {
				propsPath += suffix;
			}
			is = ClassUtil.getClassLoader().getResourceAsStream(propsPath);
			if (is != null) {
				props.load(is);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			CloseUtil.closeInputStream(is);
		}
		return props;
	}

	/**
	 * 加载属性文件，并转为 Map
	 */
	public static Map<String, String> loadPropsToMap(String propsPath) {
		Map<String, String> map = new HashMap<String, String>();
		Properties props = loadProps(propsPath);
		for (String key : props.stringPropertyNames()) {
			map.put(key, props.getProperty(key));
		}
		return map;
	}

	/**
	 * 获取字符型属性
	 */
	public static String getString(Properties props, String key) {
		String value = "";
		if (props.containsKey(key)) {
			value = props.getProperty(key);
		}
		return value;
	}

	/**
	 * 获取字符型属性（带有默认值）
	 */
	public static String getString(Properties props, String key, String defalutValue) {
		String value = defalutValue;
		if (props.containsKey(key)) {
			value = props.getProperty(key);
		}
		return value;
	}

	/**
	 * 获取数值型属性
	 */
	public static int getNumber(Properties props, String key) {
		int value = 0;
		if (props.containsKey(key)) {
			value = CastUtil.castInt(props.getProperty(key));
		}
		return value;
	}

	// 获取数值型属性（带有默认值）
	public static int getNumber(Properties props, String key, int defaultValue) {
		int value = defaultValue;
		if (props.containsKey(key)) {
			value = CastUtil.castInt(props.getProperty(key));
		}
		return value;
	}

	/**
	 * 获取布尔型属性
	 */
	public static boolean getBoolean(Properties props, String key) {
		return getBoolean(props, key, false);
	}

	/**
	 * 获取布尔型属性（带有默认值）
	 */
	public static boolean getBoolean(Properties props, String key, boolean defalutValue) {
		boolean value = defalutValue;
		if (props.containsKey(key)) {
			value = CastUtil.castBoolean(props.getProperty(key));
		}
		return value;
	}

	/**
	 * 获取指定前缀的相关属性
	 */
	public static Map<String, Object> getMap(Properties props, String prefix) {
		Map<String, Object> kvMap = new LinkedHashMap<String, Object>();
		Set<String> keySet = props.stringPropertyNames();
		if (CollectionUtils.isNotEmpty(keySet)) {
			for (String key : keySet) {
				if (key.startsWith(prefix)) {
					String value = props.getProperty(key);
					kvMap.put(key, value);
				}
			}
		}
		return kvMap;
	}
}
