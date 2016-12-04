package com.devotion.blue.cache;

import com.devotion.blue.cache.impl.JEhCache;
import com.jfinal.plugin.IPlugin;


public class JCachePlugin implements IPlugin {

    public JCachePlugin() {
        CacheManager.me().init(JEhCache.class);
    }

    public JCachePlugin(Class<? extends ICache> clazz) {
        if (clazz == null) {
            throw new RuntimeException("clazz must not be null");
        }
        CacheManager.me().init(clazz);
    }

    public com.jfinal.plugin.activerecord.cache.ICache getCache() {
        return CacheManager.me().getCache();

    }

    @Override
    public boolean start() {
        return true;
    }

    @Override
    public boolean stop() {
        CacheManager.me().destroy();
        return true;
    }

}
