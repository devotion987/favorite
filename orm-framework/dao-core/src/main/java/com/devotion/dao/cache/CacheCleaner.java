package com.devotion.dao.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能描述： 缓存清除
 *
 * @author www.ibm.com
 * @version 1.0.0
 */
public class CacheCleaner extends Thread {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheCleaner.class);

    /**
     * 清除间隔
     */
    private long cleanInterval;

    /**
     * 此处去掉了默认值false
     */
    private boolean sleep;

    /**
     * 构造方法
     *
     * @param cleanInterval 清除间隔
     */
    public CacheCleaner(long cleanInterval) {
        this.cleanInterval = cleanInterval;
        setName(this.getClass().getName());
        setDaemon(true);
    }

    /**
     * 设置清除间隔
     *
     * @param cleanInterval 清除间隔
     */
    public void setCleanInterval(long cleanInterval) {
        this.cleanInterval = cleanInterval;
        synchronized (this) {
            if (this.sleep) {
                interrupt();
            }
        }
    }

    /**
     * 线程执行
     */
    @Override
    public void run() {
        while (true) {
            try {
                CacheFactory cacheFactory = CacheFactory.getInstance();
                /** 生成缓存对象数组 */
                Object[] objIdArr = cacheFactory.getCacheIds();
                for (Object objId : objIdArr) {
                    Cache cache = cacheFactory.getCache(objId);
                    if (cache != null) {
                        cache.clean();
                    }
                    /** 放弃执行，进入可执行状态 */
                    yield();
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
            this.sleep = true;
            try {
                /** 线程睡眠 */
                sleep(this.cleanInterval);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            } finally {
                this.sleep = false;
            }
        }
    }
}
