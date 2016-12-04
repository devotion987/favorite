package com.devotion.blue.web.interceptor;

import com.devotion.blue.model.User;
import com.devotion.blue.utils.EncryptUtils;
import com.devotion.blue.web.core.JBaseController;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class UCodeInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		if (isMultipartRequest(inv)) {
			inv.getController().getFile();
		}
		String ucode = inv.getController().getPara("ucode");
		if (ucode == null || "".equals(ucode.trim())) {
			renderError(inv);
			return;
		}

		User user = InterUtils.tryToGetUser(inv);
		if (user == null) {
			renderError(inv);
			return;
		}

		if (!ucode.equals(EncryptUtils.generateUcode(user.getId(),user.getSalt()))) {
			renderError(inv);
			return;
		}

		inv.invoke();
	}

	private boolean isMultipartRequest(Invocation inv) {
		String contentType = inv.getController().getRequest().getContentType();
		return contentType != null && contentType.toLowerCase().indexOf("multipart") != -1;
	}

	private void renderError(Invocation inv) {
		Controller c = inv.getController();
		if (c instanceof JBaseController) {
			((JBaseController) c).renderAjaxResultForError("非法提交");
		} else {
			c.renderError(404);
		}
	}

}
