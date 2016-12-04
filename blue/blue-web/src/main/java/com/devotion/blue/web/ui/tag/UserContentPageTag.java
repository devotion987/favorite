package com.devotion.blue.web.ui.tag;

import com.devotion.blue.model.Content;
import com.devotion.blue.model.query.ContentQuery;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.render.freemarker.BasePaginateTag;
import com.devotion.blue.web.core.render.freemarker.JTag;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.Page;

import java.math.BigInteger;

public class UserContentPageTag extends JTag {

    public static final String TAG_NAME = "jp.userContentPage";

	BigInteger userId;
	int pageNumber;
	String action;

	public UserContentPageTag(String action, BigInteger userId, int pageNumber) {
		this.userId = userId;
		this.pageNumber = pageNumber;
		this.action = action;
	}

	@Override
	public void onRender() {

		String module = getParam("module");
		BigInteger taxonomyId = getParamToBigInteger("taxonomyId");

		int pageSize = getParamToInt("pageSize", 10);
		String orderby = getParam("orderBy");
		String status = getParam("status", Content.STATUS_NORMAL);

		BigInteger[] tids = taxonomyId == null ? null : new BigInteger[] { taxonomyId };

		Page<Content> page = ContentQuery.me().paginate(pageNumber, pageSize, module, null, status, tids, userId,orderby);
		setVariable("page", page);
		setVariable("contents", page.getList());
		
		MyPaginateTag pagination = new MyPaginateTag(page, action);
		setVariable("pagination", pagination);
		
		renderBody();
	}

	public static class MyPaginateTag extends BasePaginateTag {

		final String action;

		public MyPaginateTag(Page<Content> page, String action) {
			super(page);
			this.action = action;
		}

		@Override
		protected String getUrl(int pageNumber) {
			String url = JFinal.me().getContextPath() + "/user/center/";
			url += action;
			url += "-" + pageNumber;

			if (StringUtils.isNotBlank(getAnchor())) {
				url += "#" + getAnchor();
			}
			return url;
		}

	}

}