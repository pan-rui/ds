package com.pc.service.tenant.impl;

import com.pc.base.BaseResult;
import com.pc.core.Base64;
import com.pc.core.Page;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.dao.BaseDao;
import com.pc.dao.tenant.TenantDao;
import com.pc.service.BaseService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TenantService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());
    @Autowired
	private BaseDao baseDao;
    @Autowired
	private TenantDao tenantDao;
     
    public void addTenant(Map<String, Object> params, String ddBB) {
		String tenantId=add(params, ddBB + TableConstants.SEPARATE + TableConstants.TENANT);
		logger.info("租户新增初始化数据.....");
		initData(params,ddBB,tenantId);
	}

	public Page getTenantPage(Page<Map<String, Object>> page, String ddBB) {
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.TENANT);

	}

	public boolean updateTenant(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.TENANT) > 0;
	}

	public Map<String, Object> getTenant(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.TENANT);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.TENANT,id);
	}
	
    public List<Map<String, Object>> getTenantList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.TENANT);
		return list;
	}

	public boolean deleteTenant(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.TENANT) > 0;
	}

	@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
	public void initData(Map<String, Object> params, String ddBB,String tenantId) {
		//基本数据
		fillBaseData(params, ddBB, tenantId);
		Calendar calendar=Calendar.getInstance();
		String updateUserId= (String) params.get("UPDATE_USER_ID");
		String rolesInfoID = UUID.randomUUID().toString().replace("-", "");
		String adminID=UUID.randomUUID().toString().replace("-", "");
		ParamsMap<String, Object> rolesInfoMap = ParamsMap.newMap("ID",rolesInfoID);
		rolesInfoMap.addParams("ROLE_NAME", "后台管理员").addParams("REMARK", "所有后台操作权限").addParams("UPDATE_USER_ID", updateUserId).addParams(TableConstants.IS_SEALED, "0").addParams(TableConstants.TENANT_ID, tenantId).addParams(TableConstants.UPDATE_TIME, calendar.getTime());
		baseDao.insertByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.ROLES_INFO, rolesInfoMap);
		ParamsMap<String, Object> userMap = ParamsMap.newMap("ID",adminID);
		userMap.addParams("USER_NAME", params.get("TENANT_NAME")).addParams("REAL_NAME", "管理员").addParams("PHONE", params.get("TENANT_PHONE")).addParams("PWD", Base64.encode(DigestUtils.md5("12345678".getBytes()))).addParams(TableConstants.IS_SEALED, "0").addParams(TableConstants.User.REGISTER_TIME, calendar.getTime()).addParams(TableConstants.TENANT_ID, tenantId).addParams(TableConstants.UPDATE_TIME, calendar.getTime());
		baseDao.insertByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.USER, userMap);
		String userRoleId=UUID.randomUUID().toString().replace("-", "");
		ParamsMap userRoleMap=ParamsMap.newMap("ID",userRoleId);
		userRoleMap.addParams("USER_ID", adminID).addParams("ROLE_ID", rolesInfoID).addParams(TableConstants.UPDATE_USER_ID, updateUserId).addParams(TableConstants.TENANT_ID, tenantId);
		baseDao.insertByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.USER_ROLE_RELATE, userRoleMap);
		initAuthData(params, ddBB, tenantId,rolesInfoID);
	}
@Transactional(propagation = Propagation.NESTED,rollbackFor = Exception.class)
	public void fillBaseData(final Map<String, Object> params,String ddBB,String tenantId) {
    	Calendar calendar=Calendar.getInstance();
		String updateUserId= (String) params.get("UPDATE_USER_ID");
		ParamsMap paramsMap=ParamsMap.newMap(TableConstants.TENANT_ID,"1").addParams(TableConstants.IS_SEALED,"0");
		List<Map<String,Object>> dptMapList=baseDao.queryByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGE_TYPE, paramsMap);
		dptMapList.forEach(dptMap->{
			dptMap.put(TableConstants.TENANT_ID, tenantId);
			dptMap.put("ID", UUID.randomUUID().toString().replace("-", ""));
			dptMap.put(TableConstants.UPDATE_TIME, calendar.getTime());
			dptMap.put(TableConstants.UPDATE_USER_ID, updateUserId);
		});
		baseDao.insertBatchByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGE_TYPE, dptMapList);
		List<Map<String,Object>> siMapList=baseDao.queryByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.STATUS_INFO, paramsMap);
		siMapList.forEach(stMap->{
			stMap.put(TableConstants.TENANT_ID, tenantId);
			stMap.put("ID", UUID.randomUUID().toString().replace("-", ""));
			stMap.put(TableConstants.UPDATE_TIME, calendar.getTime());
			stMap.put(TableConstants.UPDATE_USER_ID, updateUserId);
		});
		baseDao.insertBatchByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.STATUS_INFO, siMapList);
		List<Map<String,Object>> piMapList=baseDao.queryByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.POST_INFO, paramsMap);
		piMapList.forEach(piMap->{
			piMap.put(TableConstants.TENANT_ID, tenantId);
			piMap.put("ID", UUID.randomUUID().toString().replace("-", ""));
			piMap.put(TableConstants.UPDATE_TIME, calendar.getTime());
			piMap.put(TableConstants.UPDATE_USER_ID, updateUserId);
			piMap.remove("PERSON_COUNT");
		});
		baseDao.insertBatchByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.STATUS_INFO, piMapList);
	}
	@Transactional(propagation = Propagation.NESTED,rollbackFor = Exception.class)
	public void initAuthData(final Map<String, Object> params,String ddBB,String tenantId,String roleId) {
    	logger.info("初始化权限相关数据........");
		ParamsMap paramsMap=ParamsMap.newMap(TableConstants.TENANT_ID,"1").addParams(TableConstants.IS_SEALED,"0");
		String updateUserId= (String) params.get("UPDATE_USER_ID");
		String tenantPhone= (String) params.get("TENANT_PHONE");
		Calendar calendar=Calendar.getInstance();
		List<Map<String,Object>> omMapList=baseDao.queryListInTab(ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_MODULES, paramsMap,ParamsMap.newMap("PARENT_ID","ASC"));
		omMapList.forEach(omMap->{
			omMap.put(TableConstants.TENANT_ID, tenantId);
			omMap.put("ID",omMap.get("id").toString().substring(11)+tenantPhone);
			String parentId = (String) omMap.get("parentId");
			if(StringUtils.isNotEmpty(parentId))
				omMap.put("PARENT_ID", parentId.substring(11) + tenantPhone);
			omMap.put(TableConstants.UPDATE_TIME, calendar.getTime());
			omMap.put(TableConstants.UPDATE_USER_ID, updateUserId);
		});
		baseDao.insertBatchByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_MODULES, omMapList);
		List<Map<String,Object>> opMapList=baseDao.queryByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_PRIVILEGES, paramsMap);
		List<Map<String, Object>> roleRelateMapList = new ArrayList<>();
		opMapList.forEach(opMap->{
			String privilegesId=opMap.get("id").toString().substring(11)+tenantPhone;
			opMap.put(TableConstants.TENANT_ID, tenantId);
			opMap.put("ID",privilegesId);
			opMap.put("MODULE_ID",opMap.get("moduleId").toString().substring(11)+tenantPhone);
			opMap.put("OPERATE_PRIVILEGES_CREATE_TIME", calendar.getTime());
			opMap.put(TableConstants.UPDATE_TIME, calendar.getTime());
			opMap.put(TableConstants.UPDATE_USER_ID, updateUserId);
			roleRelateMapList.add(ParamsMap.newMap("ID", UUID.randomUUID().toString().replace("-", "")).addParams("ROLE_ID", roleId).addParams("OPERATE_PRIVILEGE_ID", privilegesId).addParams(TableConstants.TENANT_ID, tenantId).addParams(TableConstants.UPDATE_USER_ID, updateUserId).addParams(TableConstants.UPDATE_TIME, calendar.getTime()));
		});
		baseDao.insertBatchByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_PRIVILEGES, opMapList);
		baseDao.insertBatchByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.ROLE_OP_PRIVILEGES_RELATE, roleRelateMapList);
	}

	public void initProcedure(String ddBB,Map<String,Object> map,String userId) {
		Map<String, Object> tenantRole = baseDao.queryByIdInTab(ddBB + TableConstants.SEPARATE + TableConstants.TENANT_ROLE, (String) map.get("FUNC_ROLE_ID"));
		Map<String, Object> tenantMap = baseDao.queryByIdInTab(ddBB + TableConstants.SEPARATE + TableConstants.TENANT, (String) map.get("tenantId"));
		if("1".equals(tenantRole.get("roleCode"))){
			tenantDao.initProcudure((String)tenantMap.get("id"),(String)tenantMap.get("tenantPhone"),userId);
		}
	}
}