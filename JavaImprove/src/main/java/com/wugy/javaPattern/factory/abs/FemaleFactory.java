package com.wugy.javaPattern.factory.abs;

/**
 * 女性“工厂”
 * 
 * @author wugy 2016年9月18日
 *
 */
public class FemaleFactory implements HumanFactory {

	@Override
	public Human createWhiteHuman() {
		return new FemaleWhiteHuman();
	}

	@Override
	public Human createBlackHuman() {
		return new FemaleBlackHuman();
	}

	@Override
	public Human createYellowHuman() {
		return new FemaleYellowHuman();
	}

}
