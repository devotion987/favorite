package com.wugy.spring.xml.ioc.beans.factory;

/**
 * bean容器
 * 
 * @author wugy 2016年4月13日
 *
 */
public interface BeanFactory {

	Object getBean(String name) throws Exception;

	<T> T getBean(String name, Class<T> classType) throws Exception;
}
