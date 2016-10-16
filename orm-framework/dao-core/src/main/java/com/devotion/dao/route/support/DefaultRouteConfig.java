package com.devotion.dao.route.support;

import com.devotion.dao.constants.DaoConstants;
import com.devotion.dao.route.RouteConfig;
import com.devotion.dao.support.sql.FreeMakerParser;
import com.devotion.dao.utils.DaoUtils;
import com.googlecode.aviator.AviatorEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 分库路由默认实现
 */
public class DefaultRouteConfig implements RouteConfig {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(DefaultRouteConfig.class);

    /**
     * 规则与数据源的映射表
     */
    private Map<String, DataSource> rules;

    /**
     * 分库路由解析方法
     *
     * @param parameter 分库参数
     * @return 分库数据源
     */
    public DataSource route(Object parameter) {
        if (rules != null && parameter != null) {
            /** 解析分库参数转成数组 */
            Object[] params = DaoUtils.convertToObjectArray(parameter);
            /** 遍历分库参数与规则进行匹配 */
            for (Object param : params) {
                logger.debug("route parameter :" + param);
                Map<String, Object> env = new HashMap<>();
                if (param instanceof Map) {
                    env.put(DaoConstants.ROUTE, param);
                    for (Map.Entry<String, DataSource> entry : rules.entrySet()) {
                        String rule = entry.getKey();
                        /** 支持Freemarker定义的规则 */
                        String expression = FreeMakerParser.process(rule, env);
                        logger.debug("parse express [" + expression + "]");
                        /** 支持aviator定义的规则 */
                        if ((Boolean) AviatorEvaluator.execute(expression, env)) {
                            DataSource dataSource = entry.getValue();
                            logger.debug("current route config : " + rule);
                            logger.debug("current route dataSource : " + dataSource);
                            /** 满足规则对象的数据源 */
                            return dataSource;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 设置规则
     *
     * @param rules 数据源路由规则
     */
    public void setRules(Map<String, DataSource> rules) {
        this.rules = rules;
    }
}
