package com.pc.controller.acceptance;

import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.DataConstants;
import com.pc.core.Page;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.service.acceptance.impl.AcceptanceBatchService;
import com.pc.service.acceptance.impl.AcceptanceNoteModifyService;
import com.pc.service.acceptance.impl.AcceptanceNoteService;
import com.pc.service.acceptance.impl.ProcedureBatchStatusService;
import com.pc.service.organization.impl.CompanyService;
import com.pc.service.procedure.impl.ProcedureInfoService;
import com.pc.service.project.impl.ProjectBuildingService;
import com.pc.service.project.impl.ProjectHouseholdService;
import com.pc.service.project.impl.ProjectInfoService;
import com.pc.service.project.impl.ProjectPeriodService;
import com.pc.util.DateUtil;
import com.pc.util.JPushUtil;
import com.pc.vo.ParamsVo;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: wady (2017-03-28 20:19)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-28
 */
@Controller
@RequestMapping("/client")
public class AcceptanceNoteController extends BaseController {
	@Autowired
	private AcceptanceNoteService acceptanceNoteService;
	@Autowired
	private ProjectInfoService projectInfoService;
	@Autowired
	private ProjectPeriodService projectPeriodService;
	@Autowired
	private ProcedureInfoService procedureInfoService;
	@Autowired
	private ProjectBuildingService projectBuildingService;
	@Autowired
	private ProjectHouseholdService projectHouseholdService;
	@Autowired
	private AcceptanceBatchService acceptanceBatchService;
	@Autowired
	private AcceptanceNoteModifyService acceptanceNoteModifyService;
	@Autowired
	private ProcedureBatchStatusService procedureBatchStatusService;
	@Autowired
	private CompanyService companyService;

	private Logger logger = LogManager.getLogger(this.getClass());
	
	
	/**
	 * 获取报验记录
	 * @param userId
	 * @param tenantId
	 * @param page
	 * @param ddBB
	 * @return
	 */
	@RequestMapping("/acceptanceNote/getPageByTeam")
	@ResponseBody
	public BaseResult getPageByTeam(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		map.put(TableConstants.AcceptanceNote.PROJECT_PERIOD_ID.name(), (String) page.getParams()
				.get(TableConstants.ProjectBuilding.PROJECT_PERIOD_ID.name()));
		map.put(TableConstants.AcceptanceNote.TEAM_INSPECTOR_ID.name(), userId);
		page.setParams(map);
		
		Map<String, Object> orderMap = new LinkedHashMap<>();
		orderMap.put(TableConstants.AcceptanceNote.TEAM_INSPECTOR_CHECK_TIME.name(), TableConstants.ORDER_BY_DESC);
		page.setOrderMap(orderMap);
		return new BaseResult(ReturnCode.OK, acceptanceNoteService.getAcceptanceNotePage(page, ddBB));
	}
	
	/**
	 * 报验
	 * @param userId
	 * @param tenantId
	 * @param params
	 * @param ddBB
	 * @return
	 */
	@RequestMapping("/acceptanceNote/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {

		String projectPeriodId = (String) params.getParams()
				.get(TableConstants.ProjectBuilding.PROJECT_PERIOD_ID.name());
		Map<String, Object> projectPeriod = projectPeriodService.getByID(projectPeriodId, ddBB);
		if(projectPeriod==null){
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}
		Map<String, Object> projectInfo = projectInfoService.getByID((String)projectPeriod.get(TableConstants.ProjectPeriod.projectId.name()), ddBB);
		
		String constructionInspectorId = (String) params.getParams()
				.get(TableConstants.AcceptanceNote.CONSTRUCTION_INSPECTOR_ID.name());
		String constructionInspector = (String) params.getParams()
				.get(TableConstants.AcceptanceNote.CONSTRUCTION_INSPECTOR.name());

		String teamInspector = (String) params.getParams().get(TableConstants.AcceptanceNote.TEAM_INSPECTOR.name());
		String teamInspectorId = (String) params.getParams()
				.get(TableConstants.AcceptanceNote.TEAM_INSPECTOR_ID.name());
		
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
		
		String regionId = (String) params.getParams().get(TableConstants.AcceptanceNote.REGION_ID.name());
		String regionName = (String) params.getParams().get(TableConstants.AcceptanceNote.REGION_NAME.name());
		String regionType = (String) params.getParams().get(TableConstants.AcceptanceNote.REGION_TYPE.name());

		if (StringUtils.isBlank(projectPeriodId) || StringUtils.isBlank(constructionInspectorId)
				|| StringUtils.isBlank(constructionInspector) || StringUtils.isBlank(teamInspector)
				|| StringUtils.isBlank(teamInspectorId) || StringUtils.isBlank(regionId)
				|| StringUtils.isBlank(regionName) || StringUtils.isBlank(regionType)) {
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}

		// 部位信息
		Map<String, Object> period=null;
		String regionMinName=null;
		if (DataConstants.REGION_PERIOD_TYPE_VAL.equals(regionType)) {
			period = projectPeriodService.getByID(regionId, ddBB);
			if(period==null){
				return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
			}
			regionMinName=(String) period.get(TableConstants.ProjectPeriod.PERIOD_NAME.name());
		} else if (DataConstants.REGION_BUILDING_TYPE_VAL.equals(regionType)) {
			period = projectBuildingService.getByID(regionId, ddBB);
			if(period==null){
				return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
			}
			regionMinName=(String) period.get(TableConstants.ProjectBuilding.buildingName.name());
		} else if (DataConstants.REGION_FLOOR_TYPE_VAL.equals(regionType)) {
			period = projectHouseholdService.getByID(regionId, ddBB);
			if(period==null){
				return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
			}
			regionMinName=(String) period.get(TableConstants.ProjectHousehold.roomName.name());
		} else if (DataConstants.REGION_ROOM_TYPE_VAL.equals(regionType)) {
			period = projectHouseholdService.getByID(regionId, ddBB);
			if(period==null){
				return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
			}
			regionMinName=(String) period.get(TableConstants.ProjectHousehold.roomName.name());
		} else {
			logger.info("部位类型不正常");
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}

		String procedureId = (String) params.getParams().get(TableConstants.AcceptanceNote.PROCEDURE_ID.name());
		String procedureName = (String) params.getParams().get(TableConstants.AcceptanceNote.PROCEDURE_NAME.name());
		Map<String, Object> procedure=procedureInfoService.getByID(procedureId, ddBB);
		if(procedure==null){
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}

		Map<String, Object> map = acceptanceNoteService
				.getAcceptanceNote(ParamsMap.newMap(TableConstants.AcceptanceNote.PROCEDURE_ID.name(), procedureId)
						.addParams(TableConstants.AcceptanceNote.REGION_ID.name(), regionId)
						.addParams(TableConstants.AcceptanceNote.REGION_TYPE.name(), regionType)
						.addParams(TableConstants.IS_SEALED, 0), ddBB);
		
		String acceptanceNoteId=null;
		
		if (map!=null) {
			if(!DataConstants.PROCEDURE_STATUS_ID_SGZ.equals((String) map.get(TableConstants.AcceptanceNote.statementId.name()))){
				return new BaseResult(ReturnCode.ACCEPTANCE_EXIST);
			}
			
			Map<String, Object> nmap = new HashMap<>();
			acceptanceNoteId=(String) map.get(TableConstants.AcceptanceNote.id.name());
			
			nmap.put(TableConstants.AcceptanceNote.ID.name(), acceptanceNoteId);
			nmap.put(TableConstants.AcceptanceNote.STATEMENT_ID.name(), DataConstants.PROCEDURE_STATUS_ID_YBY);
			nmap.put(TableConstants.AcceptanceNote.BATCH_TIMES.name(), 1);
			nmap.put(TableConstants.AcceptanceNote.CHECK_TIMES.name(), 0);
			nmap.put(TableConstants.AcceptanceNote.CONSTRUCTION_INSPECTOR.name(), constructionInspector);
			nmap.put(TableConstants.AcceptanceNote.CONSTRUCTION_INSPECTOR_ID.name(), constructionInspectorId);

			nmap.put(TableConstants.AcceptanceNote.TEAM_INSPECTOR_CHECKED.name(), 0);
			nmap.put(TableConstants.AcceptanceNote.TEAM_INSPECTOR_CHECK_TIME.name(),
					DateUtil.convertDateTimeToString(new Date(), null));
			nmap.put(TableConstants.AcceptanceNote.TEAM_INSPECTOR.name(), teamInspector);
			nmap.put(TableConstants.AcceptanceNote.TEAM_INSPECTOR_ID.name(), teamInspectorId);

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

			map.put(TableConstants.AcceptanceNote.STATEMENT_ID.name(), DataConstants.PROCEDURE_STATUS_ID_YBY);
			map.put(TableConstants.AcceptanceNote.BATCH_TIMES.name(), 1);
			map.put(TableConstants.AcceptanceNote.CHECK_TIMES.name(), 0);
			map.put(TableConstants.AcceptanceNote.CONSTRUCTION_INSPECTOR.name(), constructionInspector);
			map.put(TableConstants.AcceptanceNote.CONSTRUCTION_INSPECTOR_ID.name(), constructionInspectorId);

			map.put(TableConstants.AcceptanceNote.TEAM_INSPECTOR_CHECKED.name(), 0);
			map.put(TableConstants.AcceptanceNote.TEAM_INSPECTOR_CHECK_TIME.name(),
					DateUtil.convertDateTimeToString(new Date(), null));
			map.put(TableConstants.AcceptanceNote.TEAM_INSPECTOR.name(), teamInspector);
			map.put(TableConstants.AcceptanceNote.TEAM_INSPECTOR_ID.name(), teamInspectorId);

			acceptanceNoteService.addAcceptanceNote(map, ddBB);
			
			acceptanceNoteId=(String) map.get(TableConstants.AcceptanceNote.ID.name());
		}
		
		List<Map<String, Object>> list=acceptanceBatchService.getAcceptanceBatchList(ParamsMap.newMap(TableConstants.AcceptanceBatch.ACCEPTANCE_NOTE_ID.name(), acceptanceNoteId), ddBB);
		if(list==null||list.size()==0){
			//新建一批次
			Map<String, Object> batchMap = new HashMap<>();
			batchMap.put(TableConstants.TENANT_ID, tenantId);
			batchMap.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
			batchMap.put(TableConstants.UPDATE_USER_ID, userId);
			batchMap.put(TableConstants.IS_SEALED, 0);
			batchMap.put(TableConstants.AcceptanceBatch.ACCEPTANCE_NOTE_ID.name(), acceptanceNoteId);
			batchMap.put(TableConstants.AcceptanceBatch.BATCH_NO.name(), 1);
			batchMap.put(TableConstants.AcceptanceBatch.BATCH_ACCEPTANCE_NO.name(), 1);
			batchMap.put(TableConstants.AcceptanceBatch.MIN_PASS_RATIO.name(), (Float)procedure.get(TableConstants.AcceptanceBatch.minPassRatio.name()));
			batchMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER);
			
			Map<String, Object> zjBatchMap = new HashMap<>(batchMap);
			zjBatchMap.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), DataConstants.INSPECTOR_ROLE_ZJ);
			zjBatchMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(), DataConstants.BATCH_STATUS_ID_ZJWYS);
			zjBatchMap.put(TableConstants.AcceptanceBatch.ACCEPTANCE_PERSON_ID.name(), constructionInspectorId);
			zjBatchMap.put(TableConstants.AcceptanceBatch.ACCEPTER.name(), constructionInspector);
			acceptanceBatchService.addAcceptanceBatch(zjBatchMap, ddBB);
			
			Map<String, Object> jlBatchMap = new HashMap<>(batchMap);
			jlBatchMap.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), DataConstants.INSPECTOR_ROLE_JL);
			jlBatchMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(), DataConstants.BATCH_STATUS_ID_JLWYS);
			acceptanceBatchService.addAcceptanceBatch(jlBatchMap, ddBB);
			
			//加入批次状态
			Map<String, Object> batchStatusMap = new HashMap<>();
			batchStatusMap.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
			batchStatusMap.put(TableConstants.UPDATE_USER_ID, userId);
			batchStatusMap.put(TableConstants.IS_SEALED, 0);
			batchStatusMap.put(TableConstants.ProcedureBatchStatus.ACCEPTANCE_NOTE_ID.name(), acceptanceNoteId);
			batchStatusMap.put(TableConstants.ProcedureBatchStatus.PROCEDURE_ID.name(), procedureId);
			batchStatusMap.put(TableConstants.ProcedureBatchStatus.BATCH_NO.name(), 1);
			batchStatusMap.put(TableConstants.ProcedureBatchStatus.STATEMENT_ID.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_ID_YBY);
			procedureBatchStatusService.addProcedureBatchStatus(batchStatusMap, ddBB);
		}
		
		//加入推送
		JPushUtil.push(constructionInspectorId,"新任务","新任务：部位-"+regionName+"，工序-"+procedureName);
		
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/acceptanceNote/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = acceptanceNoteService.deleteAcceptanceNote(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/acceptanceNote/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = acceptanceNoteService.updateAcceptanceNote(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/acceptanceNote/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, acceptanceNoteService.getAcceptanceNote(map, ddBB));
	}

	@RequestMapping("/acceptanceNote/getList")
	@ResponseBody
	public BaseResult getList(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, acceptanceNoteService.getAcceptanceNoteList(map, ddBB));
	}

	@RequestMapping("/acceptanceNote/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, acceptanceNoteService.getAcceptanceNotePage(page, ddBB));
	}
}
