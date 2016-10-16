package com.devotion.dao.cache;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.devotion.dao.cache.impl.Configurator;

/**
 * 功能描述：缓存工厂类
 */
public class CacheFactory {

    /**
     * 初始化缓存工厂
     */
    private static CacheFactory cacheFactory = new CacheFactory();

    /**
     * 初始化缓存Map
     */
    private final Map<Object, Cache> cacheMap = new HashMap<>();

    /**
     * 缓存清除
     */
    private CacheCleaner cleaner;

    /**
     * 构造方法
     */
    private CacheFactory() {
        // default 30sec
        this.cleaner = new CacheCleaner(Configurator.CLEAN_INTERVAL);
        this.cleaner.start();
    }

    /**
     * 获取缓存工厂实例
     *
     * @return 缓存工厂实例
     */
    public static CacheFactory getInstance() {
        return cacheFactory;
    }

    /**
     * 装载配置信息
     *
     * @param in 输入流
     */
    public void loadConfig(InputStream in) {
        Configurator.loadConfig(in);
    }

    /**
     * 添加缓存
     *
     * @param cache 缓存
     */
    public void addCache(Cache cache) {
        if (cache == null) {
            throw new CacheException("cache is null");
        }
        /** 获取缓存配置 */
        CacheConfig cacheConfig = cache.getCacheConfig();
        if (cacheConfig == null) {
            throw new CacheException("cache config is null");
        }
        Object cacheId = cacheConfig.getCacheId();
        if (cacheId == null) {
            throw new CacheException("config.getCacheId() is null");
        }
        /** 缓存中已存在则抛出提示，不存在则添加 */
        synchronized (this.cacheMap) {
            if (this.cacheMap.containsKey(cacheId)) {
                throw new CacheException("Cache id:" + cacheId + " exists");
            }
            this.cacheMap.put(cacheId, cache);
        }
    }

    /**
     * 获取指定缓存
     *
     * @param cacheId 缓存ID
     * @return 缓存
     */
    public Cache getCache(Object cacheId) {
        if (cacheId == null) {
            throw new CacheException("cacheId is null");
        }
        synchronized (this.cacheMap) {
            return this.cacheMap.get(cacheId);
        }
    }

    /**
     * 清除指定缓存
     *
     * @param cacheId 缓存ID
     */
    public void removeCache(Object cacheId) {
        if (cacheId == null) {
            throw new CacheException("cacheId is null");
        }

        synchronized (this.cacheMap) {
            this.cacheMap.remove(cacheId);
        }
    }

    /**
     * 获取缓存ID组
     *
     * @return 缓存ID
     */
    public Object[] getCacheIds() {
        synchronized (this.cacheMap) {
            return this.cacheMap.keySet().toArray();
        }
    }

    /**
     * 设置缓存清除时间间隔
     *
     * @param time 时间
     */
    public void setCleanInterval(long time) {
        this.cleaner.setCleanInterval(time);
    }

}
