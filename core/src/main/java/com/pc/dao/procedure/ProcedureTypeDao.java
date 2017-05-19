package com.pc.dao.procedure;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.util.*;
import com.pc.core.DataSource;

/**
 * @Description: ${Description}
 * @Author: wady (2017-03-27 14:21)
 * @version: \$Rev: 1503 $
 * @UpdateAuthor: \$Author: zhangj $
 * @UpdateDateTime: \$Date: 2017-04-25 20:20:50 +0800 (周二, 25 4月 2017) $
 */
@Repository
@CacheConfig(cacheNames = "qCache", cacheManager = "cacheManagerSlave", cacheResolver = "baseImpl")
public class ProcedureTypeDao {
        private Logger logger = LogManager.getLogger(this.getClass());

	private SqlSessionTemplate sqlSessionTemplate;
	private String className;

	@Autowired
	public ProcedureTypeDao(SqlSessionTemplate sqlSessionTemplate) {
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
	public List<Map<String, Object>> queryProcedureTypeByUserInTab(Map<String, Object> map) {
		return sqlSessionTemplate.selectList(className + ".queryProcedureTypeByUserInTab", map);
	}

	@DataSource
	public List<Map<String, Object>> queryProcedureTypeByProjectRegionInTab(Map<String, Object> map) {
		return sqlSessionTemplate.selectList(className + ".queryProcedureTypeByProjectRegionInTab", map);
	}

	

}
