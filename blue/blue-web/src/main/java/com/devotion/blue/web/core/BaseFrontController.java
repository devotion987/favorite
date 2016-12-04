package com.devotion.blue.web.core;

import com.devotion.blue.model.template.TemplateManager;
import com.devotion.blue.utils.StringUtils;
import com.jfinal.core.Controller;

public class BaseFrontController extends JBaseController {

    private static final String FILE_SEPARATOR = "_";
    private String renderFile = null;

    public void render(String name) {

        renderFile = null;

        initRenderFile(name);

        if (renderFile != null) {
            super.render(renderFile);
        } else {
            renderError(404);
        }

    }

    private void initRenderFile(String name) {
        if (isWechatBrowser()) {
            initWechatFile(name);

            if (renderFile == null) {
                initMobileFile(name);
            }
        }

        if (isMoblieBrowser()) {
            initMobileFile(name);
        }

        if (renderFile == null) {
            initNormalFile(name);
        }
    }

    public boolean templateExists(String file) {
        boolean isExists = false;
        if (isWechatBrowser()) {
            isExists = TemplateManager.me().existsFileInWechat(file);

            if (!isExists) {
                isExists = TemplateManager.me().existsFileInMobile(file);
            }
        }

        if (isMoblieBrowser()) {
            isExists = TemplateManager.me().existsFileInMobile(file);
        }

        if (!isExists) {
            isExists = TemplateManager.me().existsFile(file);
        }

        return isExists;
    }

    private void initWechatFile(String name) {
        if (name.contains(FILE_SEPARATOR)) {
            do {
                if (TemplateManager.me().existsFileInWechat(name)) {
                    renderFile = buildWechatPath(name);
                    return;
                }
                name = clearProp(name);
            } while (name.contains(FILE_SEPARATOR));
        }

        if (TemplateManager.me().existsFileInWechat(name)) {
            renderFile = buildWechatPath(name);
        }
    }

    private void initMobileFile(String name) {
        if (name.contains(FILE_SEPARATOR)) {
            do {
                if (TemplateManager.me().existsFileInMobile(name)) {
                    renderFile = buildMobilePath(name);
                    return;
                }
                name = clearProp(name);
            } while (name.contains(FILE_SEPARATOR));
        }

        if (TemplateManager.me().existsFileInMobile(name)) {
            renderFile = buildMobilePath(name);
        }
    }

    private void initNormalFile(String name) {
        if (name.contains(FILE_SEPARATOR)) {
            do {
                if (TemplateManager.me().existsFile(name)) {
                    renderFile = buildPath(name);
                    return;
                }
                name = clearProp(name);
            } while (name.contains(FILE_SEPARATOR));
        }

        if (TemplateManager.me().existsFile(name)) {
            renderFile = buildPath(name);
            return;
        }

    }

    private String buildPath(String name) {
        return TemplateManager.me().currentTemplate().getPath() + "/" + name;
    }

    private String buildWechatPath(String name) {
        return TemplateManager.me().currentTemplate().getPath() + "/tpl_wechat/" + name;
    }

    private String buildMobilePath(String name) {
        return TemplateManager.me().currentTemplate().getPath() + "/tpl_mobile/" + name;
    }

    public String clearProp(String fname) {
        return fname.substring(0, fname.lastIndexOf(FILE_SEPARATOR)) + ".html";
    }

    @Override
    public Controller keepPara() {
        super.keepPara();
        String gotoUrl = getPara("goto");
        if (StringUtils.isNotBlank(gotoUrl)) {
            setAttr("goto", StringUtils.urlEncode(gotoUrl));
        }
        return this;
    }

}
