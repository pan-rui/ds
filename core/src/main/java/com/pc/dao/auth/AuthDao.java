package com.pc.dao.auth;

import com.pc.core.DataSource;
import com.pc.core.Page;
import com.pc.core.ParamsMap;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * @Description: ${Description}
 * @Author: 潘锐 (2017-03-27 14:21)
 * @version: \$Rev: 3223 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-06-23 11:54:45 +0800 (周五, 23 6月 2017) $
 */
@Repository
@CacheConfig(cacheNames = "qCache", cacheManager = "cacheManagerSlave", cacheResolver = "baseImpl")
public class AuthDao {
	private SqlSessionTemplate sqlSessionTemplate;
	private String className;

	@Autowired
	public AuthDao(SqlSessionTemplate sqlSessionTemplate) {
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
	@Cacheable(value = "auth", key = "Constants.CACHE_AUTHENTICATION_PREFIX+#dbName+'_'+#uName")
	public String authenticationQuery(String uName, String dbName) {
		Map<String, Object> paramsMap = ParamsMap.newMap("uName", uName).addParams("dbName", dbName);
		return sqlSessionTemplate.selectOne(className + ".authenticationQuery", paramsMap);
	}

	@DataSource
	@Cacheable(value = "auth", key = "Constants.CACHE_USER_ROLES_PREFIX+#dbName+'_'+#uName")
	public List<String> userRolesQuery(String uName, String dbName) {
		Map<String, Object> paramsMap = ParamsMap.newMap("uName", uName).addParams("dbName", dbName);
		return sqlSessionTemplate.selectList(className + ".userRolesQuery", paramsMap);
	}

	@DataSource
	@Cacheable(value = "auth", key = "Constants.CACHE_PERMISSIONS_PREFIX+#dbName+'_'+#roleName", sync = true)
	public List<String> permissionsQuery(String roleName, String dbName) {
		Map<String, Object> paramsMap = ParamsMap.newMap("roleName", roleName).addParams("dbName", dbName);
		return sqlSessionTemplate.selectList(className + ".permissionsQuery", paramsMap);
	}

	@DataSource
	@Cacheable(value = "auth", key = "Constants.CACHE_USER_PERMISSIONS_PREFIX+#dbName+'_'+#uName")
	public List<String> userPermissionsQuery(String uName, String dbName) {
		Map<String, Object> paramsMap = ParamsMap.newMap("uName", uName).addParams("dbName", dbName);
		return sqlSessionTemplate.selectList(className + ".userPermissionsQuery", paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryOperatRolePrivilegesTab(String operatRoleId, String orTableName,
			@Param("orprTableName") String orprTableName, String opTableName) {
		Map<String, Object> paramsMap = ParamsMap.newMap("operatRoleId", operatRoleId)
				.addParams("orTableName", orTableName).addParams("orprTableName", orprTableName)
				.addParams("opTableName", opTableName);
		return sqlSessionTemplate.selectList(className + ".queryOperatRolePrivilegesTab", paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryUserOperatPrivilegesByRoleTab(String userId, String orprTableName,
			String opTableName, String uorrTableName) {
		Map<String, Object> paramsMap = ParamsMap.newMap("userId", userId).addParams("uorrTableName", uorrTableName)
				.addParams("orprTableName", orprTableName).addParams("opTableName", opTableName);
		return sqlSessionTemplate.selectList(className + ".queryUserOperatPrivilegesByRoleTab", paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryUserOperatRolesTab(String userId, String orTableName, String uorrTableName) {
		Map<String, Object> paramsMap = ParamsMap.newMap("userId", userId).addParams("orTableName", orTableName)
				.addParams("uorrTableName", uorrTableName);
		return sqlSessionTemplate.selectList(className + ".queryUserOperatRolesTab", paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryUserOperatPrivilegesByUserTab(String userId, String opTableName,
			String uoprTableName) {
		Map<String, Object> paramsMap = ParamsMap.newMap("userId", userId).addParams("opTableName", opTableName)
				.addParams("uoprTableName", uoprTableName);
		return sqlSessionTemplate.selectList(className + ".queryUserOperatPrivilegesByUserTab", paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryUserOperatPrivilegesTab(String userId, String orprTableName,
			String uorrTableName, String opTableName, String uoprTableName) {
		Map<String, Object> paramsMap = ParamsMap.newMap("userId", userId).addParams("opTableName", opTableName)
				.addParams("uoprTableName", uoprTableName).addParams("orprTableName", orprTableName)
				.addParams("uorrTableName", uorrTableName);
		return sqlSessionTemplate.selectList(className + ".queryUserOperatPrivilegesTab", paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryDataRolePrivilegesTab(String dataRoleId, String drprTableName,
			String dpTableName) {
		Map<String, Object> paramsMap = ParamsMap.newMap("dataRoleId", dataRoleId)
				.addParams("drprTableName", drprTableName).addParams("dpTableName", dpTableName);
		return sqlSessionTemplate.selectList(className + ".drprTableName", paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryUserDataRolesTab(String userId, String drTableName, String udrrTableName) {
		Map<String, Object> paramsMap = ParamsMap.newMap("userId", userId).addParams("drTableName", drTableName)
				.addParams("udrrTableName", udrrTableName);
		return sqlSessionTemplate.selectList(className + ".queryUserDataRolesTab", paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryUserDataPrivilegesByUserTab(String userId, String dpTableName,
			String udprTableName) {
		Map<String, Object> paramsMap = ParamsMap.newMap("userId", userId).addParams("dpTableName", dpTableName)
				.addParams("udprTableName", udprTableName);
		return sqlSessionTemplate.selectList(className + ".queryUserDataPrivilegesByUserTab", paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryDataPrivilegesTypePageInTab(Page page, String dpTableName,
			String dtTableName) {
		Map<String, Object> paramsMap = ParamsMap.newMap("page", page).addParams("dpTableName", dpTableName)
				.addParams("dtTableName", dtTableName);
		List<Map<String,Object>> resultList=sqlSessionTemplate.selectList(className + ".queryDataPrivilegesTypePageInTab", paramsMap);
		page.setResults(resultList);
		return resultList;
	}

	@DataSource
	public List<Map<String, Object>> queryUserOperatPrivilegesRolePageInTab(Page page, String tenantId,
			String uTableName, String dtTableName, String rTableName) {
		Map<String, Object> paramsMap = ParamsMap.newMap("page", page).addParams("urrTableName", dtTableName)
				.addParams("dtTableName", dtTableName).addParams("tenantId", tenantId)
				.addParams("uTableName", uTableName).addParams("rTableName", rTableName);
		List<Map<String,Object>> resultList= sqlSessionTemplate.selectList(className + ".queryUserOperatPrivilegesRolePageInTab", paramsMap);
		page.setResults(resultList);
		return resultList;
	}

	@DataSource
	public List<Map<String, Object>> queryUserDataPrivilegesRolePageInTab(Page page, String tenantId, String uTableName,
			String dtTableName, String rTableName) {
		Map<String, Object> paramsMap = ParamsMap.newMap("page", page).addParams("urrTableName", dtTableName)
				.addParams("rTableName", rTableName).addParams("tenantId", tenantId)
				.addParams("uTableName", uTableName);
		List<Map<String,Object>> resultList= sqlSessionTemplate.selectList(className + ".queryUserDataPrivilegesRolePageInTab", paramsMap);
		page.setResults(resultList);
		return resultList;
	}

	@DataSource
	public List<Map<String, Object>> queryDataPrivilegesRolePageInTab(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.selectList(className + ".queryDataPrivilegesRolePageInTab", paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryFuncRolePrivilegesTab(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.selectList(className + ".queryFuncRolePrivilegesTab", paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryUserFuncPrivilegesByUserTab(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.selectList(className + ".queryUserFuncPrivilegesByUserTab", paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryUserFuncRolesTab(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.selectList(className + ".queryUserFuncRolesTab", paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryDataPrivilegesUserPageInTab(Map<String, Object> paramsMap) {
		return sqlSessionTemplate.selectList(className + ".queryDataPrivilegesUserPageInTab", paramsMap);
	}
	
	

	@DataSource
	public List<Map<String, Object>> queryDataPrivilegesByUser(String userId, String drprTableName,
			String udrrTableName) {
		Map<String, Object> paramsMap = ParamsMap.newMap("userId", userId).addParams("drprTableName", drprTableName)
				.addParams("udrrTableName", udrrTableName);
		return sqlSessionTemplate.selectList(className + ".queryDataPrivilegesByUser", paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryDataPrivilegesDetailByDataType(String userId, String dpTableName,
			String udprTableName, String dtTableName, String dataSignId) {
		Map<String, Object> paramsMap = ParamsMap.newMap("userId", userId).addParams("dpTableName", dpTableName)
				.addParams("udprTableName", udprTableName).addParams("dtTableName", dtTableName)
				.addParams("dataSignId", dataSignId);
		return sqlSessionTemplate.selectList(className + ".queryDataPrivilegesDetailByDataType", paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryDataSceneRuleGroupDetailByScene(String dsrTableName, String dsrgTableName,
			String sceneId) {
		Map<String, Object> paramsMap = ParamsMap.newMap("dsrTableName", dsrTableName)
				.addParams("dsrgTableName", dsrgTableName).addParams("sceneId", sceneId);
		return sqlSessionTemplate.selectList(className + ".queryDataSceneRuleGroupDetailByScene", paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryDataPrivilegesDetailByDataTypeRole(String userId, String drprTableName,
			String udrrTableName, String dpTableName, String dtTableName, String dataSignId) {
		Map<String, Object> paramsMap = ParamsMap.newMap("userId", userId).addParams("dpTableName", dpTableName)
				.addParams("udrrTableName", udrrTableName).addParams("dtTableName", dtTableName)
				.addParams("dataSignId", dataSignId).addParams("drprTableName", drprTableName);
		return sqlSessionTemplate.selectList(className + ".queryDataPrivilegesDetailByDataTypeRole", paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryFuncRolesByUserTab(String funcUserRoleTable, String userTable, String funcRoleTab, String username) {
		Map<String, Object> paramsMap = ParamsMap.newMap("funcUserRoleTable", funcUserRoleTable).addParams("username", username).addParams("userTable", userTable).addParams("funcRoleTab", funcRoleTab);
		return sqlSessionTemplate.selectList(className + ".queryFuncRolesByUserTab", paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryFuncPermissByRolesTab(String funcRoleTab, String funcPermissTab, List<String> roles) {
		Map<String, Object> paramsMap = ParamsMap.newMap("funcRoleTab", funcRoleTab).addParams("funcPermissTab", funcPermissTab).addParams("roles", roles);
		return sqlSessionTemplate.selectList(className + ".queryFuncPermissByRolesTab", paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryFuncPermissByRoleTab(String funcRoleTab, String funcPermissTab, String role) {
		Map<String, Object> paramsMap = ParamsMap.newMap("funcRoleTab", funcRoleTab).addParams("funcPermissTab", funcPermissTab).addParams("role", role);
		return sqlSessionTemplate.selectList(className + ".queryFuncPermissByRoleTab", paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryRoleByFunPermissTab(String funcRoleTab, String funcRolePermissTab, String perId) {
		Map<String, Object> paramsMap = ParamsMap.newMap("funcRoleTab", funcRoleTab).addParams("funcRolePermissTab", funcRolePermissTab).addParams("perId", perId);
		return sqlSessionTemplate.selectList(className + ".queryRoleByFunPermissTab", paramsMap);
	}


	@DataSource
	public List<Map<String, Object>> queryTenantRoleAsNamePageMul(final Page page,String ddBB) {
		Map<String, Object> paramsMap = ParamsMap.newMap("page", page).addParams("ddBB",ddBB);
		return sqlSessionTemplate.selectList(className + ".queryTenantRoleAsNamePageMul",paramsMap);
	}
/*	@DataSource
	public List<Map<String, Object>> queryFuncPermissByUser(String funUserPerTab, String userId) {
		Map<String,Object> paramsMap=ParamsMap.newMap("funUserPerTab",funUserPerTab).addParams("userId",userId);
		return sqlSessionTemplate.selectList(className + ".queryFuncPermissByUser", paramsMap);
	}*/

	@DataSource
	public List<Map<String, Object>> queryTenantRoles(String ddBB,String tenantId) {
		Map<String, Object> paramsMap = ParamsMap.newMap("tenantId", tenantId).addParams("ddBB",ddBB);
		return sqlSessionTemplate.selectList(className + ".queryTenantRoles",paramsMap);
	}

	@DataSource
	public List<Map<String, Object>> queryTenantPermissByRoles(String ddBB,String tenantId,List<String> roles) {
		Map<String, Object> paramsMap = ParamsMap.newMap("tenantId", tenantId).addParams("ddBB", ddBB).addParams("roles", roles);
		return sqlSessionTemplate.selectList(className + ".queryTenantRoles",paramsMap);
	}
}
