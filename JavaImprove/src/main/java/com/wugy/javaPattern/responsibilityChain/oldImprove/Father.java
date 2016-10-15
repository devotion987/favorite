package com.wugy.javaPattern.responsibilityChain.oldImprove;

import com.wugy.javaPattern.responsibilityChain.old.IWomen;
import com.wugy.javaPattern.responsibilityChain.old.StateType;

public class Father extends Handler {

	public Father() {
		super(StateType.single.getType());
	}

	@Override
	public void response(IWomen women) {
		System.out.println("--------女儿向父亲请示-------");
		System.out.println(women.getRequest());
		System.out.println("父亲的答复是:同意\n");
	}

}
