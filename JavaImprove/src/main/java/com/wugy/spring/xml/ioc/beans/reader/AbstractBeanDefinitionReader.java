package com.wugy.spring.xml.ioc.beans.reader;

import java.util.HashMap;
import java.util.Map;

import com.wugy.spring.xml.ioc.beans.BeanDefinition;
import com.wugy.spring.xml.ioc.beans.io.ResourceLoader;

abstract public class AbstractBeanDefinitionReader implements BeanDefinitionReader {

	private Map<String, BeanDefinition> registry;

	private ResourceLoader resourceLoader;

	protected AbstractBeanDefinitionReader(ResourceLoader resourceLoader) {
		this.registry = new HashMap<>();
		this.resourceLoader = resourceLoader;
	}

	public Map<String, BeanDefinition> getRegistry() {
		return registry;
	}

	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

}
