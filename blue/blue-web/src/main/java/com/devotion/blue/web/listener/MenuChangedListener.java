package com.devotion.blue.web.listener;

import com.devotion.blue.message.Actions;
import com.devotion.blue.message.Message;
import com.devotion.blue.message.MessageListener;
import com.devotion.blue.message.annotation.Listener;
import com.devotion.blue.model.Content;
import com.devotion.blue.model.Taxonomy;
import com.devotion.blue.model.query.ContentQuery;
import com.devotion.blue.model.query.TaxonomyQuery;
import com.devotion.blue.utils.Consts;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Listener(action = Actions.SETTING_CHANGED)
public class MenuChangedListener implements MessageListener {

	@Override
    public void onMessage(Message message) {
		Object temp = message.getData();
		if (temp != null && (temp instanceof Map)) {

			@SuppressWarnings("unchecked")
			Map<String, String> datas = (Map<String, String>) temp;
			// 路由状态发生变化
			if (datas.containsKey("router_content_type") || datas.containsKey("router_fakestatic_enable")) {
				updateMenus();
			}
		}
	}

	private void updateMenus() {
		List<Content> list = ContentQuery.me().findByModule(Consts.MODULE_MENU, null, "order_number ASC");
		if (list != null && list.size() > 0) {
			for (Content content : list) {
				BigInteger taxonomyId = content.getObjectId();
				if (taxonomyId != null) {
					Taxonomy taxonomy = TaxonomyQuery.me().findById(taxonomyId);
					if (taxonomy != null) {
						content.setText(taxonomy.getUrl());
						content.saveOrUpdate();
					}
				}
			}
		}
	}

}