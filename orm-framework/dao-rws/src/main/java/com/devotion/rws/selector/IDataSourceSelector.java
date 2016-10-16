package com.devotion.rws.selector;

import java.util.List;

import com.devotion.rws.schema.config.DsConfig;

/**
 * 数据源选择 接口类
 */
public interface IDataSourceSelector {

    /**
     * 从数据源列表中选择出一个合适的数据源
     *
     * @param dsConfigs 数据源配置对象
     * @return DsConfigs 数据源对象
     */
    DsConfig select(List<DsConfig> dsConfigs);
}
