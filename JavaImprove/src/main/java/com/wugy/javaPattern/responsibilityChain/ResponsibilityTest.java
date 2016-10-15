package com.wugy.javaPattern.responsibilityChain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.wugy.javaPattern.responsibilityChain.old.Father;
import com.wugy.javaPattern.responsibilityChain.old.Husband;
import com.wugy.javaPattern.responsibilityChain.old.IHandler;
import com.wugy.javaPattern.responsibilityChain.old.IWomen;
import com.wugy.javaPattern.responsibilityChain.old.Son;
import com.wugy.javaPattern.responsibilityChain.old.StateType;
import com.wugy.javaPattern.responsibilityChain.old.Woman;
import com.wugy.javaPattern.responsibilityChain.oldImprove.Handler;

public class ResponsibilityTest {

	@Test
	public void oldPatternTest() {
		// 随机挑选几个女性
		Random random = new Random();
		List<IWomen> womens = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			womens.add(new Woman(random.nextInt(4), "逛街"));
		}

		// 定义三个请示对象
		IHandler father = new Father();
		IHandler husband = new Husband();
		IHandler son = new Son();

		for (IWomen woman : womens) {
			StateType stateType = StateType.getType(woman.getType());
			switch (stateType) {
			case single:
				System.out.println("---女儿向父亲请示---");
				father.handlerMessage(woman);
				break;
			case married:
				System.out.println("---妻子向丈夫请示---");
				husband.handlerMessage(woman);
			case husbandDeath:
				System.out.println("---母亲向儿子请示---");
				son.handlerMessage(woman);
			default:
				break;
			}
		}
	}

	@Test
	public void improvePatternTest() {
		// 随机挑选几个女性
		Random random = new Random();
		List<IWomen> womens = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			womens.add(new Woman(random.nextInt(4), "逛街"));
		} // 定义三个请示对象
		Handler father = new com.wugy.javaPattern.responsibilityChain.oldImprove.Father();
		Handler husband = new com.wugy.javaPattern.responsibilityChain.oldImprove.Husband();
		Handler son = new com.wugy.javaPattern.responsibilityChain.oldImprove.Son();
		// 设置请示顺序
		father.setNext(husband);
		husband.setNext(son);
		for (IWomen women : womens) {
			father.handleMessage(women);
		}
	}
}
