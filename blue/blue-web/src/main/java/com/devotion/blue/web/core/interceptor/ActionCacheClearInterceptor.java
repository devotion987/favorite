package com.devotion.blue.web.core.interceptor;

import com.devotion.blue.web.core.cache.ActionCacheManager;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class ActionCacheClearInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		inv.invoke();
		ActionCacheManager.clearCache();
	}

}
