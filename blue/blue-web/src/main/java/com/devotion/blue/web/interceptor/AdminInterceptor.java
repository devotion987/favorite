package com.devotion.blue.web.interceptor;

import com.devotion.blue.model.User;
import com.devotion.blue.web.menu.MenuManager;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class AdminInterceptor implements Interceptor {
	
	@Override
	public void intercept(Invocation inv) {

		Controller controller = inv.getController();
		
		String target = controller.getRequest().getRequestURI();
		String cpath = controller.getRequest().getContextPath();

		if (!target.startsWith(cpath + "/admin")) {
			inv.invoke();
			return;
		}

		controller.setAttr("c", controller.getPara("c"));
		controller.setAttr("p", controller.getPara("p"));
		controller.setAttr("m", controller.getPara("m"));
		controller.setAttr("t", controller.getPara("t"));
		controller.setAttr("s", controller.getPara("s"));
		controller.setAttr("k", controller.getPara("k"));
		controller.setAttr("page", controller.getPara("page"));

		User user = InterUtils.tryToGetUser(inv);
		
		if (user != null && user.isAdministrator()) {
			controller.setAttr("_menu_html", MenuManager.me().generateHtml());
			inv.invoke();
			return;
		}

		controller.redirect("/admin/login");
	}
	

}
