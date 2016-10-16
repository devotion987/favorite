package com.devotion.dao.client;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;

import com.devotion.dao.transaction.template.TransactionTemplate;

/**
 * 数据连接 接口类
 */
public interface DaoClient {

	/**
	 * 单表添加操作 返回Number类型的主键
	 * 
	 * @param entity
	 *            对象实体
	 * @return 成果操作的记录数
	 */
	Number persist(Object entity);

	/**
	 * 根据传入主键类型requiredType，返回主键值<br>
	 * 非自增长主键，如果传入类型与主键类型不符，会抛出ClassCastException<br>
	 * 自增长主键，必须传入类型为Number类型
	 * 
	 * @param entity
	 *            对象
	 * @param requiredType
	 *            需要操作的类型
	 * @param <T>
	 *            泛型对象
	 * @return 持久化操作结果
	 */
	<T> T persist(Object entity, Class<T> requiredType);

	/**
	 * 单表修改操作 根据主键修改记录
	 * 
	 * @param entity
	 *            实体对象
	 * @return 更新成功的记录数
	 */
	int merge(Object entity);

	/**
	 * 动态更新
	 * 
	 * @param entity
	 *            实体对象
	 * @return 动态更新成功的记录数
	 */
	int dynamicMerge(Object entity);

	/**
	 * 单表删除操作 根据主键删除记录
	 * 
	 * @param entity
	 *            实体对象
	 * @return 删除成果的记录数
	 */
	int remove(Object entity);

	/**
	 * 单表查询操作 根据主键查询记录
	 * 
	 * @param entityClass
	 *            实体类
	 * @param entity
	 *            实体对象
	 * @param <T>
	 *            泛型对象
	 * @return 查询结果
	 */
	<T> T find(Class<T> entityClass, Object entity);

	/**
	 * 根据sqlId查询单个对象，返回requiredType类型对象，不需要强转，查不到返回null, 查询多个返回第一个
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param paramMap
	 *            查询参数
	 * @param requiredType
	 *            翻页处理规则
	 * @param <T>
	 *            泛型对象
	 * @return 查询结果
	 */
	<T> T queryForObject(String sqlId, Map<String, Object> paramMap, Class<T> requiredType);

	/**
	 * 根据sqlId查询单个对象，返回requiredType类型对象，不需要强转，查不到返回null, 查询多个返回第一个
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param param
	 *            查询参数
	 * @param requiredType
	 *            翻页处理规则
	 * @param <T>
	 *            泛型对象
	 * @return 查询结果
	 */
	<T> T queryForObject(String sqlId, Object param, Class<T> requiredType);

	/**
	 * 根据sqlId查询单个对象，返回rowMapper类型对象, 查不到返回null, 查询多个返回第一个
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param paramMap
	 *            查询参数
	 * @param rowMapper
	 *            翻页处理规则
	 * @param <T>
	 *            泛型对象
	 * @return 查询结果
	 */
	<T> T queryForObject(String sqlId, Map<String, Object> paramMap, RowMapper<T> rowMapper);

	/**
	 * 根据sqlId查询单个对象，返回rowMapper类型对象, 查不到返回null, 查询多个返回第一个
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param param
	 *            查询参数
	 * @param rowMapper
	 *            翻页处理规则
	 * @param <T>
	 *            泛型对象
	 * @return 查询结果
	 */
	<T> T queryForObject(String sqlId, Object param, RowMapper<T> rowMapper);

	/**
	 * 根据sqlId查询单个对象，返回Map集合，key是数据库字段 ，查不到返回null,查询多个返回第一个
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param paramMap
	 *            查询参数
	 * @return Map类型的查询结果
	 */
	Map<String, Object> queryForMap(String sqlId, Map<String, Object> paramMap);

	/**
	 * 根据sqlId查询单个对象，返回Map集合，key是数据库字段 ，查不到返回null,查询多个返回第一个
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param param
	 *            查询参数
	 * @return Map类型的查询结果
	 */
	Map<String, Object> queryForMap(String sqlId, Object param);

	/**
	 * 根据sqlId查询多个对象，返回requiredType类型对象List集合，不需要强转
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param paramMap
	 *            查询参数
	 * @param requiredType
	 *            需要操作的类型
	 * @param <T>
	 *            泛型对象
	 * @return List类型的查询结果
	 */
	<T> List<T> queryForList(String sqlId, Map<String, Object> paramMap, Class<T> requiredType);

	/**
	 * 根据sqlId查询多个对象，返回requiredType类型对象List集合，不需要强转
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param param
	 *            查询参数
	 * @param requiredType
	 *            需要操作的类型
	 * @param <T>
	 *            泛型对象
	 * @return List类型的查询结果
	 */
	<T> List<T> queryForList(String sqlId, Object param, Class<T> requiredType);

	/**
	 * 根据sqlId查询，返回Map集合List，key是数据库字段
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param paramMap
	 *            查询参数
	 * @return List类型的查询结果
	 */
	List<Map<String, Object>> queryForList(String sqlId, Map<String, Object> paramMap);

	/**
	 * 根据sqlId查询，返回Map集合List，key是数据库字段
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param param
	 *            查询参数
	 * @return List类型的查询结果
	 */
	List<Map<String, Object>> queryForList(String sqlId, Object param);

	/**
	 * 根据sqlId查询多个对象，返回rowMapper类型对象List集合
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param paramMap
	 *            查询参数
	 * @param rowMapper
	 *            翻页处理规则
	 * @param <T>
	 *            泛型对象
	 * @return List类型的查询结果
	 */
	<T> List<T> queryForList(String sqlId, Map<String, Object> paramMap, RowMapper<T> rowMapper);

	/**
	 * 根据sqlId查询多个对象，返回rowMapper类型对象List集合
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param param
	 *            查询参数
	 * @param rowMapper
	 *            翻页处理规则
	 * @param <T>
	 *            泛型对象
	 * @return List类型的查询结果
	 */
	<T> List<T> queryForList(String sqlId, Object param, RowMapper<T> rowMapper);

	/**
	 * 根据sqlId执行，返回执行成功的记录条数
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param paramMap
	 *            参数
	 * @return 执行成功的记录数
	 */
	int execute(String sqlId, Map<String, Object> paramMap);

	/**
	 * 根据sqlId执行，返回执行成功的记录条数
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param param
	 *            参数对象
	 * @return 执行成功的记录数
	 */
	int execute(String sqlId, Object param);

	/**
	 * 根据sqlId执行，批量执行
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param batchValues
	 *            批处理对象
	 * @return 执行成功的记录数
	 */
	int[] batchUpdate(String sqlId, Map<String, Object>[] batchValues);

	/**
	 * 获取默认的事务事务模板
	 * 
	 * @return 事务模板
	 */
	TransactionTemplate getTransactionTemplate();

	/**
	 * 根据业务参数获取符合业务规则的事务模板
	 * 
	 * @param parameter
	 *            参数对象
	 * @return 事务模板
	 */
	TransactionTemplate getTransactionTemplate(Object parameter);

	/**
	 * 存储过程调用 存储过程调用时，需要加上schema
	 * 
	 * @param sqlId
	 *            SQL语句ID
	 * @param paramMap
	 *            查询参数
	 * @param sqlParameters
	 *            SqlCommand参数
	 * @return 调用结果
	 */
	Map<String, Object> call(String sqlId, Map<String, Object> paramMap, List<SqlParameter> sqlParameters);
}
