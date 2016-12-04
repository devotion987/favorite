package com.devotion.blue.web.listener;

import com.devotion.blue.message.Actions;
import com.devotion.blue.message.Message;
import com.devotion.blue.message.MessageListener;
import com.devotion.blue.message.annotation.Listener;

@Listener(action = Actions.SETTING_CHANGED)
public class SettingChangedListener implements MessageListener {

	@Override
    public void onMessage(Message message) {
		//do nothing
	}


}