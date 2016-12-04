package com.devotion.blue.cache.impl;

import java.util.List;

import com.devotion.blue.cache.ICache;
import com.jfinal.plugin.ehcache.IDataLoader;


public class RedisCache implements ICache {

	@Override
	public <T> T get(String cacheName, Object key) {
		return null;
	}

	@Override
	public void put(String cacheName, Object key, Object value) {

	}

	@Override
	public List<?> getKeys(String cacheName) {
		return null;
	}

	@Override
	public void remove(String cacheName, Object key) {

	}

	@Override
	public void removeAll(String cacheName) {

	}

	@Override
	public <T> T get(String cacheName, Object key, IDataLoader dataLoader) {
		return null;
	}
}
