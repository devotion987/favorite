package com.devotion.blue.web.ui.tag;

import com.devotion.blue.model.Content;
import com.devotion.blue.model.query.ContentQuery;
import com.devotion.blue.web.core.render.freemarker.JTag;

public class NextContentTag extends JTag {

    public static final String TAG_NAME = "jp.next";

	private Content currentContent;

	public NextContentTag(Content content) {
		this.currentContent = content;
	}

	@Override
	public void onRender() {

		Content content = ContentQuery.me().findNext(currentContent);

		if (content == null) {
			renderText("");
			return;
		}

		setVariable("next", content);
		renderBody();
	}

}