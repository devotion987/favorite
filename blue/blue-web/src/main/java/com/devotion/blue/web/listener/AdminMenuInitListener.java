package com.devotion.blue.web.listener;

import com.devotion.blue.message.Message;
import com.devotion.blue.message.MessageListener;
import com.devotion.blue.message.annotation.Listener;
import com.devotion.blue.model.template.Template;
import com.devotion.blue.model.template.TemplateManager;
import com.devotion.blue.model.template.TplModule;
import com.devotion.blue.model.template.TplTaxonomyType;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.menu.MenuGroup;
import com.devotion.blue.web.menu.MenuItem;
import com.devotion.blue.web.menu.MenuManager;

import java.util.List;

@Listener(action = MenuManager.ACTION_INIT_MENU, async = false)
public class AdminMenuInitListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		Object temp = message.getData();
		if (temp == null && !(temp instanceof MenuManager)) {
			return;
		}

		MenuManager menuMnager = (MenuManager) temp;
		initModuleMenuGroup(menuMnager);

		menuMnager.addMenuGroup(createAttachmentMenuGroup());
		menuMnager.addMenuGroup(createWechatMenuGroup());

		menuMnager.addMenuGroup(MenuGroup.createBlockGroup());

		menuMnager.addMenuGroup(createUserMenuGroup());
		menuMnager.addMenuGroup(createTemplateMenuGroup());
		menuMnager.addMenuGroup(createAddonMenuGroup());
		menuMnager.addMenuGroup(createSettingMenuGroup());
		menuMnager.addMenuGroup(createToolsMenuGroup());
	}

	public void initModuleMenuGroup(MenuManager menuMnager) {
		Template t = TemplateManager.me().currentTemplate();
		if (t == null || t.getModules() == null) {
			return;
		}

		List<TplModule> modules = t.getModules();
		for (TplModule module : modules) {
			String iconClass = module.getIconClass();
			if (StringUtils.isBlank(iconClass)) {
				iconClass = "fa fa-file-text-o";
			}
			MenuGroup group = new MenuGroup(module.getName(), iconClass, module.getTitle());

			group.addMenuItem(new MenuItem("list", "/admin/content?m=" + module.getName(), module.getListTitle()));
			group.addMenuItem(new MenuItem("edit", "/admin/content/edit?m=" + module.getName(), module.getAddTitle()));

			List<TplTaxonomyType> types = module.getTaxonomyTypes();
			if (types != null && !types.isEmpty()) {
				for (TplTaxonomyType type : types) {
					group.addMenuItem(new MenuItem(type.getName(),
							"/admin/taxonomy?m=" + module.getName() + "&t=" + type.getName(), type.getTitle()));
				}
			}

			if (StringUtils.isNotBlank(module.getCommentTitle())) {
				group.addMenuItem(new MenuItem("comment", "/admin/comment?t=comment&m=" + module.getName(),
						module.getCommentTitle()));
			}

			menuMnager.addMenuGroup(group);
		}
	}

	private MenuGroup createWechatMenuGroup() {
		MenuGroup group = new MenuGroup("wechat", "fa fa-weixin", "微信");

		{
			group.addMenuItem(new MenuItem("r", "/admin/wechat", "自动回复"));
			group.addMenuItem(new MenuItem("rd", "/admin/wechat/reply_default", "默认回复"));
			group.addMenuItem(new MenuItem("menu", "/admin/wechat/menu", "菜单设置"));
			group.addMenuItem(new MenuItem("option", "/admin/wechat/option", "微信设置"));
		}
		return group;
	}

	private MenuGroup createAttachmentMenuGroup() {
		MenuGroup group = new MenuGroup("attachment", "fa fa-file-image-o", "附件");

		{
			group.addMenuItem(new MenuItem("list", "/admin/attachment", "所有附件"));
			group.addMenuItem(new MenuItem("upload", "/admin/attachment/upload", "上传"));
		}
		return group;
	}

	private MenuGroup createUserMenuGroup() {
		MenuGroup group = new MenuGroup("user", "fa fa-user", "用户");

		{
			group.addMenuItem(new MenuItem("list", "/admin/user", "所有用户"));
			group.addMenuItem(new MenuItem("edit", "/admin/user/edit", "添加"));
			group.addMenuItem(new MenuItem("info", "/admin/user/info", "我的资料"));
		}
		return group;
	}

	private MenuGroup createTemplateMenuGroup() {
		MenuGroup group = new MenuGroup("template", "fa fa-magic", "模板");
		{
			group.addMenuItem(new MenuItem("list", "/admin/template", "所有模板"));
			group.addMenuItem(new MenuItem("install", "/admin/template/install", "模板安装"));
			group.addMenuItem(new MenuItem("menu", "/admin/template/menu", "菜单"));
			group.addMenuItem(new MenuItem("setting", "/admin/template/setting", "设置"));
			group.addMenuItem(new MenuItem("edit", "/admin/template/edit", "编辑"));
		}
		return group;
	}

	private MenuGroup createAddonMenuGroup() {
		MenuGroup group = new MenuGroup("addon", "fa fa-plug", "插件");
		{
			group.addMenuItem(new MenuItem("list", "/admin/addon", "所有插件"));
			group.addMenuItem(new MenuItem("install", "/admin/addon/install", "安装"));
		}
		return group;
	}

	private MenuGroup createSettingMenuGroup() {
		MenuGroup group = new MenuGroup("option", "fa fa-cog", "设置");
		{
			group.addMenuItem(new MenuItem("list", "/admin/option/web", "常规"));
			group.addMenuItem(new MenuItem("edit", "/admin/option/comment", "评论"));
			group.addMenuItem(new MenuItem("n", "/admin/option/notification", "通知"));
			group.addMenuItem(new MenuItem("seo", "/admin/option/seo", "SEO"));
			group.addMenuItem(new MenuItem("watermark", "/admin/option/watermark", "水印"));
			group.addMenuItem(new MenuItem("url", "/admin/option/url", "连接形式"));
			group.addMenuItem(new MenuItem("reg", "/admin/option/register", "登录注册"));
			group.addMenuItem(new MenuItem("cdn", "/admin/option/cdn", "CDN加速"));
			group.addMenuItem(new MenuItem("api", "/admin/api", "API应用"));
		}

		return group;
	}

	private MenuGroup createToolsMenuGroup() {
		MenuGroup group = new MenuGroup("tools", "fa fa-wrench", "工具");

		{
			group.addMenuItem(new MenuItem("import", "/admin/tools/_import", "导入"));
			group.addMenuItem(new MenuItem("export", "/admin/tools/export", "导出"));
			group.addMenuItem(new MenuItem("sync", "/admin/tools/sync", "同步"));
		}
		return group;
	}

}
