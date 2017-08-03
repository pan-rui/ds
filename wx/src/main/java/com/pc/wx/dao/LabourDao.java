package com.pc.wx.dao;

/**
 * @Description: ${Description}
 * @Author: 潘锐 (2017-04-18 14:35)
 * @version: \$Rev: 3919 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-08-03 11:20:11 +0800 (周四, 03 8月 2017) $
 */

import com.pc.core.ParamsMap;
import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
@CacheConfig(cacheNames = "qCache", cacheManager = "cacheManager")
public class LabourDao {
    private SqlSessionTemplate sqlSessionTemplate;
    private String className;

    @Autowired
    public LabourDao(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
        this.className = this.getClass().getName();
    }

    public Connection getConnection() {
        return sqlSessionTemplate.getConnection();
    }

    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    @Cacheable(value = "qCache",key = "'labour_'+#projectId+'_'+#userId")
    public Map<String,Object> queryUserInfoMul(String ddBB,String userId,String tenantId,String projectId) {
        Map<String, Object> paramsMap = ParamsMap.newMap("ddBB", StringUtils.isEmpty(ddBB) ? "dems" : ddBB).addParams("userId", userId).addParams("tenantId", tenantId).addParams("projectId", projectId);
        return sqlSessionTemplate.selectOne(className + ".queryUserInfoMul",paramsMap);
    }

    public List<Map<String,Object>> queryCheckWork(String ddBB,String openId,String tenantId,String projectCode,String month) {
        Map<String, Object> paramsMap = ParamsMap.newMap("ddBB", StringUtils.isEmpty(ddBB) ? "dems" : ddBB).addParams("openId", openId).addParams("tenantId", tenantId).addParams("projectCode", projectCode).addParams("month", month);
        return sqlSessionTemplate.selectList(className + ".queryCheckWork",paramsMap);
    }

    public List<Map<String,Object>> querySalary(String ddBB,String openId,String tenantId,String projectCode) {
        Map<String, Object> paramsMap = ParamsMap.newMap("ddBB", StringUtils.isEmpty(ddBB) ? "dems" : ddBB).addParams("openId", openId).addParams("tenantId", tenantId).addParams("projectCode", projectCode);
        return sqlSessionTemplate.selectList(className + ".querySalary",paramsMap);
    }

}
