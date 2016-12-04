package com.devotion.blue.web.wechat;

import com.devotion.blue.model.query.OptionQuery;
import com.devotion.blue.utils.Consts;
import com.devotion.blue.utils.StringUtils;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import javax.servlet.http.HttpServletRequest;

public class WechatUserInterceptor implements Interceptor {

	public static final String AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize" + "?appid={appid}"
            + "&redirect_uri={redirecturi}" + "&response_type=code" + "&scope=snsapi_userinfo"
			+ "&state=235#wechat_redirect";

	@Override
	public void intercept(Invocation inv) {

		Controller controller = inv.getController();

		String userJson = inv.getController().getSessionAttr(Consts.SESSION_WECHAT_USER);

		if (StringUtils.isBlank(userJson)) {
			inv.invoke();
			return;
		}

		String appid = OptionQuery.me().findValue("wechat_appid");
		if (StringUtils.isBlank(appid)) {
			inv.invoke();
			return;
		}

		HttpServletRequest request = controller.getRequest();
		// 获取用户将要去的路径
		String queryString = request.getQueryString();

		// 被拦截前的请求URL
		String toUrl = request.getRequestURI();
		if (StringUtils.isNotBlank(queryString)) {
			toUrl = toUrl.concat("?").concat(queryString);
		}
		toUrl = StringUtils.urlEncode(toUrl);

		String redirectUrl = request.getScheme() + "://" + request.getServerName() + "/wechat/callback?goto=" + toUrl;
		redirectUrl = StringUtils.urlEncode(redirectUrl);

		String url = AUTHORIZE_URL.replace("{redirecturi}", redirectUrl).replace("{appid}", appid.trim());
		controller.redirect(url);

	}

}