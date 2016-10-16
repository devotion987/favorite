package com.devotion.dao.cache;

/**
 * 功能描述： 缓存异常类
 */
public class CacheException extends RuntimeException {
    /**
     * 序列化时保持版本兼容性
     */
    private static final long serialVersionUID = -193202262468464650L;

    /**
     * 构造方法
     */
    public CacheException() {
        super();
    }

    /**
     * 构造方法 重载
     *
     * @param message 异常信息
     */
    public CacheException(String message) {
        super(message);
    }

    /**
     * 构造方法 重载
     *
     * @param message 异常信息
     * @param cause   异常原因
     */
    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造方法 重载
     *
     * @param cause 异常原因
     */
    public CacheException(Throwable cause) {
        super(cause);
    }

}