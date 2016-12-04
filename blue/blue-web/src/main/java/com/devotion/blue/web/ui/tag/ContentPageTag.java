package com.devotion.blue.web.ui.tag;

import com.devotion.blue.model.Content;
import com.devotion.blue.model.ModelSorter;
import com.devotion.blue.model.Taxonomy;
import com.devotion.blue.model.query.ContentQuery;
import com.devotion.blue.model.query.TaxonomyQuery;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.render.freemarker.BasePaginateTag;
import com.devotion.blue.web.core.render.freemarker.JTag;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.Page;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class ContentPageTag extends JTag {

	public static final String TAG_NAME = "jp.contentPage";

	int pageNumber;
    String moduleName;
	String orderBy;
	List<Taxonomy> taxonomys;
	HttpServletRequest request;

	public ContentPageTag(HttpServletRequest request, int pageNumber, String moduleName, List<Taxonomy> taxonomys,
			String orderBy) {
		this.request = request;
		this.pageNumber = pageNumber;
		this.moduleName = moduleName;
		this.taxonomys = taxonomys;
		this.orderBy = orderBy;
	}

	@Override
	public void onRender() {

		int pagesize = getParamToInt("pageSize", 10);
		orderBy = StringUtils.isBlank(orderBy) ? getParam("orderBy") : orderBy;

		BigInteger[] taxonomyIds = null;
		if (taxonomys != null && taxonomys.size() > 0) {
			taxonomyIds = new BigInteger[taxonomys.size()];
			for (int i = 0; i < taxonomyIds.length; i++) {
				taxonomyIds[i] = taxonomys.get(i).getId();
			}
		}

		if (taxonomyIds != null && taxonomyIds.length > 0) {
			boolean containChild = getParamToBool("containChild", false);
			if (containChild == true) {
				for (Taxonomy taxonomy : taxonomys) {
					List<Taxonomy> childs = TaxonomyQuery.me().findListByModuleAndType(moduleName, taxonomy.getType());
					if (childs != null && childs.size() > 0) {
						ModelSorter.sort(childs, taxonomy.getId());
						BigInteger[] newIds = Arrays.copyOf(taxonomyIds, taxonomyIds.length + childs.size());
						for (int i = taxonomyIds.length; i < newIds.length; i++) {
							newIds[i] = childs.get(i - taxonomyIds.length).getId();
						}
						taxonomyIds = newIds;
					}
				}
			}
		}

		Page<Content> page = ContentQuery.me().paginateInNormal(pageNumber, pagesize, moduleName, taxonomyIds, orderBy);
		setVariable("page", page);
		setVariable("contents", page.getList());

		ContentPaginateTag pagination = new ContentPaginateTag(request, page, moduleName, taxonomys);
		setVariable("pagination", pagination);

		renderBody();
	}

	public static class ContentPaginateTag extends BasePaginateTag {

		final String moduleName;
		final List<Taxonomy> taxonomys;
		final HttpServletRequest request;

		public ContentPaginateTag(HttpServletRequest request, Page<Content> page, String moduleName,
				List<Taxonomy> taxonomys) {
			super(page);
			this.request = request;
			this.moduleName = moduleName;
			this.taxonomys = taxonomys;

		}

		@Override
		protected String getUrl(int pageNumber) {
			String url = JFinal.me().getContextPath() + "/" + moduleName + "-";
			if (taxonomys != null && taxonomys.size() > 0) {
				for (Taxonomy taxonomy : taxonomys) {
					url += taxonomy.getSlug() + ",";
				}
				url = url.substring(0, url.length() - 1);
				url += "-" + pageNumber;
			} else {
				url += pageNumber;
			}

			if (enalbleFakeStatic()) {
				url += getFakeStaticSuffix();
			}

			String queryString = request.getQueryString();
			if (StringUtils.isNotBlank(queryString)) {
				url += "?" + queryString;
			}

			if (StringUtils.isNotBlank(getAnchor())) {
				url += "#" + getAnchor();
			}
			return url;
		}

	}

}