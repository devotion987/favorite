package com.wugy.javaPattern.factory.method;

/**
 * 创建人类的工厂
 * 
 * @author wugy 2016年9月18日
 *
 */
public class HumanFactory extends AbstractHumanFactory {

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Human> T createHuman(Class<T> clazz) {
		Human human = null;

		try {
			human = (T) Class.forName(clazz.getName()).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T) human;
	}

}
