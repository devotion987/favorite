package com.devotion.blue.web.listener;

import com.devotion.blue.message.Actions;
import com.devotion.blue.message.Message;
import com.devotion.blue.message.MessageListener;
import com.devotion.blue.message.annotation.Listener;
import com.devotion.blue.model.User;

import java.util.Date;

@Listener(action = { Actions.USER_LOGINED, User.ACTION_ADD })
public class UserActionListener implements MessageListener {

	@Override
    public void onMessage(Message message) {
		if (message.getAction().equals(Actions.USER_LOGINED)) {
			User user = message.getData();
			user.setLogged(new Date());
			user.update();
		}

		else if (message.getAction().equals(User.ACTION_ADD)) {
			User user = message.getData();
			user.setLogged(new Date());
			user.update();
		}
	}

}