package com.wugy.spring.xml.aop;

import org.aopalliance.aop.Advice;

public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

	private AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

	private Advice advice;

	public void setAdvice(Advice advice) {
		this.advice = advice;
	}

	public void setExpression(String expression) {
		pointcut.setExpression(expression);
	}

	@Override
	public Advice getAdvice() {
		return advice;
	}

	@Override
	public Pointcut getPointcut() {
		return pointcut;
	}

}
