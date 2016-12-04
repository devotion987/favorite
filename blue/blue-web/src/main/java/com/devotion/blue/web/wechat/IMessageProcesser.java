package com.devotion.blue.web.wechat;

import com.jfinal.weixin.sdk.msg.in.InMsg;
import com.jfinal.weixin.sdk.msg.out.OutMsg;

public interface IMessageProcesser {

    OutMsg process(InMsg message);

    void onInit(String configInfo);
}