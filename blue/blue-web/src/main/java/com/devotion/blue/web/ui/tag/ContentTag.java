package com.devotion.blue.web.ui.tag;

import com.devotion.blue.model.Content;
import com.devotion.blue.model.query.ContentQuery;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.render.freemarker.JTag;

import java.math.BigInteger;

public class ContentTag extends JTag {

    public static final String TAG_NAME = "jp.content";

	@Override
	public void onRender() {

		BigInteger id = getParamToBigInteger("id");
		String slug = getParam("slug");

		if (id == null && StringUtils.isBlank(slug)) {
			renderText("");
			return;
		}

		Content content = null;
		if (id != null) {
			content = ContentQuery.me().findById(id);
		}

		if (content == null && StringUtils.isNotBlank(slug)) {
			content = ContentQuery.me().findBySlug(slug);
		}

		if (content == null) {
			renderText("");
			return;
		}

		setVariable("content", content);
		renderBody();
	}

}