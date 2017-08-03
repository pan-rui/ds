package com.pc.service.acceptance.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pc.core.DataConstants;
import com.pc.core.Page;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.dao.acceptance.AcceptanceNoteDao;
import com.pc.service.BaseService;

@Service
public class AcceptanceNoteService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());
    
    @Autowired
    private AcceptanceNoteDao acceptanceNoteDao;
    
    public Map<String, Object> getForUpdate(String id, String ddBB) {
    	Map<String, Object> paramsMap=new HashMap<>();
    	paramsMap.put(TableConstants.AcceptanceNote.ID.name(), id);
    	paramsMap.put("anTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE);
    	List<Map<String, Object>> list = acceptanceNoteDao.queryNoteForUpdateInTab(paramsMap);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
    
    public List<Map<String, Object>> getProjectAcceptanceDetailCount(Map<String, Object> paramsMap, String ddBB) {
    	
    	paramsMap.put("abTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_BATCH);
    	paramsMap.put("anTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE);
    	
    	paramsMap.put("ciTableName", ddBB + TableConstants.SEPARATE + TableConstants.COMPANY);
    	paramsMap.put("uiTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER);
    	paramsMap.put("pTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_INFO);
    	paramsMap.put("ptTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_TYPE);
    	
    	paramsMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER);
    	
    	paramsMap.put("inspectorRoleJL", DataConstants.INSPECTOR_ROLE_JL);
    	paramsMap.put("inspectorRoleZJ", DataConstants.INSPECTOR_ROLE_ZJ);
    	paramsMap.put("inspectorRoleJF", DataConstants.INSPECTOR_ROLE_JF);
    	paramsMap.put("inspectorRoleYF", DataConstants.INSPECTOR_ROLE_YF);
    	
		return acceptanceNoteDao.queryProjectAcceptanceDetailCountInTab(paramsMap);
	}
    
    public List<Map<String, Object>> getProjectAcceptanceCount(Map<String, Object> paramsMap, String ddBB) {
    	
    	paramsMap.put("abTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_BATCH);
    	paramsMap.put("anTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE);
    	paramsMap.put("phTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
    	paramsMap.put("pbTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
    	paramsMap.put("ppTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD);
    	paramsMap.put("hpTableName", ddBB + TableConstants.SEPARATE + TableConstants.HIDDEN_PHOTOS);
    	paramsMap.put("hprTableName", ddBB + TableConstants.SEPARATE + TableConstants.HIDDEN_PHOTOS_RECORD);
    	
    	paramsMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER);
    	paramsMap.put("inspectorRoleJL", DataConstants.INSPECTOR_ROLE_JL);
    	paramsMap.put("inspectorRoleZJ", DataConstants.INSPECTOR_ROLE_ZJ);
    	paramsMap.put("inspectorRoleJF", DataConstants.INSPECTOR_ROLE_JF);
    	paramsMap.put("inspectorRoleYF", DataConstants.INSPECTOR_ROLE_YF);
		return acceptanceNoteDao.queryProjectAcceptanceCountInTab(paramsMap);
	}
    
    public List<Map<String, Object>> getTeamInspectorList(Map<String, Object> paramsMap, String ddBB) {
    	paramsMap.put("turTableName", ddBB + TableConstants.SEPARATE + TableConstants.TEAM_USER_RELATE);
    	paramsMap.put("anTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE);
		return acceptanceNoteDao.queryTeamInspectorListInTab(paramsMap);
	}
    
    public List<Map<String, Object>> getTeamAcceptanceCountByProject(Map<String, Object> paramsMap, String ddBB) {
    	paramsMap.put("tTableName", ddBB + TableConstants.SEPARATE + TableConstants.TEAM_INFO);
    	paramsMap.put("turTableName", ddBB + TableConstants.SEPARATE + TableConstants.TEAM_USER_RELATE);
    	paramsMap.put("anTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE);
		return acceptanceNoteDao.queryTeamAcceptanceCountByProjectInTab(paramsMap);
	}
    
    public List<Map<String, Object>> getAcceptanceNoteByMonth(Map<String, Object> params, String ddBB) {
    	params.put("anTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE);
		return acceptanceNoteDao.queryAcceptanceNoteByMonthInTab(params);
	}
    
    public List<Map<String, Object>> getUserAcceptanceCountByMonth(Map<String, Object> paramsMap, String ddBB) {
    	paramsMap.put("abTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_BATCH);
    	paramsMap.put("anTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE);
		return acceptanceNoteDao.queryUserAcceptanceCountByMonthInTab(paramsMap);
	}
    
    public List<Map<String, Object>> getUserAcceptanceStatisticsByPost(Map<String, Object> paramsMap, String ddBB) {
    	paramsMap.put("abTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_BATCH);
    	paramsMap.put("anTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE);
    	paramsMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER);
		return acceptanceNoteDao.queryUserAcceptanceStatisticsByPostInTab(paramsMap);
	}
    
    public List<Map<String, Object>> getCompanyAcceptanceStatisticsRanking(Map<String, Object> paramsMap, String ddBB) {
    	paramsMap.put("cTableName", ddBB + TableConstants.SEPARATE + TableConstants.COMPANY);
    	paramsMap.put("abTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_BATCH);
    	paramsMap.put("anTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE);
    	paramsMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER);
		return acceptanceNoteDao.queryCompanyAcceptanceStatisticsRankingInTab(paramsMap);
	}
    
    public List<Map<String, Object>> getTeamAcceptanceStatisticsRanking(Map<String, Object> paramsMap, String ddBB) {
    	paramsMap.put("tTableName", ddBB + TableConstants.SEPARATE + TableConstants.TEAM_INFO);
    	paramsMap.put("pprTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PARTNER_RELATE);
    	paramsMap.put("abTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_BATCH);
    	paramsMap.put("anTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE);
    	paramsMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER);
		return acceptanceNoteDao.queryTeamAcceptanceStatisticsRankingInTab(paramsMap);
	}
    
    public List<Map<String, Object>> getProjectAcceptanceStatisticsRanking(Map<String, Object> paramsMap, String ddBB) {
    	paramsMap.put("ppTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD);
    	paramsMap.put("abTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_BATCH);
    	paramsMap.put("anTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE);
    	paramsMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER);
		return acceptanceNoteDao.queryProjectAcceptanceStatisticsRankingInTab(paramsMap);
	}
    
    public void addAcceptanceNote(Map<String, Object> params, String ddBB) {
    	add(params, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE);
	}

	public Page getAcceptanceNotePage(Page<Map<String, Object>> page, String ddBB) {
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE);
	}

	public boolean updateAcceptanceNote(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE) > 0;
	}

	public Map<String, Object> getAcceptanceNote(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE,id);
	}

    public List<Map<String, Object>> getAcceptanceNoteList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE);
		return list;
	}

	public boolean deleteAcceptanceNote(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE) > 0;
	}
}