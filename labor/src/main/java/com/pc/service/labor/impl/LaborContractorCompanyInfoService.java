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
import com.pc.dao.labor.LaborContractorCompanyInfoDao;
import com.pc.service.BaseService;

@Service
public class LaborContractorCompanyInfoService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());
    
    @Autowired
	private LaborContractorCompanyInfoDao laborContractorCompanyInfoDao;
    
    public List<Map<String, Object>> getLaborCompanyToPushList(String projectId, String ddBB) {
    	Map<String, Object> params=new HashMap<>();
    	params.put(TableConstants.LaborContractorCompanyInfo.PROJECT_ID.name(), projectId);
    	params.put("ctTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_COMPANY_TYPE_INFO);
    	params.put("cTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_CONTRACTOR_COMPANY_INFO);
    	params.put("pTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD);
		return laborContractorCompanyInfoDao.queryLaborCompanyToPushListInTab(params);
	}
     
    public void addLaborContractorCompanyInfo(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_CONTRACTOR_COMPANY_INFO);
	}
    
    public void addLaborContractorCompanyInfoList(List<Map<String, Object>> params, String ddBB) {
		super.addList(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_CONTRACTOR_COMPANY_INFO);
	}

	public Page getLaborContractorCompanyInfoPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_CONTRACTOR_COMPANY_INFO);

	}

	public boolean updateLaborContractorCompanyInfo(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_CONTRACTOR_COMPANY_INFO) > 0;
	}

	public Map<String, Object> getLaborContractorCompanyInfo(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.LABOR_CONTRACTOR_COMPANY_INFO);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

    public List<Map<String, Object>> getLaborContractorCompanyInfoList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.LABOR_CONTRACTOR_COMPANY_INFO);
		return list;
	}
        public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_CONTRACTOR_COMPANY_INFO,id);
	}


	public boolean deleteLaborContractorCompanyInfo(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_CONTRACTOR_COMPANY_INFO) > 0;
	}
}