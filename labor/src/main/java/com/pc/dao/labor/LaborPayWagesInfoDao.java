package com.pc.dao.labor;

import com.pc.core.DataSource;
import com.pc.core.Page;
import com.pc.core.ParamsMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * @Description: ${Description}
 * @Author: wady (2017-03-27 14:21)
 * @version: \$Rev$
 * @UpdateAuthor: \$Author$
 * @UpdateDateTime: \$Date$
 */
@Repository
@CacheConfig(cacheNames = "qCache", cacheManager = "cacheManagerSlave", cacheResolver = "baseImpl")
public class LaborPayWagesInfoDao {
    private Logger logger = LogManager.getLogger(this.getClass());

    private SqlSessionTemplate sqlSessionTemplate;
    private String className;

    @Autowired
    public LaborPayWagesInfoDao(SqlSessionTemplate sqlSessionTemplate) {
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
    public Page queryPayWagesPageInTab(String ddBB, final Page page) {
        Map<String, Object> paramsMap = ParamsMap.newMap("ddBB", ddBB).addParams("page", page);
        List<Map<String, Object>> resultList = sqlSessionTemplate.selectList(className + ".queryPayWagesPageInTab", paramsMap);
        page.setResults(resultList);
        return page;
    }


}
