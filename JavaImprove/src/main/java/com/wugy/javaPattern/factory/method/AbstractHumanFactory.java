package com.wugy.javaPattern.factory.method;

/**
 * 工厂方法模式
 * 
 * @author wugy 2016年9月18日
 *
 */
abstract public class AbstractHumanFactory {

	abstract public <T extends Human> T createHuman(Class<T> clazz);
}
