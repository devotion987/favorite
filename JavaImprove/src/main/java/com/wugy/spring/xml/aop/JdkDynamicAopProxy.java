package com.wugy.spring.xml.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * 基于jdk的动态代理
 * 
 * @author wugy 2016年4月17日
 *
 */
public class JdkDynamicAopProxy extends AbstractAopProxy implements InvocationHandler {

	public JdkDynamicAopProxy(AdvisedSupport advised) {
		super(advised);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getProxy() {
		return (T) Proxy.newProxyInstance(getClass().getClassLoader(), advised.getTargetSource().getInterfaces(), this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		MethodInterceptor methodInterceptor = advised.getMethodInterceptor();
		MethodMatcher methodMatcher = advised.getMethodMatcher();
		Object target = advised.getTargetSource().getTarget();
		if (null != methodMatcher && methodMatcher.matches(method, target.getClass()))
			return methodInterceptor.invoke(new ReflectiveMethodInvocation(target, method, args));
		return method.invoke(target, args);
	}

}
