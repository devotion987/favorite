package com.devotion.dao.client;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.devotion.dao.pagination.Pagination;
import com.devotion.dao.pagination.PaginationResult;

/**
 * 功能描述： 分页client 接口类
 */
public interface IPaginationDaoClient extends DaoClient {

	/**
	 * 偏移量
	 */
	String OFFSET = "offset";

	/**
	 * 返回指定的记录数
	 */
	String LIMIT = "limit";

	/**
	 * 初始SQL
	 */
	String ORIGSQL = "origsql";

	/**
	 * 获取分页处理结果
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param param
	 *            查询参数
	 * @param pagination
	 *            分页
	 * @return List类型的查询结果
	 */
	PaginationResult<List<Map<String, Object>>> queryForList(String sqlId, Object param, Pagination pagination);

	/**
	 * 获取分页处理结果
	 * 
	 * @param param
	 *            查询参数
	 * @param pagination
	 *            分页
	 * @param sqlId
	 *            SQLID数组，第一个参数三业务逻辑查询sql，第二个参数三查询总数sql
	 * @return List类型的查询结果
	 */
	PaginationResult<List<Map<String, Object>>> queryForList(Object param, Pagination pagination, String... sqlId);

	/**
	 * 获取分页处理结果 重载方法
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param paramMap
	 *            查询参数
	 * @param pagination
	 *            分页
	 * @return List类型的查询结果
	 */
	PaginationResult<List<Map<String, Object>>> queryForList(String sqlId, Map<String, Object> paramMap,
			Pagination pagination);

	/**
	 * 获取分页处理结果 重载方法
	 * 
	 * @param paramMap
	 *            查询参数
	 * @param pagination
	 *            分页
	 * @param sqlId
	 *            SQLID数组，第一个参数三业务逻辑查询sql，第二个参数三查询总数sql
	 * @return List类型的查询结果
	 */
	PaginationResult<List<Map<String, Object>>> queryForList(Map<String, Object> paramMap, Pagination pagination,
			String... sqlId);

	/**
	 * 获取分页处理结果 重载方法
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param paramMap
	 *            查询参数
	 * @param requiredType
	 *            需要操作的类型
	 * @param pagination
	 *            分页
	 * @param <T>
	 *            泛型对象
	 * @return List类型的查询结果
	 */
	<T> PaginationResult<List<T>> queryForList(String sqlId, Map<String, Object> paramMap, Class<T> requiredType,
			Pagination pagination);

	/**
	 * 获取分页处理结果 重载方法
	 * 
	 * @param paramMap
	 *            查询参数
	 * @param requiredType
	 *            需要操作的类型
	 * @param pagination
	 *            分页
	 * @param <T>
	 *            泛型对象
	 * @param sqlId
	 *            SQLID数组，第一个参数三业务逻辑查询sql，第二个参数三查询总数sql
	 * @return List类型的查询结果
	 */
	<T> PaginationResult<List<T>> queryForList(Map<String, Object> paramMap, Class<T> requiredType,
			Pagination pagination, String... sqlId);

	/**
	 * 获取分页处理结果 重载方法
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param param
	 *            查询参数
	 * @param requiredType
	 *            需要操作的类型
	 * @param pagination
	 *            分页
	 * @param <T>
	 *            泛型对象
	 * @return List类型的查询结果
	 */
	<T> PaginationResult<List<T>> queryForList(String sqlId, Object param, Class<T> requiredType,
			Pagination pagination);

	/**
	 * 获取分页处理结果 重载方法
	 * 
	 * @param param
	 *            查询参数
	 * @param requiredType
	 *            需要操作的类型
	 * @param pagination
	 *            分页
	 * @param <T>
	 *            泛型对象
	 * @param sqlId
	 *            SQLID数组，第一个参数三业务逻辑查询sql，第二个参数三查询总数sql
	 * @return List类型的查询结果
	 */
	<T> PaginationResult<List<T>> queryForList(Object param, Class<T> requiredType, Pagination pagination,
			String... sqlId);

	/**
	 * 获取分页处理结果 重载方法
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param param
	 *            查询参数
	 * @param rowMapper
	 *            翻页处理规则
	 * @param pagination
	 *            分页
	 * @param <T>
	 *            泛型对象
	 * @return List类型的查询结果
	 */
	<T> PaginationResult<List<T>> queryForList(String sqlId, Object param, RowMapper<T> rowMapper,
			Pagination pagination);

	/**
	 * 获取分页处理结果 重载方法
	 * 
	 * @param param
	 *            查询参数
	 * @param rowMapper
	 *            翻页处理规则
	 * @param pagination
	 *            分页
	 * @param <T>
	 *            泛型对象
	 * @param sqlId
	 *            SQLID数组，第一个参数三业务逻辑查询sql，第二个参数三查询总数sql
	 * @return List类型的查询结果
	 */
	<T> PaginationResult<List<T>> queryForList(Object param, RowMapper<T> rowMapper, Pagination pagination,
			String... sqlId);

	/**
	 * 获取分页处理结果 重载方法
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param paramMap
	 *            查询参数
	 * @param rowMapper
	 *            翻页处理规则
	 * @param pagination
	 *            分页
	 * @param <T>
	 *            泛型对象
	 * @return List类型的查询结果
	 */
	<T> PaginationResult<List<T>> queryForList(String sqlId, Map<String, Object> paramMap, RowMapper<T> rowMapper,
			Pagination pagination);

	/**
	 * 获取分页处理结果 重载方法
	 * 
	 * @param paramMap
	 *            查询参数
	 * @param rowMapper
	 *            翻页处理规则
	 * @param pagination
	 *            分页
	 * @param <T>
	 *            泛型对象
	 * @param sqlId
	 *            SQLID数组，第一个参数三业务逻辑查询sql，第二个参数三查询总数sql
	 * @return List类型的查询结果
	 */
	<T> PaginationResult<List<T>> queryForList(Map<String, Object> paramMap, RowMapper<T> rowMapper,
			Pagination pagination, String... sqlId);

	/**
	 * 随机取
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param param
	 *            查询参数
	 * @param num
	 *            随机数
	 * @return List类型的查询结果
	 */
	List<Map<String, Object>> queryForList(String sqlId, Object param, int num);

	/**
	 * 随机取
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param paramMap
	 *            查询参数
	 * @param num
	 *            随机数
	 * @return List类型的查询结果
	 */
	List<Map<String, Object>> queryForList(String sqlId, Map<String, Object> paramMap, int num);

	/**
	 * 随机取
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param paramMap
	 *            查询参数
	 * @param requiredType
	 *            需要操作的类型
	 * @param num
	 *            随机数
	 * @param <T>
	 *            泛型对象
	 * @return List类型的查询结果
	 */
	<T> List<T> queryForList(String sqlId, Map<String, Object> paramMap, Class<T> requiredType, int num);

	/**
	 * 随机取
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param param
	 *            查询参数
	 * @param requiredType
	 *            需要操作的类型
	 * @param num
	 *            随机数
	 * @param <T>
	 *            泛型对象
	 * @return List类型的查询结果
	 */
	<T> List<T> queryForList(String sqlId, Object param, Class<T> requiredType, int num);

	/**
	 * 随机取
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param param
	 *            查询参数
	 * @param rowMapper
	 *            翻页处理规则
	 * @param num
	 *            随机数
	 * @param <T>
	 *            泛型对象
	 * @return List类型的查询结果
	 */
	<T> List<T> queryForList(String sqlId, Object param, RowMapper<T> rowMapper, int num);

	/**
	 * 随机取
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param paramMap
	 *            查询参数
	 * @param rowMapper
	 *            翻页处理规则
	 * @param num
	 *            随机数
	 * @param <T>
	 *            泛型对象
	 * @return List类型的查询结果
	 */
	<T> List<T> queryForList(String sqlId, Map<String, Object> paramMap, RowMapper<T> rowMapper, int num);

}