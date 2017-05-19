package com.pc.service.acceptance.impl;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AcceptanceProcedureProgressService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

     
    public void addAcceptanceProcedureProgress(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_PROCEDURE_PROGRESS);
	}

	public Page getAcceptanceProcedureProgressPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_PROCEDURE_PROGRESS);

	}

	public boolean updateAcceptanceProcedureProgress(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_PROCEDURE_PROGRESS) > 0;
	}

	public Map<String, Object> getAcceptanceProcedureProgress(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_PROCEDURE_PROGRESS);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_PROCEDURE_PROGRESS,id);
	}
	
    public List<Map<String, Object>> getAcceptanceProcedureProgressList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_PROCEDURE_PROGRESS);
		return list;
	}

	public boolean deleteAcceptanceProcedureProgress(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_PROCEDURE_PROGRESS) > 0;
	}
}