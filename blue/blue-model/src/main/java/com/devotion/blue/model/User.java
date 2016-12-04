package com.devotion.blue.model;

import com.devotion.blue.model.base.BaseUser;
import com.devotion.blue.model.core.Table;

import java.util.Date;

@Table(tableName = "user", primaryKey = "id")
public class User extends BaseUser<User> {

	public static final String ROLE_ADMINISTRATOR = "administrator";
	public static final String STATUS_NORMAL = "normal";
	public static final String STATUS_FROZEN = "frozen";

	public boolean isAdministrator() {
		return ROLE_ADMINISTRATOR.equals(getRole());
	}

	public boolean isFrozen() {
		return STATUS_FROZEN.equals(getStatus());
	}

	@Override
	public boolean save() {
		if (getCreated() == null) {
			setCreated(new Date());
		}
		return super.save();
	}

	@Override
	public boolean update() {
		removeCache(getId());
		removeCache(getMobile());
		removeCache(getUsername());
		removeCache(getEmail());
		return super.update();
	}

	@Override
	public boolean delete() {
		removeCache(getId());
		removeCache(getMobile());
		removeCache(getUsername());
		removeCache(getEmail());
		return super.delete();
	}

	public String getUrl() {
		return "/user/" + getId();
	}

}
