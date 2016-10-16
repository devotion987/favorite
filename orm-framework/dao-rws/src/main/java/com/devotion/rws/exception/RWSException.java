package com.devotion.rws.exception;

/**
 * 读写分离 异常处理
 */
public class RWSException extends RuntimeException {

	/**
	 * 序列化对象版本控制
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 构造函数
	 */
	public RWSException() {
		super();
	}

	/**
	 * 构造函数重载
	 * 
	 * @param message
	 *            异常信息
	 * @param cause
	 *            异常原因
	 */
	public RWSException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 构造函数重载
	 * 
	 * @param message
	 *            异常信息
	 */
	public RWSException(String message) {
		super(message);
	}

	/**
	 * 构造函数重载
	 * 
	 * @param cause
	 *            异常原因
	 */
	public RWSException(Throwable cause) {
		super(cause);
	}
}
