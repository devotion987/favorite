package com.devotion.blue.web.admin;

import com.devotion.blue.message.Actions;
import com.devotion.blue.message.MessageKit;
import com.devotion.blue.model.Comment;
import com.devotion.blue.model.Content;
import com.devotion.blue.model.User;
import com.devotion.blue.model.query.CommentQuery;
import com.devotion.blue.model.query.ContentQuery;
import com.devotion.blue.model.query.UserQuery;
import com.devotion.blue.model.template.TemplateManager;
import com.devotion.blue.model.template.TplModule;
import com.devotion.blue.utils.Consts;
import com.devotion.blue.utils.CookieUtils;
import com.devotion.blue.utils.EncryptUtils;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.JBaseController;
import com.devotion.blue.web.core.interceptor.ActionCacheClearInterceptor;
import com.devotion.blue.web.interceptor.AdminInterceptor;
import com.devotion.blue.web.interceptor.UCodeInterceptor;
import com.devotion.blue.web.router.RouterMapping;
import com.devotion.blue.web.router.RouterNotAllowConvert;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

@RouterMapping(url = "/admin", viewPath = "/WEB-INF/admin")
@RouterNotAllowConvert
public class _AdminController extends JBaseController {

	@Before(ActionCacheClearInterceptor.class)
    public void index() {
		
		List<TplModule> moduleList = TemplateManager.me().currentTemplateModules();
		setAttr("modules", moduleList);

		if (moduleList != null && moduleList.size() > 0) {
			String moduels[] = new String[moduleList.size()];
			for (int i = 0; i < moduleList.size(); i++) {
				moduels[i] = moduleList.get(i).getName();
			}

			List<Content> contents = ContentQuery.me().findListInNormal(1, 20, null, null, null, null, moduels, null,
					null, null, null, null, null, null, null);
			setAttr("contents", contents);
		}

		Page<Comment> commentPage = CommentQuery.me().paginateWithContentNotInDelete(1, 10, null, null, null, null);
		if (commentPage != null) {
			setAttr("comments", commentPage.getList());
		}

		render("index.html");
	}

	@Clear(AdminInterceptor.class)
	public void login() {
		String username = getPara("username");
		String password = getPara("password");

		if (!StringUtils.areNotEmpty(username, password)) {
			render("login.html");
			return;
		}

		User user = UserQuery.me().findUserByUsername(username);

		if (null == user) {
			renderAjaxResultForError("没有该用户");
			return;
		}

		if (EncryptUtils.verlifyUser(user.getPassword(), user.getSalt(), password) && user.isAdministrator()) {

			MessageKit.sendMessage(Actions.USER_LOGINED, user);

			CookieUtils.put(this, Consts.COOKIE_LOGINED_USER, user.getId().toString());

			renderAjaxResultForSuccess("登录成功");
		} else {
			renderAjaxResultForError("密码错误");
		}
	}

	@Before(UCodeInterceptor.class)
	public void logout() {
		CookieUtils.remove(this, Consts.COOKIE_LOGINED_USER);
		redirect("/admin");
	}

}