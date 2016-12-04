package com.devotion.blue.web.core.interceptor;

import com.devotion.blue.web.core.addon.HookInvoker;
import com.devotion.blue.web.core.JPress;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class HookInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		if (JPress.isInstalled() && JPress.isLoaded()) {
			Boolean result = HookInvoker.intercept(inv);
			if (result == null || !result) {
				inv.invoke();
			}
		} else {
			inv.invoke();
		}
	}

}
