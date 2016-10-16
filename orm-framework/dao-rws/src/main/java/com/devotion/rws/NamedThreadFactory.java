package com.devotion.rws;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 功能描述：线程工厂类
 */
public class NamedThreadFactory implements ThreadFactory {

    /**
     * 定义原子Integer变量：序列号
     */
    private static final AtomicInteger POOL_SEQ = new AtomicInteger(1);

    /**
     * 定义原子Integer变量：线程数目
     */
    private final AtomicInteger mThreadNum = new AtomicInteger(1);

    /**
     * 前置符号
     */
    private final String mPrefix;

    /**
     * 是否是守护线程
     */
    private final boolean mDaemon;

    /**
     * 定义线程组
     */
    private final ThreadGroup mGroup;

    /**
     * 构造方法
     */
    public NamedThreadFactory() {
        this("pool-" + POOL_SEQ.getAndIncrement(), false);
    }

    /**
     * 构造方法
     *
     * @param prefix 前置符号
     */
    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    /**
     * 构造方法
     *
     * @param prefix 前置符号
     * @param daemon  是否是守护线程
     */
    public NamedThreadFactory(String prefix, boolean daemon) {
        mPrefix = prefix + "-thread-";
        mDaemon = daemon;
        SecurityManager s = System.getSecurityManager();
        mGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }

    /**
     * 启动新线程
     *
     * @param runnable 新线程
     * @return 线程
     */
    @Override
    public Thread newThread(Runnable runnable) {
        String name = mPrefix + mThreadNum.getAndIncrement();
        Thread ret = new Thread(mGroup, runnable, name, 0);
        ret.setDaemon(mDaemon);
        return ret;
    }

    /**
     * 获取线程组
     *
     * @return 线程组
     */
    public ThreadGroup getThreadGroup() {
        return mGroup;
    }
}