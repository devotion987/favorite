package com.wugy.spring.plugin;

import java.util.List;

import com.wugy.spring.annotation.aop.proxy.Proxy;

/**
 * 插件代理
 *
 * @author devotion
 */
public abstract class PluginProxy implements Proxy {

	public abstract List<Class<?>> getTargetClassList();
}
