package com.devotion.blue.web.listener;

import com.devotion.blue.message.Message;
import com.devotion.blue.message.MessageListener;
import com.devotion.blue.message.annotation.Listener;
import com.devotion.blue.model.Content;
import com.devotion.blue.search.SearcherBean;
import com.devotion.blue.search.SearcherKit;

@Listener(action = { Content.ACTION_ADD, Content.ACTION_DELETE, Content.ACTION_UPDATE })
public class SearcherActionListener implements MessageListener {

	@Override
    public void onMessage(Message message) {
		// 文章添加到数据库
		if (Content.ACTION_ADD.equals(message.getAction())) {
			SearcherKit.add(createSearcherBean(message));
		}
		// 文章被更新
		else if (Content.ACTION_UPDATE.equals(message.getAction())) {
			SearcherKit.update(createSearcherBean(message));
		}
		// 文章被删除
		else if (Content.ACTION_DELETE.equals(message.getAction())) {
			Content content = message.getData();
			SearcherKit.delete(String.valueOf(content.getId()));
		}
	}

	private SearcherBean createSearcherBean(Message message) {
		Content content = message.getData();

		SearcherBean bean = new SearcherBean();

		bean.setSid(String.valueOf(content.getId()));
		bean.setTitle(content.getTitle());
		bean.setDescription(content.getSummary());
		bean.setContent(content.getText());
		bean.setUrl(content.getUrl());
		bean.setCreated(content.getCreated());
		bean.setData(content);

		return bean;
	}

}