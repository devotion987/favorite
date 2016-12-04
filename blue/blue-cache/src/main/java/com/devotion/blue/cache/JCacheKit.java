package com.devotion.blue.cache;

import com.jfinal.plugin.ehcache.IDataLoader;

import java.util.List;


public class JCacheKit {

    public static <T> T get(String cacheName, Object key) {
        return CacheManager.me().getCache().get(cacheName, key);
    }

    public static void put(String cacheName, Object key, Object value) {
        CacheManager.me().getCache().put(cacheName, key, value);
    }

    public static void remove(String cacheName, Object key) {
        CacheManager.me().getCache().remove(cacheName, key);
    }

    public static void removeAll(String cacheName) {
        CacheManager.me().getCache().removeAll(cacheName);
    }

    public static List<?> getKeys(String cacheName) {
        return CacheManager.me().getCache().getKeys(cacheName);
    }

    public static <T> T get(String cacheName, Object key, IDataLoader dataLoader) {
        return CacheManager.me().getCache().get(cacheName, key, dataLoader);
    }

}
