package com.devotion.dao.route;

import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devotion.dao.group.IGroupDataSource;
import com.devotion.dao.resource.parse.SqlBean;
import com.devotion.dao.route.table.TableRoute;

/**
 * DAO路由层
 */
public abstract class DataSourceRoute {

	/**
	 * 日志
	 */
	private static Logger logger = LoggerFactory.getLogger(DataSourceRoute.class);

	/** 默认数据源 */
	private DataSource defaultDataSource;

	/** 分库路由 */
	private RouteConfig route;

	/** 分表路由 */
	private TableRoute tableRoute;

	/**
	 * 
	 * 功能描述: 数据源分库路由<br>
	 * 分库路由
	 * 
	 * @param parameter
	 *            分库参数
	 * @param sqlBean
	 *            定义的SQL映射对象
	 * @return 数据源
	 */
	protected DataSource routeDataSource(Object parameter, SqlBean sqlBean) {
		if (route != null) {
			/** 调用分库策略 */
			DataSource routeDataSource = route.route(parameter);
			logger.debug("Route dataSource [{}]", routeDataSource);
			if (routeDataSource != null) {
				return determineDataSource(routeDataSource, sqlBean);
			}
		}
		/** 否则使用默认数据源 */
		logger.debug("Route used the default dataSource.");
		return determineDataSource(defaultDataSource, sqlBean);
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 分表路由
	 * 
	 * @param paramMap
	 *            client传入参数
	 */
	protected void routeTable(Map<String, Object> paramMap) {
		if (tableRoute != null) {
			tableRoute.routeTable(paramMap);
		}
	}

	/**
	 * 设置默认数据源
	 * 
	 * @param defaultDataSource
	 *            默认数据源
	 */
	public void setDefaultDataSource(DataSource defaultDataSource) {
		this.defaultDataSource = defaultDataSource;
	}

	/**
	 * 设置路由
	 * 
	 * @param route
	 *            路由配置
	 */
	public void setRoute(RouteConfig route) {
		this.route = route;
	}

	/**
	 * 设置分表路由
	 * 
	 * @param tableRoute
	 *            分表路由
	 */
	public void setTableRoute(TableRoute tableRoute) {
		this.tableRoute = tableRoute;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 决定使用读写分离数据源组或原生数据源
	 * 
	 * @param dataSource
	 *            数据源
	 * @param sqlBean
	 *            SQL映射对象
	 * @return 数据源
	 */
	private DataSource determineDataSource(DataSource dataSource, SqlBean sqlBean) {
		/** 读写分离数据源组 */
		if (dataSource instanceof IGroupDataSource) {
			IGroupDataSource groupDataSource = (IGroupDataSource) dataSource;
			return groupDataSource.getDataSource(sqlBean);
		}
		return dataSource;
	}
}
