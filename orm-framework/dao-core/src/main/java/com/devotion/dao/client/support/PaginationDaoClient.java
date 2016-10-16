package com.devotion.dao.client.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.devotion.dao.utils.DaoUtils;
import com.devotion.dao.client.IPaginationDaoClient;
import com.devotion.dao.constants.ExceptionType;
import com.devotion.dao.dialect.Dialect;
import com.devotion.dao.dialect.DialectFactory;
import com.devotion.dao.exception.BaseException;
import com.devotion.dao.pagination.Pagination;
import com.devotion.dao.pagination.PaginationResult;
import com.devotion.dao.resource.parse.SqlBean;
import com.devotion.dao.support.rowmapper.RowMapperFactory;
import com.devotion.dao.support.sql.FreeMakerParser;

/**
 * 功能描述： 带分页的客户端
 */
public class PaginationDaoClient extends DefaultDaoClient implements IPaginationDaoClient {

	/**
	 * 指定记录数
	 */
	private final String LIMIT = "_limit";

	/**
	 * 查询并返回分页结果，pagesize为负数时，返回所有记录
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param paramMap
	 *            查询参数
	 * @param pagination
	 *            分页
	 * @return 分页查询结果
	 */
	@Override
	public PaginationResult<List<Map<String, Object>>> queryForList(String sqlId, Map<String, Object> paramMap,
			Pagination pagination) {
		processTableRoute(paramMap);
		SqlBean sqlBean = getSQL(sqlId);
		String sql = FreeMakerParser.process(paramMap, sqlBean.getContent(), sqlId);
		NamedParameterJdbcTemplate template = getJdbcTemplate(paramMap, sqlBean);
		/** 装配分页信息 */
		if (paramMap == null) {
			paramMap = new HashMap<String, Object>();
		}
		/** 当pagesize为负数时 ,查询该表中的所有记录 */
		List<Map<String, Object>> list = null;
		if (pagination.getPageSize() < 0) {
			list = queryForList(sqlId, paramMap);
			pagination.setTotalRows(list.size());
		} else {
			paramMap.put(LIMIT, pagination.getPageSize());
			paramMap.put("_offset", pagination.getFirstRowIndex());

			this.configurePagination(template, sql, paramMap, pagination, sqlBean.getDbType());
			list = template.queryForList(generatePaginationSql(sql, sqlBean.getDbType(), pagination),
					resetPaginationParams(paramMap, pagination, sqlBean.getDbType()));
		}
		/** 执行分页查询 */
		return new PaginationResult<List<Map<String, Object>>>(list, pagination);
	}

	/**
	 * 查询并返回分页结果，pagesize为负数时，返回所有记录
	 * 
	 * @param paramMap
	 *            查询参数
	 * @param pagination
	 *            分页
	 * @param sqlId
	 *            SQLID数组，第一个参数三业务逻辑查询sql，第二个参数三查询总数sql
	 * @return 分页查询结果
	 */
	public PaginationResult<List<Map<String, Object>>> queryForList(Map<String, Object> paramMap, Pagination pagination,
			String... sqlId) {
		if (sqlId.length == 2) {
			processTableRoute(paramMap);

			/** 装配分页信息 */
			if (paramMap == null) {
				paramMap = new HashMap<String, Object>();
			}
			/** 当pagesize为负数时 ,查询该表中的所有记录 */
			List<Map<String, Object>> list = null;
			if (pagination.getPageSize() < 0) {
				list = queryForList(sqlId[0], paramMap);
				pagination.setTotalRows(list.size());
			} else {
				SqlBean sqlBeanTotalrows = getSQL(sqlId[1]);
				String sqlTotalrows = FreeMakerParser.process(paramMap, sqlBeanTotalrows.getContent(), sqlId[1]);
				NamedParameterJdbcTemplate templateTotalrows = getJdbcTemplate(paramMap, sqlBeanTotalrows);
				paramMap.put(LIMIT, pagination.getPageSize());
				paramMap.put("_offset", pagination.getFirstRowIndex());
				this.configurePaginationTotalrows(templateTotalrows, sqlTotalrows, paramMap, pagination,
						sqlBeanTotalrows.getDbType());

				SqlBean sqlBean = getSQL(sqlId[0]);
				String sql = FreeMakerParser.process(paramMap, sqlBean.getContent(), sqlId[0]);
				NamedParameterJdbcTemplate template = getJdbcTemplate(paramMap, sqlBean);
				list = template.queryForList(generatePaginationSql(sql, sqlBean.getDbType(), pagination),
						resetPaginationParams(paramMap, pagination, sqlBean.getDbType()));
			}
			/** 执行分页查询 */
			return new PaginationResult<List<Map<String, Object>>>(list, pagination);
		} else {
			return this.queryForList(sqlId[0], paramMap, pagination);
		}

	}

	/**
	 * 查询并返回分页结果 重载方法
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param param
	 *            查询参数
	 * @param pagination
	 *            分页
	 * @return 分页查询结果
	 */
	@Override
	public PaginationResult<List<Map<String, Object>>> queryForList(String sqlId, Object param, Pagination pagination) {
		return queryForList(sqlId, DaoUtils.convertToMap(param), pagination);
	}

	/**
	 * 查询并返回分页结果 重载方法
	 * 
	 * @param param
	 *            查询参数
	 * @param pagination
	 *            分页
	 * @param sqlId
	 *            SQLID，第一个参数三业务逻辑查询sql，第二个参数三查询总数sql
	 * @return 分页查询结果
	 */
	public PaginationResult<List<Map<String, Object>>> queryForList(Object param, Pagination pagination,
			String... sqlId) {
		if (sqlId.length == 2) {
			return queryForList(DaoUtils.convertToMap(param), pagination, sqlId);
		} else {
			return this.queryForList(sqlId[0], DaoUtils.convertToMap(param), pagination);
		}
	}

	/**
	 * 查询并返回分页结果
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
	 * @return 分页查询结果
	 */

	@Override
	public <T> PaginationResult<List<T>> queryForList(String sqlId, Map<String, Object> paramMap,
			RowMapper<T> rowMapper, Pagination pagination) {

		processTableRoute(paramMap);
		SqlBean sqlBean = getSQL(sqlId);
		String sql = FreeMakerParser.process(paramMap, sqlBean.getContent(), sqlId);
		NamedParameterJdbcTemplate template = getJdbcTemplate(paramMap, sqlBean);
		/** 装配分页信息 */
		if (paramMap == null) {
			paramMap = new HashMap<String, Object>();
		}
		List<T> list = null;
		/** 当pagesize为负数时 ,查询该表中的所有记录 */
		if (pagination.getPageSize() < 0) {
			list = queryForList(sqlId, paramMap, rowMapper);
			pagination.setTotalRows(list.size());
		} else {
			paramMap.put(LIMIT, pagination.getPageSize());
			paramMap.put("_offset", pagination.getFirstRowIndex());

			/** 获取数据总数 */
			this.configurePagination(template, sql, paramMap, pagination, sqlBean.getDbType());
			list = template.query(generatePaginationSql(sql, sqlBean.getDbType(), pagination),
					resetPaginationParams(paramMap, pagination, sqlBean.getDbType()), rowMapper);

		}
		/** 执行分页查询 */
		return new PaginationResult<List<T>>(list, pagination);
	}

	/**
	 * 查询并返回分页结果
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
	 *            SQLID，第一个参数三业务逻辑查询sql，第二个参数三查询总数sql
	 * @return 分页查询结果
	 */
	public <T> PaginationResult<List<T>> queryForList(Map<String, Object> paramMap, RowMapper<T> rowMapper,
			Pagination pagination, String... sqlId) {
		if (sqlId.length == 2) {
			processTableRoute(paramMap);

			/** 装配分页信息 */
			if (paramMap == null) {
				paramMap = new HashMap<String, Object>();
			}
			List<T> list = null;
			/** 当pagesize为负数时 ,查询该表中的所有记录 */
			if (pagination.getPageSize() < 0) {
				list = queryForList(sqlId[0], paramMap, rowMapper);
				pagination.setTotalRows(list.size());
			} else {
				SqlBean sqlBeanTotalrows = getSQL(sqlId[1]);
				String sqlTotalrows = FreeMakerParser.process(paramMap, sqlBeanTotalrows.getContent(), sqlId[1]);
				NamedParameterJdbcTemplate templateTotalrows = getJdbcTemplate(paramMap, sqlBeanTotalrows);
				paramMap.put(LIMIT, pagination.getPageSize());
				paramMap.put("_offset", pagination.getFirstRowIndex());

				this.configurePaginationTotalrows(templateTotalrows, sqlTotalrows, paramMap, pagination,
						sqlBeanTotalrows.getDbType());

				SqlBean sqlBean = getSQL(sqlId[0]);
				String sql = FreeMakerParser.process(paramMap, sqlBean.getContent(), sqlId[0]);
				NamedParameterJdbcTemplate template = getJdbcTemplate(paramMap, sqlBean);
				list = template.query(generatePaginationSql(sql, sqlBean.getDbType(), pagination),
						resetPaginationParams(paramMap, pagination, sqlBean.getDbType()), rowMapper);
			}
			/** 执行分页查询 */
			return new PaginationResult<List<T>>(list, pagination);
		} else {
			return this.queryForList(sqlId[0], paramMap, rowMapper, pagination);
		}
	}

	/**
	 * 查询并返回分页结果 重载方法
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
	 * @return 分页查询结果
	 */
	@Override
	public <T> PaginationResult<List<T>> queryForList(String sqlId, Object param, RowMapper<T> rowMapper,
			Pagination pagination) {
		return queryForList(sqlId, DaoUtils.convertToMap(param), rowMapper, pagination);
	}

	/**
	 * 查询并返回分页结果 重载方法
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
	 *            SQLID，第一个参数三业务逻辑查询sql，第二个参数三查询总数sql
	 * @return 分页查询结果
	 */
	public <T> PaginationResult<List<T>> queryForList(Object param, RowMapper<T> rowMapper, Pagination pagination,
			String... sqlId) {
		if (sqlId.length == 2) {
			return queryForList(DaoUtils.convertToMap(param), rowMapper, pagination, sqlId);
		} else {
			return queryForList(sqlId[0], DaoUtils.convertToMap(param), rowMapper, pagination);
		}

	}

	/**
	 * 查询并返回分页结果 重载方法
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
	 * @return 分页查询结果
	 */
	@Override
	public <T> PaginationResult<List<T>> queryForList(String sqlId, Map<String, Object> paramMap, Class<T> requiredType,
			Pagination pagination) {
		return this.queryForList(sqlId, paramMap, new RowMapperFactory<T>(requiredType).getRowMapper(), pagination);
	}

	/**
	 * 查询并返回分页结果 重载方法
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
	 *            SQLID，第一个参数三业务逻辑查询sql，第二个参数三查询总数sql
	 * @return 分页查询结果
	 */
	public <T> PaginationResult<List<T>> queryForList(Map<String, Object> paramMap, Class<T> requiredType,
			Pagination pagination, String... sqlId) {
		if (sqlId.length == 2) {
			return queryForList(paramMap, new RowMapperFactory<T>(requiredType).getRowMapper(), pagination, sqlId);
		} else {
			return this.queryForList(sqlId[0], paramMap, new RowMapperFactory<T>(requiredType).getRowMapper(),
					pagination);
		}

	}

	/**
	 * 查询并返回分页结果 重载方法
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
	 * @return 分页查询结果
	 */
	@Override
	public <T> PaginationResult<List<T>> queryForList(String sqlId, Object param, Class<T> requiredType,
			Pagination pagination) {
		return queryForList(sqlId, DaoUtils.convertToMap(param), requiredType, pagination);
	}

	/**
	 * 查询并返回分页结果 重载方法
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
	 *            SQLID，第一个参数三业务逻辑查询sql，第二个参数三查询总数sql
	 * @return 分页查询结果
	 */
	public <T> PaginationResult<List<T>> queryForList(Object param, Class<T> requiredType, Pagination pagination,
			String... sqlId) {
		if (sqlId.length == 2) {
			return queryForList(DaoUtils.convertToMap(param), requiredType, pagination, sqlId);
		} else {
			return queryForList(sqlId[0], DaoUtils.convertToMap(param), requiredType, pagination);
		}

	}

	/**
	 * 查询并返回结果集，num为负数时，返回所有记录
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param paramMap
	 *            查询参数
	 * @param num
	 *            随机数
	 * @return 查询结果
	 */
	@Override
	public List<Map<String, Object>> queryForList(String sqlId, Map<String, Object> paramMap, int num) {
		processTableRoute(paramMap);
		SqlBean sqlBean = getSQL(sqlId);
		String sql = FreeMakerParser.process(paramMap, sqlBean.getContent(), sqlId);
		NamedParameterJdbcTemplate template = getJdbcTemplate(paramMap, sqlBean);
		/** 装配分页信息 */
		if (paramMap == null) {
			paramMap = new HashMap<String, Object>();
		}

		List<Map<String, Object>> list;
		if (num < 0) {
			list = queryForList(sqlId, paramMap);
		} else {
			paramMap.put(LIMIT, num);
			list = template.queryForList(generatePaginationSqlForRandom(sql, sqlBean.getDbType()), paramMap);
		}
		/** 执行分页查询 */
		return list;
	}

	/**
	 * 查询并返回结果集 重载方法
	 * 
	 * @param sqlId
	 *            SQLID
	 * @param param
	 *            查询参数
	 * @param num
	 *            随机数
	 * @return 查询结果
	 */
	@Override
	public List<Map<String, Object>> queryForList(String sqlId, Object param, int num) {
		return queryForList(sqlId, DaoUtils.convertToMap(param), num);
	}

	/**
	 * 查询并返回结果集
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
	 * @return 查询结果
	 */
	@Override
	public <T> List<T> queryForList(String sqlId, Map<String, Object> paramMap, RowMapper<T> rowMapper, int num) {
		processTableRoute(paramMap);
		SqlBean sqlBean = getSQL(sqlId);
		String sql = FreeMakerParser.process(paramMap, sqlBean.getContent(), sqlId);
		NamedParameterJdbcTemplate template = getJdbcTemplate(paramMap, sqlBean);
		/** 装配分页信息 */
		if (paramMap == null) {
			paramMap = new HashMap<String, Object>();
		}
		List<T> list;
		if (num < 0) {
			list = queryForList(sqlId, paramMap, rowMapper);
		} else {
			paramMap.put(LIMIT, num);
			list = template.query(generatePaginationSqlForRandom(sql, sqlBean.getDbType()), paramMap, rowMapper);
		}
		return list;
	}

	/**
	 * 查询并返回结果集 重载方法
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
	 * @return 查询结果
	 */
	@Override
	public <T> List<T> queryForList(String sqlId, Object param, RowMapper<T> rowMapper, int num) {
		return queryForList(sqlId, DaoUtils.convertToMap(param), rowMapper, num);
	}

	/**
	 * 查询并返回结果集 重载方法
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
	 * @return 查询结果
	 */
	@Override
	public <T> List<T> queryForList(String sqlId, Map<String, Object> paramMap, Class<T> requiredType, int num) {
		return queryForList(sqlId, paramMap, new RowMapperFactory<T>(requiredType).getRowMapper(), num);
	}

	/**
	 * 查询并返回结果集 重载方法
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
	 * @return 查询结果
	 */
	@Override
	public <T> List<T> queryForList(String sqlId, Object param, Class<T> requiredType, int num) {
		return queryForList(sqlId, DaoUtils.convertToMap(param), requiredType, num);
	}

	/**
	 * 装配分页信息
	 * 
	 * @param template
	 *            模板
	 * @param sql
	 *            SQL语句
	 * @param paramMap
	 *            查询参数
	 * @param pagination
	 *            分页
	 * @param dbType
	 *            数据源类型
	 */
	private void configurePagination(NamedParameterJdbcTemplate template, String sql, Map<String, Object> paramMap,
			Pagination pagination, String dbType) {
		if (pagination.getTotalRows() == 0 || pagination.getTotalRows() == -1) {
			/** 获取数据总数 */
			int totalRows = template.queryForObject(generateCountSql(sql, dbType), DaoUtils.mapIfNull(paramMap),
					Integer.class);
			pagination.setTotalRows(totalRows);
		}
	}

	private void configurePaginationTotalrows(NamedParameterJdbcTemplate template, String sql,
			Map<String, Object> paramMap, Pagination pagination, String dbType) {
		if (pagination.getTotalRows() == 0 || pagination.getTotalRows() == -1) {
			/** 获取数据总数 */
			int totalRows = template.queryForObject(sql, DaoUtils.mapIfNull(paramMap), Integer.class);
			pagination.setTotalRows(totalRows);
		}
	}

	/**
	 * 查询指定SQL的总记录数
	 * 
	 * @param sql
	 *            SQL语句
	 * @param dbType
	 *            数据源类型
	 * @return 统计SQL串
	 */
	private String generateCountSql(String sql, String dbType) {
		Dialect dialect = dialectFactory.getDBDialect(dbType);
		if (dialect == null) {
			throw new BaseException(ExceptionType.EXCEPTION_DAO.getCode(), null, new Object[] { dbType },
					ExceptionType.EXCEPTION_DAO);
		}
		return dialect.getCountString(sql);
	}

	/**
	 * 
	 * @param paramMap
	 * @param pagination
	 * @param dbType
	 * @return
	 */
	private Map<String, Object> resetPaginationParams(Map<String, Object> paramMap, Pagination pagination,
			String dbType) {

		if ("db2".equals(dbType.toLowerCase())) {
			int db2Limit = pagination.getPageSize() * pagination.getCurrentPage();
			int db2Offset = pagination.getPageSize() * (pagination.getCurrentPage() - 1);
			paramMap.put(LIMIT, db2Limit);
			paramMap.put("_offset", db2Offset);
			/*
			 * System.out.println("=================");
			 * System.out.println("db2Limit:" + db2Limit);
			 * System.out.println("_offset:" + db2Offset);
			 * System.out.println("=================");
			 */
		}

		return paramMap;
	}

	/**
	 * 生成分页sql，查询指定位置、指定行数的记录
	 * 
	 * @param sql
	 *            SQL语句
	 * @param dbType
	 *            数据源类型
	 * @param pagination
	 *            分页信息
	 * @return 分页SQL串
	 */
	// add Pagination pagination by guohuac@cn.ibm.com 20151117
	private String generatePaginationSql(String sql, String dbType, Pagination pagination) {
		Dialect dialect = dialectFactory.getDBDialect(dbType);
		if (dialect == null) {
			throw new BaseException(ExceptionType.EXCEPTION_DAO.getCode(), null, new Object[] { dbType },
					ExceptionType.EXCEPTION_DAO);
		}

		String paginationSql = dialect.getLimitString(sql);
		// System.out.println("paginationSql:"+paginationSql);

		if ("db2".equals(dbType.toLowerCase())) {
			int db2Limit = pagination.getPageSize() * pagination.getCurrentPage();
			// int db2Offset = pagination.getPageSize() *
			// (pagination.getCurrentPage() - 1);

			/*
			 * System.out.println("=================");
			 * System.out.println("db2Limit:" + db2Limit);
			 * System.out.println("_offset:" + db2Offset);
			 * System.out.println("=================");
			 */

			paginationSql = paginationSql.replaceAll(":_limit", String.valueOf(db2Limit));
		}
		return paginationSql;
		// return dialect.getLimitString(sql);
	}

	/**
	 * 生成分页sql，查询前几行记录
	 * 
	 * @param sql
	 *            SQL语句
	 * @param dbType
	 *            数据源类型
	 * @return 分页SQL串
	 */
	private String generatePaginationSqlForRandom(String sql, String dbType) {
		Dialect dialect = dialectFactory.getDBDialect(dbType);
		if (dialect == null) {
			throw new BaseException(ExceptionType.EXCEPTION_DAO.getCode(), null, new Object[] { dbType },
					ExceptionType.EXCEPTION_DAO);
		}
		return dialect.getLimitStringForRandom(sql);
	}

	/**
	 * 数据库方言工厂
	 */
	@Autowired
	private DialectFactory dialectFactory;
}
