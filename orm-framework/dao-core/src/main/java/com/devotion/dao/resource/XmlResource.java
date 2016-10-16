package com.devotion.dao.resource;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.Resource;

import com.devotion.dao.constants.ExceptionType;
import com.devotion.dao.exception.BaseException;
import com.devotion.dao.resource.parse.SqlBean;
import com.devotion.dao.resource.parse.XmlParser;
import com.devotion.dao.route.DataSourceRoute;

/**
 * SQL资源模板解析层
 */
public abstract class XmlResource extends DataSourceRoute {

    /**
     * SQL模板容器
     */
    private Map<String, SqlBean> sqlContainer = new HashMap<>();

    /**
     * SQL资源模板
     */
    private Resource[] resources;

    /**
     * 获取资源模板
     *
     * @return 资源模板
     */
    public synchronized Resource[] getResources() {
        return resources;
    }

    /**
     * 设置资源模板
     *
     * @param resources 资源模板
     */
    public synchronized void setResources(Resource[] resources) {
        this.resources = resources.clone();
    }

    /**
     * 功能描述: 解析SQL模板
     */
    protected void parseResource() {
        XmlParser.getInstance().parse(getResources(), sqlContainer);
    }

    /**
     * 功能描述: 根据SQLID获取SQLBean
     *
     * @param sqlId SQLID
     * @return SQL模板映射
     */
    protected SqlBean getSQL(String sqlId) {
        SqlBean sqlBean = sqlContainer.get(sqlId);
        if (sqlBean == null || sqlBean.getContent() == null || "".equals(sqlBean.getContent())) {
            throw new BaseException(ExceptionType.EXCEPTION_DAO.getCode(), null, null, ExceptionType.EXCEPTION_DAO);
        }
        return sqlBean;
    }
}
