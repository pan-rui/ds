package com.pc.service.labor.impl;

import java.util.List;
import java.util.Map;
 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.labor.LaborUserConstantsDataDao;
import com.pc.service.BaseService;

@Service
public class LaborUserConstantsDataService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

     
    public void addLaborUserConstantsData(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_USER_CONSTANTS_DATA);
	}

	public Page getLaborUserConstantsDataPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_USER_CONSTANTS_DATA);

	}

	public boolean updateLaborUserConstantsData(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_USER_CONSTANTS_DATA) > 0;
	}

	public Map<String, Object> getLaborUserConstantsData(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.LABOR_USER_CONSTANTS_DATA);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

        public List<Map<String, Object>> getLaborUserConstantsDataList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.LABOR_USER_CONSTANTS_DATA);
		return list;
	}
        public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_USER_CONSTANTS_DATA,id);
	}


	public boolean deleteLaborUserConstantsData(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_USER_CONSTANTS_DATA) > 0;
	}
}