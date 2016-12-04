package com.devotion.blue.oauth;

import java.util.UUID;

import com.jfinal.core.Controller;

public abstract class OauthController extends Controller {

	public void index() {
		String processerName = getPara();
		OauthConnector op = onConnectorGet(processerName);
		String state = UUID.randomUUID().toString().replace("-", "");

		String requestUrl = getRequest().getRequestURL().toString();
		String callBackUrl = requestUrl.replace("/" + processerName, "/callback/" + processerName);
		String url = op.getAuthorizeUrl(state, callBackUrl);

		setSessionAttr("oauth_state", state);
		redirect(url);
	}

	// xxx/callback/qq
	// xxx/callback/weibo
	// xxx/callback/qq
	public void callback() {
		String sessionState = getSessionAttr("oauth_state");
		String state = getPara("state");

		if (!sessionState.equals(state)) {
			onAuthorizeError("state not validate");
			return;
		}

		String code = getPara("code");
		if (null == code || "".equals(code.trim())) {
			onAuthorizeError("can't get code");
			return;
		}

		String processerName = getPara();
		OauthConnector op = onConnectorGet(processerName);

		OauthUser ouser = null;
		try {
			ouser = op.getUser(code);
		} catch (Throwable e) {
			onAuthorizeError("get oauth user exception:" + e.getMessage());
			return;
		}

		if (ouser == null) {
			onAuthorizeError("can't get user info!");
			return;
		}

		onAuthorizeSuccess(ouser);

	}

	public abstract void onAuthorizeSuccess(OauthUser oauthUser);

	public abstract void onAuthorizeError(String errorMessage);

	public abstract OauthConnector onConnectorGet(String processerName);

}
