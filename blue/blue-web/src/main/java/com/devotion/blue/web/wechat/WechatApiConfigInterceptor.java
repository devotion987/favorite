package com.devotion.blue.web.wechat;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;

public class WechatApiConfigInterceptor implements Interceptor {

	@Override
    public void intercept(Invocation inv) {
		try {
			ApiConfig ac = WechatApi.getApiConfig();
			ApiConfigKit.setThreadLocalApiConfig(ac);
			inv.invoke();
		} finally {
			ApiConfigKit.removeThreadLocalApiConfig();
		}
	}

}