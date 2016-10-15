package com.wugy.spring.xml.ioc.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 包装一个对象的所有PropertyValue
 * 
 * @author wugy 2016年4月12日
 *
 */
public class PropertyValues {

	private final List<PropertyValue> propertyValueList = new ArrayList<>();

	public PropertyValues() {
	}

	public void addPropertyValue(PropertyValue pv) {
		propertyValueList.add(pv);
	}

	public List<PropertyValue> getPropertyValue() {
		return propertyValueList;
	}
}
