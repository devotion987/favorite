package com.devotion.dao.route.support;

import com.devotion.dao.constants.DaoConstants;
import com.devotion.dao.constants.ExceptionType;
import com.devotion.dao.exception.BaseException;
import com.devotion.dao.route.RouteConfig;
import com.devotion.dao.route.support.bean.Partition;
import com.devotion.dao.route.support.bean.Shard;
import com.devotion.dao.support.sql.FreeMakerParser;
import com.devotion.dao.utils.DaoUtils;
import com.googlecode.aviator.AviatorEvaluator;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 增量区间加数据散列路由<br>
 */
public class DataIncrementSharding implements RouteConfig, InitializingBean, BeanFactoryAware {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(DataIncrementSharding.class);
    /**
     * XML解析器
     */
    private static XStream xstream = new XStream(new StaxDriver());

    static {
        /** 注解模式 */
        xstream.autodetectAnnotations(true);
        /** 注册解析映射对象 */
        xstream.alias("partition", Partition.class);
    }

    /**
     * 路由映射配置XML
     */
    private Map<String, Resource> routeMapping = new HashMap<>();

    /**
     * 路由映射配置Obj
     */
    private Map<String, Partition> partitionRoute = new HashMap<>();

    /**
     * 路由链
     */
    private RouteConfig chainRouteConfig;

    /**
     * Spring 容器
     */
    private BeanFactory beanFactory;

    /**
     * 数据源分库路由处理方法
     *
     * @param parameter 分库参数
     * @return 数据源
     */
    public DataSource route(Object parameter) {
        if (routeMapping != null && !routeMapping.isEmpty() && parameter != null) {
            /** 解析分库参数转成数组 */
            Object[] params = DaoUtils.convertToObjectArray(parameter);
            /** 遍历分库参数与规则进行匹配 */
            for (Object param : params) {
                logger.debug("route parameter :" + param);
                Map<String, Object> env = new HashMap<>();
                env.put(DaoConstants.ROUTE, param);
//                partitionRoute.entrySet().forEach((Entry<String, Partition> entry) -> {
//                    try {
//                        String rule = entry.getKey();
//                        /** 支持Freemarker定义的规则 */
//                        String expression = FreeMakerParser.process(rule, env);
//                        if (expression != null) {
//                            logger.debug("parse express [" + expression + "]");
//                            /** 支持aviator定义的规则 */
//                            Long paramValue = (Long) AviatorEvaluator.execute(expression, env);
//                            Shard shard = entry.getValue().find(paramValue);
//                            if (shard != null) {
//                                /** 满足规则对象的数据源 */
//                                DataSource dataSource = (DataSource) beanFactory.getBean(shard.getDataSourceRef());
//                                logger.debug("route datasource {}", dataSource);
//                                return dataSource;
//                            }
//                        }
//                    } catch (Exception e) {
//                        logger.warn("Datasource route exception occurred :{}", e.getMessage());
//                    }
//                });
                for (Entry<String, Partition> entry : partitionRoute.entrySet()) {
                    try {
                        String rule = entry.getKey();
                        /** 支持Freemarker定义的规则 */
                        String expression = FreeMakerParser.process(rule, env);
                        if (expression != null) {
                            logger.debug("parse express [" + expression + "]");
                            /** 支持aviator定义的规则 */
                            Long paramValue = (Long) AviatorEvaluator.execute(expression, env);
                            Shard shard = entry.getValue().find(paramValue);
                            if (shard != null) {
                                /** 满足规则对象的数据源 */
                                DataSource dataSource = (DataSource) beanFactory.getBean(shard.getDataSourceRef());
                                logger.debug("route datasource {}", dataSource);
                                return dataSource;
                            }
                        }
                    } catch (Exception e) {
                        logger.warn("Datasource route exception occurred :{}", e.getMessage());
                    }
                }
            }
        }
        /** 如果没有路由到，则往下传递路由 */
        return routeChain(chainRouteConfig, parameter);
    }

    /**
     * 功能描述: <br>
     * 路由链传递
     *
     * @param chainRouteConfig 传递的路由
     * @param parameter        路由参
     * @return 路由数据源
     */
    private DataSource routeChain(RouteConfig chainRouteConfig, Object parameter) {
        if (chainRouteConfig != null) {
            return chainRouteConfig.route(parameter);
        }
        return null;
    }

    /**
     * 初始化解析
     *
     * @throws Exception 异常
     */
    public void afterPropertiesSet() throws Exception {
        if (routeMapping != null) {
            routeMapping.entrySet().forEach((Entry<String, Resource> entry) ->
                    partitionRoute.put(entry.getKey(), parseRouteConfig(entry.getValue())));
//            Iterator<Entry<String, Resource>> iterator = routeMapping.entrySet().iterator();
//            while (iterator.hasNext()) {
//                Entry<String, Resource> entry = iterator.next();
//                partitionRoute.put(entry.getKey(), parseRouteConfig(entry.getValue()));
//            }
        }
    }

    /**
     * 功能描述: <br>
     * 解析路由规则XML
     *
     * @param resource 文件资源
     * @return 分区对象
     */
    private Partition parseRouteConfig(Resource resource) {
        try {
            return (Partition) xstream.fromXML(resource.getInputStream());
        } catch (IOException e) {
            logger.debug("XStream parse {} exception occurred.", resource.getFilename());
            throw new BaseException(ExceptionType.EXCEPTION_DAO.getCode(), e, null, ExceptionType.EXCEPTION_DAO);
        }
    }

    /**
     * 功能描述: <br>
     * 〈功能详细描述〉 设置路由链配置
     *
     * @param chainRouteConfig 路由链配置
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public void setChainRouteConfig(RouteConfig chainRouteConfig) {
        this.chainRouteConfig = chainRouteConfig;
    }

    /**
     * 功能描述: <br>
     * 〈功能详细描述〉 设置路由匹配
     *
     * @param routeMapping 路由匹配
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public void setRouteMapping(Map<String, Resource> routeMapping) {
        this.routeMapping = routeMapping;
    }

    /**
     * 设置bean factory
     *
     * @param beanFactory bean工厂
     */
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
