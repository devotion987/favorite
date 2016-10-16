package com.devotion.dao.transaction.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能描述： 路由参数配置注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RouteParam {

	/** 拦截分库参数的类型 */
	@SuppressWarnings("rawtypes")
	Class clazz();

	/** 分库参数是否数组，如果数组，取index()位置做为分库参数 */
	boolean isArray() default false;

	/** 数组时，取第index位置的序号 */
	int index() default 0;

	/** 分库参数名称 */
	String field();
}
