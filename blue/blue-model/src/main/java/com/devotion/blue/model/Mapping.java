package com.devotion.blue.model;

import com.devotion.blue.message.Message;
import com.devotion.blue.message.MessageListener;
import com.devotion.blue.message.annotation.Listener;
import com.devotion.blue.model.base.BaseMapping;
import com.devotion.blue.model.core.Table;

import java.math.BigInteger;

@Table(tableName = "mapping", primaryKey = "id")
@Listener(action = { Content.ACTION_ADD, Content.ACTION_DELETE, Content.ACTION_UPDATE }, async = false)
public class Mapping extends BaseMapping<Mapping> implements MessageListener {

	@Override
	public void onMessage(Message message) {
		if (message.getAction().equals(Content.ACTION_DELETE) || (message.getAction().equals(Content.ACTION_UPDATE))) {
			Content c = message.getData();
			removeCache(buildKeyByContentId(c.getId()));
		}

	}

	public static String buildKeyByContentId(BigInteger contentId) {
		return "content:" + contentId;
	}

}
