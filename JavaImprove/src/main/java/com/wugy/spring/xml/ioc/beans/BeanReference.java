package com.wugy.spring.xml.ioc.beans;

/**
 * 定义bean解析
 * 
 * @author wugy 2016年4月12日
 *
 */
public class BeanReference {

	private String name;
	private Object bean;

	public BeanReference(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}

}
