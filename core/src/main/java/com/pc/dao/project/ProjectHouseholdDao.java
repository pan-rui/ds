package com.pc.dao.project;

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
 * @version: \$Rev: 1693 $
 * @UpdateAuthor: \$Author: zhangj $
 * @UpdateDateTime: \$Date: 2017-05-02 10:38:50 +0800 (周二, 02 5月 2017) $
 */
@Repository
@CacheConfig(cacheNames = "qCache", cacheManager = "cacheManagerSlave", cacheResolver = "baseImpl")
public class ProjectHouseholdDao {
        private Logger logger = LogManager.getLogger(this.getClass());

	private SqlSessionTemplate sqlSessionTemplate;
	private String className;

	@Autowired
	public ProjectHouseholdDao(SqlSessionTemplate sqlSessionTemplate) {
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
	public List<Map<String, Object>> queryProjectRegionTreeInTab(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.selectList(className + ".queryProjectRegionTreeInTab", paramsMap);
	}
	
	@DataSource
	public List<Map<String, Object>> queryProjectRoomListByUserInTab(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.selectList(className + ".queryProjectRoomListByUserInTab", paramsMap);
	}
	
	@DataSource
	public List<Map<String, Object>> queryProjectFloorListByUserInTab(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.selectList(className + ".queryProjectFloorListByUserInTab", paramsMap);
	}
	
	@DataSource
	public List<Map<String, Object>> queryRoomsInTab(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.selectList(className + ".queryRoomsInTab", paramsMap);
	}
	
	@DataSource
	public List<Map<String, Object>> queryFloorsInTab(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.selectList(className + ".queryFloorsInTab", paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryHouseholdsByRegionInTab(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.selectList(className + ".queryHouseholdsByRegionInTab", paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryHouseholdsPageByRegionInTab(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.selectList(className + ".queryHouseholdsPageByRegionInTab", paramsMap);
	}
	@DataSource(DataSourceHolder.DBType.master)
	public int insertBatch(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.insert(className + ".insertBatch", paramsMap);
	}

	@DataSource(DataSourceHolder.DBType.master)
	public int updateBatch(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.insert(className + ".updateBatch", paramsMap);
	}
}
