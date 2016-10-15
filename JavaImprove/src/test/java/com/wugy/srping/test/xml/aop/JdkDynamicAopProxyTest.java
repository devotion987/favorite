package com.wugy.srping.test.xml.aop;

import org.junit.Test;

import com.wugy.spring.xml.aop.AdvisedSupport;
import com.wugy.spring.xml.aop.JdkDynamicAopProxy;
import com.wugy.spring.xml.aop.TargetSource;
import com.wugy.spring.xml.ioc.beans.context.ApplicationContext;
import com.wugy.spring.xml.ioc.beans.context.ClassPathXmlApplicationContext;
import com.wugy.srping.test.xml.HelloWorldService;
import com.wugy.srping.test.xml.HelloWorldServiceImpl;

public class JdkDynamicAopProxyTest {

	@Test
	public void testInterceptor() {
		try {
			// --------- helloWorldService without AOP
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext("aop.xml");
			HelloWorldService helloWorldService = applicationContext.getBean("helloWorldService",
					HelloWorldService.class);
			helloWorldService.helloWorld();

			// --------- helloWorldService with AOP
			// 1. 设置被代理对象(Joinpoint)
			AdvisedSupport advisedSupport = new AdvisedSupport();
			TargetSource targetSource = new TargetSource(HelloWorldServiceImpl.class, helloWorldService,
					HelloWorldService.class);
			advisedSupport.setTargetSource(targetSource);

			// 2. 设置拦截器(Advice)
			TimerInterceptor timerInterceptor = new TimerInterceptor();
			advisedSupport.setMethodInterceptor(timerInterceptor);

			// 3. 创建代理(Proxy)
			JdkDynamicAopProxy jdkDynamicAopProxy = new JdkDynamicAopProxy(advisedSupport);
			HelloWorldService helloWorldServiceProxy = jdkDynamicAopProxy.getProxy();

			// 4. 基于AOP的调用
			helloWorldServiceProxy.helloWorld();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
