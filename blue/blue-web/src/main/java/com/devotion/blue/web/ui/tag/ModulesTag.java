package com.devotion.blue.web.ui.tag;

import com.devotion.blue.model.template.Template;
import com.devotion.blue.model.template.TemplateManager;
import com.devotion.blue.model.template.TplModule;
import com.devotion.blue.web.core.render.freemarker.JTag;

import java.util.List;

public class ModulesTag extends JTag {

    public static final String TAG_NAME = "jp.modules";

	@Override
	public void onRender() {

		Template t = TemplateManager.me().currentTemplate();

		if (t != null) {
			List<TplModule> modules = t.getModules();
			setVariable("modules", modules);
			renderBody();
		} else {
			renderText("");
		}
	}

}