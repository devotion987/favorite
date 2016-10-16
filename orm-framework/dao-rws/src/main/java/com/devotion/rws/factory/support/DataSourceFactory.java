package com.devotion.rws.factory.support;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.devotion.rws.ConnectionChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devotion.rws.CollectionUtils;
import com.devotion.rws.NamedThreadFactory;
import com.devotion.rws.URFConstant;
import com.devotion.rws.exception.RWSException;
import com.devotion.rws.factory.IDataSourceFactory;
import com.devotion.rws.schema.config.DsConfig;
import com.devotion.rws.schema.config.DsGroupConfig;
import com.devotion.rws.selector.IDataSourceSelector;
import com.devotion.rws.selector.support.DefaultDataSourceSelector;

/**
 * 功能描述：读写分离 数据源工厂
 *
 * @version 1.0.0
 */
public class DataSourceFactory implements IDataSourceFactory {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(DataSourceFactory.class);

    /**
     * 数据源组
     */
    private DsGroupConfig dataSourceGroup;

    /**
     * 写库数据源
     */
    private DsConfig wrDataSource;

    /**
     * 读库数据源
     */
    private final List<DsConfig> roDataSources = Collections.synchronizedList(new ArrayList<>());

    /**
     * 暂时不可用的数据源集合
     */
    private final List<DsConfig> failedRoDataSources = Collections.synchronizedList(new ArrayList<>());

    /**
     * 默认的数据源选择器（根据权重随机选择）
     */
    private IDataSourceSelector dataSourceSelector = new DefaultDataSourceSelector();

    /**
     * 定时任务执行器
     */
    private final ScheduledExecutorService beatExecutor = Executors.newScheduledThreadPool(1,
            new NamedThreadFactory("UrfDataSourceHeartBeatTimer", true));

    /**
     * 心跳定时器，定时检查数据源是否正常
     */
    private ScheduledFuture<?> beatFuture;

    /**
     * 心跳频率
     */
    private int period;

    /**
     * 构造方法
     *
     * @param dataSourceGroup 数据源组
     */
    public DataSourceFactory(DsGroupConfig dataSourceGroup) {
        this(dataSourceGroup, 0);
    }

    /**
     * 构造方法重载
     *
     * @param dataSourceGroup 数据源组
     * @param period          心跳
     */
    public DataSourceFactory(DsGroupConfig dataSourceGroup, int period) {
        this.dataSourceGroup = dataSourceGroup;
        this.period = period;
        try {
            init();
        } catch (IllegalAccessException e) {
            throw new RWSException(e);
        }
    }

    /**
     * 异常登录检测
     *
     * @throws IllegalAccessException 数据库非法访问异常
     */
    public void init() throws IllegalAccessException {
        if (dataSourceGroup == null) {
            throw new IllegalAccessException("DataSourceFactory property dataSourceGroup is null !");
        }
        this.wrDataSource = dataSourceGroup.getWrDsConfig();
        Collection<DsConfig> roDsConfigs = dataSourceGroup.getRoDsConfigs().values();
        roDsConfigs.forEach((DsConfig roDsConfig) -> roDataSources.add(roDsConfig));
//        for (DsConfig dsc :dataSourceGroup.getRoDsConfigs().values()) {
//            roDataSources.add(dsc);
//        }
        if (wrDataSource == null) {
            throw new IllegalAccessException("DataSourceFactory property wrDataSource is null !");
        }
        if (roDataSources.size() <= 0) {
            throw new IllegalAccessException("DataSourceFactory property roDataSources is empty !");
        }
        /** 启动心跳线程 */
        int beatPeriod = period == 0 ? URFConstant.HEART_BEAT_PERIOD : period * 1000;
        beatFuture = beatExecutor.scheduleWithFixedDelay(() -> {
            try {
                doHeartBeat();
            } catch (IllegalAccessException t) {
                /** 防御性容错 */
                logger.error("Unexpected error occur at failed heartbeat, cause: " + t.getMessage(), t);
            }
        }, beatPeriod, beatPeriod, TimeUnit.MILLISECONDS);
//        this.beatFuture = beatExecutor.scheduleWithFixedDelay(new Runnable() {
//            public void run() {
//                /** 检测并连接注册中心 */
//                try {
//                    doHeartBeat();
//                } catch (IllegalAccessException t) {
//                    /** 防御性容错 */
//                    logger.error("Unexpected error occur at failed heartbeat, cause: " + t.getMessage(), t);
//                }
//            }
//        }, beatPeriod, beatPeriod, TimeUnit.MILLISECONDS);

    }

    /**
     * 数据源心跳
     *
     * @throws IllegalAccessException 数据库非法访问异常
     */
    private void doHeartBeat() throws IllegalAccessException {
        logger.debug("[{}] HeartBeat is working...!", this);
        List<Integer> failDsIndexes = new ArrayList<>();
        List<Integer> okDsIndexes = new ArrayList<>();
        for (int i = 0; i < this.roDataSources.size(); i++) {
            DsConfig roDataSource = roDataSources.get(i);
            if (!checkDataSource(roDataSource)) {
                failDsIndexes.add(i);
                logger.debug("RoDataSource {} connected failed, remove to failedRoDataSources!",
                        roDataSource.getName());
            }
        }
        for (int i = 0; i < this.failedRoDataSources.size(); i++) {
            DsConfig failRoDataSource = failedRoDataSources.get(i);
            if (checkDataSource(failRoDataSource)) {
                okDsIndexes.add(i);
                logger.debug("failed RoDataSource {} reConnected succeed, remove to roDataSources!",
                        failRoDataSource.getName());
            } else {
                logger.debug("RoDataSource {} retry connect failed!", failRoDataSource.getName());
            }
        }
        /** 这里需要从大到小删除,否则删除会越界 */
        CollectionUtils.sortDesc(failDsIndexes).forEach((Integer index) ->
                failedRoDataSources.add(roDataSources.remove(index.intValue())));
//        for (int index : CollectionUtils.sortDesc(failDsIndexes)) {
//            this.failedRoDataSources.add(this.roDataSources.remove(index));
//        }
        /** 这里需要从大到小删除,否则删除会越界 */
        CollectionUtils.sortDesc(okDsIndexes).forEach((Integer index) ->
                roDataSources.add(failedRoDataSources.remove(index.intValue())));
//        for (int index : CollectionUtils.sortDesc(okDsIndexes)) {
//            this.roDataSources.add(this.failedRoDataSources.remove(index));
//        }
    }

    /**
     * 检查数据源连接的有效性
     *
     * @param dsConfig 读写分离数据源配置
     * @return 检查结果
     * @throws IllegalAccessException 数据库非法访问异常
     */
    private boolean checkDataSource(DsConfig dsConfig) throws IllegalAccessException {

        boolean resultOk = true;
        Connection c = null;
        try {
            /** 获取连接 */
            c = dsConfig.getRefDataSource().getConnection();
        } catch (SQLException e) {
            logger.error("DsConfig" + dsConfig.getName() + "getConnection failed!", e);
            resultOk = false;
        }
        /**
         * if (ConnectionChecker.isValidConnection(c, dsConfig.getType()) !=
         * null) { resultOk = false; }
         */
        ConnectionChecker.closeConnection(c);
        return resultOk;
    }

    /**
     * 获取读数据源
     *
     * @param id 数据源ID
     * @return 读数据源
     * @throws IllegalAccessException 数据库非法访问异常
     */
    @Override
    public DsConfig getRoDataSource(String id) throws IllegalAccessException {
        if (id == null || "".equals(id)) {
            return dataSourceSelector.select(roDataSources);
        }
        DsConfig dsConfig = dataSourceGroup.getRoDsConfigs().get(id);
        if (dsConfig == null) {
            throw new IllegalAccessException("DataSource:" + id + " is not exist!");
        }
        return dsConfig;
    }

    /**
     * 获取写数据源
     *
     * @return 写数据源
     */
    @Override
    public DsConfig getWrDataSource() {
        return wrDataSource;
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {
        try {
            if (null != beatFuture) {
                beatFuture.cancel(true);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

}
