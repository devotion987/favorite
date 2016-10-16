package com.devotion.dao.cache;

/**
 * 功能描述： Cache接口
 */
public interface Cache {

    /**
     * 将对象加入缓存
     *
     * @param objId 对象ID
     * @param obj   对象
     */
    void put(Object objId, Object obj);

    /**
     * 从缓存中获取对象
     *
     * @param objId 对象ID
     * @return 缓存对象ID
     */
    Object get(Object objId);

    /**
     * 从缓存中删除对象
     *
     * @param objId 对象ID
     */
    void remove(Object objId);

    /**
     * 获取缓存中对象的总数
     *
     * @return 缓存中对象的总数
     */
    int size();

    /**
     * 移除缓存中所有对象
     */
    void clear();

    /**
     * 移除缓存中过期对象
     */
    void clean();

    /**
     * 获取该缓存实例的缓存命中率
     *
     * @return 命中率
     */
    long getCacheHits();

    /**
     * 获取该缓存实例的缓存丢失率
     *
     * @return 失效率
     */
    long getCacheMisses();

    /**
     * 获取该缓存实例的put操作总次数
     *
     * @return put总次数
     */
    long getTotalPuts();

    /**
     * 获取该缓存实例的remove操作总次数
     *
     * @return remove总次数
     */
    long getTotalRemoves();

    /**
     * 重置命中率等计数为0
     */
    void resetInfo();

    /**
     * 获取该缓存实例消耗内存总数
     *
     * @return 消耗内存总数
     */
    long getMemorySize();

    /**
     * 设置缓存配置
     *
     * @param cacheConfig 缓存配置
     */
    void setCacheConfig(CacheConfig cacheConfig);

    /**
     * 获取缓存配置
     *
     * @return 缓存配置
     */
    CacheConfig getCacheConfig();

}