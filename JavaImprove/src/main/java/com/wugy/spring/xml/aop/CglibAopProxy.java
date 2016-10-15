package com.wugy.spring.xml.aop;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CglibAopProxy extends AbstractAopProxy {

	public CglibAopProxy(AdvisedSupport advised) {
		super(advised);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getProxy() {
		Enhancer enhancer = new Enhancer();
		TargetSource targetSource = advised.getTargetSource();
		enhancer.setSuperclass(targetSource.getTargetClass());
		enhancer.setInterfaces(targetSource.getInterfaces());
		enhancer.setCallback(new DynamicAdvisedInterceptor(advised));
		return (T) enhancer.create();
	}

	private static class DynamicAdvisedInterceptor implements MethodInterceptor {

		private AdvisedSupport advised;

		private org.aopalliance.intercept.MethodInterceptor delegatedMethodInterceptor;

		public DynamicAdvisedInterceptor(AdvisedSupport advised) {
			this.advised = advised;
			this.delegatedMethodInterceptor = advised.getMethodInterceptor();
		}

		@Override
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
			MethodMatcher methodMatcher = advised.getMethodMatcher();
			TargetSource targetSource = advised.getTargetSource();
			if (null == methodMatcher || methodMatcher.matches(method, targetSource.getTargetClass()))
				return delegatedMethodInterceptor
						.invoke(new CglibMethodInvocation(targetSource.getTarget(), method, args, methodProxy));
			return new CglibMethodInvocation(targetSource.getTarget(), method, args, methodProxy).proceed();
		}

	}

	private static class CglibMethodInvocation extends ReflectiveMethodInvocation {

		private final MethodProxy methodProxy;

		public CglibMethodInvocation(Object target, Method method, Object[] arguments, MethodProxy methodProxy) {
			super(target, method, arguments);
			this.methodProxy = methodProxy;
		}

		@Override
		public Object proceed() throws Throwable {
			return this.methodProxy.invoke(this.target, this.arguments);
		}

	}

}
