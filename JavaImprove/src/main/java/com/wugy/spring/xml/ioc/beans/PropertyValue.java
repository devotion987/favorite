package com.wugy.spring.xml.ioc.beans;

/**
 * 用于bean的属性注入
 * 
 * @author wugy 2016年4月12日
 *
 */
public class PropertyValue {

	private String name;
	private Object value;

	public PropertyValue(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}

}
