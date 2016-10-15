package com.wugy.javaPattern.responsibilityChain.old;

public class Husband implements IHandler {

	@Override
	public void handlerMessage(IWomen women) {
		System.out.println("妻子的请求是：" + women.getRequest());
	}

}
