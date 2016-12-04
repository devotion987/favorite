package com.devotion.blue.web.interceptor;

import com.devotion.blue.model.User;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class UserInterceptor implements Interceptor {

	@Override
    public void intercept(Invocation inv) {
		User user = InterUtils.tryToGetUser(inv);
		if (user != null) {
			inv.invoke();
		} else {
			inv.getController().redirect("/user/login");
		}

	}

}
