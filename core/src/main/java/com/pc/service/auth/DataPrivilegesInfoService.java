package com.pc.service.auth;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.privilege.DataPrivilegesInfoDao;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataPrivilegesInfoService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());
    @Autowired
	private DataPrivilegesInfoDao dataPrivilegesInfoDao;
    @Autowired
	private DataPrivilegeTypeService dataPrivilegeTypeService;
    
    
	
    public List<Map<String, Object>> getDataList(String userId,String tenantId,String DataPrivilegeTypeId, String ddBB) {
    	Map<String, Object> dataPrivilegeType=dataPrivilegeTypeService.getByID(DataPrivilegeTypeId, ddBB);
    	Map<String, Object> params=new HashMap<>();
    	params.put(TableConstants.DataPrivilegeType.tableName.name(), ddBB + TableConstants.SEPARATE + (String)dataPrivilegeType.get(TableConstants.DataPrivilegeType.tableName.name()));
    	params.put("rdprTableName", ddBB + TableConstants.SEPARATE + TableConstants.ROLE_DATA_PRIVILEGES_RELATE);
    	params.put("dpTableName", ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES_INFO);
    	params.put("rTableName", ddBB + TableConstants.SEPARATE + TableConstants.ROLES_INFO);
    	params.put("urrTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER_ROLE_RELATE);
    	params.put(TableConstants.DataPrivilegesInfo.tenantId.name(), tenantId);
    	params.put(TableConstants.UserRoleRelate.userId.name(), userId);
		return dataPrivilegesInfoDao.queryDataListInTab(params);
	}

    public void addDataPrivilegesInfo(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES_INFO);
	}

	public Page getDataPrivilegesInfoPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES_INFO);

	}

	public boolean updateDataPrivilegesInfo(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES_INFO) > 0;
	}

	public Map<String, Object> getDataPrivilegesInfo(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES_INFO);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES_INFO,id);
	}
	
	public String getDataPrivilegesInfoId(Map<String, Object> dataPrivilegesListMap,String tenantId,String dataId,String dataTypeId, String ddBB) {
		String id=(String) dataPrivilegesListMap.get(dataId);
		if(id==null){
			Map<String, Object> map=new HashMap<>();
			map.put(TableConstants.DataPrivilegesInfo.DATA_TYPE_ID.name(), dataTypeId);
			map.put(TableConstants.DataPrivilegesInfo.DATA_ID.name(), dataId);
			map.put(TableConstants.DataPrivilegesInfo.DATA_NAME.name(), dataTypeId+"-"+dataId);
			map.put(TableConstants.TENANT_ID, tenantId);
			map.put(TableConstants.IS_VALID, 0);
			map.put(TableConstants.IS_SEALED, 0);
			((DataPrivilegesInfoService)AopContext.currentProxy()).addDataPrivilegesInfo(map, ddBB);
			id=(String) map.get(TableConstants.DataPrivilegesInfo.ID.name());
		}
		return id;
	}
	
    public List<Map<String, Object>> getDataPrivilegesInfoList(Map<String, Object> params, String ddBB) {
    	List<Map<String, Object>> list = queryList(params, null,
			ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES_INFO);
    	return list;
	}

	public boolean deleteDataPrivilegesInfo(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES_INFO) > 0;
	}
}