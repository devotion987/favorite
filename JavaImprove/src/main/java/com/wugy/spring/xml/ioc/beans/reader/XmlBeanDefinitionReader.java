package com.wugy.spring.xml.ioc.beans.reader;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.wugy.spring.xml.ioc.beans.BeanDefinition;
import com.wugy.spring.xml.ioc.beans.BeanReference;
import com.wugy.spring.xml.ioc.beans.PropertyValue;
import com.wugy.spring.xml.ioc.beans.PropertyValues;
import com.wugy.spring.xml.ioc.beans.io.ResourceLoader;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

	public XmlBeanDefinitionReader(ResourceLoader resourceLoader) {
		super(resourceLoader);
	}

	@Override
	public void loadBeanDefinitions(String location) throws Exception {
		InputStream inputStream = getResourceLoader().getResource(location).getInputStream();
		doLoadBeanDefinitions(inputStream);
	}

	protected void doLoadBeanDefinitions(InputStream inputStream) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		Document parse = documentBuilder.parse(inputStream);
		registerBeanDefinitions(parse);
		inputStream.close();
	}

	protected void registerBeanDefinitions(Document parse) throws Exception {
		Element root = parse.getDocumentElement();
		parseBeanDefinitions(root);
	}

	protected void parseBeanDefinitions(Element root) {
		NodeList childNodes = root.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node = childNodes.item(i);
			if (node instanceof Element) {
				Element element = (Element) node;
				processBeanDefinitions(element);
			}
		}
	}

	protected void processBeanDefinitions(Element element) {
		String name = element.getAttribute("id");
		String className = element.getAttribute("class");
		BeanDefinition beanDefinition = new BeanDefinition();
		processProperty(element, beanDefinition);
		beanDefinition.setBeanClassName(className);
		getRegistry().put(name, beanDefinition);
	}

	private void processProperty(Element element, BeanDefinition beanDefinition) {
		NodeList nodeList = element.getElementsByTagName("property");
		PropertyValues propertyValues = beanDefinition.getPropertyValues();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				Element propertyEle = (Element) node;
				String name = propertyEle.getAttribute("name");
				String value = propertyEle.getAttribute("value");
				Object property = value;
				if (StringUtils.isBlank(value)) {
					String ref = propertyEle.getAttribute("ref");
					if (StringUtils.isBlank(ref)) {
						throw new IllegalArgumentException("Configuration problem: <property> element for property '"
								+ name + "' must specify a ref or value");
					}
					BeanReference beanReference = new BeanReference(ref);
					property = beanReference;
				}
				propertyValues.addPropertyValue(new PropertyValue(name, property));
			}
		}
	}

}
