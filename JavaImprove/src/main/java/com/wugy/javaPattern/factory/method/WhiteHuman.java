package com.wugy.javaPattern.factory.method;

/**
 * 白种人
 * 
 * @author wugy 2016年9月18日
 *
 */
public class WhiteHuman implements Human {

	@Override
	public void getColor() {
		System.out.println("白种人皮肤是白色的！");
	}

}
