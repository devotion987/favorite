package com.devotion.blue.web.admin;

import com.devotion.blue.message.Actions;
import com.devotion.blue.message.MessageKit;
import com.devotion.blue.model.Content;
import com.devotion.blue.model.Taxonomy;
import com.devotion.blue.model.User;
import com.devotion.blue.model.query.ContentQuery;
import com.devotion.blue.model.query.MappingQuery;
import com.devotion.blue.model.query.TaxonomyQuery;
import com.devotion.blue.model.query.UserQuery;
import com.devotion.blue.model.route.ContentRouter;
import com.devotion.blue.model.template.TemplateManager;
import com.devotion.blue.model.template.TplModule;
import com.devotion.blue.model.template.TplTaxonomyType;
import com.devotion.blue.utils.Consts;
import com.devotion.blue.utils.JsoupUtils;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.JBaseCRUDController;
import com.devotion.blue.web.core.interceptor.ActionCacheClearInterceptor;
import com.devotion.blue.web.core.render.AjaxResult;
import com.devotion.blue.web.interceptor.UCodeInterceptor;
import com.devotion.blue.web.router.RouterMapping;
import com.devotion.blue.web.router.RouterNotAllowConvert;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.*;

@RouterMapping(url = "/admin/content", viewPath = "/WEB-INF/admin/content")
@Before(ActionCacheClearInterceptor.class)
@RouterNotAllowConvert
public class _ContentController extends JBaseCRUDController<Content> {

	private String getModuleName() {
        return getPara("m");
	}

	private String getStatus() {
		return getPara("s");
	}

	@Override
	public void index() {

		TplModule module = TemplateManager.me().currentTemplateModule(getModuleName());
		setAttr("module", module);
		setAttr("delete_count", ContentQuery.me().findCountByModuleAndStatus(getModuleName(), Content.STATUS_DELETE));
		setAttr("draft_count", ContentQuery.me().findCountByModuleAndStatus(getModuleName(), Content.STATUS_DRAFT));
		setAttr("normal_count", ContentQuery.me().findCountByModuleAndStatus(getModuleName(), Content.STATUS_NORMAL));
		setAttr("count", ContentQuery.me().findCountInNormalByModule(getModuleName()));

		setAttr("tids", getPara("tids"));
		BigInteger[] tids = null;
		String[] tidStrings = getPara("tids", "").split(",");

		List<BigInteger> tidList = new ArrayList<>();
		for (String stringid : tidStrings) {
			if (StringUtils.isNotBlank(stringid)) {
				tidList.add(new BigInteger(stringid));
			}
		}
		tids = tidList.toArray(new BigInteger[] {});

		String keyword = getPara("k", "").trim();

		Page<Content> page;
		if (StringUtils.isNotBlank(getStatus())) {
			page = ContentQuery.me().paginateBySearch(getPageNumber(), getPageSize(), getModuleName(), keyword,
					getStatus(), tids, null);
		} else {
			page = ContentQuery.me().paginateByModuleNotInDelete(getPageNumber(), getPageSize(), getModuleName(),
					keyword, tids, null);
		}

		filterUI(tids);

		setAttr("page", page);

		String templateHtml = String.format("admin_content_index_%s.html", module.getName());
		for (int i = 0; i < 2; i++) {
			if (TemplateManager.me().existsFile(templateHtml)) {
				setAttr("include", TemplateManager.me().currentTemplatePath() + "/" + templateHtml);
				return;
			}
			templateHtml = templateHtml.substring(0, templateHtml.lastIndexOf("_")) + ".html";
		}
		setAttr("include", "_index_include.html");

	}

	private void filterUI(BigInteger[] tids) {
		TplModule module = TemplateManager.me().currentTemplateModule(getModuleName());

		if (module == null) {
			return;
		}

		List<TplTaxonomyType> types = module.getTaxonomyTypes();
		if (types != null && !types.isEmpty()) {
			Map<String, List<Taxonomy>> _taxonomyMap = new HashMap<>();

			for (TplTaxonomyType type : types) {
				// 排除标签类的分类删选
				if (TplTaxonomyType.TYPE_SELECT.equals(type.getFormType())) {
					List<Taxonomy> taxonomys = TaxonomyQuery.me().findListByModuleAndTypeAsSort(getModuleName(),
							type.getName());
					processSelected(tids, taxonomys);
					_taxonomyMap.put(type.getTitle(), taxonomys);
				}
			}

			setAttr("_taxonomyMap", _taxonomyMap);
		}
	}

	private void processSelected(BigInteger[] tids, List<Taxonomy> taxonomys) {
		if (taxonomys == null || taxonomys.isEmpty()) {
			return;
		}
		if (tids == null || tids.length == 0) {
			return;
		}
		for (Taxonomy t : taxonomys) {
			for (BigInteger id : tids) {
				if (t.getId().compareTo(id) == 0) {
					t.put("_selected", "selected=\"selected\"");
				}
			}
		}
	}

	@Before(UCodeInterceptor.class)
	public void trash() {
		Content c = ContentQuery.me().findById(getParaToBigInteger("id"));
		if (c != null) {
			c.setStatus(Content.STATUS_DELETE);
			c.saveOrUpdate();
			renderAjaxResultForSuccess("success");
		} else {
			renderAjaxResultForError("trash error!");
		}
	}

	@Before(UCodeInterceptor.class)
	public void draft() {
		Content c = ContentQuery.me().findById(getParaToBigInteger("id"));
		if (c != null) {
			c.setStatus(Content.STATUS_DRAFT);
			c.saveOrUpdate();
			renderAjaxResultForSuccess("success");
		} else {
			renderAjaxResultForError("trash error!");
		}
	}

	@Before(UCodeInterceptor.class)
	public void batchTrash() {
		BigInteger[] ids = getParaValuesToBigInteger("dataItem");
		int count = ContentQuery.me().batchTrash(ids);
		if (count > 0) {
			renderAjaxResultForSuccess("success");
		} else {
			renderAjaxResultForError("trash error!");
		}
	}

	@Before(UCodeInterceptor.class)
	public void batchDelete() {
		BigInteger[] ids = getParaValuesToBigInteger("dataItem");
		int count = ContentQuery.me().batchDelete(ids);
		if (count > 0) {
			renderAjaxResultForSuccess("success");
		} else {
			renderAjaxResultForError("trash error!");
		}
	}

	@Before(UCodeInterceptor.class)
	public void restore() {
		BigInteger id = getParaToBigInteger("id");
		Content c = ContentQuery.me().findById(id);
		if (c != null && c.isDelete()) {
			c.setStatus(Content.STATUS_DRAFT);
			c.setModified(new Date());
			c.saveOrUpdate();
			renderAjaxResultForSuccess("success");
		} else {
			renderAjaxResultForError("restore error!");
		}
	}

	@Before(UCodeInterceptor.class)
	public void delete() {
		BigInteger id = getParaToBigInteger("id");
		final Content c = ContentQuery.me().findById(id);
		if (c == null) {
			renderAjaxResultForError();
			return;
		}
		boolean isSuccess = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				if (c.delete()) {
					MappingQuery.me().deleteByContentId(c.getId());
					return true;
				}
				return false;
			}
		});

		if (isSuccess) {
			renderAjaxResultForSuccess();
			return;
		}
		renderAjaxResultForError();
	}

	@Override
	public void edit() {

		String moduleName = getModuleName();
		BigInteger contentId = getParaToBigInteger("id");

		Content content = ContentQuery.me().findById(contentId);
		if (content != null) {
			setAttr("content", content);
			moduleName = content.getModule();
		}

		TplModule module = TemplateManager.me().currentTemplateModule(moduleName);
		setAttr("module", module);

		String _editor = getCookie("_editor", "tinymce");
		setAttr("_editor", _editor);

		setAttr("urlPreffix", ContentRouter.getContentRouterPreffix(module.getName()));
		setAttr("urlSuffix", ContentRouter.getContentRouterSuffix(module.getName()));

		setSlugInputDisplay(moduleName);

		String templateHtml = String.format("admin_content_edit_%s.html", moduleName);
		for (int i = 0; i < 2; i++) {
			if (TemplateManager.me().existsFile(templateHtml)) {
				setAttr("include", TemplateManager.me().currentTemplatePath() + "/" + templateHtml);
				return;
			}
			templateHtml = templateHtml.substring(0, templateHtml.lastIndexOf("_")) + ".html";
		}

		setAttr("include", "_edit_include.html");
	}

	private void setSlugInputDisplay(String moduleName) {
		if (Consts.MODULE_PAGE.equals(moduleName)) {
			setAttr("slugDisplay", "true");
			return;
		}

		String routerType = ContentRouter.getRouterType();
		if (StringUtils.isBlank(routerType)) { // 没设置过，默认id
			return;
		}

		if (ContentRouter.TYPE_DYNAMIC_ID.equals(routerType) || ContentRouter.TYPE_STATIC_MODULE_ID.equals(routerType)
				|| ContentRouter.TYPE_STATIC_DATE_ID.equals(routerType)
				|| ContentRouter.TYPE_STATIC_PREFIX_ID.equals(routerType)) {
			return;
		}
		setAttr("slugDisplay", "true");
	}

	public void changeEditor() {
		String name = getPara();
		setCookie("_editor", name, Integer.MAX_VALUE);
		renderAjaxResultForSuccess();
	}

	public List<BigInteger> getOrCreateTaxonomyIds(String moduleName) {
		TplModule module = TemplateManager.me().currentTemplateModule(moduleName);
		List<TplTaxonomyType> types = module.getTaxonomyTypes();
		List<BigInteger> tIds = new ArrayList<BigInteger>();
		for (TplTaxonomyType type : types) {
			if (type.isInputType()) {
				String slugsData = getPara("_" + type.getName());
				if (StringUtils.isBlank(slugsData)) {
					continue;
				}

				List<Taxonomy> taxonomyList = TaxonomyQuery.me().findListByModuleAndType(moduleName, type.getName());

				String[] slugs = slugsData.split(",");
				for (String slug : slugs) {
					BigInteger id = getTaxonomyIdFromListBySlug(slug, taxonomyList);
					if (id == null) {
						Taxonomy taxonomy = new Taxonomy();
						taxonomy.setTitle(slug);
						taxonomy.setSlug(slug);
						taxonomy.setContentModule(moduleName);
						taxonomy.setType(type.getName());
						if (taxonomy.save()) {
							id = taxonomy.getId();
						}
					}
					tIds.add(id);
				}
			} else if (type.isSelectType()) {
				BigInteger[] ids = getParaValuesToBigInteger("_" + type.getName());
				if (ids != null && ids.length > 0)
					tIds.addAll(Arrays.asList(ids));
			}
		}
		return tIds;
	}

	private BigInteger getTaxonomyIdFromListBySlug(String slug, List<Taxonomy> list) {
		for (Taxonomy taxonomy : list) {
			if (slug.equals(taxonomy.getSlug()))
				return taxonomy.getId();
		}
		return null;
	}

	@Before(UCodeInterceptor.class)
	@Override
	public void save() {

		final Map<String, String> metas = getMetas();
		final Content content = getContent();

		if (StringUtils.isBlank(content.getTitle())) {
			renderAjaxResultForError("内容标题不能为空！");
			return;
		}

		String slug = StringUtils.isBlank(content.getSlug()) ? content.getTitle() : content.getSlug();
		content.setSlug(slug);

		String username = getPara("username");
		if (StringUtils.isNotBlank(username)) {
			User user = UserQuery.me().findUserByUsername(username);
			if (user == null) {
				renderAjaxResultForError("系统没有该用户：" + username);
				return;
			}
			content.setUserId(user.getId());
		}

		Content dbContent = ContentQuery.me().findBySlug(content.getSlug());
		if (dbContent != null && content.getId() != null && dbContent.getId().compareTo(content.getId()) != 0) {
			renderAjaxResultForError();
			return;
		}

		boolean saved = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {

				Content oldContent = null;
				if (content.getId() != null) {
					oldContent = ContentQuery.me().findById(content.getId());
				}

				if (!content.saveOrUpdate()) {
					return false;
				}

				content.updateCommentCount();

				List<BigInteger> ids = getOrCreateTaxonomyIds(content.getModule());
				if (ids == null || ids.size() == 0) {
					MappingQuery.me().deleteByContentId(content.getId());
				} else {
					if (!MappingQuery.me().doBatchUpdate(content.getId(), ids.toArray(new BigInteger[0]))) {
						return false;
					}
				}

				if (metas != null) {
					for (Map.Entry<String, String> entry : metas.entrySet()) {
						content.saveOrUpdateMetadta(entry.getKey(), entry.getValue());
					}
				}

				MessageKit.sendMessage(Actions.CONTENT_COUNT_UPDATE, ids.toArray(new BigInteger[] {}));

				if (oldContent != null && oldContent.getTaxonomys() != null) {
					List<Taxonomy> taxonomys = oldContent.getTaxonomys();
					BigInteger[] taxonomyIds = new BigInteger[taxonomys.size()];
					for (int i = 0; i < taxonomys.size(); i++) {
						taxonomyIds[i] = taxonomys.get(i).getId();
					}
					MessageKit.sendMessage(Actions.CONTENT_COUNT_UPDATE, taxonomyIds);
				}

				return true;
			}
		});

		if (!saved) {
			renderAjaxResultForError();
			return;
		}

		AjaxResult ar = new AjaxResult();
		ar.setErrorCode(0);
		ar.setData(content.getId());
		renderAjaxResult("save ok", 0, content.getId());
	}

	private Content getContent() {
		Content content = getModel(Content.class);

		content.setText(JsoupUtils.getBodyHtml(content.getText()));

		if (content.getCreated() == null) {
			content.setCreated(new Date());
		}
		content.setModified(new Date());

		User user = getLoginedUser();
		content.setUserId(user.getId());

		return content;
	}

}