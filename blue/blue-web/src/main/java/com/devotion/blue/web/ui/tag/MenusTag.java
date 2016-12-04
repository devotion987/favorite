package com.devotion.blue.web.ui.tag;

import com.devotion.blue.model.Content;
import com.devotion.blue.model.ModelSorter;
import com.devotion.blue.model.Taxonomy;
import com.devotion.blue.model.query.ContentQuery;
import com.devotion.blue.model.route.TaxonomyRouter;
import com.devotion.blue.model.vo.NavigationMenu;
import com.devotion.blue.utils.Consts;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.render.freemarker.JTag;
import com.jfinal.core.JFinal;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MenusTag extends JTag {

	public static final String TAG_NAME = "jp.menus";

	private List<Taxonomy> currentTaxonomys;
    private Content currentContent;
	private HttpServletRequest request;

	public MenusTag(HttpServletRequest request) {
		this.request = request;
	}

	public MenusTag(HttpServletRequest request, List<Taxonomy> taxonomys, Content content) {
		this.request = request;
		this.currentTaxonomys = taxonomys;
		this.currentContent = content;
	}

	public MenusTag(HttpServletRequest request, Taxonomy taxonomy) {
		this.request = request;
		currentTaxonomys = new ArrayList<>();
		currentTaxonomys.add(taxonomy);
	}

	@Override
	public void onRender() {

		BigInteger parentId = getParamToBigInteger("parentId");
		String activeClass = getParam("activeClass", "active");

		List<Content> list = ContentQuery.me().findByModule(Consts.MODULE_MENU, parentId, "order_number ASC");

		if (list == null || list.isEmpty()) {
			renderText("");
			return;
		}

		setActiveMenu(list);

		if (parentId == null) {
			ModelSorter.tree(list);
		}

		List<NavigationMenu> menulist = new ArrayList<>();
		for (Content c : list) {
			menulist.add(new NavigationMenu(c, activeClass));
		}

		setVariable("menus", menulist);
		renderBody();
	}

	private void setActiveMenu(List<Content> menuContentList) {
		for (Content menuContent : menuContentList) {
			menuContent.remove("active");
			if (menuContent.getText() != null
					&& menuContent.getText().equals(StringUtils.urlDecode(request.getRequestURI()))) {
				menuContent.put("active", "active");
			}
		}

		if (currentContent != null) {
			String contentUrl = currentContent.getUrl();
			for (Content menuContent : menuContentList) {
				if (contentUrl != null && contentUrl.equals(menuContent.getText())) {
					menuContent.put("active", "active");
				}

				String onlyModuleUrl = JFinal.me().getContextPath() + "/" + currentContent.getModule();
				if (onlyModuleUrl.equals(menuContent.getText())) {
					menuContent.put("active", "active");
				}
			}
		}

		if (currentTaxonomys == null || currentTaxonomys.isEmpty()) {
			return;
		}

		for (Taxonomy taxonomy : currentTaxonomys) {
			String routerWithoutPageNumber = TaxonomyRouter.getRouterWithoutPageNumber(taxonomy);
			routerWithoutPageNumber = JFinal.me().getContextPath() + routerWithoutPageNumber;
			if (StringUtils.isNotBlank(routerWithoutPageNumber)) {
				for (Content menuContent : menuContentList) {
					if (menuContent.getText() != null
							&& menuContent.getText().startsWith(StringUtils.urlDecode(routerWithoutPageNumber))) {
						menuContent.put("active", "active");
					}

					String onlyModuleUrl = JFinal.me().getContextPath() + "/" + taxonomy.getContentModule();
					if (onlyModuleUrl.equals(menuContent.getText())) {
						menuContent.put("active", "active");
					}
				}
			}
		}

	}

}