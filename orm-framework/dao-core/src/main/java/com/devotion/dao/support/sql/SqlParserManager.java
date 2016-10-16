package com.devotion.dao.support.sql;

import java.util.HashMap;
import java.util.Map;

/**
 * SQL解析器缓存
 */
public class SqlParserManager {

    /**
     * 解析器缓存
     */
    private static final Map<Class<?>, SqlParser> cache = new HashMap<>();

    /**
     * SQL解析器装载进缓存
     *
     * @param clazz 类对象
     * @return SQL生成对象
     */
    public static SqlParser getSqlParser(Class<?> clazz) {
        SqlParser sqlParser = cache.get(clazz);
        if (sqlParser == null) {
            sqlParser = new SqlParser(clazz);
            synchronized (cache) {
                if (cache.get(clazz) == null) {
                    cache.put(clazz, sqlParser);
                }
            }
        }
        return sqlParser;
    }

    /**
     * 获取分表路由
     *
     * @param clazz 类对象
     * @return 分表路由
     */
    public static String getRouteTable(Class<?> clazz) {
        return getSqlParser(clazz).getRouteTable();
    }

    /**
     * 私有构造函数SqlParserManager
     */
    private SqlParserManager() {
    }
}
