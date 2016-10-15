package com.wugy.srping.test.xml.beans;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.wugy.spring.xml.ioc.beans.BeanDefinition;
import com.wugy.spring.xml.ioc.beans.factory.AbstractBeanFactory;
import com.wugy.spring.xml.ioc.beans.factory.AutowireCapableBeanFactory;
import com.wugy.spring.xml.ioc.beans.io.ResourceLoader;
import com.wugy.spring.xml.ioc.beans.reader.XmlBeanDefinitionReader;
import com.wugy.srping.test.xml.HelloWorldService;

public class BeanFactoryTest {

	private AbstractBeanFactory beanFactory;

	@Before
	public void initializeFactory() {
		try {
			// 1.读取配置
			XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(new ResourceLoader());
			xmlBeanDefinitionReader.loadBeanDefinitions("ioc.xml");

			// 2.初始化BeanFactory并注册bean
			beanFactory = new AutowireCapableBeanFactory();
			Set<Entry<String, BeanDefinition>> entrySet = xmlBeanDefinitionReader.getRegistry().entrySet();
			for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : entrySet) {
				beanFactory.registerBeanDefinition(beanDefinitionEntry.getKey(), beanDefinitionEntry.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testLazy() {
		try {
			// 3.获取bean
			HelloWorldService helloWorldService = beanFactory.getBean("helloWorldService", HelloWorldService.class);
			helloWorldService.helloWorld();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPreInstantiate() {
		try {
			// 3.初始化bean
			beanFactory.preInstantiateSingletons();

			// 4.获取bean
			HelloWorldService helloWorldService = beanFactory.getBean("helloWorldService", HelloWorldService.class);
			helloWorldService.helloWorld();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
