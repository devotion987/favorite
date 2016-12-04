package com.devotion.blue.web.wechat;

import com.devotion.blue.model.query.OptionQuery;
import com.devotion.blue.utils.HttpUtils;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.MenuApi;
import com.jfinal.weixin.sdk.api.UserApi;

public class WechatApi {

	public static ApiConfig getApiConfig() {
        ApiConfig config = new ApiConfig();
		config.setAppId(OptionQuery.me().findValue("wechat_appid"));
		config.setAppSecret(OptionQuery.me().findValue("wechat_appsecret"));
		config.setToken(OptionQuery.me().findValue("wechat_token"));
		return config;
	}


	public static ApiResult createMenu(String jsonString) {
		return MenuApi.createMenu(jsonString);
	}
	
	public static ApiResult getUserInfo(String openId){
		return  UserApi.getUserInfo(openId);
	}
	
	public static ApiResult getOpenId(String appId, String appSecret, String code) {

		String url = "https://api.weixin.qq.com/sns/oauth2/access_token" + "?appid={appid}"
				+ "&secret={secret}" + "&code={code}" + "&grant_type=authorization_code";

		String getOpenIdUrl = url.replace("{appid}", appId).replace("{secret}", appSecret)
				.replace("{code}", code);

		String jsonResult = null;
		try {
			jsonResult = HttpUtils.get(getOpenIdUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (jsonResult == null)
			return null;

		return new ApiResult(jsonResult);
	}

}