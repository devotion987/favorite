package com.wugy.javaPattern.factory.abs;

abstract public class AbstractBlackHuman implements Human {

	@Override
	public void getColor() {
		System.out.println("黑种人皮肤是黑色的！");
	}
}
