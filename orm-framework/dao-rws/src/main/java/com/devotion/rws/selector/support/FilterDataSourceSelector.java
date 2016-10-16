package com.devotion.rws.selector.support;

import java.util.List;

import com.devotion.rws.schema.config.DsConfig;
import com.devotion.rws.selector.IDataSourceSelector;

/**
 * 基于过滤器的选择器
 */
public class FilterDataSourceSelector implements IDataSourceSelector {

	/**
	 * 数据源选择
	 * 
	 * @param DsConfigs
	 *            数据源配置列表
	 * @return DsConfigs 数据源对象
	 */
	@Override
	public DsConfig select(List<DsConfig> DsConfigs) {
		return null;
	}

}
