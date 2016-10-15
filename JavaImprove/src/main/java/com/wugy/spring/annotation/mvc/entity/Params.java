package com.wugy.spring.annotation.mvc.entity;

import java.util.Map;

import com.wugy.java.utils.CastUtil;
import com.wugy.spring.annotation.ioc.BaseBean;

/**
 * 封装请求参数
 *
 * @author devotion
 */
public class Params extends BaseBean {

	private static final long serialVersionUID = 1L;
	private final Map<String, Object> fieldMap;

	public Params(Map<String, Object> fieldMap) {
		this.fieldMap = fieldMap;
	}

	public Map<String, Object> getFieldMap() {
		return fieldMap;
	}

	public String getString(String name) {
		return CastUtil.castString(get(name));
	}

	public double getDouble(String name) {
		return CastUtil.castDouble(get(name));
	}

	public long getLong(String name) {
		return CastUtil.castLong(get(name));
	}

	public int getInt(String name) {
		return CastUtil.castInt(get(name));
	}

	private Object get(String name) {
		return fieldMap.get(name);
	}
}
