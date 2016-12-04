package com.devotion.blue.web.ui.tag;

import com.devotion.blue.model.Taxonomy;
import com.devotion.blue.model.query.TaxonomyQuery;
import com.devotion.blue.utils.Consts;
import com.devotion.blue.web.core.render.freemarker.JTag;

import java.util.List;

public class TagsTag extends JTag {

    public static final String TAG_NAME = "jp.tags";

	@Override
	public void onRender() {

		int count = getParamToInt("count", 0);
		count = count <= 0 ? 10 : count;

		String orderby = getParam("orderBy");

		String module = getParam("module", Consts.MODULE_ARTICLE);
		List<Taxonomy> list = TaxonomyQuery.me().findListByModuleAndType(module, "tag", null, count, orderby);
		setVariable("tags", list);

		renderBody();
	}

}