package com.wugy.javaPattern.responsibilityChain.oldImprove;

import com.wugy.javaPattern.responsibilityChain.old.IWomen;
import com.wugy.javaPattern.responsibilityChain.old.StateType;

public class Women implements IWomen {

	private String request;
	private StateType stateType;

	public Women(StateType stateType, String request) {
		switch (stateType) {
		case single:
			this.request = "女儿的请求是：" + request;
			break;
		case married:
			this.request = "妻子的请求是：" + request;
			break;
		case husbandDeath:
			this.request = "母亲的请求是：" + request;
			break;
		default:
			break;
		}
	}

	@Override
	public int getType() {
		return stateType.getType();
	}

	@Override
	public String getRequest() {
		return request;
	}

}
