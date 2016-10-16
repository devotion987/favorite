package com.devotion.dao.dialect;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.devotion.dao.constants.ExceptionType;
import com.devotion.dao.exception.BaseException;

/**
 * 数据库方言工厂类<br>
 * 〈功能详细描述〉
 *
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class DialectFactory {

    /**
     * 方言Map
     */
    private Map<String, Dialect> mapDialect;

    /**
     * 获取方言Map
     *
     * @return 方言Map
     */
    public Map<String, Dialect> getMapDialect() {
        return mapDialect;
    }

    /**
     * 设置方言Map
     *
     * @param mapDialect 方言Map
     */
    public void setMapDialect(Map<String, Dialect> mapDialect) {
        this.mapDialect = mapDialect;
    }

    /**
     * 获取方言数据库类型
     *
     * @param dbType 数据源类型
     * @return 数据库方言对象
     */
    public Dialect getDBDialect(String dbType) {
        if (StringUtils.isBlank(dbType)) {
            throw new BaseException(ExceptionType.EXCEPTION_DAO.getCode(), null,
                    new Object[]{"DBType is null or empty."}, ExceptionType.EXCEPTION_DAO);
        }
        return mapDialect.get(dbType.toLowerCase());
    }
}
