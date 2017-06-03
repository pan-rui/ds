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
 * @version: \$Rev: 2652 $
 * @UpdateAuthor: \$Author: zhangj $
 * @UpdateDateTime: \$Date: 2017-05-27 18:37:56 +0800 (周六, 27 5月 2017) $
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
	public List<Map<String, Object>> queryAllAcceptanceBatchByNoticePageInTab(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.selectList(className + ".queryAllAcceptanceBatchByNoticePageInTab", paramsMap);
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
