package com.devotion.dao.transaction.template;

import com.devotion.dao.constants.ExceptionType;
import com.devotion.dao.constants.TransactionPropagation;
import com.devotion.dao.exception.BaseException;
import com.devotion.dao.utils.DataSourceContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionException;

import javax.sql.DataSource;

/**
 * 事务模板 事务异常机制 对入通过事务处理的service，抛出的异常均会被包成BaseException <br>
 * 1、连接异常code是error.dal.001 对应的venUse errorCode 是18008001 <br>
 * 2、数据库访问异常code是error.dal.007 对应的venUse errorCode 是18008002 <br>
 * 3、业务异常需要通过getCause方法获取
 */
public class TransactionTemplate {
    /**
     * 数据源
     */
    private DataSource dataSource;

    /**
     * 构造方法
     *
     * @param dataSource 数据源
     */
    public TransactionTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 事务模板执行 （匿名内部类）
     *
     * @param action 回调方法
     * @param <T>    泛型对象
     * @return 执行结果
     */
    public <T> T execute(final CallBackTemplate<T> action) {
        /** spring的数据源管理器 */
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        /** spring的事务模板 */
        org.springframework.transaction.support.TransactionTemplate transactionTemplate = new org.springframework.transaction.support.TransactionTemplate(
                transactionManager);

        try {
            /** 压事务性数据源 */
            DataSourceContext.pushCurrentDataSource(dataSource);
            /** 事务模板执行，匿名类 */
            return transactionTemplate.execute((TransactionCallback) -> action.invoke());
//			return transactionTemplate.execute(new TransactionCallback<T>() {
//				public T doInTransaction(TransactionStatus status) {
//					return action.invoke();
//				}
//			});
        } catch (TransactionException e) {
            if (CannotCreateTransactionException.class.isAssignableFrom(e.getClass())) {
                throw new BaseException(ExceptionType.DB_CONN_ERROR.getCode(), e, null,
                        ExceptionType.DB_CONN_ERROR.getDesc());
            }
            throw e;
        } finally {
            /** 弹事务性数据源 */
            DataSourceContext.popCurrentDataSource();
        }
    }

    /**
     * 事务模板执行
     *
     * @param action      回调方法
     * @param propagation 食物传播级别
     * @param <T>         泛型对象
     * @return 执行结果
     */
    public <T> T execute(final CallBackTemplate<T> action, TransactionPropagation propagation) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        org.springframework.transaction.support.TransactionTemplate transactionTemplate = new org.springframework.transaction.support.TransactionTemplate(
                transactionManager);
        /** 事务的传播行为 */
        transactionTemplate.setPropagationBehavior(propagation.getPropagationBehavior());

        try {
            /** 压事务性数据源 */
            DataSourceContext.pushCurrentDataSource(dataSource);
            /** 事务模板执行，匿名类 */
            return transactionTemplate.execute((TransactionCallback) -> action.invoke());
//            return transactionTemplate.execute(new TransactionCallback<T>() {
//                public T doInTransaction(TransactionStatus status) {
//                    return action.invoke();
//                }
//            });
        } catch (TransactionException e) {
            if (CannotCreateTransactionException.class.isAssignableFrom(e.getClass())) {
                throw new BaseException(ExceptionType.DB_CONN_ERROR.getCode(), e, null,
                        ExceptionType.DB_CONN_ERROR.getDesc());
            }
            throw e;
        } finally {
            /** 弹事务性数据源 */
            DataSourceContext.popCurrentDataSource();
        }
    }
}
