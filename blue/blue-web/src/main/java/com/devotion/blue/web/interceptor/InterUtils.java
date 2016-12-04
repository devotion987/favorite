package com.devotion.blue.web.interceptor;

import java.math.BigInteger;

import com.devotion.blue.model.User;
import com.devotion.blue.model.query.UserQuery;
import com.devotion.blue.utils.Consts;
import com.devotion.blue.utils.CookieUtils;
import com.devotion.blue.utils.StringUtils;
import com.jfinal.aop.Invocation;

public class InterUtils {

	public static User tryToGetUser(Invocation inv) {

		String userId = CookieUtils.get(inv.getController(), Consts.COOKIE_LOGINED_USER);
		if (StringUtils.isNotBlank(userId)) {
			return UserQuery.me().findById(new BigInteger(userId));
		}

		return null;
	}

}
