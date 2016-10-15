package com.wugy.spring.xml.aop;

import java.lang.reflect.Method;

public interface MethodMatcher {

	boolean matches(Method method, Class<?> targetClass);

}
