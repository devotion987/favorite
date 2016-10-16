package com.devotion.dao.dialect.impl;

import com.devotion.dao.dialect.AbstractDialect;

/**
 * 功能描述：SQL分页封装， MySQl方言
 */
public class MySQLDialect extends AbstractDialect {

    /**
     * 封装SQL，查询前几条记录
     *
     * @param sql --源SQL
     * @return SQL串
     */
    public String getLimitStringForRandom(String sql) {
        return new StringBuffer(sql.length() + 50).append(sql).append(" ORDER BY RAND() ").append(" limit :_limit")
                .toString();
    }

    /**
     * 封装SQL，查询从什么位置开始、指定行记录
     *
     * @param sql --源SQL
     * @return SQL串
     */
    public String getLimitString(String sql) {
        return new StringBuffer(sql.length() + 20).append(sql).append(" limit :_offset, :_limit").toString();
    }
}
