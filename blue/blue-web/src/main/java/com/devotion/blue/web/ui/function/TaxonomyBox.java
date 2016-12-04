package com.devotion.blue.web.ui.function;

import com.devotion.blue.model.Content;
import com.devotion.blue.model.Taxonomy;
import com.devotion.blue.model.query.TaxonomyQuery;
import com.devotion.blue.model.template.TplTaxonomyType;
import com.devotion.blue.web.core.render.freemarker.JFunction;

import java.util.List;

public class TaxonomyBox extends JFunction {

	private TplTaxonomyType taxonomyType;
    private Content content;
	private List<Taxonomy> contentTaxonomyList;

	@Override
	public Object onExec() {
		init();
		return doExec();
	}

	private void init() {

		this.taxonomyType = (TplTaxonomyType) get(0);
		this.content = (Content) get(1);

		if (content != null) {
			contentTaxonomyList = TaxonomyQuery.me().findListByContentId(content.getId());
		} else {
			contentTaxonomyList = null;
		}
	}

	private Object doExec() {

		String moduleName = taxonomyType.getModule().getName();
		String txType = taxonomyType.getName();
		List<Taxonomy> list = TaxonomyQuery.me().findListByModuleAndTypeAsTree(moduleName, txType);
		StringBuilder htmlBuilder = new StringBuilder();
		if (list != null && list.size() > 0) {
			doBuilder(list, htmlBuilder);
		}
		return htmlBuilder.toString();
	}

	private void doBuilder(List<Taxonomy> list, StringBuilder htmlBuilder) {
		htmlBuilder.append("<ul>");
		for (Taxonomy taxonomy : list) {
			
			boolean checked = contentTaxonomyList != null && contentTaxonomyList.contains(taxonomy);
			String html = "<li ><label><input  name=\"_%s\" value=\"%s\" %s type=\"checkbox\"/>%s</label></li>";
			htmlBuilder.append(String.format(html, taxonomyType.getName(), taxonomy.getId(),
					checked ? "checked=\"checked\"" : "", taxonomy.getTitle()));

			if (taxonomy.getChildList() != null && taxonomy.getChildList().size() > 0) {
				doBuilder(taxonomy.getChildList(), htmlBuilder);
			}
		}
		htmlBuilder.append("</ul>");
	}

}