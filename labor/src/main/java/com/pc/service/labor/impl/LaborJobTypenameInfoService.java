package com.pc.service.labor.impl;

import java.util.List;
import java.util.Map;
 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.BaseService;

@Service
public class LaborJobTypenameInfoService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

     
    public void addLaborJobTypenameInfo(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_JOB_TYPENAME_INFO);
	}

	public Page getLaborJobTypenameInfoPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_JOB_TYPENAME_INFO);

	}

	public boolean updateLaborJobTypenameInfo(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_JOB_TYPENAME_INFO) > 0;
	}

	public Map<String, Object> getLaborJobTypenameInfo(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.LABOR_JOB_TYPENAME_INFO);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

        public List<Map<String, Object>> getLaborJobTypenameInfoList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.LABOR_JOB_TYPENAME_INFO);
		return list;
	}
        public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_JOB_TYPENAME_INFO,id);
	}


	public boolean deleteLaborJobTypenameInfo(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_JOB_TYPENAME_INFO) > 0;
	}
	
	public void addList(List<Map<String, Object>> list, String ddBB) {
		super.addList(list, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_JOB_TYPENAME_INFO);
	}
}