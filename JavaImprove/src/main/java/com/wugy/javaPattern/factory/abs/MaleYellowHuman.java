package com.wugy.javaPattern.factory.abs;

/**
 * 黄人男性
 * 
 * @author wugy 2016年9月18日
 *
 */
public class MaleYellowHuman extends AbstractYellowHuman {

	@Override
	public void getSex() {
		System.out.println("黄人男性！");
	}

}
