package com.devotion.dao.client.support;

import org.springframework.beans.factory.InitializingBean;

import com.devotion.dao.DataBaseOperation;
import com.devotion.dao.client.DaoClient;

/**
 * 客户端API<br>
 */
public class DefaultDaoClient extends DataBaseOperation implements DaoClient, InitializingBean {

	/**
	 * 初始化
	 * 
	 * @exception Exception
	 *                异常
	 */
	public void afterPropertiesSet() throws Exception {
		parseResource();
	}
}
