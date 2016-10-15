package com.wugy.javaPattern.factory.abs;

/**
 * 黑色男性人种
 * 
 * @author wugy 2016年9月18日
 *
 */
public class MaleBlackHuman extends AbstractBlackHuman {

	@Override
	public void getSex() {
		System.out.println("黑人男性！");
	}

}
