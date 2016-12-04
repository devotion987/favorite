package com.devotion.blue.model;

import com.devotion.blue.model.base.BaseMetadata;
import com.devotion.blue.model.core.Table;

@Table(tableName = "metadata", primaryKey = "id")
public class Metadata extends BaseMetadata<Metadata> {

	@Override
	public boolean update() {
		removeCache(getId());
		removeCache(getObjectType() + getObjectId() + getMetaKey());
		return super.update();
	}

	@Override
	public boolean delete() {
		removeCache(getId());
		removeCache(getObjectType() + getObjectId() + getMetaKey());
		return super.delete();
	}

}
