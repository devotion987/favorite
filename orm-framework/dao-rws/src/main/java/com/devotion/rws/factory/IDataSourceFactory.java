package com.devotion.rws.factory;

import com.devotion.rws.schema.config.DsConfig;

/**
 * 功能描述：读写分离 数据源工厂接口
 */
public interface IDataSourceFactory {

    /**
     * 获取读库数据源
     *
     * @return DataSource 读数据源
     */
    DsConfig getWrDataSource();

    /**
     * 获取写库数据源
     *
     * @param id datasource的id，如果为null或者""则由选择器选择
     * @return DataSource 写数据源
     * @throws IllegalAccessException 数据库非法访问异常
     */
    DsConfig getRoDataSource(String id) throws IllegalAccessException;

    /**
     * 销毁心跳线程
     */
    void destroy();
}
