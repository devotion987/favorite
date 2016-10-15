package com.wugy.spring.xml.aop;

/**
 * 被代理的对象
 * 
 * @author wugy 2016年4月16日
 *
 */
public class TargetSource {

	private Class<?> targetClass;

	private Class<?>[] interfaces;

	private Object target;

	public TargetSource(Class<?> targetClass, Object target, Class<?>... interfaces) {
		this.targetClass = targetClass;
		this.interfaces = interfaces;
		this.target = target;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public Class<?>[] getInterfaces() {
		return interfaces;
	}

	public Object getTarget() {
		return target;
	}

}
