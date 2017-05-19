package com.pc.dao.sys;

import com.pc.core.ParamsMap;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * @Description: ${Description}
 * @Author: 潘锐 (2017-05-17 17:52)
 * @version: \$Rev: 2400 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-05-18 15:07:05 +0800 (周四, 18 5月 2017) $
 */
@Repository
@CacheConfig(cacheNames = "qCache")
public class StatisticsDao {
    private SqlSessionTemplate sqlSessionTemplate;
    private String className;

    @Autowired
    public StatisticsDao(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
        this.className = this.getClass().getName();
    }

    public Connection getConnection() {
        return sqlSessionTemplate.getConnection();
    }

    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    public List<Map<String, Object>> queryAccessStat(ParamsMap<String, Object> paramsMap) {

        return sqlSessionTemplate.selectList(className + ".queryAccessStat",paramsMap);
    }

}
