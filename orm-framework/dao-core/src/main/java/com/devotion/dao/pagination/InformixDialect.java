package com.devotion.dao.pagination;

/**
 * 功能描述： SQL分页封装, DB2方言
 */
public class InformixDialect {
	/**
	 * 生成sql串，随机获取指定数目的记录
	 * 
	 * @param sql
	 *            sql串
	 * @param limit
	 *            指定记录数目
	 * @return 字符串类型的组合结果
	 */
	public String getLimitStringForRandom(String sql, int limit) {

		StringBuilder pagingSelect = new StringBuilder(sql.length() + 150).append("select first ").append(limit)
				.append(" * from (").append(sql).append(" ) "); // order by
																// rand()
		return pagingSelect.toString();
	}

	// StringBuilder pagingSelect = new StringBuilder(sql.length() + 250)
	// .append("select * from ( select inner2_.*, rownumber() over(order by
	// order of inner2_) as rownumber_ from ( ")
	// .append(sql).append(" order by rand() ").append(" fetch first
	// ").append(limit)
	// .append(" rows only ) as inner2_ ) as inner1_").append(" order by
	// rownumber_");
	// return pagingSelect.toString();
	// }

	/**
	 * 生成sql串，获取指定数目的记录
	 * 
	 * @param sql
	 *            sql串
	 * @param offset
	 *            偏移量
	 * @param limit
	 *            指定记录数目
	 * @return 字符串类型的组合结果
	 */
	// public String getLimitString(String sql, int offset, int limit) {
	// if (offset == 0) {
	// return sql + " fetch first " + limit + " rows only";
	// }
	// StringBuilder pagingSelect = new StringBuilder(sql.length() + 200)
	// .append("select * from ( select inner2_.*, rownumber() over(order by
	// order of inner2_) as rownumber_ from ( ")
	// .append(sql).append(" fetch first ").append(limit)
	// .append(" rows only ) as inner2_ ) as inner1_ where rownumber_ >
	// ").append(offset)
	// .append(" order by rownumber_");
	// return pagingSelect.toString();
	// }

	public String getLimitString(String sql, int offset, int limit) {

		StringBuilder pagingSelect = new StringBuilder(sql.length() + 100).append("select skip ").append(offset)
				.append(" first ").append(limit).append(" * from (").append(sql).append(")");
		return pagingSelect.toString();

	}

	/**
	 * 生成sql串，获取统计结果
	 * 
	 * @param sql
	 *            sql串
	 * @return 字符串类型的组合结果
	 */
	public String getCountString(String sql) {
		return new StringBuffer(sql.length() + 20).append("select count(1) from( ").append(sql).append(" )").toString();
	}
}
