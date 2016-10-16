package com.devotion.dao.route.table;

import java.util.Map;

/**
 * 分表路由
 */
public interface TableRoute {

	/**
	 * 
	 * 功能描述: 确定分表表名，存放表名到传入Map参数
	 * 
	 * @param paramMap
	 *            DALClient传入参数
	 */
	void routeTable(Map<String, Object> paramMap);
}
