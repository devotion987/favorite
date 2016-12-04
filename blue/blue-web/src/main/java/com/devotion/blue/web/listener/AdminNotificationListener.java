package com.devotion.blue.web.listener;

import com.devotion.blue.message.Message;
import com.devotion.blue.message.MessageListener;
import com.devotion.blue.message.annotation.Listener;
import com.devotion.blue.model.User;
import com.devotion.blue.model.query.OptionQuery;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.notify.email.Email;
import com.devotion.blue.web.notify.email.EmailSenderFactory;

@Listener(action = User.ACTION_ADD)
public class AdminNotificationListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		// 用用户注册了
		if (User.ACTION_ADD.equals(message.getAction())) {
			notify(message);
		}

	}

	private void notify(Message message) {
		Object temp = message.getData();
		if (temp == null && !(temp instanceof User)) {
			return;
		}

		User user = (User) temp;
		notifyAuthor(user);
	}

	private void notifyAuthor(User registedUser) {
		notifyByEmail(registedUser);
		// notifyBySms(id);
	}

	private void notifyByEmail(User registedUser) {
		Boolean notify = OptionQuery.me().findValueAsBool("notify_admin_by_email_when_user_registed");
		if (notify != null && notify == true) {

			String toemail = OptionQuery.me().findValue("web_administrator_email");
			if (StringUtils.isBlank(toemail)) {
				return;
			}

			Email email = new Email();
			email.subject("您的网站有人注册了！");

			String content = OptionQuery.me().findValue("notify_admin_by_content_email_when_user_registed");

			if (!StringUtils.isNotBlank(content)) {
				content = "您的网站有人注册了！";
			}
			email.content(content);
			email.to(toemail);

			EmailSenderFactory.createSender().send(email);
		}
	}

}
