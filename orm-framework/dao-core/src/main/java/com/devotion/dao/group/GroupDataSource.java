package com.devotion.dao.group;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.devotion.dao.resource.parse.SqlBean;
import com.devotion.rws.factory.support.DataSourceFactory;
import com.devotion.rws.schema.config.DsConfig;
import com.devotion.rws.schema.config.DsGroupConfig;

/**
 * 分组数据源，当分库路由结束之后，会路由出一个数据源，然后判断该数据是否GroupDataSource， 如果是说明基于读写分离数据架构
 */
public class GroupDataSource extends DataSourceAware implements IGroupDataSource, InitializingBean, DisposableBean {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(GroupDataSource.class);

    /**
     * 分组数据源配置
     */
    private DsGroupConfig dataSourceGroup;

    /**
     * 数据源心跳
     */
    private int period;

    /**
     * 数据源工厂
     */
    private DataSourceFactory dataSourceFactory;

    /**
     * 获取数据源
     *
     * @param sqlBean SQL映射
     * @return 数据源
     */
    @SuppressWarnings("unused")
	@Override
    public DataSource getDataSource(SqlBean sqlBean) {
        DsConfig resultDataSource = null;
        if (sqlBean != null && sqlBean.isRead()) {
            try {
                /** 获取读数据源 */
                resultDataSource = dataSourceFactory.getRoDataSource(sqlBean.getDsName());
                logger.info("Fetching R-DataSource [{}:{}]", resultDataSource.getName(),
                        resultDataSource.getRefDataSource());
                /** 读库列表没有可用库时，在写库上进行操作 */
                if (resultDataSource == null) { //
                    resultDataSource = dataSourceFactory.getWrDataSource();
                    logger.info("R-DataSource is null ,current datasource convert to RW-DataSource [{}:{}]",
                            resultDataSource.getName(), resultDataSource);
                }
            } catch (IllegalAccessException e) {
                logger.warn("Can't get an available Reading DataSource.", e);
            }
        } else {
            /** 获取写数据源 */
            resultDataSource = dataSourceFactory.getWrDataSource();
            logger.info("Fetching WR-DataSource [{}:{}]", resultDataSource.getName(), resultDataSource);
        }
        if (resultDataSource != null) {
            if (sqlBean != null) {
                /** 设置数据源类型 */
                sqlBean.setDbType(resultDataSource.getType());
                logger.info("DBType: {}", sqlBean.getDbType());
            }
            return resultDataSource.getRefDataSource();
        }
        return null;
    }

    /**
     * 设置数据源组
     *
     * @param dataSourceGroup 数据源组
     */
    public void setDataSourceGroup(DsGroupConfig dataSourceGroup) {
        this.dataSourceGroup = dataSourceGroup;

    }

    /**
     * 设置数据源心跳
     *
     * @param period 数据源心跳
     */
    public void setPeriod(int period) {
        this.period = period;
    }

    /**
     * 初始化数据源工厂
     */
    private void initDataSourceFactory() {
        if (dataSourceFactory == null && dataSourceGroup != null) {
            dataSourceFactory = new DataSourceFactory(dataSourceGroup, period);
        }
    }

    /**
     * 初始化方法
     *
     * @throws Exception 异常
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        initDataSourceFactory();
    }

    /**
     * 销毁数据源工厂
     *
     * @throws Exception 异常
     */
    @Override
    public void destroy() throws Exception {
        dataSourceFactory.destroy();
    }

}
