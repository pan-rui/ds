package com.pc.service.labor.impl;

import java.util.List;
import java.util.Map;

import com.pc.dao.labor.LaborPayWagesInfoDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.BaseService;

@Service
public class LaborPayWagesInfoService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());
	@Autowired
    private LaborPayWagesInfoDao laborPayWagesInfoDao;
	
	public void addLaborPayWagesInfoList(List<Map<String, Object>> params, String ddBB) {
		super.addOrUpdateList(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PAY_WAGES_INFO);
	}
     
    public void addLaborPayWagesInfo(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PAY_WAGES_INFO);
	}

	public Page getLaborPayWagesInfoPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return laborPayWagesInfoDao.queryPayWagesPageInTab(ddBB,page);

	}

	public boolean updateLaborPayWagesInfo(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PAY_WAGES_INFO) > 0;
	}

	public Map<String, Object> getLaborPayWagesInfo(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PAY_WAGES_INFO);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

        public List<Map<String, Object>> getLaborPayWagesInfoList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PAY_WAGES_INFO);
		return list;
	}
        public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PAY_WAGES_INFO,id);
	}


	public boolean deleteLaborPayWagesInfo(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PAY_WAGES_INFO) > 0;
	}
}