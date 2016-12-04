package com.devotion.blue.web.core.render;

import com.devotion.blue.model.template.Template;
import com.devotion.blue.model.template.TemplateManager;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.JPress;
import com.jfinal.render.IErrorRenderFactory;
import com.jfinal.render.Render;
import com.jfinal.render.TextRender;

public class JErrorRenderFactory implements IErrorRenderFactory {

	@Override
	public Render getRender(int errorCode, String view) {
		if (!JPress.isInstalled()) {
			return new TextRender(errorCode + " error in jpress.");
		}

		Template template = TemplateManager.me().currentTemplate();
		if (null == template) {
			return new TextRender(String.format("%s error! you haven't configure your template yet.", errorCode));
		}

		String errorHtml = TemplateManager.me().currentTemplatePath() + "/" + errorCode + ".html";

		String renderType = TemplateManager.me().currentTemplate().getRenderType();

		// the default render type is freemarker
		if (StringUtils.isBlank(renderType)) {
			return new JFreemarkerRender(errorHtml, true);
		}

		if ("freemarker".equalsIgnoreCase(renderType)) {
			return new JFreemarkerRender(errorHtml, true);
		} else if ("thymeleaf".equalsIgnoreCase(renderType)) {
			return new ThymeleafRender(errorHtml);
		}

		return new TextRender(errorCode + " error in jpress.");
	}

}
