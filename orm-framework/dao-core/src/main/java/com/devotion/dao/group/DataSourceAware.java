package com.devotion.dao.group;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * 分组数据源类型处理类
 */
public class DataSourceAware implements DataSource {

	/**
	 * 获取日志记录器
	 * 
	 * @exception SQLException
	 *                SQL异常
	 * @return null
	 */
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	/**
	 * 设置日志记录器
	 * 
	 * @exception SQLException
	 *                SQL异常
	 * @param out
	 *            字符打印输出流
	 */
	public void setLogWriter(PrintWriter out) throws SQLException {

	}

	/**
	 * 设置登录失效时间
	 * 
	 * @exception SQLException
	 *                SQL异常
	 * @param seconds
	 *            失效时间
	 */
	public void setLoginTimeout(int seconds) throws SQLException {

	}

	/**
	 * 获取登录失效时间
	 * 
	 * @exception SQLException
	 *                SQL异常
	 * @return 失效时间
	 */
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	/**
	 * 如果调用此方法的对象实现接口参数，或者是实现接口参数的对象的直接或间接包装器，则返回 true。
	 * 
	 * @param iface
	 *            类对象
	 * @param <T>
	 *            泛型对象
	 * @exception SQLException
	 *                SQL异常
	 * @return null
	 */
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	/**
	 * 返回一个对象，该对象实现给定接口，以允许访问非标准方法或代理未公开的标准方法。
	 * 
	 * @param iface
	 *            类对象
	 * @exception SQLException
	 *                SQL异常
	 * @return false
	 */
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	/**
	 * 获取连接
	 * 
	 * @exception SQLException
	 *                SQL异常
	 * @return 连接对象
	 */
	public Connection getConnection() throws SQLException {
		return null;
	}

	/**
	 * 获取连接 重载方法
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @exception SQLException
	 *                SQL异常
	 * @return 连接对象
	 */
	public Connection getConnection(String username, String password) throws SQLException {
		return null;
	}

	/**
	 * 获取基类日志
	 * 
	 * @exception SQLFeatureNotSupportedException
	 *                SQL不支持类型异常
	 * @return 日志
	 */
	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

}
