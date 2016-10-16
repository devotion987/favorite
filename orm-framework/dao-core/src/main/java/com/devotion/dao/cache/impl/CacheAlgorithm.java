package com.devotion.dao.cache.impl;

import com.devotion.dao.cache.CacheException;

import java.util.Comparator;

/**
 * 功能描述： CacheAlgorithm
 */
public class CacheAlgorithm {

    /**
     * 最近最少使用
     */
    public static final String LRU = "lru";

    /**
     * 最不经常使用
     */
    public static final String LFU = "lfu";

    /**
     * 先进先出
     */
    public static final String FIFO = "fifo";

    /**
     * 判断采用哪种缓存算法
     *
     * @param algorithm 缓存算法
     * @return 比较器
     */
    public static Comparator<CacheObject> getAlgorithmComparator(String algorithm) {
        switch (algorithm) {
            case LRU:
                return getLRUComparator();
            case LFU:
                return getLFUComparator();
            case FIFO:
                return getFIFOComparator();
            default:
                throw new CacheException("Unknown algorithm:" + algorithm);
        }
    }

    /**
     * 判断是否支持某种缓存算法
     *
     * @param algorithm 缓存算法
     * @return 是否支持
     */
    public static boolean isSupportAlgorithm(String algorithm) {
        boolean supportAlgorithm = false;
        if (LRU.equalsIgnoreCase(algorithm) || LFU.equalsIgnoreCase(algorithm) ||
                FIFO.equalsIgnoreCase(algorithm))
            supportAlgorithm = true;
        return supportAlgorithm;
    }

    /**
     * FIFO先进先出算法比较器
     *
     * @return 比较结果
     */
    private static Comparator<CacheObject> getFIFOComparator() {
        return (co1, co2) -> {
            long c1CreateTime = co1.getCreateTime();
            long c2CreateTime = co2.getCreateTime();
            long c1Id = co1.getId();
            long c2Id = co2.getId();
            return c1CreateTime < c2CreateTime ? -1 : c1CreateTime == c2CreateTime
                    ? (c1Id < c2Id ? -1 : (c1Id == c2Id ? 0 : 1)) : 1;
        };
//        return new Comparator<CacheObject>() {
//            @Override
//            /** 比较两个缓存对象，实现FIFO */
//            public int compare(CacheObject co1, CacheObject co2) {
//                return co1.getCreateTime() < co2.getCreateTime() ? -1
//                        : co1.getCreateTime() == co2.getCreateTime()
//                        ? (co1.getId() < co2.getId() ? -1 : (co1.getId() == co2.getId() ? 0 : 1)) : 1;
//            }
//        };
    }

    /**
     * LRU最近最小使用算法比较器
     *
     * @return 比较结果
     */
    private static Comparator<CacheObject> getLRUComparator() {
        return (co1, co2) -> {
            long c1LastTime = co1.getLastAccessTime();
            long c2LastTime = co2.getLastAccessTime();
            long c1Id = co1.getId();
            long c2Id = co2.getId();
            return c1LastTime < c2LastTime ? -1 : c1LastTime == c2LastTime
                    ? (c1Id < c2Id ? -1 : (c1Id == c2Id ? 0 : 1)) : 1;
        };
//        return new Comparator<CacheObject>() {
//            @Override
//            /** 比较两个缓存对象，实现LRU */
//            public int compare(CacheObject co1, CacheObject co2) {
//                return co1.getLastAccessTime() < co2.getLastAccessTime() ? -1
//                        : co1.getLastAccessTime() == co2.getLastAccessTime()
//                        ? (co1.getId() < co2.getId() ? -1 : (co1.getId() == co2.getId() ? 0 : 1)) : 1;
//            }
//        };
    }

    /**
     * LFU最不经常使用算法比较器
     *
     * @return 比较结果
     */
    private static Comparator<CacheObject> getLFUComparator() {
        return (co1, co2) -> {
            long c1AccessCount = co1.getAccessCount();
            long c2AccessCount = co2.getAccessCount();
            long c1Id = co1.getId();
            long c2Id = co2.getId();
            return c1AccessCount < c2AccessCount ? -1 : c1AccessCount == c2AccessCount
                    ? (c1Id < c2Id ? -1 : (c1Id == c2Id ? 0 : 1)) : 1;
        };
//        return new Comparator<CacheObject>() {
//            @Override
//            /** 比较两个缓存对象，实现LFU */
//            public int compare(CacheObject co1, CacheObject co2) {
//                return co1.getAccessCount() < co2.getAccessCount() ? -1
//                        : co1.getAccessCount() == co2.getAccessCount()
//                        ? (co1.getId() < co2.getId() ? -1 : (co1.getId() == co2.getId() ? 0 : 1)) : 1;
//            }
//        };
    }

    /**
     * 私有构造函数CacheAlgorithm
     */
    private CacheAlgorithm() {

    }

}
