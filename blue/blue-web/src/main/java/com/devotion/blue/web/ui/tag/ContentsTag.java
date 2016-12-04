package com.devotion.blue.web.ui.tag;

import com.devotion.blue.model.Content;
import com.devotion.blue.model.ModelSorter;
import com.devotion.blue.model.Taxonomy;
import com.devotion.blue.model.query.ContentQuery;
import com.devotion.blue.model.query.TaxonomyQuery;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.render.freemarker.JTag;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ContentsTag extends JTag {

	public static final String TAG_NAME = "jp.contents";

	@Override
    public void onRender() {

		String orderBy = getParam("orderBy");
		String keyword = getParam("keyword");

		int pageNumber = getParamToInt("page", 1);
		int pageSize = getParamToInt("pageSize", 10);

		Integer count = getParamToInt("count");
		if (count != null && count > 0) {
			pageSize = count;
		}

		BigInteger[] typeIds = getParamToBigIntegerArray("typeId");
		String[] typeSlugs = getParamToStringArray("typeSlug");
		String[] tags = getParamToStringArray("tag");
		String[] modules = getParamToStringArray("module");
		String[] styles = getParamToStringArray("style");
		String[] flags = getParamToStringArray("flag");
		String[] slugs = getParamToStringArray("slug");
		BigInteger[] userIds = getParamToBigIntegerArray("userId");
		BigInteger[] parentIds = getParamToBigIntegerArray("parentId");
		Boolean hasThumbnail = getParamToBool("hasThumbnail");

		Taxonomy upperTaxonomy = null;
		if (modules != null && modules.length == 1) {
			
			BigInteger upperId = getParamToBigInteger("upperId");
			
			if (upperId != null) {
				upperTaxonomy = TaxonomyQuery.me().findById(upperId);
			}

			if (upperTaxonomy == null) {
				String upperSlug = getParam("upperSlug");
				if (StringUtils.isNotBlank(upperSlug)) {
					upperTaxonomy = TaxonomyQuery.me().findBySlugAndModule(upperSlug, modules[0]);
				}
			}
		}

		if (upperTaxonomy != null) {
			List<Taxonomy> list = TaxonomyQuery.me().findListByModuleAndType(modules[0], null);
			// 找到taxonomy id的所有孩子或孙子
			List<Taxonomy> newlist = new ArrayList<>();
			ModelSorter.sort(list, newlist, upperTaxonomy.getId(), 0);
			if (newlist != null && newlist.size() > 0) {
				slugs = null; // 设置 slugs无效
				typeIds = new BigInteger[newlist.size() + 1];
				typeIds[0] = upperTaxonomy.getId();
				for (int i = 1; i < typeIds.length; i++) {
					typeIds[i] = newlist.get(i - 1).getId();
				}
			}
		}

		List<Content> data = ContentQuery.me().findListInNormal(pageNumber, pageSize, orderBy, keyword, typeIds,
				typeSlugs, modules, styles, flags, slugs, userIds, parentIds, tags, hasThumbnail, null);

		if (data == null || data.isEmpty()) {
			renderText("");
			return;
		}

		setVariable("contents", data);
		renderBody();
	}

}