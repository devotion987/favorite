package com.devotion.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.object.GenericStoredProcedure;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.devotion.dao.utils.DaoUtils;
import com.devotion.dao.utils.DataSourceContext;
import com.devotion.dao.resource.parse.SqlBean;
import com.devotion.dao.support.rowmapper.RowMapperFactory;
import com.devotion.dao.support.sql.FreeMakerParser;
import com.devotion.dao.support.sql.SqlParser;
import com.devotion.dao.support.sql.SqlParserManager;
import com.devotion.dao.support.value.ValueParser;
import com.devotion.dao.transaction.TransactionOperation;

/**
 * 数据操作层，包装了数据层面的基本操作方法
 */
public class DataBaseOperation extends TransactionOperation {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DataBaseOperation.class);

    /**
     * SQL失效时间
     */
    private static final Long SQLTIMEOUT = 100L;

    /**
     * 数据持久化操作
     *
     * @param entity 实体对象
     * @return 持久化操作成功记录数
     */
    public Number persist(Object entity) {
        return persist(entity, Number.class);
    }

    /**
     * 数据持久化
     *
     * @param entity       数据实体
     * @param requiredType 需要处理的类型
     * @param <T>          泛型对象
     * @return 持久化操作成功记录数
     */
    @SuppressWarnings("unchecked")
    public <T> T persist(Object entity, Class<T> requiredType) {
        SqlParser sqlParser = SqlParserManager.getSqlParser(entity.getClass());
        String insertSQL = sqlParser.getInsert();
        Map<String, Object> paramMap = ValueParser.parser(entity);
        processTableRoute(paramMap);
        /** SQL模板渲染 */
        insertSQL = FreeMakerParser.process(insertSQL, paramMap);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        logMessage("persist", insertSQL, paramMap);
        long beginDate = System.currentTimeMillis();
        /** 渲染后获取JDBC模板 */
        getValidateJdbcTemplate(paramMap, ValueParser.parseSqlBean(entity, Boolean.FALSE)).update(insertSQL,
                new MapSqlParameterSource(paramMap), keyHolder, new String[]{sqlParser.getIdName()});
        Object key = paramMap.get(sqlParser.getId());
        if (key == null || (key instanceof Number && ((Number) key).doubleValue() == 0.0d)) {
            return (T) keyHolder.getKey();
        }
        logMessage("persist", insertSQL, paramMap, System.currentTimeMillis() - beginDate);
        return (T) key;
    }

    /**
     * 数据合并与更新
     *
     * @param entity 更新的数据实体
     * @return 数据更新后的结果
     */
    public int merge(Object entity) {
        String updateSql = SqlParserManager.getSqlParser(entity.getClass()).getUpdate();
        Map<String, Object> paramMap = ValueParser.parser(entity);
        processTableRoute(paramMap);
        /** FreeMarker模板渲染 */
        updateSql = FreeMakerParser.process(updateSql, paramMap);
        logMessage("merge", updateSql, paramMap);
        long beginDate = System.currentTimeMillis();
        /** 调用JDBCTemplate实现更新，返回更新成功的记录数 */
        int result = getValidateJdbcTemplate(paramMap, ValueParser.parseSqlBean(entity, Boolean.FALSE))
                .update(updateSql, paramMap);
        logMessage("merge", updateSql, paramMap, System.currentTimeMillis() - beginDate);

        return result;
    }

    /**
     * 动态更新
     *
     * @param entity 更新的数据实体
     * @return 返回更新的记录数目
     */
    public int dynamicMerge(Object entity) {
        Map<String, Object> paramMap = ValueParser.parser(entity);
        processTableRoute(paramMap);
        String updateSql = SqlParserManager.getSqlParser(entity.getClass()).getDynamicUpdate(paramMap);
        /** FreeMarker模板渲染 */
        updateSql = FreeMakerParser.process(updateSql, paramMap);
        logMessage("dynamicMerge", updateSql, paramMap);
        long beginDate = System.currentTimeMillis();
        /** 调用JDBCTemplate实现更新，返回更新成功的记录数 */
        int result = getValidateJdbcTemplate(paramMap, ValueParser.parseSqlBean(entity, Boolean.FALSE))
                .update(updateSql, paramMap);
        logMessage("dynamicMerge", updateSql, paramMap, System.currentTimeMillis() - beginDate);

        return result;
    }

    /**
     * 数据删除
     *
     * @param entity 删除的数据实体
     * @return 返回删除的记录数目
     */
    public int remove(Object entity) {
        String removeSql = SqlParserManager.getSqlParser(entity.getClass()).getDelete();
        Map<String, Object> paramMap = ValueParser.parser(entity);
        processTableRoute(paramMap);
        /** FreeMarker模板渲染 */
        removeSql = FreeMakerParser.process(removeSql, paramMap);
        logMessage("remove", removeSql, paramMap);
        long beginDate = System.currentTimeMillis();
        /** 调用JDBCTemplate实现更新，返回更新成功的记录数 */
        int result = getValidateJdbcTemplate(paramMap, ValueParser.parseSqlBean(entity, Boolean.FALSE))
                .update(removeSql, paramMap);
        logMessage("remove", removeSql, paramMap, System.currentTimeMillis() - beginDate);

        return result;
    }

    /**
     * 根据传入实体类查询单个记录
     *
     * @param entityClass 实体类
     * @param entity      查询对象
     * @param <T>         泛型对象
     * @return 查询结果
     */
    public <T> T find(Class<T> entityClass, Object entity) {
        String findSql = SqlParserManager.getSqlParser(entity.getClass()).getSelect();
        Map<String, Object> paramMap = ValueParser.parser(entity);
        processTableRoute(paramMap);
        /** FreeMarker模板渲染 */
        findSql = FreeMakerParser.process(findSql, paramMap);
        logMessage("find", findSql, paramMap);
        long beginDate = System.currentTimeMillis();
        /** 调用JDBCTemplate实现单记录查询，并返回查询结果 */
        List<T> resultList = getJdbcTemplate(paramMap, ValueParser.parseSqlBean(entity, null)).query(findSql, paramMap,
                new RowMapperFactory<>(entityClass).getRowMapper());
        logMessage("find", findSql, paramMap, System.currentTimeMillis() - beginDate);

        return singleResult(resultList);
    }

    /**
     * 查询单个记录
     *
     * @param sqlId        SQLID
     * @param paramMap     查询参数
     * @param requiredType 需要处理的类型
     * @param <T>          泛型对象
     * @return 查询结果
     */
    public <T> T queryForObject(String sqlId, Map<String, Object> paramMap, Class<T> requiredType) {
        return this.queryForObject(sqlId, paramMap, new RowMapperFactory<>(requiredType).getRowMapper());
    }

    /**
     * 查询单个记录
     *
     * @param sqlId        SQLID
     * @param param        查询参数
     * @param requiredType 需要处理的类型
     * @param <T>          泛型对象
     * @return 查询结果
     */
    public <T> T queryForObject(String sqlId, Object param, Class<T> requiredType) {
        return this.queryForObject(sqlId, DaoUtils.convertToMap(param), requiredType);
    }

    /**
     * 根据sqlId查询单条记录
     *
     * @param sqlId     SQLID
     * @param paramMap  查询参数
     * @param rowMapper 翻页处理规则
     * @param <T>       泛型对象
     * @return 查询结果
     */
    public <T> T queryForObject(String sqlId, Map<String, Object> paramMap, RowMapper<T> rowMapper) {
        processTableRoute(paramMap);
        SqlBean sqlBean = getSQL(sqlId);
        /** FreeMarker模板渲染 */
        String sql = FreeMakerParser.process(paramMap, sqlBean.getContent(), sqlId);
        logMessage("queryForObject", sql, paramMap);
        long beginDate = System.currentTimeMillis();
        /** 调用JDBCTemplate实现查询，并返回查询结果 */
        List<T> resultList = getJdbcTemplate(paramMap, sqlBean).query(sql, paramMap, rowMapper);
        logMessage("queryForObject", sql, paramMap, System.currentTimeMillis() - beginDate);

        return singleResult(resultList);
    }

    /**
     * queryForObject重载方法
     *
     * @param sqlId     SQLID
     * @param param     查询参数
     * @param rowMapper 翻页处理规则
     * @param <T>       泛型对象
     * @return 查询结果
     */
    public <T> T queryForObject(String sqlId, Object param, RowMapper<T> rowMapper) {
        return this.queryForObject(sqlId, DaoUtils.convertToMap(param), rowMapper);
    }

    /**
     * 查询并返回映射集
     *
     * @param sqlId    SQLID
     * @param paramMap 查询参数
     * @return 查询结果
     */
    public Map<String, Object> queryForMap(String sqlId, Map<String, Object> paramMap) {
        processTableRoute(paramMap);
        SqlBean sqlBean = getSQL(sqlId);
        /** FreeMarker模板渲染 */
        String sql = FreeMakerParser.process(paramMap, sqlBean.getContent(), sqlId);
        logMessage("queryForMap", sql, paramMap);
        long beginDate = System.currentTimeMillis();
        /** 调用JDBCTemplate实现查询，并返回查询结果 */
        Map<String, Object> map = singleResult(getJdbcTemplate(paramMap, sqlBean).queryForList(sql, paramMap));
        logMessage("queryForMap", sql, paramMap, System.currentTimeMillis() - beginDate);
        return map;
    }

    /**
     * queryForMap重载方法
     *
     * @param sqlId SQLID
     * @param param 查询参数
     * @return 查询结果
     */
    public Map<String, Object> queryForMap(String sqlId, Object param) {
        return this.queryForMap(sqlId, DaoUtils.convertToMap(param));
    }

    /**
     * queryForList重载方法
     *
     * @param sqlId        sqlId
     * @param paramMap     查询参数
     * @param requiredType 需要处理的类型
     * @param <T>          泛型对象
     * @return 查询结果
     */
    public <T> List<T> queryForList(String sqlId, Map<String, Object> paramMap, Class<T> requiredType) {

        return this.queryForList(sqlId, paramMap, new RowMapperFactory<>(requiredType).getRowMapper());

    }

    /**
     * queryForList重载方法
     *
     * @param sqlId        SQLID
     * @param param        查询参数
     * @param requiredType 需要处理的类型
     * @param <T>          泛型对象
     * @return 查询结果
     */
    public <T> List<T> queryForList(String sqlId, Object param, Class<T> requiredType) {
        return queryForList(sqlId, DaoUtils.convertToMap(param), requiredType);
    }

    /**
     * 根据sqlId查询多条记录，返回list型结果集
     *
     * @param sqlId     SQLID
     * @param paramMap  查询参数
     * @param rowMapper 翻页处理规则
     * @param <T>       泛型对象
     * @return 查询结果
     */
    public <T> List<T> queryForList(String sqlId, Map<String, Object> paramMap, RowMapper<T> rowMapper) {
        processTableRoute(paramMap);
        SqlBean sqlBean = getSQL(sqlId);
        /** FreeMarker模板渲染 */
        String sql = FreeMakerParser.process(paramMap, sqlBean.getContent(), sqlId);
        logMessage("queryForList(3 paramter)", sql, paramMap);
        long beginDate = System.currentTimeMillis();
        /** 调用JDBCTemplate实现查询，并返回查询结果 */
        List<T> list = getJdbcTemplate(paramMap, sqlBean).query(sql, DaoUtils.mapIfNull(paramMap), rowMapper);
        logMessage("queryForList(3 paramter)", sql, paramMap, System.currentTimeMillis() - beginDate);

        return list;
    }

    /**
     * 查询并返回结果集，queryForList重载方法
     *
     * @param sqlId     SQLID
     * @param param     查询参数
     * @param rowMapper 翻页处理规则
     * @param <T>       泛型对象
     * @return 查询结果
     */
    public <T> List<T> queryForList(String sqlId, Object param, RowMapper<T> rowMapper) {
        return queryForList(sqlId, DaoUtils.convertToMap(param), rowMapper);
    }

    /**
     * 根据sqlId查询多条记录，返回List<Map<String, Object>>型结果集，queryForList重载方法
     *
     * @param sqlId    SQLID
     * @param paramMap 查询参数
     * @return 查询结果
     */
    public List<Map<String, Object>> queryForList(String sqlId, Map<String, Object> paramMap) {
        processTableRoute(paramMap);
        SqlBean sqlBean = getSQL(sqlId);
        /** FreeMarker模板渲染 */
        String sql = FreeMakerParser.process(paramMap, sqlBean.getContent(), sqlId);
        logMessage("queryForList(2 paramter)", sql, paramMap);
        long beginDate = System.currentTimeMillis();
        /** 调用JDBCTemplate实现多记录查询，并返回查询结果 */
        List<Map<String, Object>> list = getJdbcTemplate(paramMap, sqlBean).queryForList(sql,
                DaoUtils.mapIfNull(paramMap));
        logMessage("queryForList(2 paramter)", sql, paramMap, System.currentTimeMillis() - beginDate);
        return list;
    }

    /**
     * queryForList重载方法
     *
     * @param sqlId SQLID
     * @param param 查询参数
     * @return 查询结果
     */
    public List<Map<String, Object>> queryForList(String sqlId, Object param) {
        return queryForList(sqlId, DaoUtils.convertToMap(param));
    }

    /**
     * 执行查询，返回结果集记录数目
     *
     * @param sqlId    SQLID
     * @param paramMap 执行参数
     * @return 查询结果
     */
    public int execute(String sqlId, Map<String, Object> paramMap) {
        processTableRoute(paramMap);
        SqlBean sqlBean = getSQL(sqlId);
        /** FreeMarker模板渲染 */
        String sql = FreeMakerParser.process(paramMap, sqlBean.getContent(), sqlId);
        logMessage("execute", sql, paramMap);
        long beginDate = System.currentTimeMillis();
        /** 调用JDBCTemplate实现更新，返回更新成功的记录数 */
        int result = getValidateJdbcTemplate(paramMap, sqlBean).update(sql, DaoUtils.mapIfNull(paramMap));
        logMessage("execute", sql, paramMap, System.currentTimeMillis() - beginDate);

        return result;
    }

    /**
     * 执行查询，execute重载方法
     *
     * @param sqlId SQLID
     * @param param 执行参数
     * @return 查询结果
     */
    public int execute(String sqlId, Object param) {
        return this.execute(sqlId, DaoUtils.convertToMap(param));
    }

    /**
     * 批量更新
     *
     * @param sqlId       sqlId
     * @param batchValues 需要批处理的集合
     * @return 批处理成功记录数
     */
    public int[] batchUpdate(String sqlId, Map<String, Object>[] batchValues) {
        Map<String, Object> paramMap = new HashMap<>();
        if (batchValues != null && batchValues[0] != null) {
            paramMap = batchValues[0];
        }
        /** 组装SQLBean */
        SqlBean sqlBean = getSQL(sqlId);
        processTableRoute(paramMap);
        /** FreeMarker模板渲染 */
        String sql = FreeMakerParser.process(paramMap, sqlBean.getContent(), sqlId);
        logMessage("batchUpdate", sql, String.valueOf(batchValues == null ? 0 : batchValues.length));
        long beginDate = System.currentTimeMillis();
        int[] result;
        /** 调用JDBCTemplate批量更新，返回更新成功的记录数 */
        result = getValidateJdbcTemplate(paramMap, sqlBean).batchUpdate(sql, batchValues);
        logMessage("batchUpdate", sql, String.valueOf(batchValues == null ? 0 : batchValues.length),
                System.currentTimeMillis() - beginDate);

        return result;
    }

    /**
     * 调存储过程
     *
     * @param sqlId         SQLID
     * @param paramMap      执行参数
     * @param sqlParameters sqlCommand参数的对象
     * @return 存储过程执行结果
     */
    public Map<String, Object> call(String sqlId, Map<String, Object> paramMap, List<SqlParameter> sqlParameters) {
        Map<String, Object> paramMapTmp = DaoUtils.mapIfNull(paramMap);
        SqlBean sqlBean = getSQL(sqlId);
        /** SQL渲染 */
        String sql = FreeMakerParser.process(paramMapTmp, sqlBean.getContent(), sqlId);
        logMessage("call", sql, paramMapTmp);
        long beginDate = System.currentTimeMillis();
        /** 调用存储过程 */
        GenericStoredProcedure storedProcedure = new GenericStoredProcedure();
        /** 放入数据源 */
        storedProcedure.setDataSource(getValidateDataSource(paramMapTmp, sqlBean));
        /** 放入SQL */
        storedProcedure.setSql(sqlBean.getContent());
        for (SqlParameter sqlParameter : sqlParameters) {
            storedProcedure.declareParameter(sqlParameter);
        }
        logMessage("call", sql, paramMapTmp, System.currentTimeMillis() - beginDate);
        return storedProcedure.execute(paramMapTmp);
    }

    /**
     * 返回JDBC模板
     *
     * @param params  参数
     * @param sqlBean SQL映射对象
     * @return JDBC模板
     */
    public NamedParameterJdbcTemplate getJdbcTemplate(Object params, SqlBean sqlBean) {
        return new NamedParameterJdbcTemplate(getDataSource(params, sqlBean));
    }

    /**
     * 返回有效的JDBC模板
     *
     * @param params  参数
     * @param sqlBean SQL映射对象
     * @return JDBC模板
     */
    public NamedParameterJdbcTemplate getValidateJdbcTemplate(Object params, SqlBean sqlBean) {
        return new NamedParameterJdbcTemplate(getValidateDataSource(params, sqlBean));
    }

    /**
     * 分表路由
     *
     * @param paramMap client传入参数
     */
    public void processTableRoute(Map<String, Object> paramMap) {
        routeTable(paramMap);
    }

    /**
     * 获取数据源
     *
     * @param params  分库参数
     * @param sqlBean SQL映射对象
     * @return 目标数据源
     */
    private DataSource getDataSource(Object params, SqlBean sqlBean) {
        DataSource targetDataSource = DataSourceContext.getDataSource();
        if (targetDataSource == null) {
            return routeDataSource(params, sqlBean);
        } else {
            return targetDataSource;
        }
    }

    /**
     * 获取有效的数据源
     *
     * @param params  分库参数
     * @param sqlBean SQL映射对象
     * @return 目标数据源
     */
    private DataSource getValidateDataSource(Object params, SqlBean sqlBean) {
        DataSource targetDataSource = DataSourceContext.getDataSource();

        if (targetDataSource == null) {
            return routeDataSource(params, sqlBean);
        }
        /**
         * if (!targetDataSource.equals(routeDataSource) && routeDataSource !=
         * null) { throw new DalException(
         * "Could not operate different DataSouce[" + targetDataSource + ", " +
         * routeDataSource + "]"); }
         */
        return targetDataSource;
    }

    /**
     * 打印sql的执行信息
     *
     * @param method 方法名
     * @param sql    SQL串
     * @param object 对象
     */
    private void logMessage(String method, String sql, Object object) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(method + " method SQL: [" + sql + "]");
            LOGGER.debug(method + " method parameter:" + object);
        }
    }

    /**
     * 打印超时sql的执行时间
     *
     * @param method      方法名
     * @param sql         SQL串
     * @param object      对象
     * @param executeTime 执行时间
     */
    private void logMessage(String method, String sql, Object object, long executeTime) {
        /** 打印超时sql的执行时间 */
        if (executeTime >= SQLTIMEOUT) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(method + " method executeTime:" + executeTime + "ms");
                LOGGER.debug(method + " method SQL: [" + sql + "]");
                LOGGER.debug(method + " method object: [" + object + "]");
            }
        }
    }

    /**
     * 返回结果集中的第一条记录
     *
     * @param resultList 结果集
     * @param <T>        泛型对象
     * @return 结果集中的第一条记录
     */
    private <T> T singleResult(List<T> resultList) {
        if (resultList != null) {
            int size = resultList.size();
            if (size > 0) {
                if (LOGGER.isDebugEnabled() && size > 1) {
                    LOGGER.debug(
                            "Incorrect result size: expected " + 1 + ", actual " + size + " return the first element.");
                }
                return resultList.get(0);
            }
            if (size == 0) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Incorrect result size: expected " + 1 + ", actual " + size);
                }
                return null;
            }
        }
        return null;
    }

}
