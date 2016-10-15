package com.wugy.spring.annotation.mvc.entity;

import java.io.InputStream;

import com.wugy.spring.annotation.ioc.BaseBean;

/**
 * 封装文件上传对象相关属性
 *
 * @author devotion
 */
public class Multipart extends BaseBean {

	private static final long serialVersionUID = 1L;
	private String fieldName;
	private String fileName;
	private long fileSize;
	private String contentType;
	private InputStream inputStream;

	public Multipart(String fieldName, String fileName, long fileSize, String contentType, InputStream inputStream) {
		this.fieldName = fieldName;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.contentType = contentType;
		this.inputStream = inputStream;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getFileName() {
		return fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public String getContentType() {
		return contentType;
	}

	public InputStream getInputStream() {
		return inputStream;
	}
}
