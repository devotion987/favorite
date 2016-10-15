package com.wugy.spring.annotation.tx;

import java.lang.reflect.Method;

import com.wugy.spring.annotation.aop.proxy.Proxy;
import com.wugy.spring.annotation.aop.proxy.ProxyChain;
import com.wugy.spring.jdbc.DatabaseHelper;

/**
 * 事务代理
 *
 * @author devotion
 */
public class TransactionProxy implements Proxy {

	/**
	 * 定义一个线程局部变量，用于保存当前线程中是否进行了事务处理，默认为 false（未处理）
	 */
	private static final ThreadLocal<Boolean> flagContainer = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return false;
		}
	};

	@Override
	public Object doProxy(ProxyChain proxyChain) throws Throwable {
		Object result;
		// 判断当前线程是否进行了事务处理
		boolean flag = flagContainer.get();
		// 获取目标方法
		Method method = proxyChain.getTargetMethod();
		// 若当前线程未进行事务处理，且在目标方法上定义了 Transaction 注解，则说明该方法需要进行事务处理
		if (!flag && method.isAnnotationPresent(Transaction.class)) {
			// 设置当前线程已进行事务处理
			flagContainer.set(true);
			try {
				// 开启事务
				DatabaseHelper.beginTransaction();
				System.out.println("begin transaction");
				// 执行目标方法
				result = proxyChain.doProxyChain();
				// 提交事务
				DatabaseHelper.commitTransaction();
				System.out.println("commit transaction");
			} catch (Exception e) {
				// 回滚事务
				DatabaseHelper.rollbackTransaction();
				System.out.println("rollback transaction");
				throw e;
			} finally {
				// 移除线程局部变量
				flagContainer.remove();
			}
		} else {
			// 执行目标方法
			result = proxyChain.doProxyChain();
		}
		return result;
	}
}
