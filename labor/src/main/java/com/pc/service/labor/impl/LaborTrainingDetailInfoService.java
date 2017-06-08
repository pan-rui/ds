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
import com.pc.dao.labor.LaborTrainingDetailInfoDao;
import com.pc.service.BaseService;

@Service
public class LaborTrainingDetailInfoService extends BaseService {
	private Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private LaborTrainingDetailInfoDao laborTrainingDetailInfoDao;

	public List<Map<String, Object>> getLaborTrainingToPushList(String projectId, String ddBB) {
		Map<String, Object> params = new HashMap<>();
		params.put(TableConstants.LaborContractorCompanyInfo.PROJECT_ID.name(), projectId);
		params.put("ltTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_TRAINING_INFO);
		params.put("ltdTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_TRAINING_DETAIL_INFO);
		params.put("lppTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PROJECT_PERSON_INFO);
		params.put("lpTableName", ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PERSON_INFO);
		return laborTrainingDetailInfoDao.queryLaborTrainingToPushListInTab(params);
	}
	
	

	public void addLaborTrainingDetailInfo(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_TRAINING_DETAIL_INFO);
	}
	
	public void addLaborTrainingDetailInfoList(List<Map<String, Object>> params, String ddBB) {
		super.addList(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_TRAINING_DETAIL_INFO);
	}

	public Page getLaborTrainingDetailInfoPage(Page<Map<String, Object>> page, String ddBB) {

		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_TRAINING_DETAIL_INFO);

	}

	public boolean updateLaborTrainingDetailInfo(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_TRAINING_DETAIL_INFO) > 0;
	}

	public Map<String, Object> getLaborTrainingDetailInfo(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.LABOR_TRAINING_DETAIL_INFO);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public List<Map<String, Object>> getLaborTrainingDetailInfoList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.LABOR_TRAINING_DETAIL_INFO);
		return list;
	}

	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_TRAINING_DETAIL_INFO, id);
	}

	public boolean deleteLaborTrainingDetailInfo(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.LABOR_TRAINING_DETAIL_INFO) > 0;
	}
}