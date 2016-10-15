package com.wugy.javaPattern.responsibilityChain.oldImprove;

import com.wugy.javaPattern.responsibilityChain.old.IWomen;

public abstract class Handler {

	private int level;
	private Handler nextHandler;

	public Handler(int level) {
		this.level = level;
	}

	public final void handleMessage(IWomen women) {
		if (women.getType() == level) {
			response(women);
		} else {
			if (null != nextHandler) {
				nextHandler.handleMessage(women);
			} else {
				System.out.println("---无处请示，按不同意处理---");
			}
		}
	}

	/**
	 * 如果不属于你处理的请求，你应该让她找下一个环节的人，如女儿出嫁了， 还向父亲请示是否可以逛街，那父亲就应该告诉女儿，应该找丈夫请示
	 */
	public void setNext(Handler _handler) {
		this.nextHandler = _handler;
	}

	public abstract void response(IWomen women);
}
