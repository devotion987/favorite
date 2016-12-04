package com.devotion.blue.web.core;

import java.math.BigInteger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.devotion.blue.model.User;
import com.devotion.blue.model.query.UserQuery;
import com.devotion.blue.utils.Consts;
import com.devotion.blue.utils.CookieUtils;
import com.devotion.blue.utils.StringUtils;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.druid.DruidStatViewHandler;

public class DruidStatViewHandlerExt extends DruidStatViewHandler {
	
	static String visitPath = "/admin/druid";

	public DruidStatViewHandlerExt() {
		super(visitPath);
	}

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		if (target.startsWith(visitPath) && JPress.isInstalled() && JPress.isLoaded()) {

			String encrypt_key = PropKit.get("encrypt_key");
			String cookieInfo = getCookie(request, Consts.COOKIE_LOGINED_USER);

			String userId = CookieUtils.getFromCookieInfo(encrypt_key, cookieInfo);
			if (StringUtils.isNotBlank(userId)) {
				User user = UserQuery.me().findById(new BigInteger(userId));
				if (user != null && user.isAdministrator()) {
					super.handle(target, request, response, isHandled);
					return;
				}
			}
		}

		next.handle(target, request, response, isHandled);
	}

	private String getCookie(HttpServletRequest request, String name) {
		Cookie cookie = getCookieObject(request, name);
		return cookie != null ? cookie.getValue() : null;
	}

	private Cookie getCookieObject(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies)
				if (cookie.getName().equals(name))
					return cookie;
		return null;
	}

}
