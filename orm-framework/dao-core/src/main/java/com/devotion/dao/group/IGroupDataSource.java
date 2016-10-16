package com.devotion.dao.group;

import javax.sql.DataSource;

import com.devotion.dao.resource.parse.SqlBean;

/**
 * 分组数据源
 */
public interface IGroupDataSource extends DataSource {

	/**
	 * 
	 * 功能描述: 根据SQL特性获取原生类型数据源
	 * 
	 * @param sqlBean
	 *            SQL模板映射对象
	 * @return 数据源
	 */
	DataSource getDataSource(SqlBean sqlBean);
}
