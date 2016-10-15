package com.wugy.spring.plugin;

/**
 * 插件接口
 *
 * @author devotion
 */
public interface Plugin {

	/**
	 * 初始化插件
	 */
	void init();

	/**
	 * 销毁插件
	 */
	void destroy();
}
