package com.devotion.blue.model.route;

import com.devotion.blue.model.Taxonomy;
import com.devotion.blue.model.template.TemplateManager;
import com.devotion.blue.utils.Consts;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class TaxonomyRouter extends RouterConverter {

	private static String getRouterWithoutSuffix(Taxonomy taxonomy) {
		String url = SLASH + taxonomy.getContentModule() + URL_PARA_SEPARATOR
				+ (taxonomy.getSlug() == null ? taxonomy.getId() : taxonomy.getSlug()) + URL_PARA_SEPARATOR + 1;

		return url;
	}

	private static String getRouterWithoutSuffix(List<Taxonomy> taxonomys) {
		Taxonomy taxonomy = taxonomys.get(0);

		String url = SLASH + taxonomy.getContentModule() + URL_PARA_SEPARATOR;

		StringBuffer buffer = new StringBuffer();
		for (Taxonomy t : taxonomys) {
			buffer.append(t.getSlug() + ",");
		}
		buffer.deleteCharAt(buffer.length() - 1);

		return url + buffer.toString() + URL_PARA_SEPARATOR + 1;
	}

	private static String getRouterWithoutSuffix(String module, String slug) {
		return SLASH + module + URL_PARA_SEPARATOR + slug + URL_PARA_SEPARATOR + 1;
	}
	
	private static String getRouterWithoutSuffix(String module) {
		return SLASH + module ;
	}

	public static String getRouterWithoutPageNumber(Taxonomy taxonomy) {
		return SLASH + taxonomy.getContentModule() + URL_PARA_SEPARATOR
				+ (taxonomy.getSlug() == null ? taxonomy.getId() : taxonomy.getSlug());
	}

	public static String getRouter(Taxonomy taxonomy) {
		String url = getRouterWithoutSuffix(taxonomy);
		if (enalbleFakeStatic()) {
			url += getFakeStaticSuffix();
		}
		return url;
	}

	public static String getRouter(List<Taxonomy> taxonomys) {
		String url = getRouterWithoutSuffix(taxonomys);
		if (enalbleFakeStatic()) {
			url += getFakeStaticSuffix();
		}
		return url;
	}

	public static String getRouter(String module, String slug) {
		String url = getRouterWithoutSuffix(module, slug);
		if (enalbleFakeStatic()) {
			url += getFakeStaticSuffix();
		}
		return url;
	}
	
	public static String getRouter(String module) {
		String url = getRouterWithoutSuffix(module);
		if (enalbleFakeStatic()) {
			url += getFakeStaticSuffix();
		}
		return url;
	}

	public static String getRouter(Taxonomy taxonomy, int pageNumber) {
		String url = getRouterWithoutSuffix(taxonomy);
		if (enalbleFakeStatic()) {
			return url + URL_PARA_SEPARATOR + pageNumber + getFakeStaticSuffix();
		}
		return url + URL_PARA_SEPARATOR + pageNumber;
	}

	@Override
	public String converter(String target, HttpServletRequest request, HttpServletResponse response) {

		String[] targetDirs = parseTarget(target);

		if (targetDirs == null || targetDirs.length != 1) {
			return null;
		}

		String[] params = parseParam(targetDirs[0]);
		if (params == null || params.length == 0) {
			return null;
		}

		String moduleName = params[0];
		if (TemplateManager.me().currentTemplateModule(moduleName) != null) {
			return Consts.ROUTER_TAXONOMY + target;
		}

		return null;
	}


}
