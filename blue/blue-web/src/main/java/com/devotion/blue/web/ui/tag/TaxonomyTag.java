package com.devotion.blue.web.ui.tag;

import com.devotion.blue.model.Taxonomy;
import com.devotion.blue.model.query.TaxonomyQuery;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.render.freemarker.JTag;

import java.math.BigInteger;

public class TaxonomyTag extends JTag {

	public static final String TAG_NAME = "jp.taxonomy";

	@Override
    public void onRender() {

		Taxonomy taxonomy = null;
		BigInteger id = getParamToBigInteger("id");
		if (id != null) {
			taxonomy = TaxonomyQuery.me().findById(id);
		}

		if (taxonomy == null) {
			String slug = getParam("slug");
			String module = getParam("module");
			if (StringUtils.areNotBlank(slug, module)) {
				taxonomy = TaxonomyQuery.me().findBySlugAndModule(slug, module);
			}
		}

		if (taxonomy == null) {
			renderText("");
			return;
		}

		setVariable("taxonomy", taxonomy);
		renderBody();
	}

}