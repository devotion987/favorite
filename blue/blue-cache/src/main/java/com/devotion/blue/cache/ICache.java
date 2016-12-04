package com.devotion.blue.cache;

import com.jfinal.plugin.ehcache.IDataLoader;

import java.util.List;


public interface ICache extends com.jfinal.plugin.activerecord.cache.ICache {

    List<?> getKeys(String cacheName);

    void remove(String cacheName, Object key);

    void removeAll(String cacheName);

    <T> T get(String cacheName, Object key, IDataLoader dataLoader);

}
