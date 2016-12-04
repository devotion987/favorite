package com.devotion.blue.web.ui.tag;

import com.devotion.blue.model.User;
import com.devotion.blue.model.query.UserQuery;
import com.devotion.blue.web.core.render.freemarker.JTag;

import java.util.List;

public class UsersTag extends JTag {

    public static final String TAG_NAME = "jp.users";

	@Override
	public void onRender() {

		int page = getParamToInt("page", 1);
		int pagesize = getParamToInt("pageSize", 10);
		String gender = getParam("gender");
		String role = getParam("role");
		String status = getParam("status", "normal");

		String orderBy = getParam("orderBy");

		List<User> list = UserQuery.me().findList(page, pagesize, gender, role, status, orderBy);
		if (list == null || list.size() == 0) {
			renderText("");
			return;
		}

		setVariable("users", list);
		renderBody();
	}

}