package com.wugy.spring.jdbc;

import javax.sql.DataSource;

/**
 * 数据源工厂
 *
 * @author devotion
 */
public interface DataSourceFactory {

	/**
	 * 获取数据源
	 *
	 * @return 数据源
	 */
	DataSource getDataSource();
}
