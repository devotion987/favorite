package com.wugy.spring.annotation.mvc.entity;

import java.util.ArrayList;
import java.util.List;

import com.wugy.spring.annotation.ioc.BaseBean;

/**
 * 封装批量文件上传对象
 *
 * @author devotion
 */
public class Multiparts extends BaseBean {

	private static final long serialVersionUID = 1L;
	private List<Multipart> multipartList = new ArrayList<Multipart>();

	public Multiparts(List<Multipart> multipartList) {
		this.multipartList = multipartList;
	}

	public int size() {
		return multipartList.size();
	}

	public List<Multipart> getAll() {
		return multipartList;
	}

	public Multipart getOne() {
		return size() == 1 ? multipartList.get(0) : null;
	}
}
