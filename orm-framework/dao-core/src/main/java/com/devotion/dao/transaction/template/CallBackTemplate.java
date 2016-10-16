package com.devotion.dao.transaction.template;

/**
 * 事务回调
 * 
 * @param <T>
 *            类型
 */
public interface CallBackTemplate<T> {

	/**
	 * 
	 * 功能描述: <br>
	 * 回调接口
	 * 
	 * @return 回调结果
	 */
	T invoke();
}
