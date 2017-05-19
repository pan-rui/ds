package com.pc.service.procedure.impl;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.procedure.ProcedureTypeDao;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProcedureTypeService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private ProcedureTypeDao procedureTypeDao;
     
    public void addProcedureType(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_TYPE);
	}

	public Page getProcedureTypePage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_TYPE);

	}

	public boolean updateProcedureType(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_TYPE) > 0;
	}

	public Map<String, Object> getProcedureType(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_TYPE);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_TYPE,id);
	}
	
    public List<Map<String, Object>> getProcedureTypeList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_TYPE);
		return list;
	}
    
    public List<Map<String, Object>> getProcedureTypeByUser(Map<String, Object> params, String ddBB) {
		params.put("procTypeTab", ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_TYPE);
		params.put("procInfoTab", ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_INFO);
		params.put("prprTab", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_REGION_PROCEDURE_RELATE);
		params.put("rdprTableName", ddBB + TableConstants.SEPARATE + TableConstants.ROLE_DATA_PRIVILEGES_RELATE);
		params.put("dpTableName", ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES_INFO);
		params.put("rTableName", ddBB + TableConstants.SEPARATE + TableConstants.ROLES_INFO);
		params.put("urrTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER_ROLE_RELATE);
		List<Map<String, Object>> list = procedureTypeDao.queryProcedureTypeByUserInTab(params);
		return list;
	}

	public List<Map<String, Object>> getProcedureTypeByProjectRegionParams(Map<String, Object> params, String ddBB) {
		params.put("procTypeTab", ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_TYPE);
		params.put("procInfoTab", ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_INFO);
		params.put("prprTab", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_REGION_PROCEDURE_RELATE);
		List<Map<String, Object>> list = procedureTypeDao.queryProcedureTypeByProjectRegionInTab(params);
		return list;
	}


	public boolean deleteProcedureType(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_TYPE) > 0;
	}
}