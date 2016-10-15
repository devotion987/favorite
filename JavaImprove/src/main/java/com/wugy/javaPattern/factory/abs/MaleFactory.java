package com.wugy.javaPattern.factory.abs;

/**
 * 男性“工厂”
 * 
 * @author wugy 2016年9月18日
 *
 */
public class MaleFactory implements HumanFactory {

	@Override
	public Human createWhiteHuman() {
		return new MaleWhiteHuman();
	}

	@Override
	public Human createBlackHuman() {
		return new MaleBlackHuman();
	}

	@Override
	public Human createYellowHuman() {
		return new MaleYellowHuman();
	}

}
