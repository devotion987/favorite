package com.devotion.blue.model;

import com.devotion.blue.model.base.BaseOption;
import com.devotion.blue.model.core.Table;

@Table(tableName = "option", primaryKey = "id")
public class Option extends BaseOption<Option> {

	private static final long serialVersionUID = 1L;

	public static final String KEY_WEB_NAME = "web_name";
	public static final String KEY_TEMPLATE_ID = "web_template_id";

	
	@Override
	public boolean update() {
		removeCache(getOptionKey());
		return super.update();
	}
	
	
	@Override
	public boolean save() {
		removeCache(getOptionKey());
		return super.save();
	}

	@Override
	public boolean delete() {
		removeCache(getOptionKey());
		return super.delete();
	}

}
