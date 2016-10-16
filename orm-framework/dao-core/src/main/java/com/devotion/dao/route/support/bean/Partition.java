package com.devotion.dao.route.support.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 分区定义<br>
 */
@XStreamAlias("partition")
public class Partition {

	/** 分区名称 */
	@XStreamAlias("name")
	@XStreamAsAttribute
	private String name;

	/** 分区组 */
	@XStreamImplicit
	private List<ShardGroup> shardGroups;

	/**
	 * 根据传入参数定位所属分区
	 * 
	 * @param paramValue
	 *            分区参数
	 * @return 分区
	 */
	public Shard find(long paramValue) {
		for (ShardGroup shardGroup : shardGroups) {
			Shard shard = shardGroup.contains(paramValue);
			if (shard != null) {
				return shard;
			}
		}
		return null;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 获取分区名称
	 * 
	 * @return 分区名称
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 设置分区名称
	 * 
	 * @param name
	 *            设置分区名称
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 获取分区列表
	 * 
	 * @return 分区列表
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public List<ShardGroup> getShardGroups() {
		return shardGroups;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 设置分区列表
	 * 
	 * @param shardGroups
	 *            分区列表
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public void setShardGroups(List<ShardGroup> shardGroups) {
		this.shardGroups = shardGroups;
	}
}
