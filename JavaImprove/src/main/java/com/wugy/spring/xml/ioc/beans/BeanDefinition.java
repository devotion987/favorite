package com.wugy.spring.xml.ioc.beans;

/**
 * bean的内容及元数据，保存在BeanFactory中，封装bean的实体
 * 
 * @author wugy 2016年4月12日
 *
 */
public class BeanDefinition {

	private Object bean;
	private Class<?> beanClass;
	private String beanClassName;
	private PropertyValues propertyValues = new PropertyValues();

	public BeanDefinition() {
	}

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}

	public Class<?> getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	public String getBeanClassName() {
		return beanClassName;
	}

	public void setBeanClassName(String beanClassName) {
		this.beanClassName = beanClassName;
		try {
			this.beanClass = Class.forName(beanClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public PropertyValues getPropertyValues() {
		return propertyValues;
	}

	public void setPropertyValues(PropertyValues propertyValues) {
		this.propertyValues = propertyValues;
	}

}
