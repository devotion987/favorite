package com.devotion.blue.web.listener;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.devotion.blue.message.Message;
import com.devotion.blue.message.MessageListener;
import com.devotion.blue.message.annotation.Listener;
import com.devotion.blue.model.Comment;
import com.devotion.blue.model.Content;
import com.devotion.blue.model.User;
import com.devotion.blue.model.query.OptionQuery;
import com.devotion.blue.model.query.UserQuery;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.notify.email.Email;
import com.devotion.blue.web.notify.email.EmailSenderFactory;
import com.jfinal.core.JFinal;

@Listener(action = { Content.ACTION_ADD, Comment.ACTION_ADD })
public class AtProcessListener implements MessageListener {

	@Override
	public void onMessage(Message message) {

		List<BigInteger> userIds = new ArrayList<>();

		// 新增文章
		if (Content.ACTION_ADD.equals(message.getAction())) {
			Content content = message.getData();
			String text = generateUserLinks(content.getText(), userIds);
			content.setText(text);
			content.update();
		}

		// 新增评论
		else if (Comment.ACTION_ADD.equals(message.getAction())) {
			Comment comment = message.getData();
			String text = generateUserLinks(comment.getText(), userIds);
			comment.setText(text);
			comment.update();
		}

		notifyUser(userIds);

	}

	private void notifyUser(List<BigInteger> userIds) {
		if (userIds != null && userIds.size() > 0) {
			for (BigInteger userId : userIds) {
				notifyByEmail(userId);
			}
		}
	}

	private void notifyByEmail(BigInteger id) {
		Boolean notify = OptionQuery.me().findValueAsBool("notify_author_by_email_when_at");
		if (notify != null && notify == true) {
			User user = UserQuery.me().findById(id);
			if (user == null || user.getEmail() == null) {
				return;
			}

			Email email = new Email();
			email.subject("有人@你了....");

			String content = OptionQuery.me().findValue("notify_author_content_by_email_when_at");
			if (StringUtils.isBlank(content)) {
				content = "有人@你了....";
			}
			email.content(content);
			email.to(user.getEmail());

			EmailSenderFactory.createSender().send(email);
		}
	}

	static Pattern userPattern = Pattern.compile("@([^@^\\s^:]{1,})([\\s\\:\\,\\;]{0,1})");

	public static String generateUserLinks(String msg, List<BigInteger> userIds) {
		StringBuilder html = new StringBuilder();
		int lastIdx = 0;
		Matcher matchr = userPattern.matcher(msg);
		while (matchr.find()) {
			String groupString = matchr.group();
			String username = groupString.substring(1, groupString.length()).trim();
			html.append(msg.substring(lastIdx, matchr.start()));
			User user = UserQuery.me().findUserByUsername(username);
			if (user != null && !user.isFrozen()) {
				String userUrl = JFinal.me().getContextPath() + user.getUrl();
				html.append("<a href=\"" + userUrl + "\" class='referer' target='_blank'>@");
				html.append(username.trim());
				html.append("</a> ");
				if (userIds != null && !userIds.contains(user.getId())) {
					userIds.add(user.getId());
				}
			} else {
				html.append(groupString);
			}
			lastIdx = matchr.end();
		}
		html.append(msg.substring(lastIdx));
		return html.toString();
	}

}
