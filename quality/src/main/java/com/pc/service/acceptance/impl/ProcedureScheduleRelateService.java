package com.pc.service.acceptance.impl;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.acceptance.ProcedureScheduleRelateDao;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProcedureScheduleRelateService extends BaseService {
	private Logger logger = LogManager.getLogger(this.getClass());
	@Autowired
	private ProcedureScheduleRelateDao procedureScheduleRelateDao;
	
	public List<Map<String, Object>> getBuildingProcedureScheduleList(Map<String, Object> params, String ddBB) {
		params.put("phTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
		params.put("anTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE);
		return procedureScheduleRelateDao.queryBuildingProcedureScheduleListInTab(params);
	}

	public List<Map<String, Object>> getProcedureScheduleRoomidList(Map<String, Object> params, String ddBB) {
		params.put("psrTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_SCHEDULE_RELATE);
		params.put("phTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
		params.put("pbTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
		return procedureScheduleRelateDao.queryProcedureScheduleRoomListInTab(params);
	}

	public void addProcedureScheduleRelate(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_SCHEDULE_RELATE);
	}

	public Page getProcedureScheduleRelatePage(Page<Map<String, Object>> page, String ddBB) {
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_SCHEDULE_RELATE);
	}

	public boolean updateProcedureScheduleRelate(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_SCHEDULE_RELATE) > 0;
	}

	public Map<String, Object> getProcedureScheduleRelate(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_SCHEDULE_RELATE);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_SCHEDULE_RELATE, id);
	}

	public List<Map<String, Object>> getProcedureScheduleRelateList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_SCHEDULE_RELATE);
		return list;
	}

	public boolean deleteProcedureScheduleRelate(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_SCHEDULE_RELATE) > 0;
	}
}