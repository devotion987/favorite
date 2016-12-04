package com.devotion.blue.web.wechat.processor;

import com.devotion.blue.web.wechat.IMessageProcesser;
import com.devotion.blue.web.wechat.MessageProcesser;
import com.jfinal.weixin.sdk.msg.in.InMsg;
import com.jfinal.weixin.sdk.msg.out.OutMsg;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;

@MessageProcesser(key = "hello")
public class HelloProcesser implements IMessageProcesser {

	@Override
    public void onInit(String configInfo) {
		
		
	}
	
	@Override
	public OutMsg process(InMsg message) {
		OutTextMsg out = new OutTextMsg(message);
		out.setContent("hello...欢迎使用JPress高级回复功能。");
		return out;
	}

	

}