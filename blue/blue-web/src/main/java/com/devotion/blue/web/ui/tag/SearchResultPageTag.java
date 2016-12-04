package com.devotion.blue.web.ui.tag;

import com.devotion.blue.search.SearcherBean;
import com.devotion.blue.search.SearcherKit;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.render.freemarker.BasePaginateTag;
import com.devotion.blue.web.core.render.freemarker.JTag;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.Page;

public class SearchResultPageTag extends JTag {

    public static final String TAG_NAME = "jp.searchResultPage";
	
	int pageNumber;
	String moduleName;
	String keyword;

	public SearchResultPageTag(String keyword, String moduleName, int pageNumber) {
		this.pageNumber = pageNumber;
		this.moduleName = moduleName;
		this.keyword = keyword;
	}

	@Override
	public void onRender() {

		int pagesize = getParamToInt("pageSize", 10);

		Page<SearcherBean> page = SearcherKit.search(keyword, moduleName, pageNumber, pagesize);
		setVariable("page", page);
		setVariable("searcherBeans", page.getList());

		MyPaginateTag pagination = new MyPaginateTag(page, keyword, moduleName);
		setVariable("pagination", pagination);

		renderBody();
	}

	public static class MyPaginateTag extends BasePaginateTag {

		final String keyword;
		final String moduleName;

		public MyPaginateTag(Page<SearcherBean> page, String keyword, String moduleName) {
			super(page);
			this.keyword = keyword;
			this.moduleName = moduleName;
		}

		@Override
		protected String getUrl(int pageNumber) {
			String url = JFinal.me().getContextPath() + "/s?";
			url += "m=" + (StringUtils.isNotBlank(moduleName) ? moduleName.trim() : "");
			url += "&k=" + StringUtils.urlDecode(keyword);
			url += "&p=" + pageNumber;

			if (StringUtils.isNotBlank(getAnchor())) {
				url += "#" + getAnchor();
			}
			return url;
		}

	}

}