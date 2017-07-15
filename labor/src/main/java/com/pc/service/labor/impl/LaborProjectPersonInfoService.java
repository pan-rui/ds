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
import com.pc.dao.labor.LaborProjectPersonInfoDao;
import com.pc.service.BaseService;

@Service
public class LaborProjectPersonInfoService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());
    
    @Autowired
	private LaborProjectPersonInfoDao laborProjectPersonInfoDao;
    
    public List<Map<String, Object>> getLaborProjectPersonInfoFailList(Map<String, Object> params, String ddBB) {
    	params.put("lppTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PROJECT_PERSON_INFO);
    	params.put("lpTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PERSON_INFO);
		return laborProjectPersonInfoDao.queryLaborProjectPersonInfoFailListInTab(params);
	}
    
    public List<Map<String, Object>> getLaborProjectList(String ddBB,String personId) {
    	Map<String, Object> params=new HashMap<>();
    	params.put("lppTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PROJECT_PERSON_INFO);
    	params.put("pTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD);
    	params.put(TableConstants.LaborProjectPersonInfo.personId.name(), personId);
		return laborProjectPersonInfoDao.queryLaborProjectListInTab(params);
	}
    
    public Page getLaborProjectPersonInfoDetailPage(Page<Map<String, Object>> page,String ddBB) {
    	Map<String, Object> params=new HashMap<>(page.getParams());
    	params.put("lppTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PROJECT_PERSON_INFO);
    	params.put("lpTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PERSON_INFO);
    	params.put("cTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_CONTRACTOR_COMPANY_INFO);
    	params.put("page", page);
    	page.setResults(laborProjectPersonInfoDao.queryLaborProjectPersonInfoDetailPageInTab(params));
		return page;
	}
    
    public List<Map<String, Object>> getLaborPersonListByFileType(String projectId, String type, String ddBB) {
    	Map<String, Object> params=new HashMap<>();
    	params.put("type", type);
    	params.put(TableConstants.LaborContractorCompanyInfo.PROJECT_ID.name(), projectId);
    	params.put("lppTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PROJECT_PERSON_INFO);
    	params.put("lpTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PERSON_INFO);
    	
		return laborProjectPersonInfoDao.queryLaborPersonListByFileTypeInTab(params);
	}
    
    public List<Map<String, Object>> getLaborPersonToPushList(String projectId, String ddBB) {
    	Map<String, Object> params=new HashMap<>();
    	params.put(TableConstants.LaborContractorCompanyInfo.PROJECT_ID.name(), projectId);
    	params.put("lppTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PROJECT_PERSON_INFO);
    	params.put("lpTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PERSON_INFO);
    	params.put("lcciTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_CONTRACTOR_COMPANY_INFO);
    	params.put("leciTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_EMP_CATEGORY_INFO);
    	params.put("ljniTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_JOB_NAME_INFO);
    	params.put("ljtiTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_JOB_TYPENAME_INFO);
    	params.put("lwtiTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_WORK_TYPENAME_INFO);
    	params.put("lpiTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD);
    	
		return laborProjectPersonInfoDao.queryLaborPersonToPushListInTab(params);
	}
     
    public void addLaborProjectPersonInfo(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PROJECT_PERSON_INFO);
	}
    
    public void addLaborProjectPersonInfoList(List<Map<String, Object>> params, String ddBB) {
		super.addOrUpdateList(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PROJECT_PERSON_INFO);
	}
    
	public Page getLaborProjectPersonInfoPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PROJECT_PERSON_INFO);

	}

	public boolean updateLaborProjectPersonInfo(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PROJECT_PERSON_INFO) > 0;
	}

	public Map<String, Object> getLaborProjectPersonInfo(Map<String, Object> params, String ddBB) {
		Map<String, Object> orderparams=new HashMap<>();
    	orderparams.put(TableConstants.LaborProjectPersonInfo.UPDATE_TIME.name(), TableConstants.ORDER_BY_DESC);
		List<Map<String, Object>> list = queryList(params, orderparams,
				ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PROJECT_PERSON_INFO);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

        public List<Map<String, Object>> getLaborProjectPersonInfoList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PROJECT_PERSON_INFO);
		return list;
	}
        public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PROJECT_PERSON_INFO,id);
	}


	public boolean deleteLaborProjectPersonInfo(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PROJECT_PERSON_INFO) > 0;
	}
}