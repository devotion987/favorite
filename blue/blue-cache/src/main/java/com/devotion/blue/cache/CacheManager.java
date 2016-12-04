package com.devotion.blue.cache;

public class CacheManager {

    private static CacheManager me = new CacheManager();

    private CacheManager() {
    }

    private ICache cache;

    public static CacheManager me() {
        return me;
    }

    void init(Class<? extends ICache> clazz) {
        try {
            cache = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    void destroy() {
    }

    public ICache getCache() {
        return cache;
    }
}
