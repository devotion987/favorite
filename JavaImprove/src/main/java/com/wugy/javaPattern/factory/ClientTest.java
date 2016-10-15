package com.wugy.javaPattern.factory;

import org.junit.Test;

import com.wugy.javaPattern.factory.abs.FemaleFactory;
import com.wugy.javaPattern.factory.abs.Human;
import com.wugy.javaPattern.factory.abs.MaleFactory;
import com.wugy.javaPattern.factory.method.AbstractHumanFactory;
import com.wugy.javaPattern.factory.method.BlackHuman;
import com.wugy.javaPattern.factory.method.HumanFactory;
import com.wugy.javaPattern.factory.method.WhiteHuman;
import com.wugy.javaPattern.factory.method.YellowHuman;

public class ClientTest {

	/**
	 * 工厂方法模式测试
	 */
	@Test
	public void testFactoryMethod() {
		AbstractHumanFactory humanFactory = new HumanFactory();

		System.out.print("白种人：");
		WhiteHuman whiteHuman = humanFactory.createHuman(WhiteHuman.class);
		whiteHuman.getColor();

		System.out.print("黑种人：");
		BlackHuman blackHuman = humanFactory.createHuman(BlackHuman.class);
		blackHuman.getColor();

		System.out.print("黄种人：");
		YellowHuman yellowHuman = humanFactory.createHuman(YellowHuman.class);
		yellowHuman.getColor();
	}

	/**
	 * 抽象工厂模式测试
	 */
	@Test
	public void testFactoryAbstract() {
		// 男性生产线
		com.wugy.javaPattern.factory.abs.HumanFactory maleHumanFactory = new MaleFactory();
		// 女性生产线
		com.wugy.javaPattern.factory.abs.HumanFactory femaleHumanFactory = new FemaleFactory();

		// 开始造人
		Human maleYellowHuman = maleHumanFactory.createYellowHuman();
		Human femaleYellowHuman = femaleHumanFactory.createYellowHuman();
		System.out.println("生产黄色男性：");
		maleYellowHuman.getColor();
		maleYellowHuman.getSex();
		System.out.println();
		System.out.println("生产黄色女性：");
		femaleYellowHuman.getColor();
		femaleYellowHuman.getSex();
	}

}
