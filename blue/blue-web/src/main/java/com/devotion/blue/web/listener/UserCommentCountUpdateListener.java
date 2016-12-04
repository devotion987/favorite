package com.devotion.blue.web.listener;

import com.devotion.blue.message.Message;
import com.devotion.blue.message.MessageListener;
import com.devotion.blue.message.annotation.Listener;
import com.devotion.blue.model.Comment;
import com.devotion.blue.model.User;
import com.devotion.blue.model.query.UserQuery;

@Listener(action = { Comment.ACTION_ADD, Comment.ACTION_UPDATE, Comment.ACTION_DELETE })
public class UserCommentCountUpdateListener implements MessageListener {

	@Override
    public void onMessage(Message message) {

		// 评论添加到数据库
		if (Comment.ACTION_ADD.equals(message.getAction())) {
			updateUserCommentCount(message);
		}

		// 文章被更新
		else if (Comment.ACTION_UPDATE.equals(message.getAction())) {
			updateUserCommentCount(message);
		}

		// 文章被删除
		else if (Comment.ACTION_DELETE.equals(message.getAction())) {
			updateUserCommentCount(message);
		}
	}

	private void updateUserCommentCount(Message message) {
		Object temp = message.getData();
		if (temp != null && temp instanceof Comment) {
			Comment comment = (Comment) temp;
			if (Comment.STATUS_NORMAL.equals(comment.getStatus()) && comment.getUserId() != null) {
				User user = UserQuery.me().findById(comment.getUserId());
				if (user != null)
					UserQuery.me().updateCommentCount(user);
			}
		}
	}

}