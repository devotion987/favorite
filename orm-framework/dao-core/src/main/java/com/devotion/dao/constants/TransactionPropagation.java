package com.devotion.dao.constants;

/**
 * 事务的传播行为
 */
public enum TransactionPropagation {

	/**
	 * 如果当前没有事务，则新建一个事务，如果已经存在一个事务中，加入到这个事务中。
	 */
	PROPAGATION_REQUIRES_NEW(3), PROPAGATION_REQUIRED(0);

	/**
	 * 键值
	 */
	private int value;

	/**
	 * 事务传播
	 * 
	 * @param value
	 *            传播级别
	 */
	private TransactionPropagation(int value) {
		this.value = value;
	}

	/**
	 * 返回当前的事务传播机制
	 * 
	 * @return 当前的事务传播机制
	 */
	public int getPropagationBehavior() {
		return this.value;
	}
}
