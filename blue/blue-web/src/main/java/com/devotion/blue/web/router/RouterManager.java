package com.devotion.blue.web.router;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.devotion.blue.model.query.OptionQuery;
import com.devotion.blue.model.route.ContentRouter;
import com.devotion.blue.model.route.PageRouter;
import com.devotion.blue.model.route.RouterConverter;
import com.devotion.blue.model.route.TaxonomyRouter;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.JPress;
import com.devotion.blue.web.core.addon.HookInvoker;
import com.jfinal.core.Action;
import com.jfinal.core.JFinal;
import com.jfinal.log.Log;

public class RouterManager {
	private static final Log log = Log.getLog(RouterManager.class);
	static String[] urlPara = {null};
	static List<RouterConverter> converters = new ArrayList<RouterConverter>();
	static{
		converters.add(new TaxonomyRouter());
		converters.add(new PageRouter());
		converters.add(new ContentRouter());
	}


	public static String converte(String target, HttpServletRequest request, HttpServletResponse response) {
		if (!JPress.isInstalled()) {
			return target;
		}

		if ("/".equals(target)) {
			return target;
		}
		
		Action action = JFinal.me().getAction(target, urlPara);
		if (action != null) {
			RouterNotAllowConvert notAllowConvert = action.getControllerClass().getAnnotation(RouterNotAllowConvert.class);
			if (notAllowConvert != null) {
				return target;
			}
			
			notAllowConvert = action.getMethod().getAnnotation(RouterNotAllowConvert.class);
			if (notAllowConvert != null) {
				return target;
			}
		}

		String hookTarget = HookInvoker.routerConverte(target, request, response);
		if(StringUtils.isNotBlank(hookTarget)){
			return hookTarget;
		}
		

		if (target.indexOf('.') != -1) {
			Boolean fakeStaticEnable = OptionQuery.me().findValueAsBool("router_fakestatic_enable");
			if (fakeStaticEnable == null || !fakeStaticEnable) {
				return target;
			}

			String fakeStaticSuffix = OptionQuery.me().findValue("router_fakestatic_suffix");

			if (!StringUtils.isNotBlank(fakeStaticSuffix)) {
				fakeStaticSuffix = ".html";
			}

			int index = target.lastIndexOf(fakeStaticSuffix);
			if (-1 == index) {
				return target;
			}

			target = target.substring(0, index);
		}

		try {
			for (RouterConverter c : converters) {
				String newTarget = c.converter(target, request, response);
				if (newTarget != null) {
					if (JPress.isDevMode()) {
						String formatString = "target\"%s\" was converted to \"%s\" by %s.(%s.java:1)";
						System.err.println(String.format(formatString, target, newTarget, c.getClass().getName(),
								c.getClass().getSimpleName()));
					}
					return newTarget;
				}
			}
		} catch (Exception e) {
			log.warn("IRouterConverter converter exception", e);
		}

		return target;

	}

}
