package com.wugy.spring.xml.aop;

public interface PointcutAdvisor extends Advisor {

	Pointcut getPointcut();
}
