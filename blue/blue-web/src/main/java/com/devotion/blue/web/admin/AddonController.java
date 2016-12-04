package com.devotion.blue.web.admin;

import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.JBaseController;
import com.devotion.blue.web.core.addon.AddonInfo;
import com.devotion.blue.web.core.addon.AddonManager;
import com.devotion.blue.web.core.interceptor.ActionCacheClearInterceptor;
import com.devotion.blue.web.menu.MenuManager;
import com.devotion.blue.web.router.RouterMapping;
import com.devotion.blue.web.router.RouterNotAllowConvert;
import com.jfinal.aop.Before;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;

import java.io.File;

@RouterMapping(url = "/admin/addon", viewPath = "/WEB-INF/admin/addon")
@Before(ActionCacheClearInterceptor.class)
@RouterNotAllowConvert
public class AddonController extends JBaseController {

	public void index() {
        keepPara();

		setAttr("addons", AddonManager.me().getAddons());
		setAttr("addonCount", AddonManager.me().getAddons().size());
		setAttr("startedAddonCount", AddonManager.me().getStartedAddons().size());
	}

	public void install() {
		keepPara();

		if (!isMultipartRequest()) {
			return;
		}

		UploadFile ufile = getFile();
		if (ufile == null) {
			renderAjaxResultForError("您还未选择插件文件");
			return;
		}

		String webRoot = PathKit.getWebRootPath();

		StringBuilder newFileName = new StringBuilder(webRoot).append("/WEB-INF/addons/").append(ufile.getFileName());

		File newfile = new File(newFileName.toString());

		if (newfile.exists()) {
			renderAjaxResultForError("该插件已经安装！");
			return;
		}

		if (!newfile.getParentFile().exists()) {
			newfile.getParentFile().mkdirs();
		}

		ufile.getFile().renameTo(newfile);

		if (AddonManager.me().install(newfile)){
			MenuManager.me().refresh();
			renderAjaxResultForSuccess();
		}else{
			renderAjaxResultForError("安装失败，可能已经有相同ID的插件了。");
		}
			
	}

	public void uninstall() {
		keepPara();

		String id = getPara("id");
		if (StringUtils.isBlank(id)) {
			renderAjaxResultForError();
			return;
		}

		AddonInfo addon = AddonManager.me().findById(id);
		if (addon == null) {
			renderAjaxResultForError();
			return;
		}

		if (AddonManager.me().uninstall(addon)) {
			MenuManager.me().refresh();
			renderAjaxResultForSuccess();
		} else {
			renderAjaxResultForError();
		}

	}

	public void start() {
		keepPara();

		String id = getPara("id");
		if (StringUtils.isBlank(id)) {
			renderAjaxResultForError();
			return;
		}

		AddonInfo addon = AddonManager.me().findById(id);
		if (addon == null) {
			renderAjaxResultForError();
			return;
		}

		if (AddonManager.me().start(addon)) {
			MenuManager.me().refresh();
			renderAjaxResultForSuccess();
		} else {
			renderAjaxResultForError();
		}

	}

	public void stop() {
		keepPara();

		String id = getPara("id");
		if (StringUtils.isBlank(id)) {
			renderAjaxResultForError();
			return;
		}

		AddonInfo addon = AddonManager.me().findById(id);
		if (addon == null) {
			renderAjaxResultForError();
			return;
		}

		if (AddonManager.me().stop(addon)) {
			MenuManager.me().refresh();
			renderAjaxResultForSuccess();
		} else {
			renderAjaxResultForError();
		}
	}

}