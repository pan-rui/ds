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
public class ProcedureBatchStatusService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

     
    public void addProcedureBatchStatus(Map<String, Object> params, String ddBB) {
    	add(params, ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_BATCH_STATUS);
	}

	public Page getProcedureBatchStatusPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_BATCH_STATUS);

	}

	public boolean updateProcedureBatchStatus(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_BATCH_STATUS) > 0;
	}

	public Map<String, Object> getProcedureBatchStatus(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_BATCH_STATUS);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_BATCH_STATUS,id);
	}
	
    public List<Map<String, Object>> getProcedureBatchStatusList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_BATCH_STATUS);
		return list;
	}

	public boolean deleteProcedureBatchStatus(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_BATCH_STATUS) > 0;
	}
}