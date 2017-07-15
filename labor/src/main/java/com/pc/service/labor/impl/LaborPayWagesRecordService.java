package com.pc.service.labor.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.labor.LaborPayWagesRecordDao;
import com.pc.dao.labor.LaborTrainingDetailInfoDao;
import com.pc.service.BaseService;

@Service
public class LaborPayWagesRecordService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());
    
    @Autowired
	private LaborPayWagesRecordDao laborPayWagesRecordDao;
    
    public List<Map<String, Object>> getLaborPayWagesRecordToPushList(String projectId, String ddBB) {
		Map<String, Object> params = new HashMap<>();
		params.put(TableConstants.LaborContractorCompanyInfo.PROJECT_ID.name(), projectId);
		params.put("lpwiTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PAY_WAGES_INFO);
		params.put("lpwrTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PAY_WAGES_RECORD);
		params.put("lppTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PROJECT_PERSON_INFO);
		params.put("lpTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PERSON_INFO);
		return laborPayWagesRecordDao.queryLaborPayWagesRecordToPushListInTab(params);
	}
    
    
    public void addLaborPayWagesRecordList(List<Map<String, Object>> params, String ddBB) {
		super.addOrUpdateList(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PAY_WAGES_RECORD);
	}
    
    public void addLaborPayWagesRecord(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PAY_WAGES_RECORD);
	}

	public Page getLaborPayWagesRecordPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PAY_WAGES_RECORD);

	}

	public boolean updateLaborPayWagesRecord(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PAY_WAGES_RECORD) > 0;
	}

	public Map<String, Object> getLaborPayWagesRecord(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PAY_WAGES_RECORD);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

        public List<Map<String, Object>> getLaborPayWagesRecordList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PAY_WAGES_RECORD);
		return list;
	}
        public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PAY_WAGES_RECORD,id);
	}


	public boolean deleteLaborPayWagesRecord(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PAY_WAGES_RECORD) > 0;
	}
}