package com.devotion.blue.web.listener;

import com.devotion.blue.message.Message;
import com.devotion.blue.message.MessageListener;
import com.devotion.blue.message.annotation.Listener;
import com.devotion.blue.model.Comment;
import com.devotion.blue.model.Content;
import com.devotion.blue.model.query.CommentQuery;
import com.devotion.blue.model.query.ContentQuery;

@Listener(action = { Comment.ACTION_ADD, Comment.ACTION_UPDATE, Comment.ACTION_DELETE })
public class CommentListener implements MessageListener {

	@Override
	public void onMessage(Message message) {

		// 有新评论
		if (Comment.ACTION_ADD.equals(message.getAction())) {
			updateContentCommentCount(message);
			updateCommentCount(message);
		}

		// 评论被更新（可能状态呗更新）
		else if (Comment.ACTION_UPDATE.equals(message.getAction())) {
			updateContentCommentCount(message);
			updateCommentCount(message);
		}

		// 评论被删除
		else if (Comment.ACTION_DELETE.equals(message.getAction())) {
			updateContentCommentCount(message);
			updateCommentCount(message);
		}
	}

	/**
	 * 更新文章评论数量
	 * 
	 * @param message
	 */
	private void updateContentCommentCount(Message message) {
		Comment comment = message.getData();
		if (comment != null && comment.getContentId() != null) {
			Content content = ContentQuery.me().findById(comment.getContentId());
			if (content != null) {
				content.updateCommentCount();
			}
		}
	}

	/**
	 * 更新评论的回复数量
	 * 
	 * @param message
	 */
	private void updateCommentCount(Message message) {
		Comment comment = message.getData();
		if (comment != null && comment.getParentId() != null) {
			Comment parentComment = CommentQuery.me().findById(comment.getParentId());
			if (parentComment != null) {
				parentComment.updateCommentCount();
			}
		}
	}

}
