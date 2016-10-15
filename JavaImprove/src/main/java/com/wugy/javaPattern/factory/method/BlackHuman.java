package com.wugy.javaPattern.factory.method;

/**
 * 黑种人
 * 
 * @author wugy 2016年9月18日
 *
 */
public class BlackHuman implements Human {

	@Override
	public void getColor() {
		System.out.println("黑种人皮肤是黑色的！");
	}

}
