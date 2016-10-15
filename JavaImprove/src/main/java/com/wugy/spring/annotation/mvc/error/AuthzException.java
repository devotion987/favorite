package com.wugy.spring.annotation.mvc.error;

/**
 * 授权异常（当权限无效时抛出）
 *
 * @author devotion
 */
public class AuthzException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AuthzException() {
		super();
	}

	public AuthzException(String message) {
		super(message);
	}

	public AuthzException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthzException(Throwable cause) {
		super(cause);
	}
}
