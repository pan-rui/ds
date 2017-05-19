package com.pc.dao.privilege;

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
 * @version: \$Rev: 1158 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-04-18 15:53:47 +0800 (周二, 18 4月 2017) $
 */
@Repository
@CacheConfig(cacheNames = "qCache", cacheManager = "cacheManagerSlave", cacheResolver = "baseImpl")
public class SocialLoginDao {
        private Logger logger = LogManager.getLogger(this.getClass());

	private SqlSessionTemplate sqlSessionTemplate;
	private String className;

	@Autowired
	public SocialLoginDao(SqlSessionTemplate sqlSessionTemplate) {
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
