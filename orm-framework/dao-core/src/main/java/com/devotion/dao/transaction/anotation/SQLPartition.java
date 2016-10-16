package com.devotion.dao.transaction.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * bean的读写分离注解定义
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLPartition {

	/** sqlId名称 */
	String id() default "";

	/** 读写类型 */
	boolean isRead();

	/** 数据源id */
	String dsName() default "";
}
