package com.devotion.dao.transaction.advisor;

import com.devotion.dao.client.DaoClient;
import com.devotion.dao.constants.ExceptionType;
import com.devotion.dao.exception.BaseException;
import com.devotion.dao.transaction.anotation.RouteParam;
import com.devotion.dao.transaction.anotation.Transactional;
import com.devotion.dao.transaction.template.TransactionTemplate;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

/**
 * 事务拦截器 事务异常机制 对入通过事务处理的service，抛出的异常均会被包成BaseException
 * 1、连接异常code是error.dal.001 对应的venUse errorCode 是18008001
 * 2、数据库访问异常code是error.dal.007 对应的venUse errorCode 是18008002
 * 3、业务异常需要通过getCause方法获取
 */
public class TransactionInterceptor implements MethodInterceptor {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionInterceptor.class);

    /**
     * 数据连接对象
     */
    private DaoClient daoClient;

    /**
     * 事务回调方法
     *
     * @param invocation 回调方法
     * @return 执行结果
     * @throws Throwable 异常
     */
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        /** 加Transactional的Method */
        if (invocation.getMethod().isAnnotationPresent(Transactional.class)) {
            Transactional transactional = invocation.getMethod().getAnnotation(Transactional.class);
            /** 根据拦截到的业务数据，路由数据源 */
            TransactionTemplate transactionTemplate = daoClient.getTransactionTemplate(getParameter(invocation));
            /** 事务回调 */
            return transactionTemplate.execute(() -> {
                try {
                    /** 事务回调执行 */
                    return invocation.proceed();
                } catch (Throwable e) {
                    if (DataAccessException.class.isAssignableFrom(e.getClass())) {
                        throw new BaseException(ExceptionType.DB_ACCESS_ERROR.getCode(), e, null,
                                ExceptionType.DB_ACCESS_ERROR.getDesc());
                    }
                    throw new BaseException(ExceptionType.EXCEPTION_DEF.getCode(), e, null, e.getMessage());
                }
            }, transactional.propagation());
//            return transactionTemplate.execute(new CallBackTemplate<Object>() {
//                public Object invoke() {
//                    try {
//                        /** 事务回调执行 */
//                        return invocation.proceed();
//                    } catch (Throwable e) {
//                        if (DataAccessException.class.isAssignableFrom(e.getClass())) {
//                            throw new BaseException("error.dal.008", e, null, "数据访问异常");
//                        } else {
//                            throw new BaseException("error.dal.007", e, null, e.getMessage());
//                        }
//                    }
//                }
//            }, transactional.propagation());
        }
        /** 没加的方法，直接通过 */
        try {
            return invocation.proceed();
        } catch (Throwable e) {
            if (CannotGetJdbcConnectionException.class.isAssignableFrom(e.getClass())) {
                throw new BaseException(ExceptionType.DB_CONN_ERROR.getCode(), e, null,
                        ExceptionType.DB_CONN_ERROR.getDesc());
            } else if (DataAccessException.class.isAssignableFrom(e.getClass())) {
                throw new BaseException(ExceptionType.DB_ACCESS_ERROR.getCode(), e, null,
                        ExceptionType.DB_ACCESS_ERROR.getDesc());
            } else {
                throw e;
            }
        }
    }

    /**
     * 获取业务参数
     *
     * @param invocation 方法调用
     * @return 业务参数
     */
    private Object[] getParameter(MethodInvocation invocation) {
        /** 事务注解 */
        Transactional transactional = invocation.getMethod().getAnnotation(Transactional.class);
        /** 路由参数注解 */
        RouteParam routeParam = invocation.getMethod().getAnnotation(RouteParam.class);
        /** 解析业务参数，确定第几个业务入参，作为分库对象 */
        Object[] indexParams = indexParam(transactional, invocation.getArguments());

        if (routeParam == null) {
            return indexParams;
        }
        /** 路由参数注解确定细粒度分库对象 */
        Object obj = isInstance(indexParams, routeParam);

        if (obj == null) {
            return indexParams;
        }
        try {
            /** 解析分库参数类型，生成Map类型的分库参数 */
            obj = getValue(obj, routeParam.field());
            LOGGER.debug("interceptor route param is : " + obj);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return indexParams;
        }
        return new Object[]{obj};
    }

    /**
     * 功能描述: <br>
     * 根据分库注解，分析业务参数，实例化分库注解定义的分库参数规则
     *
     * @param indexParams 业务参数
     * @param routeParam  分库注解
     * @return 对象
     */
    private Object isInstance(Object[] indexParams, RouteParam routeParam) {
        Object obj = null;
        for (Object param : indexParams) {
            /** 处理数组 */
            if (routeParam.isArray()) {
                param = ((Object[]) param)[routeParam.index()];
            }
            /** 实例化 */
            if (routeParam.clazz().isInstance(param)) {
                obj = param;
                break;
            }
        }
        return obj;
    }

    /**
     * 功能描述: <br>
     * 解析分库对象
     *
     * @param obj   分库对象
     * @param field 分库定义
     * @return 值
     * @throws BaseException 异常
     */

    private Object getValue(Object obj, String field) throws /* Exception */ BaseException {
        Map<String, Object> routeMap = new HashMap<>();
        if (obj instanceof Map<?, ?>) {
            /** Map类型 */
            Map<?, ?> parentParamMap = (Map<?, ?>) obj;
            if (!parentParamMap.containsKey(field)) {
                throw new BaseException("param " + obj + " not contains key " + field);
            }
            /** 取出分库注解定义的值 */
            routeMap.put(field, parentParamMap.get(field));
        } else if (obj instanceof String) {
            /** 字符类型 */
            routeMap.put(field, obj);
        } else if (obj instanceof Number) {
            /** 数字类型 */
            routeMap.put(field, obj);
        } else {
            /** 对象类型 */
            PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(obj.getClass(), field);
            // Object value = descriptor.getReadMethod().invoke(obj, new
            // Object[] {});

            Object value;
            try {
                value = descriptor.getReadMethod().invoke(obj, new Object[]{});
            } catch (Exception e) {
                throw new BaseException("param " + obj + " not invoke " + field);
            }

            routeMap.put(field, value);
        }
        return routeMap;
    }

    /**
     * 功能描述: <br>
     * 根据事务注解确定分库对象
     *
     * @param transactional 事务注解
     * @param arguments     拦截业务对象参数
     * @return 对象列表
     */
    private Object[] indexParam(Transactional transactional, Object[] arguments) {
        Object[] parameter = null;
        /** 获取分库注解在方法入参的位置 */
        int paramIndex = transactional.paramIndex();
        if (paramIndex < 0) {
            parameter = arguments;
        } else if (paramIndex < arguments.length) {
            parameter = new Object[]{arguments[paramIndex]};
        }
        return parameter;
    }

    /**
     * 设置数据连接客户端
     *
     * @param daoClient 数据连接客户端
     */
    public void setDalClient(DaoClient daoClient) {
        this.daoClient = daoClient;
    }
}
