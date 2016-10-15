package com.wugy.spring.utils;

/**
 * 用于获取子类的模板类
 *
 * @author devotion
 */
public abstract class SupperClassTemplate extends ClassTemplate {

	protected final Class<?> superClass;

	protected SupperClassTemplate(String packageName, Class<?> superClass) {
		super(packageName);
		this.superClass = superClass;
	}
}
