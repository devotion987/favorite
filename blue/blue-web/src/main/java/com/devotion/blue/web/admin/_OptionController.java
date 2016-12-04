package com.devotion.blue.web.admin;

import com.devotion.blue.message.Actions;
import com.devotion.blue.message.MessageKit;
import com.devotion.blue.model.query.OptionQuery;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.JBaseController;
import com.devotion.blue.web.interceptor.UCodeInterceptor;
import com.jfinal.aop.Before;

import java.util.HashMap;
import java.util.Map;

public class _OptionController extends JBaseController {

	public void index() {
        render((getPara() == null ? "web" : getPara()) + ".html");
	}

	@Before(UCodeInterceptor.class)
	public void save() {

		HashMap<String, String> filesMap = getUploadFilesMap();

		HashMap<String, String> datasMap = new HashMap<>();

		Map<String, String[]> paraMap = getParaMap();
		if (paraMap != null && !paraMap.isEmpty()) {
			for (Map.Entry<String, String[]> entry : paraMap.entrySet()) {
				if (entry.getValue() != null && entry.getValue().length > 0) {
					String value = null;
					for (String v : entry.getValue()) {
						if (StringUtils.isNotEmpty(v)) {
							value = v;
							break;
						}
					}
					datasMap.put(entry.getKey(), value);
				}
			}
		}

		String autosaveString = getPara("autosave");
		if (StringUtils.isNotBlank(autosaveString)) {
			String[] keys = autosaveString.split(",");
			for (String key : keys) {
				if (StringUtils.isNotBlank(key) && !datasMap.containsKey(key)) {
					datasMap.put(key.trim(), getRequest().getParameter(key.trim()));
				}
			}
		}
		
		if(filesMap!=null && !filesMap.isEmpty()){
			datasMap.putAll(filesMap);
		}

		for (Map.Entry<String, String> entry : datasMap.entrySet()) {
			OptionQuery.me().saveOrUpdate(entry.getKey(), entry.getValue());
		}

		MessageKit.sendMessage(Actions.SETTING_CHANGED, datasMap);
		renderAjaxResultForSuccess();
	}


}