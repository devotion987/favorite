package com.wugy.spring.annotation.aop;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义切面类
 *
 * @author devotion 2016年9月2日14:10:37
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

	/**
	 * 包名
	 */
	String pkg() default "";

	/**
	 * 类名
	 */
	String cls() default "";

	/**
	 * 注解
	 */
	Class<? extends Annotation> annotation() default Aspect.class;
}
