package com.pc.service.procedure.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.procedure.ProcedureInfoDao;
import com.pc.service.BaseService;

@Service
public class ProcedureInfoService extends BaseService {
	private Logger logger = LogManager.getLogger(this.getClass());
	@Autowired
	private ProcedureInfoDao procedureInfoDao;
	
	public List<Map<String, Object>> getProcedureTree(Map<String, Object> paramsMap, String ddBB) {
		paramsMap.put("pTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_INFO);
		paramsMap.put("ptTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_TYPE);
		return procedureInfoDao.queryProcedureTreeInTab(paramsMap);
	}
	
	public List<Map<String, Object>> getListByRegionAndUser(Map<String, Object> paramsMap, String ddBB) {
		paramsMap.put("pTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_INFO);
		paramsMap.put("prprTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_REGION_PROCEDURE_RELATE);
		paramsMap.put("rdprTableName", ddBB + TableConstants.SEPARATE + TableConstants.ROLE_DATA_PRIVILEGES_RELATE);
		paramsMap.put("dpTableName", ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES_INFO);
		paramsMap.put("rTableName", ddBB + TableConstants.SEPARATE + TableConstants.ROLES_INFO);
		paramsMap.put("urrTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER_ROLE_RELATE);
		return procedureInfoDao.queryListByUserInTab(paramsMap);
	}
	
	public List<Map<String, Object>> getListByRegion(Map<String, Object> paramsMap, String ddBB) {
		paramsMap.put("pTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_INFO);
		paramsMap.put("prprTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_REGION_PROCEDURE_RELATE);
		return procedureInfoDao.queryListByRegionInTab(paramsMap);
	}
	
	public void addProcedureInfo(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_INFO);
	}

	public Page getProcedureInfoPage(Page<Map<String, Object>> page, String ddBB) {

		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_INFO);

	}

	public boolean updateProcedureInfo(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_INFO) > 0;
	}

	public Map<String, Object> getProcedureInfo(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_INFO);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_INFO, id);
	}

	public List<Map<String, Object>> getProcedureInfoList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_INFO);
		return list;
	}

	public boolean deleteProcedureInfo(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_INFO) > 0;
	}
}