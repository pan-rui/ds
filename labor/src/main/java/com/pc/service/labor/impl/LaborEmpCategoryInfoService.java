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
public class LaborEmpCategoryInfoService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

     
    public void addLaborEmpCategoryInfo(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_EMP_CATEGORY_INFO);
	}

	public Page getLaborEmpCategoryInfoPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_EMP_CATEGORY_INFO);

	}

	public boolean updateLaborEmpCategoryInfo(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_EMP_CATEGORY_INFO) > 0;
	}

	public Map<String, Object> getLaborEmpCategoryInfo(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.LABOR_EMP_CATEGORY_INFO);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

        public List<Map<String, Object>> getLaborEmpCategoryInfoList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.LABOR_EMP_CATEGORY_INFO);
		return list;
	}
        public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_EMP_CATEGORY_INFO,id);
	}


	public boolean deleteLaborEmpCategoryInfo(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_EMP_CATEGORY_INFO) > 0;
	}
	
	public void addList(List<Map<String, Object>> list, String ddBB) {
		super.addList(list, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_EMP_CATEGORY_INFO);
	}
}