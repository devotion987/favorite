package com.devotion.dao.cache;

/**
 * 功能描述： Cache接口类
 */
public interface CacheConfig {

    /**
     * 获取缓存ID
     *
     * @return 缓存ID
     */
    Object getCacheId();

    /**
     * 获取缓存描述
     *
     * @return 缓存描述
     */
    String getCacheDesc();

    /**
     * 获取生存时间
     *
     * @return 生存时间
     */
    long getTimeToLive();

    /**
     * 获取最大内存
     *
     * @return 最大内存
     */
    long getMaxMemorySize();

    /**
     * 获取最大容量
     *
     * @return 最大容量
     */
    int getMaxSize();

    /**
     * 获取缓存算法
     *
     * @return 缓存算法
     */
    String getAlgorithm();

}
