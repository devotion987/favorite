package com.devotion.dao.support.value;

import com.devotion.dao.constants.DaoConstants;
import com.devotion.dao.resource.parse.SqlBean;
import com.devotion.dao.transaction.anotation.SQLPartition;
import com.devotion.dao.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 对象解析<br>
 */
public class ValueParser {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(ValueParser.class);

    /**
     * 对象解析器，返回Map类型的解析结果
     *
     * @param entity 解析对象
     * @return 解析结果Map
     */
    public static Map<String, Object> parser(Object entity) {
        Map<String, Object> values = new HashMap<>();
        Method[] methods = entity.getClass().getMethods();
        /** 分表路由解析，返回对应关系 */
        for (Method method : methods) {
            if (method.isAnnotationPresent(Column.class)) {
                Column column = method.getAnnotation(Column.class);
                /** 调用Bean的方法 */
                PropertyDescriptor descriptor = BeanUtils.findPropertyForMethod(method);
                String key = descriptor.getName();
                Object value = null;
                try {
                    /** 方法函数回调 */
                    value = method.invoke(entity, new Object[]{});
                    if (value instanceof Date) {
                        value = dateFormat(column, (Date) value);
                    }
                } catch (Exception e) {
                    logger.debug("reflect error.[" + method + "]", e);
                }
                String table = column.table();
                if (Utils.stringIsNotEmpty(table)) {
                    values.put(DaoConstants.ROUTE_TABLE, table);
                }
                values.put(key, value);
            }
        }

        return values;
    }

    /**
     * 解析SqlBean
     *
     * @param entity 解析对象
     * @param isRead 是否可读
     * @return 解析结果SQL映射
     */
    public static SqlBean parseSqlBean(Object entity, Boolean isRead) {
        if (entity.getClass().isAnnotationPresent(SQLPartition.class)) {
            SQLPartition sqlPartition = entity.getClass().getAnnotation(SQLPartition.class);

            // TODO 还要做进一步的test, 修正dyType指定问题。
            /*
             * return new SqlBean(sqlPartition.id(), isRead == null ?
			 * sqlPartition.isRead() : isRead, null, sqlPartition.dsName());
			 */
            return new SqlBean(sqlPartition.id(), isRead == null ? sqlPartition.isRead() : isRead, null,
                    sqlPartition.dsName(), null);

        }

        // TODO 还要做进一步的test, 修正dyType指定问题。
        // return new SqlBean(null, false, null, null);
        return new SqlBean(null, false, null, null, null);
    }

    /**
     * 日期-时间格式化
     *
     * @param column 列
     * @param date   日期
     * @return 格式化结果
     */
    private static Object dateFormat(Column column, Date date) {
        if (date != null && !"".equals(column.columnDefinition())) {
            SimpleDateFormat format = new SimpleDateFormat(column.columnDefinition());
            return format.format(date);
        }
        return date;
    }

    /**
     * 私有构造函数ValueParser
     */
    private ValueParser() {
    }
}
