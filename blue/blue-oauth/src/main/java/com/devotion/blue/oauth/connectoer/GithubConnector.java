package com.devotion.blue.oauth.connectoer;

import com.alibaba.fastjson.JSONObject;
import com.devotion.blue.oauth.OauthConnector;
import com.devotion.blue.oauth.OauthUser;

public class GithubConnector extends OauthConnector {

    public GithubConnector(String name, String appkey, String appSecret) {
        super(name, appkey, appSecret);
    }

    @Override
    public String createAuthorizeUrl(String state) {
        StringBuilder sb = new StringBuilder("https://github.com/login/oauth/authorize?");
        sb.append("scope=user");
        sb.append("&client_id=" + getClientId());
        sb.append("&redirect_uri=" + getRedirectUri());
        sb.append("&state=" + state);

        return sb.toString();
    }

    @Override
    protected OauthUser getOauthUser(String code) {
        String accessToken = getAccessToken(code);

        String url = "https://api.github.com/user?access_token=" + accessToken;

        String httpString = httpGet(url);
        JSONObject json = JSONObject.parseObject(httpString);

        OauthUser user = new OauthUser();
        user.setAvatar(json.getString("avatar_url"));
        user.setOpenId(json.getString("id"));
        user.setNickname(json.getString("login"));
        user.setSource(getName());

        return null;
    }

    protected String getAccessToken(String code) {

        StringBuilder urlBuilder = new StringBuilder("https://github.com/login/oauth/access_token?");
        urlBuilder.append("client_id=" + getClientId());
        urlBuilder.append("&client_secret=" + getClientSecret());
        urlBuilder.append("&code=" + code);

        String url = urlBuilder.toString();

        String httpString = httpGet(url);
        JSONObject json = JSONObject.parseObject(httpString);
        return json.getString("access_token");
    }
}
