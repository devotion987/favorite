package com.devotion.blue.web.ui.tag;

import com.devotion.blue.model.Comment;
import com.devotion.blue.model.Content;
import com.devotion.blue.model.query.CommentQuery;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.render.freemarker.BasePaginateTag;
import com.devotion.blue.web.core.render.freemarker.JTag;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.Page;

import javax.servlet.http.HttpServletRequest;

public class CommentPageTag extends JTag {

    public static final String TAG_NAME = "jp.commentPage";

	Content content;
	int pageNumber;
	HttpServletRequest request;

	public CommentPageTag(HttpServletRequest request, Content content, int pageNumber) {
		this.request = request;
		this.content = content;
		this.pageNumber = pageNumber;
	}

	@Override
	public void onRender() {

		int pageSize = getParamToInt("pageSize", 10);

		Page<Comment> page = CommentQuery.me().paginateByContentId(pageNumber, pageSize, content.getId());
		setVariable("page", page);
		setVariable("comments", page.getList());
		
		CommentPaginateTag pagination = new CommentPaginateTag(request, page, content);
		setVariable("pagination", pagination);

		renderBody();
	}

	public static class CommentPaginateTag extends BasePaginateTag {

		final Content content;
		final HttpServletRequest request;

		public CommentPaginateTag(HttpServletRequest request, Page<Comment> page, Content content) {
			super(page);
			this.request = request;
			this.content = content;
		}

		@Override
		protected String getUrl(int pageNumber) {
			String url = content.getUrlWithPageNumber(pageNumber);

			String queryString = request.getQueryString();
			if (StringUtils.isNotBlank(queryString)) {
				url += "?" + queryString;
			}

			if (StringUtils.isNotBlank(getAnchor())) {
				url += "#" + getAnchor();
			}
			return JFinal.me().getContextPath() + url;
		}

	}

}