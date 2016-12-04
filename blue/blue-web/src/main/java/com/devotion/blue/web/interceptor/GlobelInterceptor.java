package com.devotion.blue.web.interceptor;

import com.devotion.blue.model.User;
import com.devotion.blue.utils.Consts;
import com.devotion.blue.utils.EncryptUtils;
import com.devotion.blue.web.core.JPress;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class GlobelInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		if (JPress.isInstalled() && JPress.isInstalled()) {
			doGlobleSetting(inv);
		}
		inv.invoke();
	}

	private void doGlobleSetting(Invocation inv) {
		User user = InterUtils.tryToGetUser(inv);
		if (user != null) {
			inv.getController().setAttr(Consts.ATTR_USER, user);
			inv.getController().setAttr("ucode", EncryptUtils.generateUcode(user.getId(), user.getSalt()));
		}
	}

}
