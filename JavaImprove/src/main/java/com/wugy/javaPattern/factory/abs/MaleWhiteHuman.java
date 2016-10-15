package com.wugy.javaPattern.factory.abs;

/**
 * 白人男性
 * 
 * @author wugy 2016年9月18日
 *
 */
public class MaleWhiteHuman extends AbstractWhiteHuman {

	@Override
	public void getSex() {
		System.out.println("白人男性！");
	}

}
