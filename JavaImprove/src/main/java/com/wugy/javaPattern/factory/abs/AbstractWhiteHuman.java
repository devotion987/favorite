package com.wugy.javaPattern.factory.abs;

abstract public class AbstractWhiteHuman implements Human {

	@Override
	public void getColor() {
		System.out.println("白种人皮肤是白色的！");
	}

}
