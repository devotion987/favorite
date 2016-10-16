/*
 * Copyright (C), 2015, IBM GBS China.
 * FileName: Dialect.java
 * Author:   www.ibm.com
 * Date:     
 * Description: //模块目的、功能描述      
 * History:     //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.devotion.dao.dialect;

/**
 * 各数据库方言操作处理<br>
 * 包括数据库分页处理等
 *
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface Dialect {

    /**
     * 功能描述: <br>
     * 获取分页SQL，查询前几条记录
     *
     * @param sql --源SQL
     * @return SQL串
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    String getLimitStringForRandom(String sql);

    /**
     * 功能描述: <br>
     * 获取分页SQL，查询从什么位置开始、指定行记录
     *
     * @param sql --源SQL
     * @return SQL串
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    String getLimitString(String sql);

    /**
     * 功能描述: <br>
     * 获取分页SQL,查询指定SQL的总记录数。
     *
     * @param sql --源SQL
     * @return SQL串
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    String getCountString(String sql);
}
