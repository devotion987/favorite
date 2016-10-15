package com.wugy.spring.xml.aop;

abstract public class AbstractAopProxy implements AopProxy {

	protected AdvisedSupport advised;

	public AbstractAopProxy(AdvisedSupport advised) {
		this.advised = advised;
	}

}
