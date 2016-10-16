package com.devotion.rws.schema.parse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.devotion.rws.schema.config.DsConfig;

/**
 * 功能描述：URF配置文件paser 用于解析XSD文件中的定义和组件定义
 */
public class UrfBeanDefinitionParser implements BeanDefinitionParser {

	/** 日志 */
	private static final Logger logger = LoggerFactory.getLogger(UrfBeanDefinitionParser.class);

	/** Bean类 */
	private final Class<?> beanClass;

	/** 标志位 */
	private final boolean required;

	/** 写数据源 */
	private final static String WR_DS = "wr_ds";

	/** 读数据源 */
	private final static String RO_DS = "ro_ds";

	/**
	 * 构造函数
	 * 
	 * @param beanClass
	 *            被解析的类
	 * @param required
	 *            是否被要求解析
	 */
	public UrfBeanDefinitionParser(Class<?> beanClass, boolean required) {
		this.beanClass = beanClass;
		this.required = required;
	}

	/**
	 * 解析方法
	 * 
	 * @param element
	 *            被解析的元素
	 * @param parserContext
	 *            解析器上下文
	 * @return bean定义
	 */
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		return parse(element, parserContext, beanClass, required);
	}

	/**
	 * 解析方法
	 * 
	 * @param element
	 *            被解析的元素
	 * @param parserContext
	 *            解析器上下文
	 * @param beanClass
	 *            映射类
	 * @param required
	 *            标志位
	 * @return BEAN定义
	 */
	private BeanDefinition parse(Element element, ParserContext parserContext, Class<?> beanClass, boolean required) {
		if (required) {
			logger.debug("被要求解析");
		}
		/** 抽象对bean的定义 */
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setLazyInit(false);
		String id = element.getAttribute("id");
		if (id != null && id.length() > 0) {
			if (parserContext.getRegistry().containsBeanDefinition(id)) {
				throw new IllegalStateException("Duplicate spring bean id " + id);
			}
			parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);
			beanDefinition.getPropertyValues().addPropertyValue("id", id);
		}
		/** 写数据库Bean配置读取、解析、并注册为BeanDefinition */
		BeanDefinition wrdsBeanDefinition = getWrDsBeanDefinition(element, parserContext);
		beanDefinition.getPropertyValues().addPropertyValue("wrDsConfig", wrdsBeanDefinition);
		/** 读数据库Bean配置读取、解析、并注册为BeanDefinition */
		ManagedMap<String, BeanDefinition> roDsBeanDefinitionMap = getRoDsBeanDefinitionMap(element, parserContext);
		beanDefinition.getPropertyValues().addPropertyValue("roDsConfigs", roDsBeanDefinitionMap);
		return beanDefinition;
	}

	/**
	 * 写数据库的BeanDefinition
	 * 
	 * @param element
	 *            被解析的元素
	 * @param parserContext
	 *            解析器上下文
	 * @return 写数据库的BEAN定义
	 */
	private BeanDefinition getWrDsBeanDefinition(Element element, ParserContext parserContext) {
		logger.debug(parserContext.toString());

		/** 获取写库节点 */
		NodeList wrdsNodes = element.getElementsByTagNameNS("*", WR_DS);

		if (wrdsNodes == null || wrdsNodes.getLength() == 0 || wrdsNodes.getLength() > 1) {
			throw new IllegalStateException("每个ds_group要求必须有且仅有一个wr_ds节点 !");
		}

		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(DsConfig.class);
		beanDefinition.setLazyInit(false);
		/** 写库节点 */
		Node wrdsNode = wrdsNodes.item(0);
		if (wrdsNode.getNodeType() == Node.ELEMENT_NODE) {
			Element wrdsElement = (Element) wrdsNode;
			String name = wrdsElement.getAttribute("name");
			String ref = wrdsElement.getAttribute("ref");
			String type = wrdsElement.getAttribute("type");
			MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
			propertyValues.addPropertyValue("name", name);
			propertyValues.addPropertyValue("refDataSource", new RuntimeBeanReference(ref));
			propertyValues.addPropertyValue("type", type);
		}
		return beanDefinition;
	}

	/**
	 * 读数据库的BeanDefinition
	 * 
	 * @param element
	 *            被解析的元素
	 * @param parserContext
	 *            解析器上下文
	 * @return 读数据源的BEAN定义MAP集合
	 */
	private ManagedMap<String, BeanDefinition> getRoDsBeanDefinitionMap(Element element, ParserContext parserContext) {
		logger.debug(parserContext.toString());

		/** 获取读库节点 */
		NodeList rodsNodes = element.getElementsByTagNameNS("*", RO_DS);

		ManagedMap<String, BeanDefinition> roDsBeanDefinitionMap = new ManagedMap<>();

		if (rodsNodes == null || rodsNodes.getLength() <= 0) {
			throw new IllegalStateException("每个ds_group要求必须至少有一个wr_ds节点 !");
		}

		for (int i = 0; i < rodsNodes.getLength(); i++) {
			RootBeanDefinition beanDefinition = new RootBeanDefinition();
			beanDefinition.setBeanClass(DsConfig.class);
			beanDefinition.setLazyInit(false);
			Node rodsNode = rodsNodes.item(i);
			if (rodsNode.getNodeType() == Node.ELEMENT_NODE) {
				Element ro_dsElement = (Element) rodsNode;
				String name = ro_dsElement.getAttribute("name");
				String weight = ro_dsElement.getAttribute("weight");
				String refId = ro_dsElement.getAttribute("ref");
				String type = ro_dsElement.getAttribute("type");
				MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
				propertyValues.addPropertyValue("name", name);
				propertyValues.addPropertyValue("weight", weight);
				propertyValues.addPropertyValue("refDataSource", new RuntimeBeanReference(refId));
				propertyValues.addPropertyValue("type", type);
				roDsBeanDefinitionMap.put(name, beanDefinition);
			}
		}
		return roDsBeanDefinitionMap;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 获取被解析的类
	 * 
	 * @return 被解析的类
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public Class<?> getBeanClass() {
		return beanClass;
	}

	/**
	 * 标志位
	 * 
	 * @return 标志位
	 */
	public boolean isRequired() {
		return required;
	}

}