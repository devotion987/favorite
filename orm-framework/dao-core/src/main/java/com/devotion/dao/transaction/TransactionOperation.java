package com.devotion.dao.transaction;

import javax.sql.DataSource;

import com.devotion.dao.resource.XmlResource;
import com.devotion.dao.transaction.template.TransactionTemplate;

/**
 * 编程式事务处理层
 */
public abstract class TransactionOperation extends XmlResource {

	/**
	 * 
	 * 功能描述: <br>
	 * 获取非分库事务模板
	 * 
	 * @return 非分库事务模板
	 */
	public TransactionTemplate getTransactionTemplate() {
		return getTransactionTemplate(new Object());
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 获取分库事务模板
	 * 
	 * @param param
	 *            分库参数
	 * @return 分库事务模板
	 */
	public TransactionTemplate getTransactionTemplate(Object param) {
		/** 分库路由数据源 */
		DataSource dataSource = routeDataSource(param, null);
		return new TransactionTemplate(dataSource);
	}
}
