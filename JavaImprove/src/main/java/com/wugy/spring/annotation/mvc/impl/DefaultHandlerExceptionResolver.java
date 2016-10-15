package com.wugy.spring.annotation.mvc.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wugy.spring.annotation.mvc.error.AuthcException;
import com.wugy.spring.annotation.mvc.error.AuthzException;
import com.wugy.spring.annotation.mvc.service.HandlerExceptionResolver;
import com.wugy.spring.utils.FrameworkConstant;
import com.wugy.spring.utils.WebUtil;

/**
 * 默认 Handler 异常解析器
 *
 * @author devotion
 */
public class DefaultHandlerExceptionResolver implements HandlerExceptionResolver {

	@Override
	public void resolveHandlerException(HttpServletRequest request, HttpServletResponse response, Exception e) {
		// 判断异常原因
		Throwable cause = e.getCause();
		if (cause == null) {
			e.printStackTrace();
			return;
		}
		if (cause instanceof AuthcException) {
			// 分两种情况进行处理
			if (WebUtil.isAJAX(request)) {
				// 跳转到 403 页面
				WebUtil.sendError(HttpServletResponse.SC_FORBIDDEN, "", response);
			} else {
				// 重定向到首页
				WebUtil.redirectRequest(FrameworkConstant.HOME_PAGE, request, response);
			}
		} else if (cause instanceof AuthzException) {
			// 跳转到 403 页面
			WebUtil.sendError(HttpServletResponse.SC_FORBIDDEN, "", response);
		} else {
			// 跳转到 500 页面
			WebUtil.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, cause.getMessage(), response);
		}
	}
}
