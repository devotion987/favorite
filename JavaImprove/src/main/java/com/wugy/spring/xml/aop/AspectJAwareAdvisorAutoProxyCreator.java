package com.wugy.spring.xml.aop;

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;

import com.wugy.spring.xml.ioc.beans.BeanPostProcessor;
import com.wugy.spring.xml.ioc.beans.factory.AbstractBeanFactory;
import com.wugy.spring.xml.ioc.beans.factory.BeanFactory;
import com.wugy.spring.xml.ioc.beans.factory.BeanFactoryAware;

public class AspectJAwareAdvisorAutoProxyCreator implements BeanPostProcessor, BeanFactoryAware {

	private AbstractBeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws Exception {
		this.beanFactory = (AbstractBeanFactory) beanFactory;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
		if (bean instanceof AspectJExpressionPointcutAdvisor || bean instanceof MethodInterceptor)
			return bean;
		List<AspectJExpressionPointcutAdvisor> advisors = beanFactory
				.getBeanForType(AspectJExpressionPointcutAdvisor.class);
		for (AspectJExpressionPointcutAdvisor advisor : advisors) {
			if (advisor.getPointcut().getClassFilter().matches(bean.getClass())) {
				ProxyFactory advisedSupport = new ProxyFactory();
				advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
				advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());

				TargetSource targetSource = new TargetSource(bean.getClass(), bean, bean.getClass().getInterfaces());
				advisedSupport.setTargetSource(targetSource);

				return advisedSupport.getProxy();
			}
		}
		return bean;
	}

}
