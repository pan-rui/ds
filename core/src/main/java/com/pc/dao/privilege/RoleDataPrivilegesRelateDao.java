package com.pc.dao.privilege;

import com.pc.core.DataSource;
import com.pc.core.DataSourceHolder;
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
 * @version: \$Rev$
 * @UpdateAuthor: \$Author$
 * @UpdateDateTime: \$Date$
 */
@Repository
@CacheConfig(cacheNames = "dems", cacheManager = "cacheManagerSlave", cacheResolver = "baseImpl")
public class RoleDataPrivilegesRelateDao {
        private Logger logger = LogManager.getLogger(this.getClass());

	private SqlSessionTemplate sqlSessionTemplate;
	private String className;

	@Autowired
	public RoleDataPrivilegesRelateDao(SqlSessionTemplate sqlSessionTemplate) {
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
	public List<Map<String, Object>> queryDataPrivilegesListInTab(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.selectList(className + ".queryDataPrivilegesListInTab", paramsMap);
	}

	@DataSource(DataSourceHolder.DBType.master)
	public int insertList(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.insert(className + ".insertList", paramsMap);
	}
	
	
	@DataSource(DataSourceHolder.DBType.master)
	public int deleteList(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.delete(className + ".deleteList", paramsMap);
	}

}
