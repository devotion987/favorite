package com.wugy.spring.annotation.mvc.error;

/**
 * 认证异常（当非法访问时抛出）
 *
 * @author devotion
 */
public class AuthcException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AuthcException() {
		super();
	}

	public AuthcException(String message) {
		super(message);
	}

	public AuthcException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthcException(Throwable cause) {
		super(cause);
	}
}
