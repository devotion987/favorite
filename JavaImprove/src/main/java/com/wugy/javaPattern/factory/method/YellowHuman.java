package com.wugy.javaPattern.factory.method;

/**
 * 黄种人
 * 
 * @author wugy 2016年9月18日
 *
 */
public class YellowHuman implements Human {

	@Override
	public void getColor() {
		System.out.println("黄种人皮肤是黄色的！");
	}

}
