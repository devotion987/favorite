package com.wugy.javaPattern.responsibilityChain.old;

public class Father implements IHandler {

	@Override
	public void handlerMessage(IWomen women) {
		System.out.println("女儿的请求是：" + women.getRequest());
	}

}
