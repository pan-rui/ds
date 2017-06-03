package com.pc.service.acceptance.impl;

import com.pc.core.DataConstants;
import com.pc.core.Page;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.dao.acceptance.AcceptanceBatchDao;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AcceptanceBatchService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());
    
    @Autowired
    private AcceptanceBatchDao acceptanceBatchDao;
    
    public Page getAllAcceptanceBatchByNotic(Page page,String userId, String inspectorRole, String ddBB) {
    	Map<String, Object> params=new HashMap<>();
		params.put("abTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_BATCH);
		params.put("anTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE);
		params.put("pbsTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_BATCH_STATUS);
		params.put(TableConstants.AcceptanceBatch.batchStatus.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER);
		params.put("inspectorRole", inspectorRole);
		params.put("userId", userId);
		params.put("isNoticeJL", 0);
		params.put("statementId", DataConstants.ACCEPTANCE_BATCH_STATUS_ID_ZJYYS);
		params.put("batchStatusId", DataConstants.BATCH_STATUS_ID_ZJYYS);
		params.put("page", page);
		page.setResults(acceptanceBatchDao.queryAllAcceptanceBatchByNoticePageInTab(params));
    	return page;
    }
    
    public List<Map<String, Object>> getAllAcceptanceBatchByPost(boolean hasFloorType,boolean hasRoomType,String batchStatusId, String projectPeriodId, String inspectorRole, String procedureId, String regionType, String regionId, String ddBB) {
    	Map<String, Object> params=new HashMap<>();
    	params.put("pTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_INFO);
		params.put("abTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_BATCH);
		params.put("anTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE);
		params.put("pbsTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_BATCH_STATUS);
		
		params.put("inspectorRole", inspectorRole);
		params.put("batchStatusId", batchStatusId);
		params.put("batchStatusId", batchStatusId);
		params.put("statementId", DataConstants.ACCEPTANCE_BATCH_STATUS_ID_WBY);
		
		params.put(TableConstants.AcceptanceBatch.batchStatus.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER);
		params.put("projectPeriodId", projectPeriodId);
		
		if(procedureId!=null){
			params.put("procedureId", procedureId);
		}
    	
    	if(DataConstants.REGION_PERIOD_TYPE_VAL.equals(regionType)){
    		params.put("tid", regionId);
    		params.put("tableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD);
			params.put("regionMinName", TableConstants.ProjectPeriod.PERIOD_NAME.name());
			params.put("regionType", regionType);
		}else if(DataConstants.REGION_BUILDING_TYPE_VAL.equals(regionType)){
			params.put("tid", regionId);
    		params.put("tableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
			params.put("regionMinName", TableConstants.ProjectBuilding.BUILDING_NAME.name());
			params.put("regionType", regionType);
		}else{
			Map<String, Object> period = super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD,regionId);
			params.put("buildingId", period.get(TableConstants.ProjectHousehold.buildingId.name()));
			params.put("floor", period.get(TableConstants.ProjectHousehold.floor.name()));
			params.put("tableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
			params.put("regionMinName", TableConstants.ProjectHousehold.ROOM_NAME.name());
			if(hasFloorType&&(!hasRoomType)){
				params.put("regionType", DataConstants.REGION_FLOOR_TYPE_VAL);
			}
			if((!hasFloorType)&&hasRoomType){
				params.put("regionType", DataConstants.REGION_ROOM_TYPE_VAL);
			}
		}
		
		return acceptanceBatchDao.queryAllAcceptanceBatchByPostInTab(params);
	}
    
    public Map<String, Object> getBatchInfo(String id, String ddBB) {
    	Map<String, Object> paramsMap = ParamsMap.newMap(TableConstants.AcceptanceBatch.id.name(), id);
    	paramsMap.put("abTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_BATCH);
    	paramsMap.put("anTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE);
		return acceptanceBatchDao.queryBatchInfoInTab(paramsMap);
	}
    
    public void addAcceptanceBatch(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_BATCH);
	}

	public Page getPageByStatus(Page<Map<String, Object>> page, String acceptanceStatus, String batchStatus, String userId, String ddBB) {
		Map<String, Object> paramsMap = ParamsMap.newMap("page", page).addParams("abTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_BATCH)
				.addParams("pbsTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_BATCH_STATUS).addParams("anTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE)
				.addParams("userId", userId).addParams("pbsStatementId", acceptanceStatus).addParams("anStatementId", DataConstants.PROCEDURE_STATUS_ID_SGZ)
				.addParams("projectPeriodId", (String)page.getParams().get(TableConstants.AcceptanceNote.PROJECT_PERIOD_ID.name()))
				.addParams(TableConstants.AcceptanceBatch.batchStatus.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER)
				.addParams(TableConstants.AcceptanceBatch.batchStatusId.name(), batchStatus);
		page.setResults(acceptanceBatchDao.queryAcceptanceBatchByStatusPageInTab(paramsMap));
		return page;
	}
	
	public Page getAcceptanceBatchPage(Page<Map<String, Object>> page, String ddBB) {
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_BATCH);
	}

	public boolean updateAcceptanceBatch(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_BATCH) > 0;
	}

	public Map<String, Object> getAcceptanceBatch(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_BATCH);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_BATCH,id);
	}
	
    public List<Map<String, Object>> getAcceptanceBatchList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_BATCH);
		return list;
	}

	public boolean deleteAcceptanceBatch(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_BATCH) > 0;
	}
}