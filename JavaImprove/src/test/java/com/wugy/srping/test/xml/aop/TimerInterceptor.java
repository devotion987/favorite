package com.wugy.srping.test.xml.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class TimerInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation invocation) {
		try {
			long time = System.nanoTime();
			System.out.println("Invocation of Method " + invocation.getMethod().getName() + " start!");
			Object proceed = invocation.proceed();
			System.out.println("Invocation of Method " + invocation.getMethod().getName() + " end!");
			System.out.println("It takes " + (System.nanoTime() - time) + " nanoseconds.");
			return proceed;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

}
