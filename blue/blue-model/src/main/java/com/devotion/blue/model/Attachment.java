package com.devotion.blue.model;

import com.devotion.blue.model.base.BaseAttachment;
import com.devotion.blue.model.core.Table;
import com.devotion.blue.model.query.UserQuery;
import com.devotion.blue.utils.AttachmentUtils;

@Table(tableName = "attachment", primaryKey = "id")
public class Attachment extends BaseAttachment<Attachment> {

	private User user;

	public boolean isImage() {
		return AttachmentUtils.isImage(getPath());
	}

	public User getUser() {
		if (user != null)
			return user;

		if (getUserId() == null)
			return null;

		user = UserQuery.me().findById(getUserId());
		return user;
	}
}
