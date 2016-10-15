package com.wugy.spring.annotation.mvc.error;

/**
 * 上传异常（当文件上传失败时抛出）
 *
 * @author devotion
 */
public class UploadException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UploadException() {
		super();
	}

	public UploadException(String message) {
		super(message);
	}

	public UploadException(String message, Throwable cause) {
		super(message, cause);
	}

	public UploadException(Throwable cause) {
		super(cause);
	}
}
