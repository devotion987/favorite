package com.devotion.dao.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devotion.dao.constants.DaoConstants;
import com.devotion.dao.support.sql.SqlParserManager;

/**
 * 功能描述：工具类
 */
public class DaoUtils {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(DaoUtils.class);

    /**
     * 把参数转成对象数组
     *
     * @param parameter 参数
     * @return 对象数组
     */
    public static Object[] convertToObjectArray(Object parameter) {
        Object[] retObject;
        if (parameter instanceof Object[]) {
            retObject = (Object[]) parameter;
        } else {
            retObject = new Object[]{parameter};
        }
        return retObject;
    }

    /**
     * 把对象转成Map集合，分表路由使用
     *
     * @param arg 参数
     * @return Map集合
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> convertToMap(Object arg) {
        if (arg instanceof Map) {
            return (Map<String, Object>) arg;
        }
        Map<String, Object> returnMap = new HashMap<>();
        /** 将实体Bean转为Map，设置分表路由 */
        try {
            returnMap = convertBeanToMap(arg);
            setRouteTable(arg, returnMap);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return returnMap;
    }

    /**
     * 设置分表路由
     *
     * @param entity   实体类型
     * @param paramMap 分表参数
     */
    public static void setRouteTable(Object entity, Map<String, Object> paramMap) {
        String routeTable = SqlParserManager.getRouteTable(entity.getClass());
        if (!StringUtils.isBlank(routeTable)) {
            paramMap.put(DaoConstants.ROUTE_TABLE, routeTable);
        }
    }

    /**
     * 将实体Bean转换成Map集合
     *
     * @param bean 对象
     * @return Map集合
     * @throws Exception 异常
     */
    public static Map<String, Object> convertBeanToMap(Object bean) throws Exception {
        Map<String, Object> map = new HashMap<>();
        PropertyUtilsBean propUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
        /** 通过传入的对象类参数值，读取其属性数组 */
        PropertyDescriptor[] descriptors = propUtilsBean.getPropertyDescriptors(bean);
        /** 遍历属性数组，生成Map */
        for (PropertyDescriptor descriptor : descriptors) {
            String name = descriptor.getName();
            if ("class".equals(name)) {
                continue;
            }
            /** 获取属性的Method对象 */
            Method method = propUtilsBean.getReadMethod(descriptor);
            /** 若其不为空则执行invoke调用 */
            if (method != null) {
                Object value = method.invoke(bean);
                if (value != null) {
                    map.put(name, value);
                }
            }
        }
        return map;
    }

    /**
     * Map集合为空则新建
     *
     * @param map Map集合
     * @return 非空Map集合
     */
    public static Map<String, Object> mapIfNull(Map<String, Object> map) {
        if (map == null) {
            return new HashMap<>();
        }
        return map;
    }

    /**
     * 设置对象属性
     *
     * @param targetObject  目标对象
     * @param propertyName  对象属性名
     * @param propertyValue 对象属性值
     */
    public static void setProperty(Object targetObject, String propertyName, Object propertyValue) {
        try {
            Field field = targetObject.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            field.set(targetObject, propertyValue);
        } catch (SecurityException | IllegalArgumentException | NoSuchFieldException |
                IllegalAccessException e) {
            logger.warn(e.getMessage(), e);
        }
    }
}
