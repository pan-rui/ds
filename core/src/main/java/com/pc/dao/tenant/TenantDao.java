package com.pc.dao.tenant;

import com.pc.core.DataSource;
import com.pc.core.DataSourceHolder;
import com.pc.core.ParamsMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.util.Map;

/**
 * @Description: ${Description}
 * @Author: wady (2017-03-27 14:21)
 * @version: \$Rev: 3596 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-07-16 00:27:43 +0800 (周日, 16 7月 2017) $
 */
@Repository
@CacheConfig(cacheNames = "qCache", cacheManager = "cacheManagerSlave", cacheResolver = "baseImpl")
public class TenantDao {
        private Logger logger = LogManager.getLogger(this.getClass());

	private SqlSessionTemplate sqlSessionTemplate;
	private String className;

	@Autowired
	public TenantDao(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
		this.className = this.getClass().getName();
	}

	public Connection getConnection() {
		return sqlSessionTemplate.getConnection();
	}

	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}

	@DataSource(DataSourceHolder.DBType.master)
	public void initProcudure(String tenantId, String tenantPhone,String updateUserId) {
		Map<String, Object> paramsMap = ParamsMap.newMap("tId", tenantId).addParams("tPhone", tenantPhone).addParams("uId",updateUserId);
		sqlSessionTemplate.selectOne(className + ".initProcudure", paramsMap);
	}

	

}
