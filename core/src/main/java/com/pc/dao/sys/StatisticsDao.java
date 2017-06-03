package com.pc.dao.sys;

import com.pc.core.Page;
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
 * @version: \$Rev: 2485 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-05-22 17:03:00 +0800 (周一, 22 5月 2017) $
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

    public List<Map<String, Object>> queryAccessByPageTab(String ddBB,final Page page) {
        return sqlSessionTemplate.selectList(className + ".queryAccessByPageTab",ParamsMap.newMap("page",page).addParams("ddBB",ddBB));
    }

    public List<Map<String, Object>> queryAccessTByPageTab(String ddBB,final Page page) {
        return sqlSessionTemplate.selectList(className + ".queryAccessTByPageTab",ParamsMap.newMap("page",page).addParams("ddBB",ddBB));
    }

}
