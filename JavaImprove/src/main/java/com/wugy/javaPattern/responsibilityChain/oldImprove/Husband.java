package com.wugy.javaPattern.responsibilityChain.oldImprove;

import com.wugy.javaPattern.responsibilityChain.old.IWomen;
import com.wugy.javaPattern.responsibilityChain.old.StateType;

public class Husband extends Handler {

	// 丈夫只处理妻子的请求
	public Husband() {
		super(StateType.married.getType());
	}

	@Override
	public void response(IWomen women) {
		System.out.println("--------妻子向丈夫请示-------");
		System.out.println(women.getRequest());
		System.out.println("丈夫的答复是：同意\n");
	}
}