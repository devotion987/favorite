package com.wugy.spring.xml.aop;

public interface ClassFilter {

	boolean matches(Class<?> targetClass);
}
