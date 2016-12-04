package com.devotion.blue.web.listener;

import com.devotion.blue.message.Message;
import com.devotion.blue.message.MessageListener;
import com.devotion.blue.message.annotation.Listener;
import com.devotion.blue.model.Content;
import com.devotion.blue.model.User;
import com.devotion.blue.model.query.UserQuery;

@Listener(action = { Content.ACTION_ADD, Content.ACTION_DELETE, Content.ACTION_UPDATE })
public class UserContentCountChangedListener implements MessageListener {

	@Override
    public void onMessage(Message message) {

		// 文章添加到数据库
		if (Content.ACTION_ADD.equals(message.getAction())) {
			updateUserConentCount(message);
		}

		// 文章被更新
		else if (Content.ACTION_UPDATE.equals(message.getAction())) {
			updateUserConentCount(message);
		}

		// 文章被删除
		else if (Content.ACTION_DELETE.equals(message.getAction())) {
			updateUserConentCount(message);
		}
	}

	private void updateUserConentCount(Message message) {
		Object temp = message.getData();
		if (temp != null && temp instanceof Content) {
			Content content = (Content) temp;
			if (Content.STATUS_NORMAL.equals(content.getStatus()) && content.getUserId() != null) {
				User user = UserQuery.me().findById(content.getUserId());
				if (user != null)
					UserQuery.me().updateContentCount(user);
			}
		}
	}

}