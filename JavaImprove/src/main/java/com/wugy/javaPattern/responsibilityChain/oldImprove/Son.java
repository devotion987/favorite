package com.wugy.javaPattern.responsibilityChain.oldImprove;

import com.wugy.javaPattern.responsibilityChain.old.IWomen;
import com.wugy.javaPattern.responsibilityChain.old.StateType;

public class Son extends Handler {
	// 儿子只处理母亲的请求
	public Son() {
		super(StateType.husbandDeath.getType());
	}

	@Override
	public void response(IWomen women) {
		System.out.println("--------母亲向儿子请示-------");
		System.out.println(women.getRequest());
		System.out.println("儿子的答复是：同意\n");
	}
}