package com.devotion.dao.transaction.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.devotion.dao.constants.TransactionPropagation;

/**
 * 事务注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {

	/** 分库参数在业务方法入参的位置，-1表示全部都是 */
	int paramIndex() default -1;

	/** 事务的传播行为 */
	TransactionPropagation propagation() default TransactionPropagation.PROPAGATION_REQUIRED;
}
