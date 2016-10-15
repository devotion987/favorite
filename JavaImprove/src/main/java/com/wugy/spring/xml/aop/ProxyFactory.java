package com.wugy.spring.xml.aop;

public class ProxyFactory extends AdvisedSupport implements AopProxy {

	@Override
	public <T> T getProxy() {
		return createProxy().getProxy();
	}

	protected AopProxy createProxy() {
		return new CglibAopProxy(this);
	}

}
