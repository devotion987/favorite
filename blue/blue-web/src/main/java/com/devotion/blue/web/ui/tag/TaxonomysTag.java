package com.devotion.blue.web.ui.tag;

import com.devotion.blue.model.ModelSorter;
import com.devotion.blue.model.Taxonomy;
import com.devotion.blue.model.query.TaxonomyQuery;
import com.devotion.blue.web.core.render.freemarker.JTag;

import java.math.BigInteger;
import java.util.List;

public class TaxonomysTag extends JTag {

	public static final String TAG_NAME = "jp.taxonomys";

	private List<Taxonomy> filterList;

	public TaxonomysTag() {
    }

	public TaxonomysTag(List<Taxonomy> taxonomys) {
		filterList = taxonomys;
	}

	@Override
	public void onRender() {

		Integer count = getParamToInt("count");
		String module = getParam("module");
		String activeClass = getParam("activeClass", "active");
		String type = getParam("type");
		String orderby = getParam("orderBy");
		BigInteger parentId = getParamToBigInteger("parentId");
		Boolean asTree = getParamToBool("asTree");

		List<Taxonomy> list = TaxonomyQuery.me().findListByModuleAndType(module, type, parentId, count, orderby);
		if (filterList != null && list != null && list.size() > 0) {
			for (Taxonomy taxonomy : list) {
				taxonomy.initFilterList(filterList, activeClass);
			}
		}

		if (asTree != null && asTree == true) {
			ModelSorter.tree(list);
		}

		setVariable("taxonomys", list);
		renderBody();
	}

}