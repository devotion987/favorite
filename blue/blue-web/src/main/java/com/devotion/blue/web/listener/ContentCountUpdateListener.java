package com.devotion.blue.web.listener;

import com.devotion.blue.message.Actions;
import com.devotion.blue.message.Message;
import com.devotion.blue.message.MessageListener;
import com.devotion.blue.message.annotation.Listener;
import com.devotion.blue.model.Taxonomy;
import com.devotion.blue.model.query.TaxonomyQuery;

import java.math.BigInteger;

@Listener(action = Actions.CONTENT_COUNT_UPDATE)
public class ContentCountUpdateListener implements MessageListener {
	@Override
	public void onMessage(Message message) {
		BigInteger[] ids = message.getData();
		if (ids != null && ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {
				Taxonomy t = TaxonomyQuery.me().findById(ids[i]);
				if (t != null)
					t.updateContentCount();
			}
		}
	}


}
