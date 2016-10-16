package com.devotion.dao.cache.impl;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.devotion.dao.cache.Cache;
import com.devotion.dao.cache.CacheConfig;
import com.devotion.dao.cache.CacheException;
import com.devotion.dao.utils.Utils;

/**
 * 功能描述： 默认缓存实现类
 */
public class DefCacheImpl implements Cache {

    /**
     * 缓存对象Map
     */
    private Map<Object, CacheObject> map;

    /**
     * 缓存对象TreeMap
     */
    private TreeMap<CacheObject, CacheObject> treeMap;

    /**
     * 缓存配置
     */
    private CacheConfig config;

    /**
     * 缓存容量
     */
    private long memorySize;

    /**
     * 命中
     */
    private long hit;

    /**
     * 缺失
     */
    private long miss;

    /**
     * 放入
     */
    private long put;

    /**
     * 移除
     */
    private long remove;

    /**
     * 对象放入cache
     *
     * @param objId 对象ID
     * @param obj   对象
     */
    public void put(Object objId, Object obj) {
        if (objId == null) {
            throw new CacheException("objId is null");
        }
        /** 1、比较对象与缓存配置的内存的大小*/
        int objSize;
        try {
            objSize = this.config.getMaxMemorySize() > 0 ? Utils.size(obj) : 0;
        } catch (IOException e) {
            throw new CacheException(e.getMessage(), e);
        }
        /** 2、检查是否溢出 */
        checkOverflow(objSize);
        /** 3、对象放入缓存*/
        CacheObject co = this.map.get(objId);
        if (co != null) {
            this.treeMap.remove(co);
            resetCacheObject(co);
        } else {
            co = newCacheObject(objId);
        }
        /** 4、调整相应的缓存容量 */
        this.put++;
        co.setObject(obj);
        co.setObjectSize(objSize);
        this.memorySize = this.memorySize + objSize;
        this.treeMap.put(co, co);
    }

    /**
     * 判断缓存缺失或命中，获取指定对象
     *
     * @param objId 对象ID
     * @return 缓存对象
     */
    public Object get(Object objId) {
        if (objId == null) {
            throw new CacheException("objId is null");
        }

        CacheObject co = this.map.get(objId);
        Object obj = co == null ? null : co.getObject();
        /** 1、缓存对象为空 */
        if (null == obj) {
            this.miss++;
            return null;
        }
        /** 2、缓存对象不为空 */
        /** 判断是否失效，失效则移除并增加缺失率计数 */
        if (!valid(co)) {
            remove(co.getObjectId());
            this.miss++;
            return null;
        }
        /** 更新缓存对象，treeMap */
        this.treeMap.remove(co);
        co.updateStatistics();
        this.treeMap.put(co, co);
        /** 命中率计数增加 */
        this.hit++;
        return obj;
    }

    /**
     * 移除指定对象
     *
     * @param objId 对象ID
     */
    public void remove(Object objId) {
        if (objId == null) {
            throw new CacheException("objId is null");
        }
        /** 移除对象，并增加移除计数 */
        CacheObject co = this.map.remove(objId);
        this.remove++;
        if (co != null) {
            this.treeMap.remove(co);
            resetCacheObject(co);
        }
    }

    /**
     * 返回Map的大小
     *
     * @return Map的大小
     */
    public int size() {
        return this.map.size();
    }

    /**
     * 清除临时缓存内容
     */
    public void clear() {
        this.map.clear();
        this.treeMap.clear();
        this.memorySize = 0;
    }

    /**
     * 获取缓存配置
     *
     * @return 缓存配置
     */
    public CacheConfig getCacheConfig() {
        return this.config;
    }

    /**
     * 设置缓存配置参数
     *
     * @param config 缓存配置
     */
    public void setCacheConfig(CacheConfig config) {
        if (config == null) {
            throw new CacheException("config is null");
        }
        this.config = config;
        this.map = new HashMap<>(this.config.getMaxSize());
        this.memorySize = 0;
        Comparator<CacheObject> comparator = CacheAlgorithm.getAlgorithmComparator(this.config.getAlgorithm());
        this.treeMap = new TreeMap<>(comparator);
    }

    /**
     * 清除缓存
     */
    public void clean() {
        if (this.config.getTimeToLive() == 0) {
            return;
        }
        Object[] objArr;
        synchronized (this) {
            objArr = this.map.values().toArray();
        }

        for (Object obj : objArr) {
            CacheObject co = (CacheObject) obj;
            if (!valid(co)) {
                remove(co.getObjectId());
            }
        }
    }

    /**
     * 溢出检测
     *
     * @param objSize 对象大小
     */
    private void checkOverflow(int objSize) {
        int maxSize = config.getMaxSize();
        long maxMemorySize = config.getMaxMemorySize();
        int mapSize = map.size();
        while ((maxSize > 0 && mapSize + 1 > maxSize)
                || (maxMemorySize > 0 && this.memorySize + objSize > maxMemorySize)) {
            CacheObject co = this.treeMap.size() == 0 ? null : this.treeMap.remove(this.treeMap.firstKey());
            if (co != null) {
                this.map.remove(co.getObjectId());
                resetCacheObject(co);
            }
        }
    }

    /**
     * 将新缓存对象放入Map
     *
     * @param objId 对象ID
     * @return 缓存对象
     */
    private CacheObject newCacheObject(Object objId) {
        CacheObject co = new CacheObject(objId);
        this.map.put(objId, co);
        return co;
    }

    /**
     * 判断缓存是否失效
     *
     * @param co 缓存对象
     * @return 是否失效
     */
    private boolean valid(CacheObject co) {
        long curTime = System.currentTimeMillis();
        long timeToLive = config.getTimeToLive();
        return (timeToLive == 0 || (co.getCreateTime() + timeToLive) >= curTime)
                && co.getObject() != null;
    }

    /**
     * 缓存对象重置
     *
     * @param co 缓存对象
     */
    private void resetCacheObject(CacheObject co) {
        this.memorySize = this.memorySize - co.getObjectSize();
        co.reset();
    }

    /**
     * 获取缓存命中的总数
     *
     * @return 缓存命中的总数
     */
    public long getCacheHits() {
        return this.hit;
    }

    /**
     * 获取缓存缺失的总数
     *
     * @return 缓存缺失的总数
     */
    public long getCacheMisses() {
        return this.miss;
    }

    /**
     * 获取存入对象的总数
     *
     * @return 存入对象的总数
     */
    public long getTotalPuts() {
        return this.put;
    }

    /**
     * 获取移除对象的总数
     *
     * @return 移除对象的总数
     */
    public long getTotalRemoves() {
        return this.remove;
    }

    /**
     * 重置所有信息
     */
    public void resetInfo() {
        this.hit = 0;
        this.miss = 0;
        this.put = 0;
        this.remove = 0;
    }

    /**
     * 获取缓存大小
     *
     * @return 缓存大小
     */
    public long getMemorySize() {
        return this.memorySize;
    }

}
