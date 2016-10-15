package com.wugy.javaPattern.factory.abs;

/**
 * 黑色女性人种
 * 
 * @author wugy 2016年9月18日
 *
 */
public class FemaleBlackHuman extends AbstractBlackHuman {

	@Override
	public void getSex() {
		System.out.println("黑人女性！");
	}

}
