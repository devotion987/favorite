package com.devotion.blue.web.ui.tag;

import com.devotion.blue.model.Content;
import com.devotion.blue.model.query.ContentQuery;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.render.freemarker.BasePaginateTag;
import com.devotion.blue.web.core.render.freemarker.JTag;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.Page;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;

public class IndexPageTag extends JTag {

    public static final String TAG_NAME = "jp.indexPage";

	int pageNumber;
	String pagePara;
	String orderBy;
	HttpServletRequest request;

	public IndexPageTag(HttpServletRequest request, String pagePara, int pageNumber, String orderBy) {
		this.pagePara = pagePara;
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		this.pageNumber = pageNumber;
		this.request = request;
		this.orderBy = orderBy;
	}

	@Override
	public void onRender() {

		orderBy = StringUtils.isBlank(orderBy) ? getParam("orderBy") : orderBy;
		String keyword = getParam("keyword");

		int pagesize = getParamToInt("pageSize", 10);

		BigInteger[] typeIds = getParamToBigIntegerArray("typeId");
		String[] modules = getParamToStringArray("module");
		String status = getParam("status", Content.STATUS_NORMAL);

		Page<Content> page = ContentQuery.me().paginate(pageNumber, pagesize, modules, keyword, status, typeIds, null,orderBy);
		setVariable("page", page);
		setVariable("contents", page.getList());
		
		IndexPaginateTag pagination = new IndexPaginateTag(request, page, pagePara);
		setVariable("pagination", pagination);

		renderBody();
	}

	public static class IndexPaginateTag extends BasePaginateTag {

		String pagePara;
		HttpServletRequest request;

		public IndexPaginateTag(HttpServletRequest request, Page<Content> page, String pagePara) {
			super(page);
			this.request = request;
			this.pagePara = pagePara;
		}

		@Override
		protected String getUrl(int pageNumber) {
			String url = JFinal.me().getContextPath() + "/";

			if (StringUtils.isNotBlank(pagePara)) {
				url += pagePara + "-" + pageNumber;
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