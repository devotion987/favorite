package com.devotion.blue.web.listener;

import com.devotion.blue.message.Message;
import com.devotion.blue.message.MessageListener;
import com.devotion.blue.message.annotation.Listener;
import com.devotion.blue.model.Comment;
import com.devotion.blue.model.Content;
import com.devotion.blue.model.User;
import com.devotion.blue.model.query.CommentQuery;
import com.devotion.blue.model.query.ContentQuery;
import com.devotion.blue.model.query.OptionQuery;
import com.devotion.blue.model.query.UserQuery;
import com.devotion.blue.utils.DateUtils;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.notify.email.Email;
import com.devotion.blue.web.notify.email.EmailSenderFactory;

import java.math.BigInteger;

@Listener(action = Comment.ACTION_ADD)
public class AuthorNotificationListener implements MessageListener {

	@Override
	public void onMessage(Message message) {

		// 评论添加到数据库
		if (Comment.ACTION_ADD.equals(message.getAction())) {
			notify(message);
		}

	}

	private void notify(Message message) {
		Object temp = message.getData();
		if (temp == null && !(temp instanceof Comment)) {
			return;
		}

		Comment comment = (Comment) temp;
		if (Comment.STATUS_NORMAL.equals(comment.getStatus()) && comment.getContentId() != null) {
			Content content = ContentQuery.me().findById(comment.getContentId());
			if (content != null) {
				BigInteger authorId = content.getUserId();
				notifyAuthorByEmail(authorId, comment);
				notifyParentCommentAuthorByEmail(comment);
			}
		}
	}

	// private void notifyAuthor(BigInteger id) {
	// notifyAuthorByEmail(id);
	//// notifyBySms(id);
	// }

	// private void notifyBySms(BigInteger id) {
	// Boolean notify =
	// OptionQuery.me().findValueAsBool("notify_author_by_sms_when_has_comment");
	// if (notify != null && notify == true) {
	// User user = UserQuery.me().findById(id);
	// if (user == null || user.getPhone() == null) {
	// return;
	// }
	//
	// String content =
	// OptionQuery.me().findValue("notify_author_content_by_sms_when_has_comment");
	// if (StringUtils.isBlank(content)) {
	// return;
	// }
	//
	// SmsMessage sms = new SmsMessage();
	//
	// sms.setContent(content);
	// sms.setRec_num(user.getPhone());
	// sms.setTemplate(content);
	//// sms.setParam("{\"code\":\"8888\",\"product\":\"JPress\",\"customer\":\"杨福海\"}");
	//// sms.setSign_name("登录验证");
	//
	// SmsSenderFactory.createSender().send(sms);
	//
	// }
	// }

	/**
	 * 通知文章作者
	 * 
	 * @param id
	 * @param comment
	 */
	private void notifyAuthorByEmail(BigInteger id, Comment comment) {
		Boolean notify = OptionQuery.me().findValueAsBool("notify_author_by_email_when_has_comment");
		if (notify != null && notify == true) {
			User user = UserQuery.me().findById(id);
			if (user == null || user.getEmail() == null) {
				return;
			}

			String webname = OptionQuery.me().findValue("web_name");
			webname = StringUtils.isBlank(webname) ? "" : webname;

			String webdomain = OptionQuery.me().findValue("web_domain");
			String url = webdomain + comment.getContentUrl();

			Email email = new Email();
			email.subject("有人在 [" + webname + "] 评论了您的文章！");

			String content = OptionQuery.me().findValue("notify_author_content_by_email_when_has_comment");
			if (StringUtils.isBlank(content)) {
				content = "有人在 [${webname}] 回复了您的文章。<br />回复内容是：${comment.text}<br />详情：<a href=\"${contentUrl}\">${contentUrl}</a>";
			}

			content = replaceContent(content, comment, webname, webdomain, url);

			email.content(content);
			email.to(user.getEmail());

			EmailSenderFactory.createSender().send(email);
		}
	}

	/**
	 * 通知被评论人
	 * 
	 * @param comment
	 */
	private void notifyParentCommentAuthorByEmail(Comment comment) {
		Boolean notify = OptionQuery.me().findValueAsBool("notify_parent_author_by_email_when_has_comment");
		if (notify != null && notify == true) {
			BigInteger parentCommentId = comment.getParentId();
			if (parentCommentId == null) {
				return;
			}

			Comment parentComment = CommentQuery.me().findById(parentCommentId);
			if (parentComment == null) {
				return;
			}

			User user = UserQuery.me().findById(parentComment.getUserId());
			if (user == null || user.getEmail() == null) {
				return;
			}

			String webname = OptionQuery.me().findValue("web_name");
			webname = StringUtils.isBlank(webname) ? "" : webname;

			String webdomain = OptionQuery.me().findValue("web_domain");
			String url = webdomain + comment.getContentUrl();

			Email email = new Email();
			email.subject("有人在 [" + webname + "] 回复了您的评论！");

			String content = OptionQuery.me().findValue("notify_parent_author_content_by_email_when_has_comment");
			if (StringUtils.isBlank(content)) {
				content = "有人在 [${webname}] 回复了您的评论。<br />回复内容是：${comment.text}<br />详情：<a href=\"${contentUrl}\">${contentUrl}</a>";
			}

			content = replaceContent(content, comment, webname, webdomain, url);

			email.content(content);
			email.to(user.getEmail());

			EmailSenderFactory.createSender().send(email);
		}
	}

	private String replaceContent(String content, Comment comment, String webname, String webdomain,
			String contentUrl) {
		if (StringUtils.isNotBlank(webname)) {
			content = content.replace("${webname}", webname);
		}
		if (StringUtils.isNotBlank(webdomain)) {
			content = content.replace("${webdomain}", webdomain);
		}
		if (StringUtils.isNotBlank(contentUrl)) {
			content = content.replace("${contentUrl}", contentUrl);
		}
		if (StringUtils.isNotBlank(comment.getText())) {
			content = content.replace("${comment.text}", comment.getText());
		}
		if (StringUtils.isNotBlank(comment.getAuthor())) {
			content = content.replace("${comment.author}", comment.getAuthor());
		}
		if (StringUtils.isNotBlank(comment.getEmail())) {
			content = content.replace("${comment.email}", comment.getEmail());
		}
		if (StringUtils.isNotBlank(comment.getContentUrl())) {
			content = content.replace("${comment.contentUrl}", contentUrl);
		}
		if (StringUtils.isNotBlank(comment.getContent().getTitle())) {
			content = content.replace("${comment.contentTitle}", comment.getContent().getTitle());
		}
		if (StringUtils.isNotBlank(comment.getUser().getUsername())) {
			content = content.replace("${comment.username}", comment.getUser().getUsername());
		}
		if (comment.getCreated() != null) {
			content = content.replace("${comment.created}", DateUtils.format(comment.getCreated()));
		}
		return content;
	}

}
