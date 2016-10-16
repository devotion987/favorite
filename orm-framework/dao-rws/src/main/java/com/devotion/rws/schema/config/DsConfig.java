package com.devotion.rws.schema.config;

import javax.sql.DataSource;

/**
 * 功能描述： 自定义的读写分离spring schema中的数据源配置
 */
public class DsConfig {

	/** 数据源名称 */
	private String name;

	/** 定义数据源信息 */
	private DataSource refDataSource;

	/** 定义权重 */
	private int weight;

	/** 定义类型 */
	private String type;

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 获取数据源名称
	 * 
	 * @return 数据源名称
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 设置数据源名称
	 * 
	 * @param name
	 *            数据源名称
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 获取权重
	 * 
	 * @return 权重
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 设置权重
	 * 
	 * @param weight
	 *            权重
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 获取数据源信息
	 * 
	 * @return 数据源信息
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public DataSource getRefDataSource() {
		return refDataSource;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 设置数据源信息
	 * 
	 * @param refDataSource
	 *            数据源信息
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public void setRefDataSource(DataSource refDataSource) {
		this.refDataSource = refDataSource;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 获取数据库类型
	 * 
	 * @return 数据库类型
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public String getType() {
		return type;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 设置数据库类型
	 * 
	 * @param type
	 *            数据库类型
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public void setType(String type) {
		this.type = type;
	}

}
