package com.devotion.blue.web.core.render;

import com.devotion.blue.model.template.TemplateManager;
import com.jfinal.render.IMainRenderFactory;
import com.jfinal.render.Render;

public class JPressRenderFactory implements IMainRenderFactory {

	public JPressRenderFactory() {
	}

	@Override
	public Render getRender(String view) {
		// front url
		if (view.startsWith("/templates")) {
			String renderType = TemplateManager.me().currentTemplate().getRenderType();

			if (renderType == null) {
				return new JFreemarkerRender(view, true);
			}

			if (renderType.equalsIgnoreCase("freemarker")) {
				return new JFreemarkerRender(view, true);
			}

			else if (renderType.equalsIgnoreCase("thymeleaf")) {
				return new ThymeleafRender(view);
			}

			return new JFreemarkerRender(view, true);

		}

		// admin url
		return new JFreemarkerRender(view, false);
	}

	@Override
	public String getViewExtension() {
		return ".html";
	}

}
