package com.pc.dao.labor;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.pc.core.DataSource;
import com.pc.core.DataSourceHolder;
import com.pc.core.Page;
import com.pc.core.ParamsMap;

/**
 * @Description: ${Description}
 * @Author: wady (2017-03-27 14:21)
 * @version: \$Rev$
 * @UpdateAuthor: \$Author$
 * @UpdateDateTime: \$Date$
 */
@Repository
@CacheConfig(cacheNames = "qCache", cacheManager = "cacheManagerSlave", cacheResolver = "baseImpl")
public class LaborAttendanceDetailRecordDao {
        private Logger logger = LogManager.getLogger(this.getClass());

	private SqlSessionTemplate sqlSessionTemplate;
	private String className;

	@Autowired
	public LaborAttendanceDetailRecordDao(SqlSessionTemplate sqlSessionTemplate) {
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
	public List<Map<String, Object>> queryLaborAttendanceToPushListInTab(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.selectList(className + ".queryLaborAttendanceToPushListInTab", paramsMap);
	}
	
	@DataSource(DataSourceHolder.DBType.master)
	public int updateLaborAttendanceListToPushInTab(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.update(className + ".updateLaborAttendanceListToPushInTab", paramsMap);
	}

}
