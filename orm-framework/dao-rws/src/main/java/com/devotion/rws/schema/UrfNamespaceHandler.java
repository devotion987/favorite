package com.devotion.rws.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.devotion.rws.schema.config.DsGroupConfig;
import com.devotion.rws.schema.parse.UrfBeanDefinitionParser;

/**
 * 功能描述：将组件注册到spring容器
 */
public class UrfNamespaceHandler extends NamespaceHandlerSupport {

	/**
	 * 初始化
	 */
	public void init() {
		/** 用于把节点名和解析类联系起来 */
		registerBeanDefinitionParser("ds_group", new UrfBeanDefinitionParser(DsGroupConfig.class, true));
	}

}