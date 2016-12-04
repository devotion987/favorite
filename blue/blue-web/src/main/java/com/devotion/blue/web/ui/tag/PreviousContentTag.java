package com.devotion.blue.web.ui.tag;

import com.devotion.blue.model.Content;
import com.devotion.blue.model.query.ContentQuery;
import com.devotion.blue.web.core.render.freemarker.JTag;

public class PreviousContentTag extends JTag {

    public static final String TAG_NAME = "jp.previous";

	private Content currentContent;

	public PreviousContentTag(Content content) {
		this.currentContent = content;
	}

	@Override
	public void onRender() {

		Content content = ContentQuery.me().findPrevious(currentContent);

		if (content == null) {
			renderText("");
			return;
		}

		setVariable("previous", content);
		renderBody();
	}

}