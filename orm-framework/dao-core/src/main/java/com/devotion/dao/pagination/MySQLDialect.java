package com.devotion.dao.pagination;

/**
 * 功能描述：SQL分页封装，MySQL方言
 */
public class MySQLDialect {

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
		return new StringBuffer(sql.length() + 50).append(sql).append(" ORDER BY RAND() ").append(" limit ")
				.append(limit).toString();
	}

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
	public String getLimitString(String sql, int offset, int limit) {
		return new StringBuffer(sql.length() + 20).append(sql).append(" limit ").append(offset).append(",")
				.append(limit).toString();
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
