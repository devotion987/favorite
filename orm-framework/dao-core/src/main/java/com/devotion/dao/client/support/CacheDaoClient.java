package com.devotion.dao.client.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.RowMapper;

import com.devotion.dao.cache.Cache;
import com.devotion.dao.cache.CacheException;
import com.devotion.dao.cache.CacheFactory;
import com.devotion.dao.pagination.Pagination;
import com.devotion.dao.pagination.PaginationResult;

/**
 * 功能描述： 带缓存的客户端
 */
public class CacheDaoClient extends PaginationDaoClient {

    /**
     * 缓存配置文件
     */
    private Resource[] cacheRecs = getResources();

    /**
     * 要使用的缓存实例id
     */
    private String cacheID;

    /**
     * 缓存
     */
    private Cache cache;

    /**
     * 缓存初始化
     *
     * @throws Exception 异常
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        CacheFactory cFac = CacheFactory.getInstance();
        for (Resource r : cacheRecs) {
            InputStream in;
            try {
                in = r.getInputStream();
            } catch (IOException e) {
                throw new CacheException(e.getMessage(), e);
            }
            if (null != in) {
                cFac.loadConfig(in);
                try {
                    in.close();
                } catch (IOException e) {
                    throw new CacheException(e.getMessage(), e);
                }
            }
        }
        cache = cFac.getCache(cacheID);
    }

    /**
     * 根据sqlId进行cache查询，返回单个对象
     *
     * @param entityClass 实体类
     * @param entity      实体对象
     * @param <T>         泛型对象
     * @return 查询结果
     */
    @Override
    public <T> T find(final Class<T> entityClass, final Object entity) {
        String key = generateKey(entity);
        return cacheHandle((() -> CacheDaoClient.super.find(entityClass, entity)), key);
//        return this.cacheHandle(new CallBack<T>() {
//            @Override
//            public T invoke() {
//                return CacheDaoClient.super.find(entityClass, entity);
//            }
//        }, key);
    }

    /**
     * 根据sqlId进行cache查询，返回单个对象
     *
     * @param sqlId     SQLID
     * @param paramMap  查询参数
     * @param rowMapper 翻页处理规则
     * @param <T>       泛型对象
     * @return 查询结果
     */
    @Override
    public <T> T queryForObject(final String sqlId, final Map<String, Object> paramMap, final RowMapper<T> rowMapper) {
        String key = generateKey(sqlId, paramMap);
        return cacheHandle((() -> CacheDaoClient.super.queryForObject(sqlId, paramMap, rowMapper)), key);
//        return this.cacheHandle(new CallBack<T>() {
//            @Override
//            public T invoke() {
//                return CacheDaoClient.super.queryForObject(sqlId, paramMap, rowMapper);
//            }
//        }, key);
    }

    /**
     * 根据sqlId查询多个对象，返回map类型查询结果集
     *
     * @param sqlId    sqlId
     * @param paramMap 查询参数
     * @return 查询结果
     */
    @Override
    public Map<String, Object> queryForMap(final String sqlId, final Map<String, Object> paramMap) {
        String key = generateKey(sqlId, paramMap);
        return cacheHandle((() -> CacheDaoClient.super.queryForMap(sqlId, paramMap)), key);
//        return this.cacheHandle(new CallBack<Map<String, Object>>() {
//            @Override
//            public Map<String, Object> invoke() {
//                return CacheDaoClient.super.queryForMap(sqlId, paramMap);
//            }
//        }, key);
    }

    /**
     * 根据sqlId查询多个对象，返回list类型查询结果集
     *
     * @param sqlId     sqlId
     * @param paramMap  查询参数
     * @param rowMapper 翻页处理规则
     * @param <T>       泛型对象
     * @return 查询结果
     */
    @Override
    public <T> List<T> queryForList(final String sqlId, final Map<String, Object> paramMap,
                                    final RowMapper<T> rowMapper) {
        String key = generateKey(sqlId, paramMap);
        return cacheHandle((() -> CacheDaoClient.super.queryForList(sqlId, paramMap, rowMapper)), key);
//        return this.cacheHandle(new CallBack<List<T>>() {
//            @Override
//            public List<T> invoke() {
//                return CacheDaoClient.super.queryForList(sqlId, paramMap, rowMapper);
//            }
//        }, key);
    }

    /**
     * 根据sqlId查询多个对象，返回list<map<>>类型查询结果集
     *
     * @param sqlId    SQLID
     * @param paramMap 查询参数
     * @return 查询结果
     */
    @Override
    public List<Map<String, Object>> queryForList(final String sqlId, final Map<String, Object> paramMap) {
        String key = generateKey(sqlId, paramMap);
        return cacheHandle((() -> CacheDaoClient.super.queryForList(sqlId, paramMap)), key);
//        return this.cacheHandle(new CallBack<List<Map<String, Object>>>() {
//            @Override
//            public List<Map<String, Object>> invoke() {
//                return CacheDaoClient.super.queryForList(sqlId, paramMap);
//            }
//        }, key);
    }

    /**
     * 根据sqlId查询多个对象，返回分页结果集
     *
     * @param sqlId      sqlId
     * @param paramMap   查询参数
     * @param pagination 分页
     * @return 分页查询结果
     */
    @Override
    public PaginationResult<List<Map<String, Object>>> queryForList(final String sqlId,
                                                                    final Map<String, Object> paramMap, final Pagination pagination) {
        String key = generateKey(sqlId, paramMap, pagination);
        return cacheHandle((() -> CacheDaoClient.super.queryForList(sqlId, paramMap, pagination)), key);
//        return this.cacheHandle(new CallBack<PaginationResult<List<Map<String, Object>>>>() {
//            @Override
//            public PaginationResult<List<Map<String, Object>>> invoke() {
//                return CacheDaoClient.super.queryForList(sqlId, paramMap, pagination);
//            }
//        }, key);
    }

    /**
     * 根据sqlId查询多个对象，返回分页结果集
     *
     * @param sqlId      SQLID
     * @param paramMap   查询参数
     * @param rowMapper  翻页处理规则
     * @param pagination 分页
     * @param <T>        泛型对象
     * @return 分页查询结果
     */
    @Override
    public <T> PaginationResult<List<T>> queryForList(final String sqlId, final Map<String, Object> paramMap,
                                                      final RowMapper<T> rowMapper, final Pagination pagination) {
        String key = generateKey(sqlId, paramMap, pagination);
        return cacheHandle((() -> CacheDaoClient.super.queryForList(sqlId, paramMap, rowMapper, pagination)), key);
//        return this.cacheHandle(new CallBack<PaginationResult<List<T>>>() {
//            @Override
//            public PaginationResult<List<T>> invoke() {
//                return CacheDaoClient.super.queryForList(sqlId, paramMap, rowMapper, pagination);
//            }
//        }, key);
    }

    /**
     * 生成MD5码
     *
     * @param o 对象
     * @return MD5串
     */
    private String generateKey(Object... o) {
        int hashCode = 0;
        for (Object t : o) {
            if (t instanceof Map) {
                hashCode = hashCode + t.hashCode();
            } else {
                hashCode = hashCode + HashCodeBuilder.reflectionHashCode(t);
            }
        }
        return DigestUtils.md5Hex(String.valueOf(hashCode));
    }

    /**
     * 获取CacheID
     *
     * @return 缓存ID
     */
    public String getCacheID() {
        return cacheID;
    }

    /**
     * 设置CacheID
     *
     * @param cacheID 缓存ID
     */
    public void setCacheID(String cacheID) {
        this.cacheID = cacheID;
    }

    /**
     * 缓存句柄
     *
     * @param callBack 回调
     * @param key      键值
     * @param <R>      泛型对象
     * @return 回调结果
     */
    @SuppressWarnings("unchecked")
    private <R> R cacheHandle(CallBack<R> callBack, String key) {
        Object o = cache.get(key);
        if (o == null) {
            R r = callBack.invoke();
            if (r != null) {
                cache.put(key, r);
            }
            return r;
        }
        return (R) o;
    }

    /**
     * 功能描述：回调
     *
     * @param <T> 回调结果类型
     * @author www.ibm.com
     * @version 1.0.0
     */
    private interface CallBack<T> {
        /**
         * 回调
         *
         * @return 回调结果
         */
        T invoke();
    }
}
