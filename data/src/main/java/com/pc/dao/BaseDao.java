package com.pc.dao;

import com.pc.core.DataSource;
import com.pc.core.DataSourceHolder;
import com.pc.core.MapResultHandler;
import com.pc.core.Page;
import com.pc.core.ParamsMap;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Description: 基本Dao 操作定义,,,第一个参数为表名,第二个参数为传入的条件(Map),顺序不可逆
 * Author: 潘锐 (2017-03-28 14:35)
 * version: \$Rev: 1925 $
 * UpdateAuthor: \$Author: panrui $
 * UpdateDateTime: \$Date: 2017-05-05 21:43:07 +0800 (周五, 05 5月 2017) $
 */
@Repository
@CacheConfig(cacheNames = "qCache", cacheResolver = "baseImpl")
public class BaseDao {
    private SqlSessionTemplate sqlSessionTemplate;
    private String className;

    @Autowired
    public BaseDao(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
        this.className = this.getClass().getName();
    }

    public Connection getConnection() {
        return sqlSessionTemplate.getConnection();
    }

    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    @DataSource
    public List<Map<String, Object>> queryListInTab(String tableName, Map<String, Object> params, Map<String, Object> orderMap) {
        Map<String, Object> paramsMap = ParamsMap.newMap("tableName", tableName).addParams("params", params).addParams("orderMap", orderMap);
        return sqlSessionTemplate.selectList(className + ".queryListInTab", paramsMap);
    }

    //    @Cacheable(keyGenerator = "myKeyGenerator")
    @DataSource
    public List<Map<String, Object>> queryByProsInTab(String tableName, Map<String, Object> params) {
        Map<String, Object> paramsMap = ParamsMap.newMap("tableName", tableName).addParams("params", params);
        return sqlSessionTemplate.selectList(className + ".queryByProsInTab", paramsMap);
    }

    @DataSource
    @Cacheable(key = "(#tableName).replaceFirst('\\.','-')+'$'+#id")
    public Map<String, Object> queryByIdInTab(String tableName, String id) {
        Map<String, Object> paramsMap = ParamsMap.newMap("tableName", tableName).addParams("id", id);
        return sqlSessionTemplate.selectOne(className + ".queryByIdInTab", paramsMap);
    }

    @DataSource
    @Cacheable(key = "T(java.lang.String).valueOf(#p0).replaceFirst('\\.','-')")
    public List<Map<String, Object>> queryAllInTab(String tableName) {
        Map<String, Object> paramsMap = ParamsMap.newMap("tableName", tableName);
        return sqlSessionTemplate.selectList(className + ".queryAllInTab", paramsMap);
    }

    @DataSource
    public List<Map<String, Object>> queryAllOnSortInTab(String tableName, Map<String, Object> orderMap) {
        Map<String, Object> paramsMap = ParamsMap.newMap("tableName", tableName).addParams("orderMap", orderMap);
        return sqlSessionTemplate.selectList(className + ".queryAllOnSortInTab", paramsMap);
    }

    @CacheEvict(keyGenerator = "myKeyGenerator", cacheManager = "cacheManager")
    @DataSource(DataSourceHolder.DBType.master)
    public int updateByProsInTab(String tableName, Map<String, Object> params) {
        Map<String, Object> paramsMap = ParamsMap.newMap("tableName", tableName).addParams("params", params);
        return sqlSessionTemplate.update(className + ".updateByProsInTab", paramsMap);
    }

    @CacheEvict(keyGenerator = "myKeyGenerator", cacheManager = "cacheManager")
    @DataSource(DataSourceHolder.DBType.master)
    public int deleteByProsInTab(String tableName, Map<String, Object> params) {
        Map<String, Object> paramsMap = ParamsMap.newMap("tableName", tableName).addParams("params", params);
        return sqlSessionTemplate.delete(className + ".deleteByProsInTab", paramsMap);
    }

    @CacheEvict(key = "T(java.lang.String).valueOf(#p0).replaceFirst('\\.','-')", cacheManager = "cacheManager")
    @DataSource(DataSourceHolder.DBType.master)
    public int insertByProsInTab(String tableName, Map<String, Object> params) {
        Map<String, Object> paramsMap = ParamsMap.newMap("tableName", tableName).addParams("params", params);
        return sqlSessionTemplate.insert(className + ".insertByProsInTab", paramsMap);
    }

    @CacheEvict(key = "T(java.lang.String).valueOf(#p0).replaceFirst('\\.','-')", cacheManager = "cacheManager")
    @DataSource(DataSourceHolder.DBType.master)
    public int insertBatchByProsInTab(String tableName, List<Map<String, Object>> dataList) {
        Map<String, Object> paramsMap = ParamsMap.newMap("tableName", tableName).addParams("dataList", dataList);
        return sqlSessionTemplate.insert(className + ".insertBatchByProsInTab", paramsMap);
    }

    @CacheEvict(key = "T(java.lang.String).valueOf(#p0).replaceFirst('\\.','-')", cacheManager = "cacheManager")
    @DataSource(DataSourceHolder.DBType.master)
    public int insertIgnoreByProsInTab(String tableName, Map<String, Object> params) {
        Map<String, Object> paramsMap = ParamsMap.newMap("tableName", tableName).addParams("params", params);
        return sqlSessionTemplate.insert(className + ".insertByProsInTab", paramsMap);
    }

    @DataSource
    public List<Map<String, Object>> queryPageInTab(String tableName, final Page page) {
        Map<String, Object> paramsMap = ParamsMap.newMap("tableName", tableName).addParams("page", page);
        List<Map<String, Object>> resultList = sqlSessionTemplate.selectList(className + ".queryPageInTab", paramsMap);
        page.setResults(resultList);
        return resultList;
    }

    @DataSource
    public List<Map<String, Object>> queryBySql(String dynSql) {
        MapResultHandler mapResultHandler = new MapResultHandler();
        sqlSessionTemplate.select(className + ".queryBySql", ParamsMap.newMap("dynSql", dynSql), mapResultHandler);
        return mapResultHandler.getMappedResults();
    }

    @DataSource
    public List<Map<String, Object>> queryByS(String dynSql) {
        return sqlSessionTemplate.selectList(className + ".queryByS", ParamsMap.newMap("dynSql", dynSql));
    }

    @DataSource(DataSourceHolder.DBType.master)
    public void ddlBySql(String dynSql) {
        sqlSessionTemplate.insert(className + ".ddlBySql", ParamsMap.newMap("dynSql", dynSql));
    }

}
