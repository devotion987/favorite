package com.devotion.blue.web.ui.tag;

import com.devotion.blue.model.Content;
import com.devotion.blue.model.query.ContentQuery;
import com.devotion.blue.model.vo.Archive;
import com.devotion.blue.utils.Consts;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.render.freemarker.JTag;

import java.util.List;

public class ArchivesTag extends JTag {

    public static final String TAG_NAME = "jp.archives";

	@Override
	public void onRender() {

		String module = getParam("module", Consts.MODULE_ARTICLE);
		if (StringUtils.isBlank(module)) {
			renderText("");
			return;
		}

		List<Archive> list = ContentQuery.me().findArchives(module);
		if (list == null || list.isEmpty()) {
			renderText("");
			return;
		}

		List<Content> contents = ContentQuery.me().findArchiveByModule(module);
		if (contents == null || contents.isEmpty()) {
			renderText("");
			return;
		}

		for (Content c : contents) {
			String archiveDate = c.getStr("archiveDate");
			if (archiveDate == null)
				continue;
			for (Archive a : list) {
				if (archiveDate.equals(a.getDate())) {
					a.addData(c);
				}
			}
		}

		setVariable("archives", list);
		renderBody();
	}

}