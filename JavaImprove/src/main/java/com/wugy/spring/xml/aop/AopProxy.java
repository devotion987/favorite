package com.wugy.spring.xml.aop;

/**
 * aop代理
 * 
 * @author wugy 2016年4月16日
 *
 */
public interface AopProxy {

	/**
	 * 避免子类类型转换
	 */
	<T> T getProxy();

}
