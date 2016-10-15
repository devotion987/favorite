package com.wugy.spring.xml.ioc.beans.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.wugy.spring.xml.ioc.beans.BeanDefinition;
import com.wugy.spring.xml.ioc.beans.BeanPostProcessor;

abstract public class AbstractBeanFactory implements BeanFactory {

	private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

	private List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

	@Override
	public Object getBean(String name) throws Exception {
		BeanDefinition beanDefinition = beanDefinitionMap.get(name);
		if (null == beanDefinition) {
			throw new IllegalArgumentException("No bean named " + name + " is defined");
		}
		Object bean = beanDefinition.getBean();
		if (null == bean) {
			bean = doCreateBean(beanDefinition);
			bean = initializeBean(bean, name);
			beanDefinition.setBean(bean);
		}
		return bean;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(String name, Class<T> classType) throws Exception {
		return (T) getBean(name);
	}

	protected Object initializeBean(Object bean, String name) throws Exception {
		for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
			bean = beanPostProcessor.postProcessBeforeInitialization(bean, name);
		}
		for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
			bean = beanPostProcessor.postProcessAfterInitialization(bean, name);
		}
		return bean;
	}

	protected Object doCreateBean(BeanDefinition beanDefinition) throws Exception {
		Object bean = createBeanInstance(beanDefinition);
		beanDefinition.setBean(bean);
		applyPropertyValues(bean, beanDefinition);
		return bean;
	}

	protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception {

	}

	protected Object createBeanInstance(BeanDefinition beanDefinition) throws Exception {
		return beanDefinition.getBeanClass().newInstance();
	}

	public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
		beanDefinitionMap.put(name, beanDefinition);
	}

	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
		beanPostProcessors.add(beanPostProcessor);
	}

	public void preInstantiateSingletons() throws Exception {
		for (String beanName : beanDefinitionMap.keySet()) {
			getBean(beanName);
		}
	}

	public <T> List<T> getBeanForType(Class<T> type) throws Exception {
		List<T> beans = new ArrayList<>();
		for (String beanDefinitionName : beanDefinitionMap.keySet()) {
			Class<?> beanClass = beanDefinitionMap.get(beanDefinitionName).getBeanClass();
			if (type.isAssignableFrom(beanClass)) {
				beans.add(getBean(beanDefinitionName, type));
			}
		}
		return beans;
	}
}
