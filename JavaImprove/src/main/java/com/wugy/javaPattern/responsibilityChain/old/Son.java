package com.wugy.javaPattern.responsibilityChain.old;

public class Son implements IHandler {

	@Override
	public void handlerMessage(IWomen women) {
		System.out.println("母亲的请求是：" + women.getRequest());
	}

}
