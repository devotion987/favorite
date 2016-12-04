package com.devotion.blue.web.wechat.processor;

import com.devotion.blue.model.Content;
import com.devotion.blue.model.query.ContentQuery;
import com.devotion.blue.model.query.OptionQuery;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.wechat.IMessageProcesser;
import com.devotion.blue.web.wechat.MessageProcesser;
import com.jfinal.weixin.sdk.msg.in.InMsg;
import com.jfinal.weixin.sdk.msg.out.News;
import com.jfinal.weixin.sdk.msg.out.OutMsg;
import com.jfinal.weixin.sdk.msg.out.OutNewsMsg;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@MessageProcesser(key = "contents")
public class ContentsProcesser implements IMessageProcesser {

	List<BigInteger> contentIds;

	@Override
    public void onInit(String configInfo) {
		if (StringUtils.isBlank(configInfo)) {
			return;
		}

		contentIds = new ArrayList<>();

		String[] ids = configInfo.split(",");
		for (String id : ids) {
			try {
				contentIds.add(new BigInteger(id.trim()));
			} catch (Exception e) {
			}
		}
	}

	@Override
	public OutMsg process(InMsg message) {

		String domain = OptionQuery.me().findValue("web_domain");
		if (StringUtils.isBlank(domain)) {
			OutTextMsg otm = new OutTextMsg(message);
			otm.setContent("您还没有配置您的域名，请先在后台的【设置】>【常规】里配置您的网站域名！");
			return otm;
		}

		if (contentIds == null || contentIds.isEmpty()) {
			OutTextMsg otm = new OutTextMsg(message);
			otm.setContent("配置错误，请添加正确的内容ID。");
			return otm;
		}

		List<Content> contents = new ArrayList<Content>();
		if (contentIds != null && contentIds.size() > 0) {
			for (BigInteger id : contentIds) {
				contents.add(ContentQuery.me().findById(id));
			}
		}

		if (contents.isEmpty()) {
			OutTextMsg otm = new OutTextMsg(message);
			otm.setContent("配置错误，暂未找到相应内容！请联系管理员");
			return otm;
		}

		OutNewsMsg out = new OutNewsMsg(message);
		News news;
		for (Content content : contents) {
			news = new News();
			news.setTitle(content.getTitle());
			news.setDescription(content.getSummary());
			news.setPicUrl(domain + content.getImage());
			news.setUrl(domain + content.getUrl());
			out.addNews(news);
		}
		return out;
	}

}