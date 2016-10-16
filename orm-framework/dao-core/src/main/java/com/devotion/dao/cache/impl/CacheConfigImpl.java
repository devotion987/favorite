package com.devotion.dao.cache.impl;

import com.devotion.dao.cache.CacheConfig;

/**
 * 功能描述： 缓存配置实现
 */
public class CacheConfigImpl implements CacheConfig {
	/**
	 * 缓存ID
	 */
	private Object cacheId;

	/**
	 * 缓存描述
	 */
	private String cacheDesc;

	/**
	 * 生存时间
	 */
	private long ttl;

	/**
	 * 缓存容量最大值
	 */
	private long maxMemorySize;

	/**
	 * 最大容量
	 */
	private int maxSize;

	/**
	 * 缓存算法
	 */
	private String algorithm;

	/**
	 * 构造方法
	 * 
	 * @param cacheId
	 *            缓存ID
	 * @param cacheDesc
	 *            缓存描述
	 * @param ttl
	 *            生存时间
	 * @param maxMemorySize
	 *            缓存容量最大值
	 * @param maxSize
	 *            最大容量
	 * @param algorithm
	 *            缓存算法
	 */
	public CacheConfigImpl(Object cacheId, String cacheDesc, long ttl, long maxMemorySize, int maxSize,
			String algorithm) {
		this.cacheId = cacheId;
		this.cacheDesc = cacheDesc;
		this.ttl = ttl < 0 ? 0 : ttl;
		this.maxMemorySize = maxMemorySize < 0 ? 0 : maxMemorySize;
		this.maxSize = maxSize < 0 ? 0 : maxSize;
		this.algorithm = algorithm;
	}

	/**
	 * 获取缓存ID
	 * 
	 * @return 缓存ID
	 */
	public Object getCacheId() {
		return this.cacheId;
	}

	/**
	 * 获取缓存描述
	 * 
	 * @return 缓存描述
	 */
	public String getCacheDesc() {
		return this.cacheDesc;
	}

	/**
	 * 获取生存时间
	 * 
	 * @return 生存时间
	 */
	public long getTimeToLive() {
		return this.ttl;
	}

	/**
	 * 获取缓存容量最大值
	 * 
	 * @return 缓存容量最大值
	 */
	public long getMaxMemorySize() {
		return this.maxMemorySize;
	}

	/**
	 * 获取最大容量
	 * 
	 * @return 最大容量
	 */
	public int getMaxSize() {
		return this.maxSize;
	}

	/**
	 * 获取缓存算法
	 * 
	 * @return 缓存算法
	 */
	public String getAlgorithm() {
		return this.algorithm;
	}
}
