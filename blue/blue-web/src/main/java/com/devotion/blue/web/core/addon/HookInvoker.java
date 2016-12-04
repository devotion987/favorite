package com.devotion.blue.web.core.addon;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.devotion.blue.web.menu.MenuManager;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.render.Render;

public class HookInvoker {

	public static String routerConverte(String target, HttpServletRequest request, HttpServletResponse response) {
		return (String) AddonManager.me().invokeHook(Hooks.ROUTER_CONVERTE, request, response);
	}

	public static Render processController(Controller controller) {
		return (Render) AddonManager.me().invokeHook(Hooks.PROCESS_CONTROLLER, controller);
	}

	public static Boolean intercept(Invocation inv) {
		return (Boolean) AddonManager.me().invokeHook(Hooks.INTERCEPT, inv);
	}

	public static Render indexRenderBefore(Controller controller) {
		return (Render) AddonManager.me().invokeHook(Hooks.INDEX_RENDER_BEFORE, controller);
	}

	public static void indexRenderAfter(Controller controller) {
		AddonManager.me().invokeHook(Hooks.INDEX_RENDER_AFTER, controller);
	}

	public static Render taxonomyRenderBefore(Controller controller) {
		return (Render) AddonManager.me().invokeHook(Hooks.TAXONOMY_RENDER_BEFORE, controller);
	}

	public static void taxonomyRenderAfter(Controller controller) {
		AddonManager.me().invokeHook(Hooks.TAXONOMY_RENDER_AFTER, controller);
	}

	public static Render contentRenderBefore(Controller controller) {
		return (Render) AddonManager.me().invokeHook(Hooks.CONTENT_RENDER_BEFORE, controller);
	}

	public static void contentRenderAfter(Controller controller) {
		AddonManager.me().invokeHook(Hooks.CONTENT_RENDER_AFTER, controller);
	}

	public static void menuInitBefore(MenuManager menuManager) {
		AddonManager.me().invokeHook(Hooks.MENU_INIT_BEFORE, menuManager);
	}

	public static void menuInitAfter(MenuManager menuManager) {
		AddonManager.me().invokeHook(Hooks.MENU_INIT_AFTER, menuManager);
	}

}
