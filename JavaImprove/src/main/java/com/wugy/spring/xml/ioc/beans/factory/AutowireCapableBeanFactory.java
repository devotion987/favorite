package com.wugy.spring.xml.ioc.beans.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import com.wugy.spring.xml.ioc.beans.BeanDefinition;
import com.wugy.spring.xml.ioc.beans.BeanReference;
import com.wugy.spring.xml.ioc.beans.PropertyValue;

/**
 * 可自动装配的beanFactory
 * 
 * @author wugy 2016年4月13日
 *
 */
public class AutowireCapableBeanFactory extends AbstractBeanFactory {

	@Override
	protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception {
		if (bean instanceof BeanFactoryAware) {
			((BeanFactoryAware) bean).setBeanFactory(this);
		}
		List<PropertyValue> propertyValueList = beanDefinition.getPropertyValues().getPropertyValue();
		for (PropertyValue propertyValue : propertyValueList) {
			Object value = propertyValue.getValue();
			if (value instanceof BeanReference) {
				BeanReference beanReference = (BeanReference) value;
				value = getBean(beanReference.getName());
			}
			String property = propertyValue.getName();
			StringBuilder setMethod = new StringBuilder();
			setMethod.append("set").append(property.substring(0, 1).toUpperCase()).append(property.substring(1));
			try {
				Method declaredMethod = bean.getClass().getMethod(setMethod.toString(), value.getClass());
				declaredMethod.setAccessible(true);
				declaredMethod.invoke(bean, value);
			} catch (NoSuchMethodException e) {
				Field declaredField = bean.getClass().getDeclaredField(property);
				declaredField.setAccessible(true);
				declaredField.set(bean, value);
			}
		}
	}

}
