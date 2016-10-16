package com.devotion.dao.utils;

import java.util.Stack;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据源上下文<br>
 * 基于线程局部变量及栈实现
 */
public class DataSourceContext {

	/**
	 * 日志
	 */
	private static Logger logger = LoggerFactory.getLogger(DataSourceContext.class);

	/**
	 * 事务上下文
	 */
	private static ThreadLocal<Stack<DataSource>> transactionContext = new ThreadLocal<>();

	/**
	 * 获取数据源
	 * 
	 * @return 数据源
	 */
	public static DataSource getDataSource() {
		DataSource dataSource = null;
		/** 数据源入栈 */
		Stack<DataSource> stack = getStack();
		if (!stack.empty()) {
			dataSource = stack.peek();
		}

		logger.debug("get currentThread datasource : " + dataSource);
		return dataSource;
	}

	/**
	 * 将当前数据源推入栈
	 * 
	 * @param dataSource
	 *            数据源
	 */
	public static void pushCurrentDataSource(DataSource dataSource) {
		Stack<DataSource> stack = getStack();
		stack.push(dataSource);
		logger.debug("bind currentThread datasource : " + dataSource.toString());
	}

	/**
	 * 当前数据源从栈中取出
	 */
	public static void popCurrentDataSource() {
		Stack<DataSource> stack = getStack();
		if (!stack.empty()) {
			logger.debug("release currentThread datasource : " + stack.pop());
		}
	}

	/**
	 * 数据源容器
	 * 
	 * @return 数据源堆栈
	 */
	private static Stack<DataSource> getStack() {
		if (transactionContext.get() == null) {
			transactionContext.set(new Stack<>());
		}
		return transactionContext.get();
	}
}
