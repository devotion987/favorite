package com.devotion.blue.model.query;

import com.devotion.blue.model.Metadata;

import java.math.BigInteger;
import java.util.List;


public class MetaDataQuery extends JBaseQuery {

	protected static final Metadata DAO = new Metadata();
	private static final MetaDataQuery QUERY = new MetaDataQuery();

	public static MetaDataQuery me() {
		return QUERY;
	}

	public List<Metadata> findListByTypeAndId(String type, BigInteger id) {
		return DAO.doFind("object_type = ? and object_id = ?", type, id);
	}

	public Metadata findFirstByTypeAndValue(String type, String key, Object value) {

		return DAO.doFindFirst("object_type = ? and meta_key = ? and meta_value = ?", type, key, value);

	}

	public List<Metadata> findListByTypeAndValue(String type, String key, Object value) {

		return DAO.doFind("object_type = ? and meta_key = ? and meta_value = ?", type, key, value);

	}

	public Metadata findByTypeAndIdAndKey(String type, BigInteger id, String key) {
		String cachekey = type + id + key;
		return DAO.doFindFirstByCache(Metadata.CACHE_NAME, cachekey,
				"object_type = ? and object_id = ? and meta_key = ? ", type, id, key);

	}
}
