package com.pc.dao.cache;

/**
 * @Description: ${Description}
 * @Author: 潘锐 (2017-04-18 14:35)
 * @version: \$Rev: 2282 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-05-15 14:11:40 +0800 (周一, 15 5月 2017) $
 */

import com.pc.core.ParamsMap;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
@CacheConfig(cacheNames = "qCache", cacheManager = "cacheManager")
public class OfflineDao {
    private SqlSessionTemplate sqlSessionTemplate;
    private String className;

    @Autowired
    public OfflineDao(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
        this.className = this.getClass().getName();
    }

    public Connection getConnection() {
        return sqlSessionTemplate.getConnection();
    }

    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    public List<Map<String,Object>> queryOffLineProject(String ddBB,String tenantId,String projectPeriodId,Date updateTime) {
        Map<String, Object> paramsMap = ParamsMap.newMap("ddBB", ddBB).addParams("tenantId",tenantId).addParams("updateTime", updateTime).addParams("projectPeriodId", projectPeriodId);
        return sqlSessionTemplate.selectList(className + ".queryOffLineProject",paramsMap);
    }
public List<Map<String,Object>> queryChart(String ddBB,String tenantId,String projectPeriodId,Date updateTime) {
    Map<String, Object> paramsMap = ParamsMap.newMap("ddBB", ddBB).addParams("tenantId",tenantId).addParams("updateTime", updateTime).addParams("projectPeriodId", projectPeriodId);
    return sqlSessionTemplate.selectList(className + ".queryChart",paramsMap);
}
    public List<Map<String, Object>> queryOfflineProcedure(String ddBB,String tenantId,String projectPeriodId,List<String> statusList) {
        Map<String, Object> paramsMap = ParamsMap.newMap("ddBB", ddBB).addParams("tenantId",tenantId).addParams("projectPeriodId", projectPeriodId).addParams("statusList", statusList);
        return sqlSessionTemplate.selectList(className + ".queryOfflineProcedure",paramsMap);
    }
}
