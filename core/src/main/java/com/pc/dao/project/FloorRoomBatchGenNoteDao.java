package com.pc.dao.project;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

import java.sql.Connection;

/**
 * @Description: ${Description}
 * @Author: wady (2017-03-27 14:21)
 * @version: \$Rev$
 * @UpdateAuthor: \$Author$
 * @UpdateDateTime: \$Date$
 */
@Repository
@CacheConfig(cacheNames = "dems", cacheManager = "cacheManagerSlave", cacheResolver = "baseImpl")
public class FloorRoomBatchGenNoteDao {
        private Logger logger = LogManager.getLogger(this.getClass());

	private SqlSessionTemplate sqlSessionTemplate;
	private String className;

	@Autowired
	public FloorRoomBatchGenNoteDao(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
		this.className = this.getClass().getName();
	}

	public Connection getConnection() {
		return sqlSessionTemplate.getConnection();
	}

	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}

	/*@DataSource
	@Cacheable(value = "auth", key = "Constants.CACHE_AUTHENTICATION_PREFIX+#dbName+'_'+#uName")
	public String authenticationQuery(String uName, String dbName) {
		Map<String, Object> paramsMap = ParamsMap.newMap("uName", uName).addParams("dbName", dbName);
		return sqlSessionTemplate.selectOne(className + ".authenticationQuery", paramsMap);
	}*/

	

}
