package com.devotion.rws.schema.config;

import java.util.Map;

/**
 * 功能描述： 自定义的读写分离spring schema中的数据源组配置
 */
public class DsGroupConfig {

	/**
	 * 数据源ID
	 */
	private String id;

	/**
	 * 写库对应的数据源配置
	 */
	private DsConfig wrDsConfig;

	/**
	 * 读库对应的数据源配置列表
	 */
	private Map<String, DsConfig> roDsConfigs;

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 获取数据源id
	 * 
	 * @return 数据源id
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 设置数据源id
	 * 
	 * @param id
	 *            数据源id
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 获取写库对应的数据源配置
	 * 
	 * @return 写库对应的数据源配置
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public DsConfig getWrDsConfig() {
		return wrDsConfig;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 设置写库对应的数据源配置
	 * 
	 * @param wrDsConfig
	 *            写库对应的数据源配置
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public void setWrDsConfig(DsConfig wrDsConfig) {
		this.wrDsConfig = wrDsConfig;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 获取读库对应的数据源列表
	 * 
	 * @return 读库对应的数据源列表
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public Map<String, DsConfig> getRoDsConfigs() {
		return roDsConfigs;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 设置读库对应的数据源列表
	 * 
	 * @param roDsConfigs
	 *            读库对应的数据源列表
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public void setRoDsConfigs(Map<String, DsConfig> roDsConfigs) {
		this.roDsConfigs = roDsConfigs;
	}

}
