package com.devotion.blue.web.core.cache;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.devotion.blue.utils.StringUtils;
import com.jfinal.core.Action;
import com.jfinal.core.JFinal;
import com.jfinal.handler.Handler;
import com.jfinal.log.Log;
import com.jfinal.render.RenderFactory;

public class ActionCacheHandler extends Handler {

	static String[] urlPara = { null };
	static Log log = Log.getLog(ActionCacheHandler.class);

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {

		if (ActionCacheManager.isCloseActionCache()) {
			next.handle(target, request, response, isHandled);
			return;
		}

		Action action = JFinal.me().getAction(target, urlPara);
		if (action == null) {
			next.handle(target, request, response, isHandled);
			return;
		}

		ActionCache actionCache = action.getMethod().getAnnotation(ActionCache.class);
		if (actionCache == null) {
			actionCache = action.getControllerClass().getAnnotation(ActionCache.class);
			if (actionCache == null) {
				next.handle(target, request, response, isHandled);
				return;
			}
		}

		String originalTarget = (String) request.getAttribute("_original_target");
		String cacheKey = StringUtils.isNotBlank(originalTarget) ? originalTarget : target;

		String queryString = request.getQueryString();
		if (queryString != null) {
			queryString = "?" + queryString;
			cacheKey += queryString;
		}

		ActionCacheManager.enableCache(request);
		ActionCacheManager.setCacheKey(request, cacheKey);
		ActionCacheManager.setCacheContentType(request, actionCache.contentType());

		String renderContent = ActionCacheManager.getCache(request, cacheKey);
		if (renderContent != null) {
			response.setContentType(actionCache.contentType());

			PrintWriter writer = null;
			try {
				writer = response.getWriter();
				writer.write(renderContent);
				isHandled[0] = true;
			} catch (Exception e) {
				RenderFactory.me().getErrorRender(500).setContext(request, response, action.getViewPath()).render();
			} finally {
				if (writer != null) {
					writer.close();
				}
			}
		} else {
			next.handle(target, request, response, isHandled);
		}
	}

}
