package com.devotion.blue.cache.impl;

import java.util.List;

import com.devotion.blue.cache.ICache;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;


public class JEhCache implements ICache {

	@Override
	public <T> T get(String cacheName, Object key) {
		return CacheKit.get(cacheName, key);
	}

	@Override
	public void put(String cacheName, Object key, Object value) {
		CacheKit.put(cacheName, key, value);
	}

	@Override
	public void remove(String cacheName, Object key) {
		CacheKit.remove(cacheName, key);
	}

	@Override
	public void removeAll(String cacheName) {
		CacheKit.removeAll(cacheName);
	}

	@Override
	public List<?> getKeys(String cacheName) {
		return CacheKit.getKeys(cacheName);
	}

	@Override
	public <T> T get(String cacheName, Object key, IDataLoader dataLoader) {
		return CacheKit.get(cacheName, key, dataLoader);
	}
}
