package com.wugy.spring.xml.ioc.beans.context;

import java.util.Map;

import com.wugy.spring.xml.ioc.beans.BeanDefinition;
import com.wugy.spring.xml.ioc.beans.factory.AbstractBeanFactory;
import com.wugy.spring.xml.ioc.beans.factory.AutowireCapableBeanFactory;
import com.wugy.spring.xml.ioc.beans.io.ResourceLoader;
import com.wugy.spring.xml.ioc.beans.reader.XmlBeanDefinitionReader;

public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

	private String configLocation;

	public ClassPathXmlApplicationContext(String configLocation) throws Exception {
		this(configLocation, new AutowireCapableBeanFactory());
	}

	public ClassPathXmlApplicationContext(String configLocation, AbstractBeanFactory beanFactory) throws Exception {
		super(beanFactory);
		this.configLocation = configLocation;
		refresh();
	}

	@Override
	protected void loadBeanDefinitions(AbstractBeanFactory beanFactory) throws Exception {
		XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(new ResourceLoader());
		xmlBeanDefinitionReader.loadBeanDefinitions(configLocation);
		Map<String, BeanDefinition> registry = xmlBeanDefinitionReader.getRegistry();
		for (Map.Entry<String, BeanDefinition> entry : registry.entrySet()) {
			beanFactory.registerBeanDefinition(entry.getKey(), entry.getValue());
		}
	}

}
