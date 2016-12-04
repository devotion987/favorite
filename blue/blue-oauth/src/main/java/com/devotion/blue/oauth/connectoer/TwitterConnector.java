package com.devotion.blue.oauth.connectoer;

import com.devotion.blue.oauth.OauthConnector;
import com.devotion.blue.oauth.OauthUser;

public class TwitterConnector extends OauthConnector {

	public TwitterConnector(String name, String appkey, String appSecret) {
		super(name, appkey, appSecret);
	}

	@Override
	public String createAuthorizeUrl(String state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected OauthUser getOauthUser(String code) {
		// TODO Auto-generated method stub
		return null;
	}
}
