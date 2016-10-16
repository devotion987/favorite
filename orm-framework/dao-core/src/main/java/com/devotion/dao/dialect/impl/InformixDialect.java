package com.devotion.dao.dialect.impl;

import com.devotion.dao.dialect.AbstractDialect;

/**
 * 功能描述： SQL分页封装, Informix方言
 */
public class InformixDialect extends AbstractDialect {

    /**
     * 封装SQL，查询前几条记录
     *
     * @param sql --源SQL
     * @return SQL串
     */
    public String getLimitStringForRandom(String sql) {
        StringBuilder pagingSelect = new StringBuilder(sql.length() + 150).append("select first :_limit * from (")
                .append(sql).append(" ) "); // order by rand
        return pagingSelect.toString();
    }

    /**
     * 封装SQL，查询从什么位置开始、指定行记录
     *
     * @param sql --源SQL
     * @return SQL串 .append(" order by rownumber_")
     */
    public String getLimitString(String sql) {
        StringBuilder pagingSelect = new StringBuilder(sql.length() + 100).append("select first :_limit * from (")
                .append(sql).append(")");
        return pagingSelect.toString();
    }
}
