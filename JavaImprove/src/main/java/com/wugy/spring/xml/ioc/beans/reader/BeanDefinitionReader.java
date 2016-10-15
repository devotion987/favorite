package com.wugy.spring.xml.ioc.beans.reader;

/**
 * 从配置中读取BeanDefinition
 * 
 * @author wugy 2016年4月12日
 *
 */
public interface BeanDefinitionReader {

	void loadBeanDefinitions(String location) throws Exception;
}
