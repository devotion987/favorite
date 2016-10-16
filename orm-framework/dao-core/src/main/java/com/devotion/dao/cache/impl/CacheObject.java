package com.devotion.dao.cache.impl;

/**
 * 功能描述：缓存对象
 */
public class CacheObject {

	/** 缓存ID */
	private static long cacheID;

	/** 声明缓存对象 */
	private Object obj;

	/** 声明缓存对象ID */
	private Object objId;

	/** 声明连接数目 */
	private long accessCount;

	/**
	 * 创建时间
	 */
	private long createTime;

	/**
	 * 最近一次连接时间
	 */
	private long lastAccessTime;

	/**
	 * 对象大小
	 */
	private int objSize;

	/**
	 * 对象ID
	 */
	private long id;

	/**
	 * 构造方法
	 * 
	 * @param objId
	 *            对象ID
	 */
	CacheObject(Object objId) {
		this.objId = objId;
		this.obj = null;
		this.createTime = System.currentTimeMillis();
		this.accessCount = 1;
		this.lastAccessTime = this.createTime;
		this.objSize = 0;
		this.id = nextId();
	}

	/**
	 * 获取对象
	 * 
	 * @return 对象
	 */
	Object getObject() {
		return this.obj;
	}

	/**
	 * 设置对象
	 * 
	 * @param obj
	 *            对象
	 */
	void setObject(Object obj) {
		this.obj = obj;
	}

	/**
	 * 获取对象ID
	 * 
	 * @return 对象ID
	 */
	Object getObjectId() {
		return this.objId;
	}

	/**
	 * 获取连接数
	 * 
	 * @return 连接数
	 */
	long getAccessCount() {
		return this.accessCount;
	}

	/**
	 * 获取创建时间
	 * 
	 * @return 创建时间
	 */
	long getCreateTime() {
		return this.createTime;
	}

	/**
	 * 获取最近一次连接时间
	 * 
	 * @return 最近一次连接时间
	 */
	long getLastAccessTime() {
		return this.lastAccessTime;
	}

	/**
	 * 获取对象大小
	 * 
	 * @return 对象大小
	 */
	long getObjectSize() {
		return this.objSize;
	}

	/**
	 * 设置对象大小
	 * 
	 * @param objSize
	 *            对象大小
	 */
	void setObjectSize(int objSize) {
		this.objSize = objSize;
	}

	/**
	 * 更新最新信息
	 */
	void updateStatistics() {
		this.accessCount++;
		this.lastAccessTime = System.currentTimeMillis();
		this.id = nextId();
	}

	/**
	 * 缓存对象重置
	 */
	void reset() {
		this.obj = null;
		this.objSize = 0;
		this.createTime = System.currentTimeMillis();
		this.accessCount = 1;
		this.lastAccessTime = this.createTime;

		this.id = nextId();
	}

	/**
	 * 获取ID
	 * 
	 * @return ID
	 */
	long getId() {
		return this.id;
	}

	/**
	 * 拼接缓存对象字符串
	 * 
	 * @return 拼接后字符串
	 */
	public String toString() {
		return "id:" + this.objId + " createTime:" + this.createTime + " lastAccess:" + this.lastAccessTime
				+ " accessCount:" + this.accessCount + " size:" + this.objSize + " object:" + this.obj;
	}

	/**
	 * 获取下一个缓存对象ID
	 * 
	 * @return 缓存ID
	 */
	private static synchronized long nextId() {
		return ++cacheID;
	}
}
