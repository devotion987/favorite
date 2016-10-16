package com.devotion.dao.route.support.bean;

import javax.sql.DataSource;

/**
 * 结点对<br>
 */
public class EntryNode {

	/** 节点名称 */
	private String nodeName;

	/** 数据源 */
	private DataSource dataSource;

	/**
	 * 构造方法
	 * 
	 * @param nodeName
	 *            节点名称
	 * @param dataSource
	 *            数据源
	 */
	EntryNode(String nodeName, DataSource dataSource) {
		super();
		this.nodeName = nodeName;
		this.dataSource = dataSource;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 获取节点名称
	 * 
	 * @return 节点名称
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 设置节点名称
	 * 
	 * @param nodeName
	 *            节点名称
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 获取数据源
	 * 
	 * @return 数据源
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 设置数据源
	 * 
	 * @param dataSource
	 *            数据源
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
