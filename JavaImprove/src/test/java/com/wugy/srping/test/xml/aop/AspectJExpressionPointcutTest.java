package com.wugy.srping.test.xml.aop;

import org.junit.Assert;
import org.junit.Test;

import com.wugy.spring.xml.aop.AspectJExpressionPointcut;
import com.wugy.srping.test.xml.HelloWorldService;
import com.wugy.srping.test.xml.HelloWorldServiceImpl;

public class AspectJExpressionPointcutTest {

	@Test
	public void testClassFilter() {
		String expression = "execution(* com.wugy.spring.*.*(..))";
		AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
		aspectJExpressionPointcut.setExpression(expression);
		boolean matches = aspectJExpressionPointcut.getClassFilter().matches(HelloWorldService.class);
		Assert.assertTrue(matches);
	}

	@Test
	public void testMethodInterceptor() {
		try {
			String expression = "execution(* com.wugy.spring.*.*(..))";
			AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
			aspectJExpressionPointcut.setExpression(expression);
			boolean matches = aspectJExpressionPointcut.getMethodMatcher()
					.matches(HelloWorldServiceImpl.class.getDeclaredMethod("helloWorld"), HelloWorldServiceImpl.class);
			Assert.assertTrue(matches);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
}
