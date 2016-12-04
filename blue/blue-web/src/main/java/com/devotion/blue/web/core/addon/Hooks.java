package com.devotion.blue.web.core.addon;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.devotion.blue.utils.StringUtils;
import com.jfinal.log.Log;

public class Hooks {

	private static final Log log = Log.getLog(Hooks.class);
	public static final String ROUTER_CONVERTE = "routerConverte";
	public static final String PROCESS_CONTROLLER = "processController";
	public static final String INTERCEPT = "intercept";
	public static final String INDEX_RENDER_BEFORE = "indexRenderBefore";
	public static final String INDEX_RENDER_AFTER = "indexRenderAfter";
	public static final String TAXONOMY_RENDER_BEFORE = "taxonomyRenderBefore";
	public static final String TAXONOMY_RENDER_AFTER = "taxonomyRenderAfter";
	public static final String CONTENT_RENDER_BEFORE = "contentRenderBefore";
	public static final String CONTENT_RENDER_AFTER = "contentRenderAfter";
	public static final String MENU_INIT_BEFORE = "menuInitBefore";
	public static final String MENU_INIT_AFTER = "menuInitAfter";

	private Map<String, Method> hookMethods = new ConcurrentHashMap<String, Method>();
	private Addon target;

	public Hooks(Addon target) {
		this.target = target;
		autoRegister();
	}

	public void autoRegister() {
		Method[] methods = target.getClass().getDeclaredMethods();
		if (methods != null && methods.length > 0) {
			for (Method m : methods) {
				Hook hook = m.getAnnotation(Hook.class);
				if (hook != null && StringUtils.isNotBlank(hook.value())) {
					hookMethods.put(hook.value(), m);
				}
			}
		}

	}

	public Object invokeHook(String hook, Object... objects) {
		Method method = hookMethods.get(hook);
		if (method != null) {
			try {
				return method.invoke(target, objects);
			} catch (Throwable e) {
				log.error("invokeHook error , hook name:+" + hook + " \r\n + error addon:\r\n" + target, e);
			} finally {
				target.hookInvokeFinished();
			}
		}
		return null;
	}

}
