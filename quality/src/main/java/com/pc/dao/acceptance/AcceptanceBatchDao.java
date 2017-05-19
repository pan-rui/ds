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

/**
 * @Description: ${Description}
 * @Author: wady (2017-03-27 14:21)
 * @version: \$Rev: 1404 $
 * @UpdateAuthor: \$Author: zhangj $
 * @UpdateDateTime: \$Date: 2017-04-22 09:40:51 +0800 (周六, 22 4月 2017) $
 */
@Repository
@CacheConfig(cacheNames = "qCache", cacheManager = "cacheManagerSlave", cacheResolver = "baseImpl")
public class AcceptanceBatchDao {
        private Logger logger = LogManager.getLogger(this.getClass());

	private SqlSessionTemplate sqlSessionTemplate;
	private String className;

	@Autowired
	public AcceptanceBatchDao(SqlSessionTemplate sqlSessionTemplate) {
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
	public List<Map<String, Object>> queryAllAcceptanceBatchByNoticeInTab(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.selectList(className + ".queryAllAcceptanceBatchByNoticeInTab", paramsMap);
	}
	
	@DataSource
	public List<Map<String, Object>> queryAllAcceptanceBatchByPostInTab(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.selectList(className + ".queryAllAcceptanceBatchByPostInTab", paramsMap);
	}
	
	@DataSource
	public List<Map<String, Object>> queryAcceptanceBatchByStatusPageInTab(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.selectList(className + ".queryAcceptanceBatchByStatusPageInTab", paramsMap);
	}

	@DataSource
	public Map<String, Object> queryBatchInfoInTab(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.selectOne(className + ".queryBatchInfoInTab", paramsMap);
	}

}
