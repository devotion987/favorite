package com.devotion.dao.route.support.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 分区组<br>
 */
@XStreamAlias("shard-group")
public class ShardGroup {

	/**
	 * 分区位置参数
	 */
	private static final int W1 = 10000;

	/** 分区哈希 */
	@XStreamAlias("shard-hash")
	@XStreamAsAttribute
	private int shardHash;

	/** 开始区域 */
	@XStreamAlias("start")
	@XStreamAsAttribute
	private String startArea;

	/** 结束区域 */
	@XStreamAlias("end")
	@XStreamAsAttribute
	private String endArea;

	/** 是否可写入 */
	@XStreamAlias("can-write")
	@XStreamAsAttribute
	private boolean canWrite;

	/** 起始 */
	@XStreamOmitField
	private long start;

	/** 结束 */
	@XStreamOmitField
	private long end;

	/** 分区 */
	@XStreamImplicit
	private List<Shard> shards;

	/**
	 * 构造函数
	 */
	private ShardGroup() {
		super();
		init();
	}

	/**
	 * 初始化分区位置
	 */
	private void init() {
		this.end = compute(this.endArea);
		this.start = compute(this.startArea);
	}

	/**
	 * 根据传入参数判断所属分区
	 * 
	 * @param paramValue
	 *            分区值
	 * @return 分区对象
	 */
	public Shard contains(long paramValue) {
		init();
		/** 判断是否落在区间内，结果为真则继续判断包含在哪个分区内 */
		if (start <= paramValue && paramValue < end) {
			for (Shard shard : shards) {
				if (shard.contains(this.shardHash, paramValue)) {
					return shard;
				}
			}
		}
		return null;
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
	public List<Shard> getShards() {
		return shards;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 设置分区列表
	 * 
	 * @param shards
	 *            分区列表
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public void setShards(List<Shard> shards) {
		this.shards = shards;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 获取分区hash值
	 * 
	 * @return 分区hash值
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public int getShardHash() {
		return shardHash;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 设置分区hash值
	 * 
	 * @param shardHash
	 *            分区hash值
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public void setShardHash(int shardHash) {
		this.shardHash = shardHash;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 获取开始区域
	 * 
	 * @return 开始区域
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public String getStartArea() {
		return startArea;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 设置开始区域
	 * 
	 * @param startArea
	 *            开始区域
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public void setStartArea(String startArea) {
		this.startArea = startArea;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 获取结束区域
	 * 
	 * @return 结束区域
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public String getEndArea() {
		return endArea;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 设置结束区域
	 * 
	 * @param endArea
	 *            结束区域
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public void setEndArea(String endArea) {
		this.endArea = endArea;
	}

	/**
	 * 计算分区位置
	 * 
	 * @param value
	 *            区域值
	 * @return 分区位置
	 */
	private long compute(String value) {
		try {
			return Long.valueOf(value);
		} catch (NumberFormatException e) {
			if (value != null) {
				String subIndex = value.substring(0, this.endArea.toUpperCase().indexOf("W"));
				return Long.valueOf(subIndex) * W1;
			}
		}
		return 0L;
	}

	/**
	 * 是否可写入
	 * 
	 * @return 是否可写入
	 */
	public boolean isCanWrite() {
		return canWrite;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 设置是否可写入
	 * 
	 * @param canWrite
	 *            是否可写入
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public void setCanWrite(boolean canWrite) {
		this.canWrite = canWrite;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 获取开始区域
	 * 
	 * @return 开始区域
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public long getStart() {
		return start;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 设置开始区域
	 * 
	 * @param start
	 *            开始区域
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public void setStart(long start) {
		this.start = start;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 获取结束区域
	 * 
	 * @return 结束区域
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public long getEnd() {
		return end;
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 设置结束区域
	 * 
	 * @param end
	 *            结束区域
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public void setEnd(long end) {
		this.end = end;
	}

}
