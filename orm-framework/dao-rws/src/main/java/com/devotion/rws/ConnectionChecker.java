package com.devotion.rws;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能描述：数据库实例连接可用性监测（心跳用）
 */
public class ConnectionChecker {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ConnectionChecker.class);

    /**
     * 判断是否是有效的连接
     *
     * @param c    数据库连接
     * @param type 数据库类型
     * @return SQLException SQL异常
     * @throws IllegalAccessException 数据库非法访问异常
     */
    public static SQLException isValidConnection(Connection c, String type) throws IllegalAccessException {
        String sql;
        /** 多类型数据库支持 */
        if ("mysql".equalsIgnoreCase(type)) {
            sql = "SELECT 1";
        } else if ("db2".equalsIgnoreCase(type)) {
            sql = "SELECT 1 FROM SYSIBM.SYSDUMMY1";
        } else if ("oracle".equalsIgnoreCase(type)) {
            sql = "select 1 from dual";
        } else {
            throw new IllegalAccessException("DataBase type:" + type + "is not supported!");
        }

        return isConnectionValid(c, sql);
    }

    /**
     * 连接的有效验证方法
     *
     * @param c   数据库连接
     * @param sql sql语句
     * @return SQLException SQL异常
     */
    public static SQLException isConnectionValid(Connection c, String sql) {

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            if (e instanceof SQLException) {
                logger.warn("Unexpected error while execute ({})", sql);
                return (SQLException) e;
            } else {
                logger.warn("Unexpected error while execute ({})", sql);
                return new SQLException("execute (" + sql + ") failed: " + e.toString());
            }
        } finally {
            /** cleanup the Statement */
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                logger.warn("Unexpected error");
            }
        }

        return null;
    }

    /**
     * 关闭连接
     *
     * @param c 数据库连接
     */
    public static void closeConnection(Connection c) {
        if (c != null) {
            try {
                c.close();
            } catch (SQLException e) {
                logger.warn("Unexpected error occur at close connection");
            }

        }
    }

    /**
     * 私有构造函数ConnectionChecker
     */
    private ConnectionChecker() {

    }

}
