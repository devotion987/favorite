package com.devotion.dao.route.table;

import com.devotion.dao.constants.DaoConstants;
import com.devotion.dao.support.sql.FreeMakerParser;
import com.googlecode.aviator.AviatorEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 动态路由分段表<br>
 */
public class DynamicTableRoute implements TableRoute {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(DynamicTableRoute.class);

    /**
     * 分表规则
     */
    private Map<String, String> expressions;

    /**
     * 分表规则映射
     */
    private Map<String, RouteEntry> tableMap = new HashMap<>();

    /**
     * 分表路由处理
     *
     * @param paramMap 路由参数
     */
    @Override
    public void routeTable(Map<String, Object> paramMap) {
        if (tableMap != null && paramMap != null) {
            /** 获取分表路由关键字 */
            String routeTables = (String) paramMap.get(DaoConstants.ROUTE_TABLE);
            if (routeTables != null) {
                String[] routeTableAry = routeTables.split(",");
                /** 根据规则处理分表逻辑 */
                for (String routeTable : routeTableAry) {
                    Map<String, Object> env = new HashMap<>();
                    env.put(DaoConstants.ROUTE, paramMap);
                    try {
                        RouteEntry routeEntry = tableMap.get(routeTable.toUpperCase());
                        if (routeEntry != null) {
                            /** 解析生成表达式 */
                            String expression = FreeMakerParser.process(routeEntry.routeKey, env);
                            /** Aviator表达式求值 */
                            Object result = AviatorEvaluator.execute(expression, env);
                            /** 处理大小写 */
                            paramMap.put(routeTable.toUpperCase(), String.format(routeEntry.formatKey, result));
                            paramMap.put(routeTable.toLowerCase(), String.format(routeEntry.formatKey, result));
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }
    }

    /**
     * 解析表达式规则
     *
     * @param expressions 表达式
     */
    public void setExpressions(Map<String, String> expressions) {
        this.expressions = expressions;
        parseTableMapping();
    }

    /**
     * 功能描述: <br>
     * 解析分表路由规则
     */
    private void parseTableMapping() {
        /** 逐一读取迭代器内容并解析，结果存入Map */
        expressions.entrySet().forEach((Entry<String, String> entry) -> {
            String value = entry.getValue();
            String keyMapping = value.substring(0, value.indexOf(".")).toUpperCase();
            RouteEntry valueMapping = new RouteEntry(value.substring(value.indexOf(".") + 1)
                    .replaceAll("[\\t\\n\\r]", "").trim(),
                    entry.getKey());
            tableMap.put(keyMapping, valueMapping);
        });
//		Iterator<Entry<String, String>> iterator = expressions.entrySet().iterator();
//		while (iterator.hasNext()) {
//			Entry<String, String> entry = iterator.next();
//			String keyMapping = entry.getValue().substring(0, entry.getValue().indexOf(".")).toUpperCase();
//			RouteEntry valueMapping = new RouteEntry(
//					entry.getValue().substring(entry.getValue().indexOf(".") + 1).replaceAll("[\\t\\n\\r]", "").trim(),
//					entry.getKey());
//			tableMap.put(keyMapping, valueMapping);
//		}
    }

    /**
     * 路由对<br>
     */
    private static class RouteEntry {
        /**
         * 路由键
         */
        private String routeKey;

        /**
         * 规约键
         */
        private String formatKey;

        /**
         * 路由对
         *
         * @param routeKey  路由键
         * @param formatKey 规约键
         */
        private RouteEntry(String routeKey, String formatKey) {
            super();
            this.routeKey = routeKey;
            this.formatKey = formatKey;
        }
    }
}
