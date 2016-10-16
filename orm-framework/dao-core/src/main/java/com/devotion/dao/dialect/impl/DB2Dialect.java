package com.devotion.dao.dialect.impl;

import com.devotion.dao.dialect.AbstractDialect;

/**
 * 功能描述： SQL分页封装, DB2方言
 */
public class DB2Dialect extends AbstractDialect {

    /**
     * 封装SQL，查询前几条记录
     *
     * @param sql --源SQL
     * @return SQL串
     */
    public String getLimitStringForRandom(String sql) {
        StringBuilder pagingSelect = new StringBuilder(sql.length() + 250)
                .append("select * from ( select inner2_.*, rownumber() over(order by order of inner2_) as rownumber_ from ( ")
                .append(sql).append(" order by rand() ").append(" fetch first :_limit")
                .append(" rows only ) as inner2_ ) as inner1_").append(" order by rownumber_");
        return pagingSelect.toString();
    }

    /**
     * 封装SQL，查询从什么位置开始、指定行记录
     *
     * @param sql --源SQL
     * @return SQL串
     */
    public String getLimitString(String sql) {
        StringBuilder pagingSelect = new StringBuilder(sql.length() + 200)
                .append("select * from ( select inner2_.*, rownumber() over(order by order of inner2_) as rownumber_ from ( ")
                .append(sql).append(" fetch first :_limit")
                .append(" rows only ) as inner2_ ) as inner1_ where rownumber_ > :_offset")
                .append(" order by rownumber_");
        return pagingSelect.toString();
    }
}
