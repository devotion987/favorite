package com.wugy.spring.xml.ioc.beans.context;

import java.util.List;

import com.wugy.spring.xml.ioc.beans.BeanPostProcessor;
import com.wugy.spring.xml.ioc.beans.factory.AbstractBeanFactory;

abstract public class AbstractApplicationContext implements ApplicationContext {

	protected AbstractBeanFactory beanFactory;

	public AbstractApplicationContext(AbstractBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public void refresh() throws Exception {
		loadBeanDefinitions(beanFactory);
		registerBeanPostProcessors(beanFactory);
		onRefresh();
	}

	protected void onRefresh() throws Exception {
		beanFactory.preInstantiateSingletons();
	}

	protected void registerBeanPostProcessors(AbstractBeanFactory beanFactory) throws Exception {
		List<BeanPostProcessor> beanPostProcessors = beanFactory.getBeanForType(BeanPostProcessor.class);
		for (Object beanPostProcessor : beanPostProcessors) {
			beanFactory.addBeanPostProcessor((BeanPostProcessor) beanPostProcessor);
		}
	}

	abstract protected void loadBeanDefinitions(AbstractBeanFactory beanFactory) throws Exception;

	@Override
	public Object getBean(String name) throws Exception {
		return beanFactory.getBean(name);
	}

	@Override
	public <T> T getBean(String name, Class<T> classType) throws Exception {
		return beanFactory.getBean(name, classType);
	}

}
