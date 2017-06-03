package com.pc.dao.acceptance;

import com.pc.core.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.util.List;
import java.util.Map;


@Repository
@CacheConfig(cacheNames = "qCache", cacheManager = "cacheManagerSlave", cacheResolver = "baseImpl")
public class ProcedureScheduleRelateDao {
        private Logger logger = LogManager.getLogger(this.getClass());

	private SqlSessionTemplate sqlSessionTemplate;
	private String className;

	@Autowired
	public ProcedureScheduleRelateDao(SqlSessionTemplate sqlSessionTemplate) {
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
	public List<Map<String, Object>> queryProcedureScheduleRoomListInTab(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.selectList(className + ".queryProcedureScheduleRoomListInTab", paramsMap);
	}
	
	@DataSource
	public List<Map<String, Object>> queryBuildingProcedureScheduleListInTab(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.selectList(className + ".queryBuildingProcedureScheduleListInTab", paramsMap);
	}

}
