package com.devotion.dao.route.support;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.devotion.dao.route.support.bean.VirtualNodeLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.devotion.dao.utils.DaoUtils;
import com.devotion.dao.constants.DaoConstants;
import com.devotion.dao.route.RouteConfig;
import com.googlecode.aviator.AviatorEvaluator;
import com.devotion.dao.support.sql.FreeMakerParser;

/**
 * 根据用户设定的路由规则进行数据库的路由选择，路由根据一致性hash进行选择
 */
public class ConsistentHashSharding implements RouteConfig, InitializingBean {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(ConsistentHashSharding.class);

    /**
     * 路由规则key
     */
    private String routeParam;

    /**
     * 虚拟节点配置Map
     */
    private Map<String, DataSource> nodeMapping;

    /**
     * 虚拟节点定位
     */
    private VirtualNodeLocator virtualNodeLocator;

    /**
     * 路由链
     */
    private RouteConfig chainRouteConfig;

    /**
     * 数据源路由解析
     *
     * @param parameter 路由参数
     * @return 解析得到的数据源
     */
    @Override
    public DataSource route(Object parameter) {
        if (routeParam != null && parameter != null) {
            /** 将得到的参数值进行转换 */
            Object[] params = DaoUtils.convertToObjectArray(parameter);
            for (Object param : params) {
                try {
                    logger.debug("route parameter :" + param);
                    Map<String, Object> env = new HashMap<>();
                    env.put(DaoConstants.ROUTE, param);
                    /** 通过Freemarker解析器进行解析参数值 */
                    String expression = FreeMakerParser.process(routeParam, env);
                    /** 通过表达式解析出相应的值 */
                    expression = String.valueOf(AviatorEvaluator.execute(expression, env));
                    logger.debug("nodeName is {}", virtualNodeLocator.getPrimary(expression).getNodeName());
                    /** 根据虚拟节点的名称获得对应的数据源 */
                    return virtualNodeLocator.getPrimary(expression).getDataSource();
                } catch (Exception e) {
                    logger.warn("Datasource route exception occurred :{}", e);
                }
            }
        }
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
     * 功能描述: <br>
     * 〈功能详细描述〉 设置路由参数
     *
     * @param routeParam 路由参数
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public void setRouteParam(String routeParam) {
        this.routeParam = routeParam;
    }

    /**
     * 功能描述: <br>
     * 〈功能详细描述〉 设置节点匹配
     *
     * @param nodeMapping 节点匹配
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public void setNodeMapping(Map<String, DataSource> nodeMapping) {
        this.nodeMapping = nodeMapping;
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
     * 配置bean加载时候实例化虚拟节点定位
     *
     * @throws Exception 异常
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        virtualNodeLocator = new VirtualNodeLocator(nodeMapping);
    }

}
