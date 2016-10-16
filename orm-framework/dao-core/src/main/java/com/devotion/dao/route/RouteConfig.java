package com.devotion.dao.route;

import javax.sql.DataSource;

/**
 * 分库路由策略配置
 */
public interface RouteConfig {

	/**
	 * 
	 * 功能描述: 分库路由数据源
	 * 
	 * @param parameter
	 *            分库参数
	 * @return 数据源
	 */
	DataSource route(Object parameter);
}
