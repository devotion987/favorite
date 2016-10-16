package com.devotion.dao.dialect;

/**
 * 数据库方言操作虚类<br>
 * 〈功能详细描述〉
 *
 * @author www.ibm.com
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public abstract class AbstractDialect implements Dialect {

    /**
     * 生成SQL串，查询总记录数
     *
     * @param sql --源SQL
     * @return SQL串
     */
    @Override
    public String getCountString(String sql) {
        return new StringBuffer(sql.length() + 20).append("select count(1) from( ").append(sql).append(" ) t")
                .toString();
    }
}
