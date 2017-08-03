package com.pc.service.acceptance.impl;

import com.pc.core.DataConstants;
import com.pc.core.Page;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.dao.acceptance.AcceptanceBatchDao;
import com.pc.service.BaseService;
import com.pc.service.organization.impl.CompanyService;
import com.pc.service.procedure.impl.ProcedureInfoService;
import com.pc.service.project.impl.ProjectBuildingService;
import com.pc.service.project.impl.ProjectHouseholdService;
import com.pc.service.project.impl.ProjectInfoService;
import com.pc.service.project.impl.ProjectPeriodService;
import com.pc.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AcceptanceBatchService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());
    
    @Autowired
    private AcceptanceBatchDao acceptanceBatchDao;
    
    @Autowired
    private ProjectPeriodService projectPeriodService;
    
    @Autowired
    private ProjectInfoService projectInfoService;
    
    @Autowired
    private CompanyService companyService;
    
    @Autowired
    private ProjectBuildingService projectBuildingService;
    
    @Autowired
    private ProjectHouseholdService projectHouseholdService;
    
    @Autowired
    private ProcedureInfoService procedureInfoService;
    
    @Autowired
    private AcceptanceNoteService acceptanceNoteService;
    
    @Autowired
    private ProcedureBatchStatusService procedureBatchStatusService;
    
    public List<Map<String, Object>> getAcceptanceBatchByQTPost(Map<String, Object> params, String ddBB) {
    	params.put("abTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_BATCH);
		params.put("inspectorRoleJL", DataConstants.INSPECTOR_ROLE_JL);
		params.put("inspectorRoleZJ", DataConstants.INSPECTOR_ROLE_ZJ);
		params.put("inspectorRoleJF", DataConstants.INSPECTOR_ROLE_JF);
		params.put("inspectorRoleYF", DataConstants.INSPECTOR_ROLE_YF);
		
		return acceptanceBatchDao.queryAcceptanceBatchByQTPostInTab(params);
	}
    
    public List<Map<String, Object>> getRegionAcceptanceRecordInfo(String regionType, String regionId, String ddBB) {
    	Map<String, Object> params=new HashMap<>();
    	params.put("pbsTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_BATCH_STATUS);
    	params.put("abTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_BATCH);
		params.put("anTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE);
		params.put(TableConstants.AcceptanceNote.REGION_TYPE.name(), regionType);
		params.put(TableConstants.AcceptanceNote.REGION_ID.name(), regionId);
		params.put(TableConstants.AcceptanceBatch.BATCH_STATUS.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER);
		params.put("inspectorRoleJL", DataConstants.INSPECTOR_ROLE_JL);
		params.put("inspectorRoleZJ", DataConstants.INSPECTOR_ROLE_ZJ);
		params.put("inspectorRoleJF", DataConstants.INSPECTOR_ROLE_JF);
		params.put("inspectorRoleYF", DataConstants.INSPECTOR_ROLE_YF);
		
		return acceptanceBatchDao.queryRegionAcceptanceRecordInfoInTab(params);
	}
    
    public List<Map<String, Object>> getTotalCheckedFailTimesByRegion(String regionType, String regionId, String ddBB) {
    	Map<String, Object> params=new HashMap<>();
    	params.put("abTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_BATCH);
		params.put("anTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE);
		params.put(TableConstants.AcceptanceNote.REGION_TYPE.name(), regionType);
		params.put(TableConstants.AcceptanceNote.REGION_ID.name(), regionId);
		params.put(TableConstants.AcceptanceBatch.BATCH_STATUS.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER);
		params.put("batchStatusId1", DataConstants.BATCH_STATUS_ID_ZJNFY);
		params.put("batchStatusId2", DataConstants.BATCH_STATUS_ID_JLNFY);
		params.put("batchStatusId3", DataConstants.BATCH_STATUS_ID_JFNFY);
		params.put("batchStatusId4", DataConstants.BATCH_STATUS_ID_YFNFY);
		params.put("batchStatusId6", DataConstants.BATCH_STATUS_ID_QTNFY);
    	if(DataConstants.REGION_PERIOD_TYPE_VAL.equals(regionType)){
    		params.put("rTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD);
    		params.put("REGION_COLUMN", TableConstants.ProjectPeriod.PROJECT_ID.name());
    	}else if(DataConstants.REGION_BUILDING_TYPE_VAL.equals(regionType)){
    		params.put("rTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
    		params.put("REGION_COLUMN", TableConstants.ProjectBuilding.PROJECT_PERIOD_ID.name());
    	}else if(DataConstants.REGION_FLOOR_TYPE_VAL.equals(regionType)){
    		params.put("rTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
    		params.put("REGION_COLUMN", TableConstants.ProjectHousehold.BUILDING_ID.name());
    	}else if(DataConstants.REGION_ROOM_TYPE_VAL.equals(regionType)){
    		params.put("rTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
    		params.put("REGION_COLUMN", TableConstants.ProjectHousehold.FLOOR_ID.name());
    	}
		
		return acceptanceBatchDao.queryTotalCheckedFailTimesByRegionInTab(params);
	}
    
    public List<Map<String, Object>> getTotalCheckedTimesByRegion(String regionType, String regionId, String ddBB) {
    	Map<String, Object> params=new HashMap<>();
    	params.put("pbsTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_BATCH_STATUS);
		params.put("anTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE);
		params.put(TableConstants.AcceptanceNote.REGION_TYPE.name(), regionType);
		params.put(TableConstants.AcceptanceNote.REGION_ID.name(), regionId);
    	if(DataConstants.REGION_PERIOD_TYPE_VAL.equals(regionType)){
    		params.put("rTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD);
    		params.put("REGION_COLUMN", TableConstants.ProjectPeriod.PROJECT_ID.name());
    	}else if(DataConstants.REGION_BUILDING_TYPE_VAL.equals(regionType)){
    		params.put("rTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
    		params.put("REGION_COLUMN", TableConstants.ProjectBuilding.PROJECT_PERIOD_ID.name());
    	}else if(DataConstants.REGION_FLOOR_TYPE_VAL.equals(regionType)){
    		params.put("rTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
    		params.put("REGION_COLUMN", TableConstants.ProjectHousehold.BUILDING_ID.name());
    	}else if(DataConstants.REGION_ROOM_TYPE_VAL.equals(regionType)){
    		params.put("rTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
    		params.put("REGION_COLUMN", TableConstants.ProjectHousehold.FLOOR_ID.name());
    	}
		
		return acceptanceBatchDao.queryTotalCheckedTimesByRegionInTab(params);
	}
    
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
		
		params.put("batchStatusId", batchStatusId);
		params.put("statementId", DataConstants.ACCEPTANCE_BATCH_STATUS_ID_WBY);
		
		params.put(TableConstants.AcceptanceBatch.batchStatus.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER);
		params.put("projectPeriodId", projectPeriodId);
		
		params.put("inspectorRole", inspectorRole);
		
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
	
	public Map<String, Object> getAcceptanceBatch(Map<String, Object> params, Map<String, Object> orderMap, String ddBB) {
		List<Map<String, Object>> list = queryList(params, orderMap,
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
	
	public String addNoteBatch(String ddBB,String tenantId,String projectPeriodId,String inspectorRole,
			String userId,String regionType,String regionId,String procedureId,String batchNo){
		Map<String, Object> projectPeriod = projectPeriodService.getByID(projectPeriodId, ddBB);
		Map<String, Object> projectInfo = projectInfoService.getByID((String)projectPeriod.get(TableConstants.ProjectPeriod.projectId.name()), ddBB);
		
		String generalContractId=(String) projectInfo.get(TableConstants.ProjectInfo.generalContractId.name());
		String constructionId=(String) projectInfo.get(TableConstants.ProjectInfo.constructionId.name());
		String generalContract=null;
		String construction=null;
		if(generalContractId!=null){
			generalContract=(String)companyService.getByID(generalContractId, ddBB).get(TableConstants.Company.corporateName.name());
		}
		if(constructionId!=null){
			construction=(String)companyService.getByID(constructionId, ddBB).get(TableConstants.Company.corporateName.name());
		}
		
		if (StringUtils.isBlank(projectPeriodId) ||  StringUtils.isBlank(regionId) || StringUtils.isBlank(regionType)) {
			return null;
		}
		
		Map<String, Object> period=null;
		String regionMinName=null;
		String regionName=null;
		if (DataConstants.REGION_PERIOD_TYPE_VAL.equals(regionType)) {
			period = projectPeriodService.getByID(regionId, ddBB);
			if(period==null){
				return null;
			}
			regionMinName=(String) period.get(TableConstants.ProjectPeriod.periodName.name());
			regionName=(String) period.get(TableConstants.ProjectPeriod.periodName.name())+TableConstants.SEPARATE_TREE+DataConstants.REGION_PERIOD_KEY;
		} else if (DataConstants.REGION_BUILDING_TYPE_VAL.equals(regionType)) {
			period = projectBuildingService.getByID(regionId, ddBB);
			if(period==null){
				return null;
			}
			regionMinName=(String) period.get(TableConstants.ProjectBuilding.buildingName.name());
			regionName=(String) period.get(TableConstants.ProjectBuilding.nameTree.name())+TableConstants.SEPARATE_TREE+DataConstants.REGION_BUILDING_KEY;
		} else if (DataConstants.REGION_FLOOR_TYPE_VAL.equals(regionType)) {
			period = projectHouseholdService.getByID(regionId, ddBB);
			if(period==null){
				return null;
			}
			regionMinName=(String) period.get(TableConstants.ProjectHousehold.roomName.name());
			regionName=(String) period.get(TableConstants.ProjectHousehold.nameTree.name())+TableConstants.SEPARATE_TREE + DataConstants.REGION_FLOOR_KEY;
		} else if (DataConstants.REGION_ROOM_TYPE_VAL.equals(regionType)) {
			period = projectHouseholdService.getByID(regionId, ddBB);
			if(period==null){
				return null;
			}
			regionMinName=(String) period.get(TableConstants.ProjectHousehold.roomName.name());
			regionName=((String) period.get(TableConstants.ProjectHousehold.nameTree.name()));
		} else {
			logger.info("部位类型不正常");
			return null;
		}
		
		Map<String, Object> procedure=procedureInfoService.getByID(procedureId, ddBB);
		if(procedure==null){
			return null;
		}
		String procedureName = (String) procedure.get(TableConstants.ProcedureInfo.procedureName.name());

		Map<String, Object> map = acceptanceNoteService
				.getAcceptanceNote(ParamsMap.newMap(TableConstants.AcceptanceNote.PROCEDURE_ID.name(), procedureId)
						.addParams(TableConstants.AcceptanceNote.REGION_ID.name(), regionId)
						.addParams(TableConstants.AcceptanceNote.REGION_TYPE.name(), regionType)
						.addParams(TableConstants.IS_SEALED, 0), ddBB);
		
		String acceptanceNoteId=null;
		
		if (map!=null) {
			Map<String, Object> nmap = new HashMap<>();
			acceptanceNoteId=(String) map.get(TableConstants.AcceptanceNote.id.name());
			nmap.put(TableConstants.AcceptanceNote.ID.name(), acceptanceNoteId);
			nmap.put(TableConstants.AcceptanceNote.STATEMENT_ID.name(), DataConstants.PROCEDURE_STATUS_ID_YBY);
			nmap.put(TableConstants.AcceptanceNote.BATCH_TIMES.name(), batchNo);
			acceptanceNoteService.updateAcceptanceNote(nmap, ddBB);
		} else {
			map = new HashMap<>();
			map.put(TableConstants.TENANT_ID, tenantId);
			map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
			map.put(TableConstants.UPDATE_USER_ID, userId);
			map.put(TableConstants.IS_SEALED, 0);

			map.put(TableConstants.AcceptanceNote.PROJECT_PERIOD_ID.name(), projectPeriodId);
			map.put(TableConstants.AcceptanceNote.PROJECT_NAME.name(),
					(String) projectPeriod.get(TableConstants.ProjectPeriod.periodName.name()));
			
			map.put(TableConstants.AcceptanceNote.SUPERVISOR_COMPANY_ID.name(),generalContractId);
			map.put(TableConstants.AcceptanceNote.CONTRACTING_PRO_ID.name(),constructionId);
			
			map.put(TableConstants.AcceptanceNote.SUPERVISOR_COMPANY.name(),generalContract);
			map.put(TableConstants.AcceptanceNote.CONTRACTING_PRO.name(),construction);
			
			map.put(TableConstants.AcceptanceNote.REGION_ID.name(), regionId);
			map.put(TableConstants.AcceptanceNote.REGION_NAME.name(), regionName);
			map.put(TableConstants.AcceptanceNote.REGION_MIN_NAME.name(), regionMinName);
			map.put(TableConstants.AcceptanceNote.REGION_TYPE.name(), regionType);
			map.put(TableConstants.AcceptanceNote.REGION_ID_TREE.name(),
					(String) period.get(TableConstants.ProjectHousehold.idTree.name()));
			map.put(TableConstants.AcceptanceNote.REGION_NAME_TREE.name(),
					(String) period.get(TableConstants.ProjectHousehold.nameTree.name()));
			map.put(TableConstants.AcceptanceNote.PROCEDURE_ID.name(), procedureId);
			map.put(TableConstants.AcceptanceNote.PROCEDURE_NAME.name(), procedureName);

			map.put(TableConstants.AcceptanceNote.STATEMENT_ID.name(), DataConstants.PROCEDURE_STATUS_ID_SGZ);
			map.put(TableConstants.AcceptanceNote.BATCH_TIMES.name(), 1);
			map.put(TableConstants.AcceptanceNote.CHECK_TIMES.name(), 0);

			map.put(TableConstants.AcceptanceNote.TEAM_INSPECTOR_CHECK_TIME.name(),
					DateUtil.convertDateTimeToString(new Date(), null));

			acceptanceNoteService.addAcceptanceNote(map, ddBB);
			acceptanceNoteId=(String) map.get(TableConstants.AcceptanceNote.ID.name());
		}
		
		Map<String, Object> batchParams=new HashMap<>();
		batchParams.put(TableConstants.AcceptanceBatch.ACCEPTANCE_NOTE_ID.name(), acceptanceNoteId);
		batchParams.put(TableConstants.AcceptanceBatch.BATCH_NO.name(), batchNo);
		batchParams.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), inspectorRole);
		List<Map<String, Object>> list=getAcceptanceBatchList(batchParams, ddBB);
		
		Map<String, Object> zjBatchParams=new HashMap<>();
		zjBatchParams.put(TableConstants.AcceptanceBatch.ACCEPTANCE_NOTE_ID.name(), acceptanceNoteId);
		zjBatchParams.put(TableConstants.AcceptanceBatch.BATCH_NO.name(), batchNo);
		List<Map<String, Object>> zjBatchList=getAcceptanceBatchList(zjBatchParams, ddBB);
		
		
		if(list==null||list.size()==0){
			//新建一批次
			Map<String, Object> batchMap = new HashMap<>();
			batchMap.put(TableConstants.TENANT_ID, tenantId);
			batchMap.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
			batchMap.put(TableConstants.UPDATE_USER_ID, userId);
			batchMap.put(TableConstants.IS_SEALED, 0);
			batchMap.put(TableConstants.AcceptanceBatch.ACCEPTANCE_NOTE_ID.name(), acceptanceNoteId);
			batchMap.put(TableConstants.AcceptanceBatch.BATCH_NO.name(), batchNo);
			batchMap.put(TableConstants.AcceptanceBatch.BATCH_ACCEPTANCE_NO.name(), 1);
			batchMap.put(TableConstants.AcceptanceBatch.MIN_PASS_RATIO.name(), (Float)procedure.get(TableConstants.AcceptanceBatch.minPassRatio.name()));
			batchMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER);
			
			String zjid=UUID.randomUUID().toString().replace("-", "");
			String jlid=UUID.randomUUID().toString().replace("-", "");
			//验收批不存在时才新增
			if(zjBatchList==null||zjBatchList.size()==0||DataConstants.INSPECTOR_ROLE_ZJ.equals(inspectorRole)||DataConstants.INSPECTOR_ROLE_JL.equals(inspectorRole)){
				Map<String, Object> zjBatchMap = new HashMap<>(batchMap);
				zjBatchMap.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), DataConstants.INSPECTOR_ROLE_ZJ);
				zjBatchMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(), DataConstants.BATCH_STATUS_ID_ZJWYS);
				zjBatchMap.put("ID", zjid);
				addAcceptanceBatch(zjBatchMap, ddBB);
				
				Map<String, Object> jlBatchMap = new HashMap<>(batchMap);
				jlBatchMap.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), DataConstants.INSPECTOR_ROLE_JL);
				jlBatchMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(), DataConstants.BATCH_STATUS_ID_JLWYS);
				jlBatchMap.put("ID", jlid);
				addAcceptanceBatch(jlBatchMap, ddBB);
				
				//加入批次状态
				Map<String, Object> batchStatusMap = new HashMap<>();
				batchStatusMap.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
				batchStatusMap.put(TableConstants.UPDATE_USER_ID, userId);
				batchStatusMap.put(TableConstants.IS_SEALED, 0);
				batchStatusMap.put(TableConstants.ProcedureBatchStatus.ACCEPTANCE_NOTE_ID.name(), acceptanceNoteId);
				batchStatusMap.put(TableConstants.ProcedureBatchStatus.PROCEDURE_ID.name(), procedureId);
				batchStatusMap.put(TableConstants.ProcedureBatchStatus.BATCH_NO.name(), batchNo);
				batchStatusMap.put(TableConstants.ProcedureBatchStatus.STATEMENT_ID.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_ID_WBY);
				procedureBatchStatusService.addProcedureBatchStatus(batchStatusMap, ddBB);
			}
			
			if (DataConstants.INSPECTOR_ROLE_ZJ.equals(inspectorRole)) {
				return zjid;
			}else if(DataConstants.INSPECTOR_ROLE_JL.equals(inspectorRole)){
				return jlid;
			}else if(DataConstants.INSPECTOR_ROLE_JF.equals(inspectorRole)){
				String jfcyid=UUID.randomUUID().toString().replace("-", "");
				Map<String, Object> jfcyBatchMap = new HashMap<>(batchMap);
				jfcyBatchMap.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), DataConstants.INSPECTOR_ROLE_JF);
				jfcyBatchMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(), DataConstants.BATCH_STATUS_ID_JFWYS);
				jfcyBatchMap.put("ID", jfcyid);
				addAcceptanceBatch(jfcyBatchMap, ddBB);
				return jfcyid;
			}else if(DataConstants.INSPECTOR_ROLE_YF.equals(inspectorRole)){
				String yfcyid=UUID.randomUUID().toString().replace("-", "");
				Map<String, Object> yfcyBatchMap = new HashMap<>(batchMap);
				yfcyBatchMap.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), DataConstants.INSPECTOR_ROLE_YF);
				yfcyBatchMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(), DataConstants.BATCH_STATUS_ID_YFWYS);
				yfcyBatchMap.put("ID", yfcyid);
				addAcceptanceBatch(yfcyBatchMap, ddBB);
				return yfcyid;
			}else if(DataConstants.INSPECTOR_ROLE_BZ.equals(inspectorRole)){
				String bzcyid=UUID.randomUUID().toString().replace("-", "");
				Map<String, Object> bzcyBatchMap = new HashMap<>(batchMap);
				bzcyBatchMap.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), DataConstants.INSPECTOR_ROLE_BZ);
				bzcyBatchMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(), DataConstants.BATCH_STATUS_ID_BZWYS);
				bzcyBatchMap.put("ID", bzcyid);
				addAcceptanceBatch(bzcyBatchMap, ddBB);
				return bzcyid;
			}else{
				String qtcyid=UUID.randomUUID().toString().replace("-", "");
				Map<String, Object> qtcyBatchMap = new HashMap<>(batchMap);
				qtcyBatchMap.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), DataConstants.INSPECTOR_ROLE_QT);
				qtcyBatchMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(), DataConstants.BATCH_STATUS_ID_QTWYS);
				qtcyBatchMap.put("ID", qtcyid);
				addAcceptanceBatch(qtcyBatchMap, ddBB);
				return qtcyid;
			}
		}else{
			return (String) list.get(0).get("id");
		}
	}
	
}