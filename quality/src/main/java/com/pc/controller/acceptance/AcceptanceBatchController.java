package com.pc.controller.acceptance;

import com.alibaba.fastjson.JSONObject;
import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.DataConstants;
import com.pc.core.Page;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.service.acceptance.impl.AcceptanceAttachService;
import com.pc.service.acceptance.impl.AcceptanceBatchService;
import com.pc.service.acceptance.impl.AcceptanceDominantItemService;
import com.pc.service.acceptance.impl.AcceptanceGeneralItemService;
import com.pc.service.acceptance.impl.AcceptanceNoteService;
import com.pc.service.acceptance.impl.AcceptancePointService;
import com.pc.service.acceptance.impl.ProcedureBatchStatusService;
import com.pc.service.acceptance.impl.ProcedureScheduleRelateService;
import com.pc.service.organization.impl.CompanyService;
import com.pc.service.organization.impl.PostInfoService;
import com.pc.service.organization.impl.TeamInfoService;
import com.pc.service.procedure.impl.DominantItemService;
import com.pc.service.procedure.impl.GeneralItemService;
import com.pc.service.procedure.impl.ProcedureInfoService;
import com.pc.service.procedure.impl.ProcedureTypeService;
import com.pc.service.project.impl.ProjectBuildingService;
import com.pc.service.project.impl.ProjectHouseholdService;
import com.pc.service.project.impl.ProjectInfoService;
import com.pc.service.project.impl.ProjectPeriodService;
import com.pc.service.project.impl.ProjectRegionProcedureRelateService;
import com.pc.service.user.UserService;
import com.pc.util.DateUtil;
import com.pc.util.ImgUtil;
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

import java.util.ArrayList;
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
public class AcceptanceBatchController extends BaseController {
	
	@Autowired
	private ProjectInfoService projectInfoService;
	
	@Autowired
	private ProjectPeriodService projectPeriodService;
	
	@Autowired
	private ProjectBuildingService projectBuildingService;
	
	@Autowired
	private ProjectHouseholdService projectHouseholdService;
	
	@Autowired
	private AcceptanceBatchService acceptanceBatchService;

	@Autowired
	private ProcedureBatchStatusService procedureBatchStatusService;

	@Autowired
	private AcceptanceNoteService acceptanceNoteService;

	@Autowired
	private UserService userService;

	@Autowired
	private PostInfoService postInfoService;

	@Autowired
	private AcceptanceDominantItemService acceptanceDominantItemService;

	@Autowired
	private AcceptanceGeneralItemService acceptanceGeneralItemService;

	@Autowired
	private GeneralItemService generalItemService;

	@Autowired
	private DominantItemService dominantItemService;

	@Autowired
	private AcceptancePointService acceptancePointService;
	
	@Autowired
	private ProcedureInfoService procedureInfoService;
	
	@Autowired
	private ProcedureTypeService procedureTypeService;

	@Autowired
	private AcceptanceAttachService acceptanceAttachService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private TeamInfoService teamInfoService;
	
	@Autowired
	private ProcedureScheduleRelateService procedureScheduleRelateService;
	
	@Autowired
	private ProjectRegionProcedureRelateService projectRegionProcedureRelateService;

	private Logger logger = LogManager.getLogger(this.getClass());
	
	
	/**
	 * 发送通知
	 * @param userId
	 * @param tenantId
	 * @param ddBB
	 * @return
	 */
	@RequestMapping("/acceptanceBatch/sendBatchNotice")
	@ResponseBody
	public BaseResult sendBatchNotice(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @RequestAttribute String ddBB, 
			@EncryptProcess ParamsVo params) {

		String batchId = (String) params.getParams().get(TableConstants.AcceptanceBatch.ID.name());
		String acceptancePersonId = (String) params.getParams().get(TableConstants.AcceptanceBatch.ACCEPTANCE_PERSON_ID.name());
		if(StringUtils.isBlank(batchId)||StringUtils.isBlank(acceptancePersonId)){
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}
		
		Map<String, Object> acceptanceBatch = acceptanceBatchService.getByID(batchId, ddBB);
		Map<String, Object> acceptanceNote = acceptanceNoteService
				.getByID((String) acceptanceBatch.get(TableConstants.AcceptanceBatch.acceptanceNoteId.name()), ddBB);
		Map<String, Object> acceptancePerson = userService.getByID(acceptancePersonId, ddBB);
		
		if(!DataConstants.CHECK_SUCCESS.equals((String)acceptanceBatch.get(TableConstants.AcceptanceBatch.totalCheckResult.name()))){
			return new BaseResult(ReturnCode.CANNOT_SEND_WITH_CHECK_FAIL);
		}
		
		//加入监理名称
		Map<String, Object> batchParamsMap = new HashMap<>();
		batchParamsMap.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), DataConstants.INSPECTOR_ROLE_JL);
		batchParamsMap.put(TableConstants.AcceptanceBatch.ACCEPTANCE_NOTE_ID.name(),
				(String) acceptanceBatch.get(TableConstants.AcceptanceBatch.acceptanceNoteId.name()));
		batchParamsMap.put(TableConstants.AcceptanceBatch.BATCH_NO.name(),
				acceptanceBatch.get(TableConstants.AcceptanceBatch.batchNo.name()));
		batchParamsMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS.name(),
				DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER);
		Map<String, Object> jlAcceptanceBatch = acceptanceBatchService.getAcceptanceBatch(batchParamsMap, ddBB);
		if(jlAcceptanceBatch==null){
			jlAcceptanceBatch=new HashMap<>();
			jlAcceptanceBatch.put(TableConstants.TENANT_ID, tenantId);
			jlAcceptanceBatch.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
			jlAcceptanceBatch.put(TableConstants.UPDATE_USER_ID, userId);
			jlAcceptanceBatch.put(TableConstants.IS_SEALED, 0);
			jlAcceptanceBatch.put(TableConstants.AcceptanceBatch.ACCEPTANCE_NOTE_ID.name(),
					(String) acceptanceBatch.get(TableConstants.AcceptanceNote.id.name()));
			jlAcceptanceBatch.put(TableConstants.AcceptanceBatch.MIN_PASS_RATIO.name(),
					(Float) acceptanceBatch.get(TableConstants.AcceptanceBatch.minPassRatio.name()));
			jlAcceptanceBatch.put(TableConstants.AcceptanceBatch.BATCH_NO.name(), (Integer)acceptanceBatch.get(TableConstants.AcceptanceBatch.batchNo.name()));
			jlAcceptanceBatch.put(TableConstants.AcceptanceBatch.BATCH_ACCEPTANCE_NO.name(), 1);
			jlAcceptanceBatch.put(TableConstants.AcceptanceBatch.BATCH_STATUS.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER);
			
			jlAcceptanceBatch.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), DataConstants.INSPECTOR_ROLE_JL);
			jlAcceptanceBatch.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(), DataConstants.BATCH_STATUS_ID_JLWYS);
			jlAcceptanceBatch.put(TableConstants.AcceptanceBatch.ACCEPTER.name(),
					(String) acceptancePerson.get(TableConstants.User.realName.name()));
			jlAcceptanceBatch.put(TableConstants.AcceptanceBatch.ACCEPTANCE_PERSON_ID.name(),
					(String) acceptancePerson.get(TableConstants.User.id.name()));
			
			acceptanceBatchService.addAcceptanceBatch(jlAcceptanceBatch, ddBB);
		}else{
			Map<String, Object> updateAcceptanceBatch=new HashMap<>();
			updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.ACCEPTER.name(),
					(String) acceptancePerson.get(TableConstants.User.realName.name()));
			updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.ACCEPTANCE_PERSON_ID.name(),
					(String) acceptancePerson.get(TableConstants.User.id.name()));
			updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.ID.name(), (String)jlAcceptanceBatch.get(TableConstants.AcceptanceBatch.id.name()));
			acceptanceBatchService.updateAcceptanceBatch(updateAcceptanceBatch, ddBB);
		}
		
		//加入推送
		JPushUtil.push(acceptancePersonId,"新任务" ,"新任务：部位-"+acceptanceNote.get(TableConstants.AcceptanceNote.regionName.name())+"，工序-"+
				acceptanceNote.get(TableConstants.AcceptanceNote.procedureName.name()));
		
		//更新状态为已发送
		Map<String, Object> batchStatusParamsMap = new HashMap<>();
		batchStatusParamsMap.put(TableConstants.ProcedureBatchStatus.PROCEDURE_ID.name(),
				acceptanceNote.get(TableConstants.AcceptanceNote.procedureId.name()));
		batchStatusParamsMap.put(TableConstants.ProcedureBatchStatus.BATCH_NO.name(),
				acceptanceBatch.get(TableConstants.AcceptanceBatch.batchNo.name()));
		batchStatusParamsMap.put(TableConstants.ProcedureBatchStatus.ACCEPTANCE_NOTE_ID.name(),
				acceptanceNote.get(TableConstants.AcceptanceNote.id.name()));
		Map<String, Object> oldBatchStatus = procedureBatchStatusService.getProcedureBatchStatus(batchStatusParamsMap,
				ddBB);
		
		Map<String, Object> updateBatchStatus=new HashMap<>();
		updateBatchStatus.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		updateBatchStatus.put(TableConstants.UPDATE_USER_ID, userId);
		updateBatchStatus.put(TableConstants.ProcedureBatchStatus.IS_NOTICE_JL.name(), 1);
		updateBatchStatus.put(TableConstants.ProcedureBatchStatus.ID.name(), (String)oldBatchStatus.get(TableConstants.ProcedureBatchStatus.id.name()));
		procedureBatchStatusService.updateProcedureBatchStatus(updateBatchStatus, ddBB);
		
		return new BaseResult(ReturnCode.OK);
	}
	
	/**
	 * 获取修改任务单列表
	 * @param userId
	 * @param tenantId
	 * @param ddBB
	 * @return
	 */
	@RequestMapping("/acceptanceBatch/getAllAcceptanceBatchByNotice")
	@ResponseBody
	public BaseResult getAllAcceptanceBatchByNotice(@EncryptProcess Page page, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @RequestAttribute String ddBB) {

		Map<String, Object> user = userService.getByID(userId, ddBB);
		if (user.get(TableConstants.User.postId.name()) == null) {
			return new BaseResult(ReturnCode.NO_POST_AUTH);
		}
		Map<String, Object> postInfo = postInfoService
				.getByID((String) user.get(TableConstants.User.postId.name()), ddBB);
		if (postInfo == null) {
			return new BaseResult(ReturnCode.NO_POST_AUTH);
		}
		
		if(!DataConstants.INSPECTOR_ROLE_NAME_ZJ.equals((String)postInfo.get(TableConstants.PostInfo.postName.name()))){
			return new BaseResult(ReturnCode.NO_POST_AUTH);
		}
		
		return new BaseResult(ReturnCode.OK,acceptanceBatchService.getAllAcceptanceBatchByNotic(page,userId, DataConstants.INSPECTOR_ROLE_ZJ, ddBB));
	}

	/**
	 * 新增检验批
	 * 
	 * @param userId
	 * @param tenantId
	 * @param params
	 * @param ddBB
	 * @return
	 */
	@RequestMapping("/acceptanceBatch/addNewBatchAcceptance")
	@ResponseBody
	public BaseResult addNewBatchAcceptance(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {

		String batchId = (String) params.getParams().get(TableConstants.AcceptanceBatch.ID.name());
		
		if (batchId == null) {
			String projectPeriodId = (String) params.getParams().get(TableConstants.AcceptanceNote.PROJECT_PERIOD_ID.name());
			String inspectorRole = (String) params.getParams().get(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name());
			String regionType = (String) params.getParams().get(TableConstants.AcceptanceNote.REGION_TYPE.name());
			String regionId = (String) params.getParams().get(TableConstants.AcceptanceNote.REGION_ID.name());
			String procedureId = (String) params.getParams().get(TableConstants.AcceptanceNote.PROCEDURE_ID.name());
			if(StringUtils.isBlank(projectPeriodId)||StringUtils.isBlank(inspectorRole)||StringUtils.isBlank(regionType)||
					StringUtils.isBlank(regionId)||StringUtils.isBlank(procedureId)){
				return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
			}
			Map<String, Object> map = acceptanceNoteService
					.getAcceptanceNote(ParamsMap.newMap(TableConstants.AcceptanceNote.PROCEDURE_ID.name(), procedureId)
							.addParams(TableConstants.AcceptanceNote.REGION_ID.name(), regionId)
							.addParams(TableConstants.AcceptanceNote.REGION_TYPE.name(), regionType)
							.addParams(TableConstants.IS_SEALED, 0), ddBB);
			batchId = (String) acceptanceBatchService
					.getAcceptanceBatch(ParamsMap.newMap(TableConstants.AcceptanceBatch.ACCEPTANCE_NOTE_ID.name(), (String)map.get(TableConstants.AcceptanceNote.id.name()))
							.addParams(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), inspectorRole)
							.addParams(TableConstants.AcceptanceBatch.BATCH_STATUS.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER)
							.addParams(TableConstants.IS_SEALED, 0), ddBB).get(TableConstants.AcceptanceBatch.id.name());
		}
		
		if(batchId==null){
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}

		Map<String, Object> oldBatch = acceptanceBatchService.getByID(batchId, ddBB);

		Map<String, Object> noteMap = acceptanceNoteService
				.getByID((String) oldBatch.get(TableConstants.AcceptanceBatch.acceptanceNoteId.name()), ddBB);

		Map<String, Object> batchStatusParamsMap = new HashMap<>();
		batchStatusParamsMap.put(TableConstants.ProcedureBatchStatus.PROCEDURE_ID.name(),
				noteMap.get(TableConstants.AcceptanceNote.procedureId.name()));
		batchStatusParamsMap.put(TableConstants.ProcedureBatchStatus.BATCH_NO.name(),
				oldBatch.get(TableConstants.AcceptanceBatch.batchNo.name()));
		batchStatusParamsMap.put(TableConstants.ProcedureBatchStatus.ACCEPTANCE_NOTE_ID.name(),
				noteMap.get(TableConstants.AcceptanceNote.id.name()));
		Map<String, Object> batchStatus = procedureBatchStatusService.getProcedureBatchStatus(batchStatusParamsMap,
				ddBB);
		
		Map<String, Object> user = userService.getByID(userId, ddBB);
		if (oldBatch == null || noteMap == null || batchStatus == null||user==null) {
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}

		Integer batchTimes = 1 + (Integer) noteMap.get(TableConstants.AcceptanceNote.batchTimes.name());

		Map<String, Object> batchMap = new HashMap<>();
		batchMap.put(TableConstants.TENANT_ID, tenantId);
		batchMap.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		batchMap.put(TableConstants.UPDATE_USER_ID, userId);
		batchMap.put(TableConstants.IS_SEALED, 0);
		batchMap.put(TableConstants.AcceptanceBatch.ACCEPTANCE_NOTE_ID.name(),
				(String) noteMap.get(TableConstants.AcceptanceNote.id.name()));
		batchMap.put(TableConstants.AcceptanceBatch.MIN_PASS_RATIO.name(),
				(Float) oldBatch.get(TableConstants.AcceptanceBatch.minPassRatio.name()));
		batchMap.put(TableConstants.AcceptanceBatch.BATCH_NO.name(), batchTimes);
		batchMap.put(TableConstants.AcceptanceBatch.BATCH_ACCEPTANCE_NO.name(), 1);
		batchMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER);

		batchMap.put(TableConstants.AcceptanceBatch.ACCEPTER.name(),
				(String) oldBatch.get(TableConstants.AcceptanceBatch.accepter.name()));
		batchMap.put(TableConstants.AcceptanceBatch.ACCEPTANCE_PERSON_ID.name(),
				(String) oldBatch.get(TableConstants.AcceptanceBatch.acceptancePersonId.name()));

		Map<String, Object> zjBatchMap = new HashMap<>(batchMap);
		zjBatchMap.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), DataConstants.INSPECTOR_ROLE_ZJ);
		zjBatchMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(), DataConstants.BATCH_STATUS_ID_ZJWYS);

		Map<String, Object> jlBatchMap = new HashMap<>(batchMap);
		jlBatchMap.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), DataConstants.INSPECTOR_ROLE_JL);
		jlBatchMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(), DataConstants.BATCH_STATUS_ID_JLWYS);

		if (DataConstants.INSPECTOR_ROLE_ZJ
				.equals((String) oldBatch.get(TableConstants.AcceptanceBatch.inspectorRole.name()))) {
			Map<String, Object> batchParamsMap = new HashMap<>();
			batchParamsMap.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), DataConstants.INSPECTOR_ROLE_JL);
			batchParamsMap.put(TableConstants.AcceptanceBatch.ACCEPTANCE_NOTE_ID.name(),
					(String) oldBatch.get(TableConstants.AcceptanceBatch.acceptanceNoteId.name()));
			batchParamsMap.put(TableConstants.AcceptanceBatch.BATCH_NO.name(),
					oldBatch.get(TableConstants.AcceptanceBatch.batchNo.name()));
			Map<String, Object> jlAcceptanceBatch = acceptanceBatchService.getAcceptanceBatch(batchParamsMap, ddBB);

			jlBatchMap.put(TableConstants.AcceptanceBatch.ACCEPTER.name(),
					(String) jlAcceptanceBatch.get(TableConstants.AcceptanceBatch.accepter.name()));
			jlBatchMap.put(TableConstants.AcceptanceBatch.ACCEPTANCE_PERSON_ID.name(),
					(String) jlAcceptanceBatch.get(TableConstants.AcceptanceBatch.acceptancePersonId.name()));
		} else if (DataConstants.INSPECTOR_ROLE_JL
				.equals((String) oldBatch.get(TableConstants.AcceptanceBatch.inspectorRole.name()))) {
			Map<String, Object> batchParamsMap = new HashMap<>();
			batchParamsMap.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), DataConstants.INSPECTOR_ROLE_ZJ);
			batchParamsMap.put(TableConstants.AcceptanceBatch.ACCEPTANCE_NOTE_ID.name(),
					(String) oldBatch.get(TableConstants.AcceptanceBatch.acceptanceNoteId.name()));
			batchParamsMap.put(TableConstants.AcceptanceBatch.BATCH_NO.name(),
					oldBatch.get(TableConstants.AcceptanceBatch.batchNo.name()));
			Map<String, Object> zjAcceptanceBatch = acceptanceBatchService.getAcceptanceBatch(batchParamsMap, ddBB);

			zjBatchMap.put(TableConstants.AcceptanceBatch.ACCEPTER.name(),
					(String) zjAcceptanceBatch.get(TableConstants.AcceptanceBatch.accepter.name()));
			zjBatchMap.put(TableConstants.AcceptanceBatch.ACCEPTANCE_PERSON_ID.name(),
					(String) zjAcceptanceBatch.get(TableConstants.AcceptanceBatch.acceptancePersonId.name()));
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

		acceptanceBatchService.addAcceptanceBatch(zjBatchMap, ddBB);
		acceptanceBatchService.addAcceptanceBatch(jlBatchMap, ddBB);

		// 加入批次状态
		Map<String, Object> batchStatusMap = new HashMap<>();
		batchStatusMap.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		batchStatusMap.put(TableConstants.UPDATE_USER_ID, userId);
		batchStatusMap.put(TableConstants.IS_SEALED, 0);
		batchStatusMap.put(TableConstants.ProcedureBatchStatus.PROCEDURE_ID.name(),
				(String) noteMap.get(TableConstants.AcceptanceNote.procedureId.name()));
		batchStatusMap.put(TableConstants.ProcedureBatchStatus.BATCH_NO.name(), batchTimes);
		batchStatusMap.put(TableConstants.ProcedureBatchStatus.ACCEPTANCE_NOTE_ID.name(),
				(String) noteMap.get(TableConstants.AcceptanceNote.id.name()));
		if (DataConstants.INSPECTOR_ROLE_ZJ
				.equals((String) oldBatch.get(TableConstants.AcceptanceBatch.inspectorRole.name()))) {
			batchStatusMap.put(TableConstants.ProcedureBatchStatus.STATEMENT_ID.name(),
					DataConstants.ACCEPTANCE_BATCH_STATUS_ID_YBY);
		} else {
			batchStatusMap.put(TableConstants.ProcedureBatchStatus.STATEMENT_ID.name(),
					(String) batchStatus.get(TableConstants.ProcedureBatchStatus.statementId.name()));
		}
		
		procedureBatchStatusService.addProcedureBatchStatus(batchStatusMap, ddBB);

		Map<String, Object> newNote = new LinkedHashMap<>();
		newNote.put(TableConstants.AcceptanceNote.BATCH_TIMES.name(), batchTimes);
		newNote.put(TableConstants.AcceptanceNote.ID.name(),
				(String) noteMap.get(TableConstants.AcceptanceNote.id.name()));
		acceptanceNoteService.updateAcceptanceNote(newNote, ddBB);

		// 加入验收信息
		if (DataConstants.INSPECTOR_ROLE_ZJ
				.equals((String) oldBatch.get(TableConstants.AcceptanceBatch.inspectorRole.name()))) {
			batchId = (String) zjBatchMap.get(TableConstants.AcceptanceBatch.ID.name());
		} else {
			batchId = (String) jlBatchMap.get(TableConstants.AcceptanceBatch.ID.name());
		}

		String supervisorCompanyId = (String) params.getParams()
				.get(TableConstants.AcceptanceNote.SUPERVISOR_COMPANY_ID.name());
		String contractingProId = (String) params.getParams()
				.get(TableConstants.AcceptanceNote.CONTRACTING_PRO_ID.name());
		String constructionTeamId = (String) params.getParams()
				.get(TableConstants.AcceptanceNote.CONSTRUCTION_TEAM_ID.name());

		List<Map<String, Object>> dominantList = (List<Map<String, Object>>) params.getParams()
				.get(TableConstants.ACCEPTANCE_DOMINANT_ITEM);
		List<Map<String, Object>> generalList = (List<Map<String, Object>>) params.getParams()
				.get(TableConstants.ACCEPTANCE_GENERAL_ITEM);

		if (StringUtils.isBlank(batchId) || StringUtils.isBlank(supervisorCompanyId)
				|| StringUtils.isBlank(contractingProId) || StringUtils.isBlank(constructionTeamId)) {
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}

		Map<String, Object> teamInfo = teamInfoService.getByID(constructionTeamId, ddBB);
		Map<String, Object> supervisorCompany = companyService.getByID(supervisorCompanyId, ddBB);
		Map<String, Object> contractingPro = companyService.getByID(contractingProId, ddBB);

		Map<String, Object> acceptanceBatch = acceptanceBatchService.getByID(batchId, ddBB);
		if (acceptanceBatch == null || teamInfo == null || supervisorCompany == null || contractingPro == null) {
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}

		String acceptanceNoteId = (String) acceptanceBatch.get(TableConstants.AcceptanceBatch.acceptanceNoteId.name());
		Map<String, Object> acceptanceNote = acceptanceNoteService.getByID(acceptanceNoteId, ddBB);
		if (acceptanceNote == null) {
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}
		Map<String, Object> updateAcceptanceNote = new HashMap<>();
		updateAcceptanceNote.put(TableConstants.AcceptanceNote.ID.name(), acceptanceNoteId);
		updateAcceptanceNote.put(TableConstants.AcceptanceNote.SUPERVISOR_COMPANY_ID.name(), supervisorCompanyId);
		updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONTRACTING_PRO_ID.name(), contractingProId);
		updateAcceptanceNote.put(TableConstants.AcceptanceNote.SUPERVISOR_COMPANY.name(),
				(String) supervisorCompany.get(TableConstants.Company.corporateName.name()));
		updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONTRACTING_PRO.name(),
				(String) contractingPro.get(TableConstants.Company.corporateName.name()));
		updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONSTRUCTION_TEAM_ID.name(), constructionTeamId);
		updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONSTRUCTION_TEAM.name(),
				teamInfo.get(TableConstants.TeamInfo.teamName.name()));
		if (acceptanceNote.get(TableConstants.AcceptanceNote.checkTimes.name()) == null) {
			updateAcceptanceNote.put(TableConstants.AcceptanceNote.CHECK_TIMES.name(), 1);
		} else {
			updateAcceptanceNote.put(TableConstants.AcceptanceNote.CHECK_TIMES.name(),
					(Integer) acceptanceNote.get(TableConstants.AcceptanceNote.checkTimes.name()) + 1);
		}

		Map<String, Object> updateAcceptanceBatch = new HashMap<>();
		updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.UPDATE_USER_ID.name(), userId);
		updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.UPDATE_TIME.name(),
				DateUtil.convertDateTimeToString(new Date(), null));
		updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.ID.name(),
				acceptanceBatch.get(TableConstants.AcceptanceBatch.id.name()));
		updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.REMARK.name(),
				(String) params.getParams().get(TableConstants.AcceptanceBatch.REMARK.name()));
		updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.ACCEPTANCE_PERSON_ID.name(),userId);
		updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.ACCEPTER.name(),(String)user.get(TableConstants.User.realName.name()));

		if (DataConstants.BATCH_STATUS_ID_ZJWYS
				.equals((String) acceptanceBatch.get(TableConstants.AcceptanceBatch.batchStatusId.name()))
				|| DataConstants.BATCH_STATUS_ID_JLWYS
						.equals((String) acceptanceBatch.get(TableConstants.AcceptanceBatch.batchStatusId.name()))) {
			// 验收
			updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.DOMINANT_ITEM_CHECK_RESULT.name(),
					(String) params.getParams().get(TableConstants.AcceptanceBatch.DOMINANT_ITEM_CHECK_RESULT.name()));
			updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.GENERAL_ITEM_CHECK_RESULT.name(),
					(String) params.getParams().get(TableConstants.AcceptanceBatch.GENERAL_ITEM_CHECK_RESULT.name()));
			updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.GENERAL_ITEM_CHECK_SCORE.name(), Float.valueOf(
					(String) params.getParams().get(TableConstants.AcceptanceBatch.GENERAL_ITEM_CHECK_SCORE.name())));
			updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.TOTAL_CHECK_RESULT.name(),
					(String) params.getParams().get(TableConstants.AcceptanceBatch.TOTAL_CHECK_RESULT.name()));
			updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.TOTAL_CHECK_SCORE.name(), Float
					.valueOf((String) params.getParams().get(TableConstants.AcceptanceBatch.TOTAL_CHECK_SCORE.name())));
			updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.ELIGIBLE_RATE.name(),
					(String) params.getParams().get(TableConstants.AcceptanceBatch.ELIGIBLE_RATE.name()));

			// 修改验收状态
			Map<String, Object> updateBatchStatus = new HashMap<>();
			updateBatchStatus.put(TableConstants.AcceptanceBatch.UPDATE_USER_ID.name(), userId);
			updateBatchStatus.put(TableConstants.AcceptanceBatch.UPDATE_TIME.name(),
					DateUtil.convertDateTimeToString(new Date(), null));
			updateBatchStatus.put(TableConstants.ProcedureBatchStatus.ID.name(),
					(String) batchStatusMap.get(TableConstants.ProcedureBatchStatus.ID.name()));
			
			if (DataConstants.INSPECTOR_ROLE_ZJ
					.equals((String) acceptanceBatch.get(TableConstants.AcceptanceBatch.inspectorRole.name()))) {
				if (DataConstants.CHECK_SUCCESS.equals(
						(String) params.getParams().get(TableConstants.AcceptanceBatch.TOTAL_CHECK_RESULT.name()))) {
					updateBatchStatus.put(TableConstants.ProcedureBatchStatus.STATEMENT_ID.name(),
							DataConstants.ACCEPTANCE_BATCH_STATUS_ID_ZJYYS);

					updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(),
							DataConstants.BATCH_STATUS_ID_ZJYYS);

					updateAcceptanceNote.put(TableConstants.AcceptanceNote.STATEMENT_ID.name(),
							DataConstants.PROCEDURE_STATUS_ID_ZJYYS);
				}else{
					updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(),
							DataConstants.BATCH_STATUS_ID_ZJNFY);
				}
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONSTRUCTION_INSPECTOR_CHECKED.name(),
						(String) params.getParams().get(TableConstants.AcceptanceBatch.TOTAL_CHECK_RESULT.name()));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONSTRUCTION_INSPECTOR.name(),
						(String) acceptanceBatch.get(TableConstants.AcceptanceBatch.accepter.name()));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONSTRUCTION_INSPECTOR_ID.name(),
						(String) acceptanceBatch.get(TableConstants.AcceptanceBatch.acceptancePersonId.name()));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONSTRUCTION_INSPECTOR_CHECK_RESULT.name(),
						Float.valueOf((String) params.getParams()
								.get(TableConstants.AcceptanceBatch.TOTAL_CHECK_SCORE.name())));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONSTRUCTION_INSPECTOR_CHECK_DATE.name(),
						DateUtil.convertDateTimeToString(new Date(), null));
			} else if (DataConstants.INSPECTOR_ROLE_JL
					.equals((String) acceptanceBatch.get(TableConstants.AcceptanceBatch.inspectorRole.name()))) {
				
				if (DataConstants.CHECK_SUCCESS.equals(
						(String) params.getParams().get(TableConstants.AcceptanceBatch.TOTAL_CHECK_RESULT.name()))) {
					updateBatchStatus.put(TableConstants.ProcedureBatchStatus.STATEMENT_ID.name(),
							DataConstants.ACCEPTANCE_BATCH_STATUS_ID_JLYYS);

					updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(),
							DataConstants.BATCH_STATUS_ID_JLYYS);

					updateAcceptanceNote.put(TableConstants.AcceptanceNote.STATEMENT_ID.name(),
							DataConstants.PROCEDURE_STATUS_ID_JLYYS);
				}else{
					updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(),
							DataConstants.BATCH_STATUS_ID_JLNFY);
				}
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.SUPERVISOR_CHECKED.name(),
						(String) params.getParams().get(TableConstants.AcceptanceBatch.TOTAL_CHECK_RESULT.name()));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.SUPERVISOR_NAME.name(),
						(String) acceptanceBatch.get(TableConstants.AcceptanceBatch.accepter.name()));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.SUPERVISOR_ID.name(),
						(String) acceptanceBatch.get(TableConstants.AcceptanceBatch.acceptancePersonId.name()));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.SUPERVISOR_CHECK_RESULT.name(),
						Float.valueOf((String) params.getParams().get(TableConstants.AcceptanceBatch.TOTAL_CHECK_SCORE.name())));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.SUPERVISOR_CHECK_TIME.name(),
						DateUtil.convertDateTimeToString(new Date(), null));
			} else {
				return new BaseResult(ReturnCode.FAIL);
			}
			

			// 插入主控项检查信息
			for (Map<String, Object> item : dominantList) {
				item.put(TableConstants.AcceptanceDominantItem.TENANT_ID.name(), tenantId);
				item.put(TableConstants.AcceptanceDominantItem.ACCEPTANCE_NOTE_ID.name(), acceptanceNoteId);
				item.put(TableConstants.AcceptanceDominantItem.ACCEPTANCE_BATCH_ID.name(), batchId);
				acceptanceDominantItemService.addAcceptanceDominantItem(item, ddBB);
			}

			// 插入一般项检查信息
			for (Map<String, Object> item : generalList) {
				item.put(TableConstants.AcceptanceGeneralItem.TENANT_ID.name(), tenantId);
				item.put(TableConstants.AcceptanceGeneralItem.ACCEPTANCE_NOTE_ID.name(), acceptanceNoteId);
				item.put(TableConstants.AcceptanceGeneralItem.ACCEPTANCE_BATCH_ID.name(), batchId);

				// 添加一般项目记录点
				List<Map<String, Object>> pointList = (List<Map<String, Object>>) item
						.get(TableConstants.ACCEPTANCE_POINT);
				for (Map<String, Object> point : pointList) {
					point.put(TableConstants.AcceptancePoint.TENANT_ID.name(), tenantId);
					point.put(TableConstants.AcceptancePoint.ACCEPTANCE_GENERAN_NOTE_ID.name(),
							(String) item.get(TableConstants.AcceptanceGeneralItem.ID.name()));
					point.put(TableConstants.AcceptancePoint.GENERAL_ITEM_ID.name(),
							(String) item.get(TableConstants.AcceptanceGeneralItem.GENERAL_ITEM_ID.name()));
					point.put(TableConstants.AcceptancePoint.ACCEPTANCE_BATCH_ID.name(), batchId);
					point.put(TableConstants.AcceptancePoint.UNIT.name(),
							(String) item.get(TableConstants.AcceptanceGeneralItem.UNIT.name()));
					acceptancePointService.addAcceptancePoint(point, ddBB);
				}
				item.remove(TableConstants.ACCEPTANCE_POINT);
				acceptanceGeneralItemService.addAcceptanceGeneralItem(item, ddBB);
			}

			// 处理上传的图片
			List<Map<String, Object>> acceptanceAttachList = (List<Map<String, Object>>) params.getParams()
					.get(TableConstants.ACCEPTANCE_ATTACH);
			for (Map<String, Object> acceptanceAttach : acceptanceAttachList) {
				acceptanceAttach.put(TableConstants.AcceptanceAttach.TENANT_ID.name(), tenantId);
				acceptanceAttach.put(TableConstants.AcceptanceAttach.ACCEPT_NOTE_ID.name(), acceptanceNoteId);
				acceptanceAttach.put(TableConstants.AcceptanceAttach.ACCEPT_BATCH_ID.name(), batchId);
				acceptanceAttach.put(TableConstants.AcceptanceAttach.UPLOAD_TIME.name(),
						DateUtil.convertDateTimeToString(new Date(), null));
				acceptanceAttach.put(TableConstants.AcceptanceAttach.UPDATE_TIME.name(),
						DateUtil.convertDateTimeToString(new Date(), null));
				acceptanceAttach.put(TableConstants.AcceptanceAttach.UPDATE_USER_ID.name(), userId);
				acceptanceAttach.put(TableConstants.AcceptanceAttach.IS_SEALED.name(), 0);

				String idTree = ((String) acceptanceNote.get(TableConstants.AcceptanceNote.regionIdTree.name()))
						.replace(">", "/");
				String path = ImgUtil.saveAndUpdateFile(null,
						(String) acceptanceAttach.get(TableConstants.AcceptanceAttach.ATTACH_PATH.name()), null,
						ImgUtil.ACCEPTANCE_IMG_PATH, idTree);
				acceptanceAttach.put(TableConstants.AcceptanceAttach.ATTACH_PATH.name(), path);
				acceptanceAttachService.addAcceptanceAttach(acceptanceAttach, ddBB);
			}
			procedureBatchStatusService.updateProcedureBatchStatus(updateBatchStatus, ddBB);
			acceptanceBatchService.updateAcceptanceBatch(updateAcceptanceBatch, ddBB);
			acceptanceNoteService.updateAcceptanceNote(updateAcceptanceNote, ddBB);
			
			updateProcedureScheduleRelate(ddBB, userId, (String)acceptanceNote.get(TableConstants.AcceptanceNote.procedureId.name()),
					(String)acceptanceNote.get(TableConstants.AcceptanceNote.regionType.name()), 
					(String)acceptanceNote.get(TableConstants.AcceptanceNote.regionId.name()));
		}
		
		JSONObject result=new JSONObject();
		result.put(TableConstants.AcceptanceBatch.id.name(), batchId);
		return new BaseResult(ReturnCode.OK,result);
	}

	/**
	 * 获取验收记录详细信息
	 * 
	 * @param userId
	 * @param tenantId
	 * @param params
	 * @param ddBB
	 * @return
	 */
	@RequestMapping("/acceptanceBatch/getAcceptanceRecordInfo")
	@ResponseBody
	public BaseResult getAcceptanceRecordInfo(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {

		String batchId = (String) params.getParams().get(TableConstants.AcceptanceBatch.ID.name());
		if (batchId == null) {
			String projectPeriodId = (String) params.getParams().get(TableConstants.AcceptanceNote.PROJECT_PERIOD_ID.name());
			String inspectorRole = (String) params.getParams().get(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name());
			String regionType = (String) params.getParams().get(TableConstants.AcceptanceNote.REGION_TYPE.name());
			String regionId = (String) params.getParams().get(TableConstants.AcceptanceNote.REGION_ID.name());
			String procedureId = (String) params.getParams().get(TableConstants.AcceptanceNote.PROCEDURE_ID.name());
			if(StringUtils.isBlank(projectPeriodId)||StringUtils.isBlank(inspectorRole)||StringUtils.isBlank(regionType)||
					StringUtils.isBlank(regionId)||StringUtils.isBlank(procedureId)){
				return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
			}
			Map<String, Object> map = acceptanceNoteService
					.getAcceptanceNote(ParamsMap.newMap(TableConstants.AcceptanceNote.PROCEDURE_ID.name(), procedureId)
							.addParams(TableConstants.AcceptanceNote.REGION_ID.name(), regionId)
							.addParams(TableConstants.AcceptanceNote.REGION_TYPE.name(), regionType)
							.addParams(TableConstants.IS_SEALED, 0), ddBB);
			if(map!=null){
				Map<String, Object> batchMap = acceptanceBatchService
						.getAcceptanceBatch(ParamsMap.newMap(TableConstants.AcceptanceBatch.ACCEPTANCE_NOTE_ID.name(), (String)map.get(TableConstants.AcceptanceNote.id.name()))
								.addParams(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), inspectorRole)
								.addParams(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(), inspectorRole.equals(DataConstants.INSPECTOR_ROLE_ZJ)?DataConstants.BATCH_STATUS_ID_ZJWYS:DataConstants.BATCH_STATUS_ID_JLWYS)
								.addParams(TableConstants.AcceptanceBatch.BATCH_STATUS.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER)
								.addParams(TableConstants.IS_SEALED, 0), ddBB);
				if(batchMap!=null){
					batchId=(String) batchMap.get(TableConstants.AcceptanceBatch.id.name());
				}else{
					batchId=addNoteBatch(ddBB, tenantId, projectPeriodId, inspectorRole, userId, regionType, regionId, procedureId);
				}
			}else{
				batchId=addNoteBatch(ddBB, tenantId, projectPeriodId, inspectorRole, userId, regionType, regionId, procedureId);
			}
		}
		// 主信息
		Map<String, Object> result = acceptanceBatchService.getBatchInfo(batchId, ddBB);

		// 一般主控项信息
		Map<String, Object> map = new LinkedHashMap<>();
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		map.put(TableConstants.AcceptanceNote.PROCEDURE_ID.name(),
				(String) result.get(TableConstants.AcceptanceNote.procedureId.name()));
		List<Map<String, Object>> dominantItemList = dominantItemService.getDominantItemList(map, ddBB);
		List<Map<String, Object>> generalItemList = generalItemService.getGeneralItemList(map, ddBB);

		Map<String, Object> acceptanceParamsMap = new HashMap<String, Object>();
		acceptanceParamsMap.put(TableConstants.AcceptanceDominantItem.ACCEPTANCE_BATCH_ID.name(), batchId);
		acceptanceParamsMap.put(TableConstants.TENANT_ID, tenantId);
		List<Map<String, Object>> dominantItemResultList = acceptanceDominantItemService
				.getAcceptanceDominantItemList(acceptanceParamsMap, ddBB);
		for (Map<String, Object> dominantItem : dominantItemList) {
			for (Map<String, Object> dominantItemResult : dominantItemResultList) {
				if (((String) dominantItem.get(TableConstants.DominantItem.id.name())).equals(
						(String) dominantItemResult.get(TableConstants.AcceptanceDominantItem.dominantItemId.name()))) {
					dominantItem.put(TableConstants.AcceptanceDominantItem.eligible.name(),
							dominantItemResult.get(TableConstants.AcceptanceDominantItem.eligible.name()));
				}
			}
		}

		List<Map<String, Object>> generalItemResultList = acceptanceGeneralItemService
				.getAcceptanceGeneralItemList(acceptanceParamsMap, ddBB);
		Map<String, Object> orderMap = new HashMap<String, Object>();
		orderMap.put(TableConstants.AcceptancePoint.ORDER_NO.name(), TableConstants.ORDER_BY_ASC);
		List<Map<String, Object>> pointList = acceptancePointService.getAcceptancePointList(acceptanceParamsMap,
				orderMap, ddBB);

		for (Map<String, Object> generalItem : generalItemList) {
			for (Map<String, Object> generalItemResult : generalItemResultList) {
				if (((String) generalItem.get(TableConstants.GeneralItem.id.name())).equals(
						(String) generalItemResult.get(TableConstants.AcceptanceGeneralItem.generalItemId.name()))) {
					List<Map<String, Object>> points = new ArrayList<>();
					for (Map<String, Object> point : pointList) {
						if (((String) point.get(TableConstants.AcceptancePoint.generalItemId.name()))
								.equals((String) generalItemResult
										.get(TableConstants.AcceptanceGeneralItem.generalItemId.name()))) {
							points.add(point);
						}
					}
					generalItem.put("points", points);
					generalItem.put(TableConstants.AcceptanceGeneralItem.eligible.name(),
							generalItemResult.get(TableConstants.AcceptanceGeneralItem.eligible.name()));
					generalItem.put(TableConstants.AcceptanceGeneralItem.score.name(),
							generalItemResult.get(TableConstants.AcceptanceGeneralItem.score.name()));
					generalItem.put(TableConstants.AcceptanceGeneralItem.eligibleRate.name(),
							generalItemResult.get(TableConstants.AcceptanceGeneralItem.eligibleRate.name()));
					generalItem.put(TableConstants.AcceptanceGeneralItem.checkPointSize.name(),
							generalItemResult.get(TableConstants.AcceptanceGeneralItem.checkPointSize.name()));
				}
			}
		}
		result.put("dominantItem", dominantItemList);
		result.put("generalItem", generalItemList);

		// 照片
		Map<String, Object> acceptanceAttactParamsMap = new HashMap<String, Object>();
		acceptanceAttactParamsMap.put(TableConstants.AcceptanceAttach.ACCEPT_BATCH_ID.name(), batchId);
		acceptanceAttactParamsMap.put(TableConstants.TENANT_ID, tenantId);
		acceptanceAttactParamsMap.put(TableConstants.IS_SEALED, 0);
		result.put("acceptanceAttach",
				acceptanceAttachService.getAcceptanceAttachList(acceptanceAttactParamsMap, ddBB));

		return new BaseResult(ReturnCode.OK, result);
	}

	/**
	 * 获取用户验收列表（已验收/未验收）
	 * 
	 * @param userId
	 * @param tenantId
	 * @param params
	 * @param ddBB
	 * @return
	 */
	@RequestMapping("/acceptanceBatch/getAllAcceptanceBatchByUser")
	@ResponseBody
	public BaseResult getAllAcceptanceBatch(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {

		String projectPeriodId = (String) params.getParams()
				.get(TableConstants.ProjectBuilding.PROJECT_PERIOD_ID.name());
		String regionId = (String) params.getParams().get(TableConstants.AcceptanceNote.REGION_ID.name());
		String regionType = (String) params.getParams().get(TableConstants.AcceptanceNote.REGION_TYPE.name());
		String procedureId = (String) params.getParams().get(TableConstants.AcceptanceNote.PROCEDURE_ID.name());
		
		if((!(DataConstants.REGION_PERIOD_TYPE_VAL.equals(regionType)||DataConstants.REGION_BUILDING_TYPE_VAL.equals(regionType)||
				DataConstants.REGION_FLOOR_TYPE_VAL.equals(regionType)))||StringUtils.isBlank(projectPeriodId)||
				StringUtils.isBlank(regionId)||StringUtils.isBlank(procedureId)){
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}
		
		boolean hasFloorType=false;
		boolean hasRoomType=false;
		if(DataConstants.REGION_FLOOR_TYPE_VAL.equals(regionType)){
			Map<String, Object> typeMap=new HashMap<>();
			typeMap.put(TableConstants.ProjectRegionProcedureRelate.PROCEDURE_ID.name(), procedureId);
			typeMap.put(TableConstants.ProjectRegionProcedureRelate.PROJECT_PERIOD_ID.name(), projectPeriodId);
			
			typeMap.put(TableConstants.ProjectRegionProcedureRelate.REGION_TYPE_ID.name(), DataConstants.REGION_FLOOR_TYPE_VAL);
			hasFloorType=projectRegionProcedureRelateService.getProjectRegionProcedureRelateList(typeMap, ddBB).size()>0;
			
			typeMap.put(TableConstants.ProjectRegionProcedureRelate.REGION_TYPE_ID.name(), DataConstants.REGION_ROOM_TYPE_VAL);
			hasRoomType=projectRegionProcedureRelateService.getProjectRegionProcedureRelateList(typeMap, ddBB).size()>0;
			
			if((!hasFloorType)&&(!hasRoomType)){
				return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
			}
		}
		
		Map<String, Object> user = userService.getByID(userId, ddBB);
		if (user.get(TableConstants.User.postId.name()) == null) {
			return new BaseResult(ReturnCode.NO_POST_AUTH);
		}
		Map<String, Object> postInfo = postInfoService
				.getByID((String) user.get(TableConstants.User.postId.name()), ddBB);
		if (postInfo == null) {
			return new BaseResult(ReturnCode.NO_POST_AUTH);
		}
		
		String inspectorRole=null;
		String batchStatusId=null;
		if(DataConstants.INSPECTOR_ROLE_NAME_ZJ.equals((String)postInfo.get(TableConstants.PostInfo.postName.name()))){
			inspectorRole=DataConstants.INSPECTOR_ROLE_ZJ;
			batchStatusId=DataConstants.BATCH_STATUS_ID_ZJWYS;
		}else if(DataConstants.INSPECTOR_ROLE_NAME_JL.equals((String)postInfo.get(TableConstants.PostInfo.postName.name()))){
			inspectorRole=DataConstants.INSPECTOR_ROLE_JL;
			batchStatusId=DataConstants.BATCH_STATUS_ID_JLWYS;
		}else{
			return new BaseResult(ReturnCode.NO_POST_AUTH);
		}
		
		return new BaseResult(ReturnCode.OK, acceptanceBatchService.getAllAcceptanceBatchByPost(hasFloorType,hasRoomType,batchStatusId,projectPeriodId, inspectorRole,
				procedureId, regionType, regionId, ddBB));
	}

	/**
	 * 新增检验记录
	 * 
	 * @param params
	 * @param ddBB
	 * @return
	 */
	@RequestMapping("/acceptanceBatch/addAcceptanceBatchRecord")
	@ResponseBody
	public BaseResult addAcceptanceBatchRecord(@RequestHeader(Constants.TENANT_ID) String tenantId,
			@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {

		String batchId = (String) params.getParams().get(TableConstants.AcceptanceBatch.ID.name());

		String supervisorCompanyId = (String) params.getParams()
				.get(TableConstants.AcceptanceNote.SUPERVISOR_COMPANY_ID.name());
		String contractingProId = (String) params.getParams()
				.get(TableConstants.AcceptanceNote.CONTRACTING_PRO_ID.name());
		String constructionTeamId = (String) params.getParams()
				.get(TableConstants.AcceptanceNote.CONSTRUCTION_TEAM_ID.name());

		List<Map<String, Object>> dominantList = (List<Map<String, Object>>) params.getParams()
				.get(TableConstants.ACCEPTANCE_DOMINANT_ITEM);
		List<Map<String, Object>> generalList = (List<Map<String, Object>>) params.getParams()
				.get(TableConstants.ACCEPTANCE_GENERAL_ITEM);

		if (StringUtils.isBlank(batchId) || StringUtils.isBlank(supervisorCompanyId)
				|| StringUtils.isBlank(contractingProId) || StringUtils.isBlank(constructionTeamId)) {
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}

		Map<String, Object> teamInfo = teamInfoService.getByID(constructionTeamId, ddBB);
		Map<String, Object> supervisorCompany = companyService.getByID(supervisorCompanyId, ddBB);
		Map<String, Object> contractingPro = companyService.getByID(contractingProId, ddBB);
		Map<String, Object> user = userService.getByID(userId, ddBB);

		Map<String, Object> acceptanceBatch = acceptanceBatchService.getByID(batchId, ddBB);
		if (DataConstants.ACCEPTANCE_BATCH_STATUS_MEMBER.equals((String)acceptanceBatch.get(TableConstants.AcceptanceBatch.batchStatus.name()))
				||acceptanceBatch == null || teamInfo == null || supervisorCompany == null || contractingPro == null||user==null) {
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}
		
		String acceptanceNoteId = (String) acceptanceBatch.get(TableConstants.AcceptanceBatch.acceptanceNoteId.name());
		Map<String, Object> acceptanceNote = acceptanceNoteService.getByID(acceptanceNoteId, ddBB);
		if (acceptanceNote == null) {
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}
		Map<String, Object> updateAcceptanceNote = new HashMap<>();
		updateAcceptanceNote.put(TableConstants.AcceptanceNote.ID.name(), acceptanceNoteId);
		updateAcceptanceNote.put(TableConstants.AcceptanceNote.SUPERVISOR_COMPANY_ID.name(), supervisorCompanyId);
		updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONTRACTING_PRO_ID.name(), contractingProId);
		updateAcceptanceNote.put(TableConstants.AcceptanceNote.SUPERVISOR_COMPANY.name(),
				(String) supervisorCompany.get(TableConstants.Company.corporateName.name()));
		updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONTRACTING_PRO.name(),
				(String) contractingPro.get(TableConstants.Company.corporateName.name()));
		updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONSTRUCTION_TEAM_ID.name(), constructionTeamId);
		updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONSTRUCTION_TEAM.name(),
				teamInfo.get(TableConstants.TeamInfo.teamName.name()));
		if (acceptanceNote.get(TableConstants.AcceptanceNote.checkTimes.name()) == null) {
			updateAcceptanceNote.put(TableConstants.AcceptanceNote.CHECK_TIMES.name(), 1);
		} else {
			updateAcceptanceNote.put(TableConstants.AcceptanceNote.CHECK_TIMES.name(),
					(Integer) acceptanceNote.get(TableConstants.AcceptanceNote.checkTimes.name()) + 1);
		}

		Map<String, Object> updateAcceptanceBatch = new HashMap<>();
		updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.ID.name(),
				acceptanceBatch.get(TableConstants.AcceptanceBatch.id.name()));

		if ((DataConstants.BATCH_STATUS_ID_ZJWYS
				.equals((String) acceptanceBatch.get(TableConstants.AcceptanceBatch.batchStatusId.name()))
				|| DataConstants.BATCH_STATUS_ID_JLWYS
						.equals((String) acceptanceBatch.get(TableConstants.AcceptanceBatch.batchStatusId.name())))) {
			// 验收
			updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.UPDATE_USER_ID.name(), userId);
			updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.UPDATE_TIME.name(),
					DateUtil.convertDateTimeToString(new Date(), null));
			updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.REMARK.name(),
					(String) params.getParams().get(TableConstants.AcceptanceBatch.REMARK.name()));
			updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.DOMINANT_ITEM_CHECK_RESULT.name(),
					(String) params.getParams().get(TableConstants.AcceptanceBatch.DOMINANT_ITEM_CHECK_RESULT.name()));
			updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.GENERAL_ITEM_CHECK_RESULT.name(),
					(String) params.getParams().get(TableConstants.AcceptanceBatch.GENERAL_ITEM_CHECK_RESULT.name()));
			updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.GENERAL_ITEM_CHECK_SCORE.name(), Float.valueOf(
					(String) params.getParams().get(TableConstants.AcceptanceBatch.GENERAL_ITEM_CHECK_SCORE.name())));
			updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.TOTAL_CHECK_RESULT.name(),
					(String) params.getParams().get(TableConstants.AcceptanceBatch.TOTAL_CHECK_RESULT.name()));
			updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.TOTAL_CHECK_SCORE.name(), Float
					.valueOf((String) params.getParams().get(TableConstants.AcceptanceBatch.TOTAL_CHECK_SCORE.name())));
			updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.ELIGIBLE_RATE.name(),
					(String) params.getParams().get(TableConstants.AcceptanceBatch.ELIGIBLE_RATE.name()));
			updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.REMARK.name(),
					(String) params.getParams().get(TableConstants.AcceptanceBatch.REMARK.name()));
			updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.ACCEPTANCE_PERSON_ID.name(),userId);
			updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.ACCEPTER.name(),(String)user.get(TableConstants.User.realName.name()));

			// 修改验收状态
			Map<String, Object> batchStatusParamsMap = new HashMap<>();
			batchStatusParamsMap.put(TableConstants.ProcedureBatchStatus.PROCEDURE_ID.name(),
					acceptanceNote.get(TableConstants.AcceptanceNote.procedureId.name()));
			batchStatusParamsMap.put(TableConstants.ProcedureBatchStatus.BATCH_NO.name(),
					acceptanceBatch.get(TableConstants.AcceptanceBatch.batchNo.name()));
			batchStatusParamsMap.put(TableConstants.ProcedureBatchStatus.ACCEPTANCE_NOTE_ID.name(),
					acceptanceNote.get(TableConstants.AcceptanceNote.id.name()));
			Map<String, Object> batchStatus = procedureBatchStatusService.getProcedureBatchStatus(batchStatusParamsMap,
					ddBB);
			if (batchStatus == null) {
				return new BaseResult(ReturnCode.FAIL);
			}
			Map<String, Object> updateBatchStatus = new HashMap<>();
			updateBatchStatus.put(TableConstants.AcceptanceBatch.UPDATE_USER_ID.name(), userId);
			updateBatchStatus.put(TableConstants.AcceptanceBatch.UPDATE_TIME.name(),
					DateUtil.convertDateTimeToString(new Date(), null));
			updateBatchStatus.put(TableConstants.ProcedureBatchStatus.ID.name(),
					batchStatus.get(TableConstants.ProcedureBatchStatus.id.name()));
			
			if (DataConstants.INSPECTOR_ROLE_ZJ
					.equals((String) acceptanceBatch.get(TableConstants.AcceptanceBatch.inspectorRole.name()))) {
				if (DataConstants.CHECK_SUCCESS.equals(
						(String) params.getParams().get(TableConstants.AcceptanceBatch.TOTAL_CHECK_RESULT.name()))) {
					if(!DataConstants.ACCEPTANCE_BATCH_STATUS_ID_JLYYS.equals((String)batchStatus.get(TableConstants.ProcedureBatchStatus.statementId.name()))){
						updateBatchStatus.put(TableConstants.ProcedureBatchStatus.STATEMENT_ID.name(),
								DataConstants.ACCEPTANCE_BATCH_STATUS_ID_ZJYYS);
					}

					updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(),
							DataConstants.BATCH_STATUS_ID_ZJYYS);

					updateAcceptanceNote.put(TableConstants.AcceptanceNote.STATEMENT_ID.name(),
							DataConstants.PROCEDURE_STATUS_ID_ZJYYS);
				}else{
					if(DataConstants.ACCEPTANCE_BATCH_STATUS_ID_WBY.equals((String)batchStatus.get(TableConstants.ProcedureBatchStatus.statementId.name()))){
						updateBatchStatus.put(TableConstants.ProcedureBatchStatus.STATEMENT_ID.name(),
								DataConstants.ACCEPTANCE_BATCH_STATUS_ID_YBY);
					}
					if(DataConstants.PROCEDURE_STATUS_ID_SGZ.equals((String)acceptanceNote.get(TableConstants.AcceptanceNote.statementId.name()))){
						updateAcceptanceNote.put(TableConstants.AcceptanceNote.STATEMENT_ID.name(),
								DataConstants.PROCEDURE_STATUS_ID_YBY);
					}
					
					updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(),
							DataConstants.BATCH_STATUS_ID_ZJNFY);
				}
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONSTRUCTION_INSPECTOR_CHECKED.name(),
						(String) params.getParams().get(TableConstants.AcceptanceBatch.TOTAL_CHECK_RESULT.name()));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONSTRUCTION_INSPECTOR.name(),
						(String) acceptanceBatch.get(TableConstants.AcceptanceBatch.accepter.name()));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONSTRUCTION_INSPECTOR_ID.name(),
						(String) acceptanceBatch.get(TableConstants.AcceptanceBatch.acceptancePersonId.name()));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONSTRUCTION_INSPECTOR_CHECK_RESULT.name(),
						Float.valueOf((String) params.getParams()
								.get(TableConstants.AcceptanceBatch.TOTAL_CHECK_SCORE.name())));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONSTRUCTION_INSPECTOR_CHECK_DATE.name(),
						DateUtil.convertDateTimeToString(new Date(), null));
			} else if (DataConstants.INSPECTOR_ROLE_JL
					.equals((String) acceptanceBatch.get(TableConstants.AcceptanceBatch.inspectorRole.name()))) {
				if (DataConstants.CHECK_SUCCESS.equals(
						(String) params.getParams().get(TableConstants.AcceptanceBatch.TOTAL_CHECK_RESULT.name()))) {
					updateBatchStatus.put(TableConstants.ProcedureBatchStatus.STATEMENT_ID.name(),
							DataConstants.ACCEPTANCE_BATCH_STATUS_ID_JLYYS);

					updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(),
							DataConstants.BATCH_STATUS_ID_JLYYS);

					updateAcceptanceNote.put(TableConstants.AcceptanceNote.STATEMENT_ID.name(),
							DataConstants.PROCEDURE_STATUS_ID_JLYYS);
				}else{
					if(DataConstants.ACCEPTANCE_BATCH_STATUS_ID_WBY.equals((String)batchStatus.get(TableConstants.ProcedureBatchStatus.statementId.name()))){
						updateBatchStatus.put(TableConstants.ProcedureBatchStatus.STATEMENT_ID.name(),
								DataConstants.ACCEPTANCE_BATCH_STATUS_ID_ZJYYS);
					}
					
					if(DataConstants.PROCEDURE_STATUS_ID_SGZ.equals((String)acceptanceNote.get(TableConstants.AcceptanceNote.statementId.name()))){
						updateAcceptanceNote.put(TableConstants.AcceptanceNote.STATEMENT_ID.name(),
								DataConstants.PROCEDURE_STATUS_ID_ZJYYS);
					}
					
					updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(),
							DataConstants.BATCH_STATUS_ID_JLNFY);
				}
				
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.SUPERVISOR_CHECKED.name(),
						(String) params.getParams().get(TableConstants.AcceptanceBatch.TOTAL_CHECK_RESULT.name()));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.SUPERVISOR_NAME.name(),
						(String) acceptanceBatch.get(TableConstants.AcceptanceBatch.accepter.name()));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.SUPERVISOR_ID.name(),
						(String) acceptanceBatch.get(TableConstants.AcceptanceBatch.acceptancePersonId.name()));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.SUPERVISOR_CHECK_RESULT.name(),
						Float.valueOf((String) params.getParams().get(TableConstants.AcceptanceBatch.TOTAL_CHECK_SCORE.name())));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.SUPERVISOR_CHECK_TIME.name(),
						DateUtil.convertDateTimeToString(new Date(), null));
			} else {
				return new BaseResult(ReturnCode.FAIL);
			}

			// 插入主控项检查信息
			for (Map<String, Object> item : dominantList) {
				item.put(TableConstants.AcceptanceDominantItem.TENANT_ID.name(), tenantId);
				item.put(TableConstants.AcceptanceDominantItem.ACCEPTANCE_NOTE_ID.name(), acceptanceNoteId);
				item.put(TableConstants.AcceptanceDominantItem.ACCEPTANCE_BATCH_ID.name(), batchId);
				acceptanceDominantItemService.addAcceptanceDominantItem(item, ddBB);
			}

			// 插入一般项检查信息
			for (Map<String, Object> item : generalList) {
				item.put(TableConstants.AcceptanceGeneralItem.TENANT_ID.name(), tenantId);
				item.put(TableConstants.AcceptanceGeneralItem.ACCEPTANCE_NOTE_ID.name(), acceptanceNoteId);
				item.put(TableConstants.AcceptanceGeneralItem.ACCEPTANCE_BATCH_ID.name(), batchId);

				// 添加一般项目记录点
				List<Map<String, Object>> pointList = (List<Map<String, Object>>) item
						.get(TableConstants.ACCEPTANCE_POINT);
				for (Map<String, Object> point : pointList) {
					point.put(TableConstants.AcceptancePoint.TENANT_ID.name(), tenantId);
					point.put(TableConstants.AcceptancePoint.ACCEPTANCE_GENERAN_NOTE_ID.name(),
							(String) item.get(TableConstants.AcceptanceGeneralItem.ID.name()));
					point.put(TableConstants.AcceptancePoint.GENERAL_ITEM_ID.name(),
							(String) item.get(TableConstants.AcceptanceGeneralItem.GENERAL_ITEM_ID.name()));
					point.put(TableConstants.AcceptancePoint.ACCEPTANCE_BATCH_ID.name(), batchId);
					point.put(TableConstants.AcceptancePoint.UNIT.name(),
							(String) item.get(TableConstants.AcceptanceGeneralItem.UNIT.name()));
					acceptancePointService.addAcceptancePoint(point, ddBB);
				}
				item.remove(TableConstants.ACCEPTANCE_POINT);
				acceptanceGeneralItemService.addAcceptanceGeneralItem(item, ddBB);
			}

			// 处理上传的图片
			List<Map<String, Object>> acceptanceAttachList = (List<Map<String, Object>>) params.getParams()
					.get(TableConstants.ACCEPTANCE_ATTACH);
			for (Map<String, Object> acceptanceAttach : acceptanceAttachList) {
				acceptanceAttach.put(TableConstants.AcceptanceAttach.TENANT_ID.name(), tenantId);
				acceptanceAttach.put(TableConstants.AcceptanceAttach.ACCEPT_NOTE_ID.name(), acceptanceNoteId);
				acceptanceAttach.put(TableConstants.AcceptanceAttach.ACCEPT_BATCH_ID.name(), batchId);
				acceptanceAttach.put(TableConstants.AcceptanceAttach.UPLOAD_TIME.name(),
						DateUtil.convertDateTimeToString(new Date(), null));
				acceptanceAttach.put(TableConstants.AcceptanceAttach.UPDATE_TIME.name(),
						DateUtil.convertDateTimeToString(new Date(), null));
				acceptanceAttach.put(TableConstants.AcceptanceAttach.UPDATE_USER_ID.name(), userId);
				acceptanceAttach.put(TableConstants.AcceptanceAttach.IS_SEALED.name(), 0);

				String idTree = ((String) acceptanceNote.get(TableConstants.AcceptanceNote.regionIdTree.name()))
						.replace(">", "/");
				String path = ImgUtil.saveAndUpdateFile(null,
						(String) acceptanceAttach.get(TableConstants.AcceptanceAttach.ATTACH_PATH.name()), null,
						ImgUtil.ACCEPTANCE_IMG_PATH, idTree);
				acceptanceAttach.put(TableConstants.AcceptanceAttach.ATTACH_PATH.name(), path);
				acceptanceAttachService.addAcceptanceAttach(acceptanceAttach, ddBB);
			}

			procedureBatchStatusService.updateProcedureBatchStatus(updateBatchStatus, ddBB);
			acceptanceBatchService.updateAcceptanceBatch(updateAcceptanceBatch, ddBB);
			acceptanceNoteService.updateAcceptanceNote(updateAcceptanceNote, ddBB);
		} else if(DataConstants.BATCH_STATUS_ID_ZJNFY
				.equals((String) acceptanceBatch.get(TableConstants.AcceptanceBatch.batchStatusId.name()))
				|| DataConstants.BATCH_STATUS_ID_JLNFY
						.equals((String) acceptanceBatch.get(TableConstants.AcceptanceBatch.batchStatusId.name()))){
			// 复验
			Map<String, Object> batchAcceptance = new HashMap<>();
			batchAcceptance.put(TableConstants.AcceptanceBatch.ACCEPTANCE_FIRST_ID.name(),
					(String) acceptanceBatch.get(TableConstants.AcceptanceBatch.id.name()));
			batchAcceptance.put(TableConstants.AcceptanceBatch.ACCEPTANCE_NOTE_ID.name(), acceptanceNoteId);
			batchAcceptance.put(TableConstants.AcceptanceBatch.ACCEPTER.name(),
					(String) acceptanceBatch.get(TableConstants.AcceptanceBatch.accepter.name()));
			batchAcceptance.put(TableConstants.AcceptanceBatch.ACCEPTANCE_PERSON_ID.name(), userId);
			batchAcceptance.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(),
					(String) acceptanceBatch.get(TableConstants.AcceptanceBatch.inspectorRole.name()));
			batchAcceptance.put(TableConstants.AcceptanceBatch.BATCH_NO.name(),
					(Integer) acceptanceBatch.get(TableConstants.AcceptanceBatch.batchNo.name()));
			batchAcceptance.put(TableConstants.AcceptanceBatch.CREATE_DATE.name(),
					DateUtil.convertDateTimeToString(new Date(), null));
			batchAcceptance.put(TableConstants.AcceptanceBatch.UPDATE_USER_ID.name(), userId);
			batchAcceptance.put(TableConstants.AcceptanceBatch.UPDATE_TIME.name(),
					DateUtil.convertDateTimeToString(new Date(), null));
			batchAcceptance.put(TableConstants.TENANT_ID, tenantId);
			batchAcceptance.put(TableConstants.IS_SEALED, 0);
			// 检查结果信息
			batchAcceptance.put(TableConstants.AcceptanceBatch.DOMINANT_ITEM_CHECK_RESULT.name(),
					(String) params.getParams().get(TableConstants.AcceptanceBatch.DOMINANT_ITEM_CHECK_RESULT.name()));
			batchAcceptance.put(TableConstants.AcceptanceBatch.GENERAL_ITEM_CHECK_RESULT.name(),
					(String) params.getParams().get(TableConstants.AcceptanceBatch.GENERAL_ITEM_CHECK_RESULT.name()));
			batchAcceptance.put(TableConstants.AcceptanceBatch.GENERAL_ITEM_CHECK_SCORE.name(), Float.valueOf(
					(String) params.getParams().get(TableConstants.AcceptanceBatch.GENERAL_ITEM_CHECK_SCORE.name())));
			batchAcceptance.put(TableConstants.AcceptanceBatch.TOTAL_CHECK_RESULT.name(),
					(String) params.getParams().get(TableConstants.AcceptanceBatch.TOTAL_CHECK_RESULT.name()));
			batchAcceptance.put(TableConstants.AcceptanceBatch.TOTAL_CHECK_SCORE.name(), Float
					.valueOf((String) params.getParams().get(TableConstants.AcceptanceBatch.TOTAL_CHECK_SCORE.name())));
			batchAcceptance.put(TableConstants.AcceptanceBatch.ELIGIBLE_RATE.name(),
					(String) params.getParams().get(TableConstants.AcceptanceBatch.ELIGIBLE_RATE.name()));
			batchAcceptance.put(TableConstants.AcceptanceBatch.REMARK.name(),
					(String) params.getParams().get(TableConstants.AcceptanceBatch.REMARK.name()));
			batchAcceptance.put(TableConstants.AcceptanceBatch.ACCEPTANCE_PERSON_ID.name(),userId);
			batchAcceptance.put(TableConstants.AcceptanceBatch.ACCEPTER.name(),(String)user.get(TableConstants.User.realName.name()));

			int batchAcceptanceNo = acceptanceBatchService
					.getAcceptanceBatchList(
							ParamsMap.newMap(TableConstants.AcceptanceBatch.ACCEPTANCE_NOTE_ID.name(), acceptanceNoteId)
									.addParams(TableConstants.AcceptanceBatch.BATCH_NO.name(),
											(Integer) acceptanceBatch
													.get(TableConstants.AcceptanceBatch.batchNo.name()))
									.addParams(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(),
											(String) acceptanceBatch
													.get(TableConstants.AcceptanceBatch.inspectorRole.name())),
							ddBB)
					.size();
			batchAcceptance.put(TableConstants.AcceptanceBatch.BATCH_ACCEPTANCE_NO.name(), batchAcceptanceNo + 1);
			batchAcceptance.put(TableConstants.AcceptanceBatch.BATCH_STATUS.name(),
					DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER);
			acceptanceBatchService.addAcceptanceBatch(batchAcceptance, ddBB);
			
			batchId = (String) batchAcceptance.get(TableConstants.AcceptanceBatch.ID.name());

			// 修改验收状态
			Map<String, Object> batchStatusParamsMap = new HashMap<>();
			batchStatusParamsMap.put(TableConstants.ProcedureBatchStatus.PROCEDURE_ID.name(),
					acceptanceNote.get(TableConstants.AcceptanceNote.procedureId.name()));
			batchStatusParamsMap.put(TableConstants.ProcedureBatchStatus.BATCH_NO.name(),
					acceptanceBatch.get(TableConstants.AcceptanceBatch.batchNo.name()));
			batchStatusParamsMap.put(TableConstants.ProcedureBatchStatus.ACCEPTANCE_NOTE_ID.name(),
					acceptanceNote.get(TableConstants.AcceptanceNote.id.name()));
			Map<String, Object> batchStatus = procedureBatchStatusService.getProcedureBatchStatus(batchStatusParamsMap,
					ddBB);
			if (batchStatus == null) {
				return new BaseResult(ReturnCode.FAIL);
			}
			Map<String, Object> updateBatchStatus = new HashMap<>();
			updateBatchStatus.put(TableConstants.AcceptanceBatch.UPDATE_USER_ID.name(), userId);
			updateBatchStatus.put(TableConstants.AcceptanceBatch.UPDATE_TIME.name(),
					DateUtil.convertDateTimeToString(new Date(), null));
			updateBatchStatus.put(TableConstants.ProcedureBatchStatus.ID.name(),
			batchStatus.get(TableConstants.ProcedureBatchStatus.id.name()));
			
			if (DataConstants.INSPECTOR_ROLE_ZJ
					.equals((String) acceptanceBatch.get(TableConstants.AcceptanceBatch.inspectorRole.name()))) {
				if (DataConstants.CHECK_SUCCESS.equals(
						(String) params.getParams().get(TableConstants.AcceptanceBatch.TOTAL_CHECK_RESULT.name()))) {
					
					if(!DataConstants.ACCEPTANCE_BATCH_STATUS_ID_JLYYS.equals((String)batchStatus.get(TableConstants.ProcedureBatchStatus.statementId.name()))){
						updateBatchStatus.put(TableConstants.ProcedureBatchStatus.STATEMENT_ID.name(),
								DataConstants.ACCEPTANCE_BATCH_STATUS_ID_ZJYYS);
					}

					batchAcceptance.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(),
							DataConstants.BATCH_STATUS_ID_ZJYYS);

					updateAcceptanceNote.put(TableConstants.AcceptanceNote.STATEMENT_ID.name(),
							DataConstants.PROCEDURE_STATUS_ID_ZJYYS);
				}else{
					batchAcceptance.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(),
							DataConstants.BATCH_STATUS_ID_ZJNFY);
				}
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONSTRUCTION_INSPECTOR_CHECKED.name(),
						(String) params.getParams().get(TableConstants.AcceptanceBatch.TOTAL_CHECK_RESULT.name()));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONSTRUCTION_INSPECTOR.name(),
						(String) acceptanceBatch.get(TableConstants.AcceptanceBatch.accepter.name()));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONSTRUCTION_INSPECTOR_ID.name(),
						(String) acceptanceBatch.get(TableConstants.AcceptanceBatch.acceptancePersonId.name()));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONSTRUCTION_INSPECTOR_CHECK_RESULT.name(),
						Float.valueOf((String) params.getParams()
								.get(TableConstants.AcceptanceBatch.TOTAL_CHECK_SCORE.name())));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.CONSTRUCTION_INSPECTOR_CHECK_DATE.name(),
						DateUtil.convertDateTimeToString(new Date(), null));
			} else if (DataConstants.INSPECTOR_ROLE_JL
					.equals((String) acceptanceBatch.get(TableConstants.AcceptanceBatch.inspectorRole.name()))) {
				if (DataConstants.CHECK_SUCCESS.equals(
						(String) params.getParams().get(TableConstants.AcceptanceBatch.TOTAL_CHECK_RESULT.name()))) {
					updateBatchStatus.put(TableConstants.ProcedureBatchStatus.STATEMENT_ID.name(),
							DataConstants.ACCEPTANCE_BATCH_STATUS_ID_JLYYS);

					batchAcceptance.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(),
							DataConstants.BATCH_STATUS_ID_JLYYS);

					updateAcceptanceNote.put(TableConstants.AcceptanceNote.STATEMENT_ID.name(),
							DataConstants.PROCEDURE_STATUS_ID_JLYYS);
				}else{
					batchAcceptance.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(),
							DataConstants.BATCH_STATUS_ID_JLNFY);
				}
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.SUPERVISOR_CHECKED.name(),
						(String) params.getParams().get(TableConstants.AcceptanceBatch.TOTAL_CHECK_RESULT.name()));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.SUPERVISOR_NAME.name(),
						(String) acceptanceBatch.get(TableConstants.AcceptanceBatch.accepter.name()));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.SUPERVISOR_ID.name(),
						(String) acceptanceBatch.get(TableConstants.AcceptanceBatch.acceptancePersonId.name()));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.SUPERVISOR_CHECK_RESULT.name(),
						Float.valueOf((String) params.getParams().get(TableConstants.AcceptanceBatch.TOTAL_CHECK_SCORE.name())));
				updateAcceptanceNote.put(TableConstants.AcceptanceNote.SUPERVISOR_CHECK_TIME.name(),
						DateUtil.convertDateTimeToString(new Date(), null));
			} else {
				return new BaseResult(ReturnCode.FAIL);
			}

			// 插入主控项检查信息
			for (Map<String, Object> item : dominantList) {
				item.put(TableConstants.AcceptanceDominantItem.TENANT_ID.name(), tenantId);
				item.put(TableConstants.AcceptanceDominantItem.ACCEPTANCE_NOTE_ID.name(), acceptanceNoteId);
				item.put(TableConstants.AcceptanceDominantItem.ACCEPTANCE_BATCH_ID.name(), batchId);
				acceptanceDominantItemService.addAcceptanceDominantItem(item, ddBB);
			}

			// 插入一般项检查信息
			for (Map<String, Object> item : generalList) {
				item.put(TableConstants.AcceptanceGeneralItem.TENANT_ID.name(), tenantId);
				item.put(TableConstants.AcceptanceGeneralItem.ACCEPTANCE_NOTE_ID.name(), acceptanceNoteId);
				item.put(TableConstants.AcceptanceGeneralItem.ACCEPTANCE_BATCH_ID.name(), batchId);

				// 添加一般项目记录点
				List<Map<String, Object>> pointList = (List<Map<String, Object>>) item
						.get(TableConstants.ACCEPTANCE_POINT);
				for (Map<String, Object> point : pointList) {
					point.put(TableConstants.AcceptancePoint.TENANT_ID.name(), tenantId);
					point.put(TableConstants.AcceptancePoint.ACCEPTANCE_GENERAN_NOTE_ID.name(),
							(String) item.get(TableConstants.AcceptanceGeneralItem.ID.name()));
					point.put(TableConstants.AcceptancePoint.GENERAL_ITEM_ID.name(),
							(String) item.get(TableConstants.AcceptanceGeneralItem.GENERAL_ITEM_ID.name()));
					point.put(TableConstants.AcceptancePoint.ACCEPTANCE_BATCH_ID.name(), batchId);
					point.put(TableConstants.AcceptancePoint.UNIT.name(),
							(String) item.get(TableConstants.AcceptanceGeneralItem.UNIT.name()));
					acceptancePointService.addAcceptancePoint(point, ddBB);
				}
				item.remove(TableConstants.ACCEPTANCE_POINT);
				acceptanceGeneralItemService.addAcceptanceGeneralItem(item, ddBB);
			}

			// 处理上传的图片
			List<Map<String, Object>> acceptanceAttachList = (List<Map<String, Object>>) params.getParams()
					.get(TableConstants.ACCEPTANCE_ATTACH);
			for (Map<String, Object> acceptanceAttach : acceptanceAttachList) {
				acceptanceAttach.put(TableConstants.AcceptanceAttach.TENANT_ID.name(), tenantId);
				acceptanceAttach.put(TableConstants.AcceptanceAttach.ACCEPT_NOTE_ID.name(), acceptanceNoteId);
				acceptanceAttach.put(TableConstants.AcceptanceAttach.ACCEPT_BATCH_ID.name(), batchId);
				acceptanceAttach.put(TableConstants.AcceptanceAttach.UPLOAD_TIME.name(),
						DateUtil.convertDateTimeToString(new Date(), null));
				acceptanceAttach.put(TableConstants.AcceptanceAttach.UPDATE_TIME.name(),
						DateUtil.convertDateTimeToString(new Date(), null));
				acceptanceAttach.put(TableConstants.AcceptanceAttach.UPDATE_USER_ID.name(), userId);
				acceptanceAttach.put(TableConstants.AcceptanceAttach.IS_SEALED.name(), 0);

				String idTree = ((String) acceptanceNote.get(TableConstants.AcceptanceNote.regionIdTree.name()))
						.replace(">", "/");
				String path = ImgUtil.saveAndUpdateFile(null,
						(String) acceptanceAttach.get(TableConstants.AcceptanceAttach.ATTACH_PATH.name()), null,
						ImgUtil.ACCEPTANCE_IMG_PATH, idTree);
				acceptanceAttach.put(TableConstants.AcceptanceAttach.ATTACH_PATH.name(), path);
				acceptanceAttachService.addAcceptanceAttach(acceptanceAttach, ddBB);
			}
			
			updateAcceptanceBatch.put(TableConstants.AcceptanceBatch.BATCH_STATUS.name(),
					DataConstants.ACCEPTANCE_BATCH_STATUS_MEMBER);
			
			procedureBatchStatusService.updateProcedureBatchStatus(updateBatchStatus, ddBB);
			acceptanceBatchService.updateAcceptanceBatch(batchAcceptance, ddBB);
			acceptanceBatchService.updateAcceptanceBatch(updateAcceptanceBatch, ddBB);
			acceptanceNoteService.updateAcceptanceNote(updateAcceptanceNote, ddBB);
			
		}else{
			return new BaseResult(ReturnCode.ACCEPTANCE_IS_SUCCEED);
		}
		
		updateProcedureScheduleRelate(ddBB, userId, (String)acceptanceNote.get(TableConstants.AcceptanceNote.procedureId.name()),
				(String)acceptanceNote.get(TableConstants.AcceptanceNote.regionType.name()), 
				(String)acceptanceNote.get(TableConstants.AcceptanceNote.regionId.name()));
		
		JSONObject result=new JSONObject();
		result.put(TableConstants.AcceptanceBatch.id.name(), batchId);
		
		return new BaseResult(ReturnCode.OK,result);
	}

	/**
	 * 获取验收批信息
	 * 
	 * @param params
	 * @param ddBB
	 * @return
	 */
	@RequestMapping("/acceptanceBatch/getBatchInfo")
	@ResponseBody
	public BaseResult getBatchInfo(@RequestAttribute(Constants.USER_ID) String userId, @RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		String batchId = (String) params.getParams().get(TableConstants.AcceptanceBatch.ID.name());
		
		if(batchId==null){
			String projectPeriodId = (String) params.getParams().get(TableConstants.AcceptanceNote.PROJECT_PERIOD_ID.name());
			String inspectorRole = (String) params.getParams().get(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name());
			String regionType = (String) params.getParams().get(TableConstants.AcceptanceNote.REGION_TYPE.name());
			String regionId = (String) params.getParams().get(TableConstants.AcceptanceNote.REGION_ID.name());
			String procedureId = (String) params.getParams().get(TableConstants.AcceptanceNote.PROCEDURE_ID.name());
			
			Map<String, Object> map = acceptanceNoteService
					.getAcceptanceNote(ParamsMap.newMap(TableConstants.AcceptanceNote.PROCEDURE_ID.name(), procedureId)
							.addParams(TableConstants.AcceptanceNote.REGION_ID.name(), regionId)
							.addParams(TableConstants.AcceptanceNote.REGION_TYPE.name(), regionType)
							.addParams(TableConstants.IS_SEALED, 0), ddBB);
			if(map!=null){
				batchId = (String) acceptanceBatchService
						.getAcceptanceBatch(ParamsMap.newMap(TableConstants.AcceptanceBatch.ACCEPTANCE_NOTE_ID.name(), (String)map.get(TableConstants.AcceptanceNote.id.name()))
								.addParams(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), inspectorRole)
								.addParams(TableConstants.AcceptanceBatch.BATCH_STATUS.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER)
								.addParams(TableConstants.IS_SEALED, 0), ddBB).get(TableConstants.AcceptanceBatch.id.name());
			}else{
				batchId=addNoteBatch(ddBB, tenantId, projectPeriodId, inspectorRole, userId, regionType, regionId, procedureId);
			}
		}
		if(StringUtils.isBlank(batchId)){
			new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}

		Map<String, Object> result = acceptanceBatchService.getBatchInfo(batchId, ddBB);

		Map<String, Object> map = new LinkedHashMap<>();
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		map.put(TableConstants.AcceptanceNote.PROCEDURE_ID.name(),
				(String) result.get(TableConstants.AcceptanceNote.procedureId.name()));
		result.put("dominantItem", dominantItemService.getDominantItemList(map, ddBB));
		result.put("generalItem", generalItemService.getGeneralItemList(map, ddBB));
		return new BaseResult(ReturnCode.OK, result);
	}

	/**
	 * 获取工作台任务列表
	 * 
	 * @param userId
	 * @param tenantId
	 * @param page
	 * @param ddBB
	 * @return
	 */
	@RequestMapping("/acceptanceBatch/getPageByStatus")
	@ResponseBody
	public BaseResult getPageByStatus(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		String index = (String) page.getParams().get(TableConstants.AcceptanceNote.STATEMENT_ID.name());

		String postId = (String) userService.getByID(userId, ddBB).get(TableConstants.User.postId.name());
		String batchStatus = null;
		String acceptanceStatus = null;

		if ("1".equals(index)) {
			if (DataConstants.INSPECTOR_ROLE_ZJ.equals(postId)) {
				batchStatus = DataConstants.BATCH_STATUS_ID_ZJWYS;
				acceptanceStatus = DataConstants.ACCEPTANCE_BATCH_STATUS_ID_YBY;
			} else if (DataConstants.INSPECTOR_ROLE_JL.equals(postId)) {
				batchStatus = DataConstants.BATCH_STATUS_ID_JLWYS;
				acceptanceStatus = DataConstants.ACCEPTANCE_BATCH_STATUS_ID_ZJYYS;
			}
		} else if ("2".equals(index)) {
			if (DataConstants.INSPECTOR_ROLE_ZJ.equals(postId)) {
				batchStatus = DataConstants.BATCH_STATUS_ID_ZJNFY;
			} else if (DataConstants.INSPECTOR_ROLE_JL.equals(postId)) {
				batchStatus = DataConstants.BATCH_STATUS_ID_JLNFY;
			}
		} else if ("3".equals(index)) {
			if (DataConstants.INSPECTOR_ROLE_ZJ.equals(postId)) {
				batchStatus = DataConstants.BATCH_STATUS_ID_ZJYYS;
			} else if (DataConstants.INSPECTOR_ROLE_JL.equals(postId)) {
				batchStatus = DataConstants.BATCH_STATUS_ID_JLYYS;
			}
		}
		if (batchStatus == null) {
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}
		return new BaseResult(ReturnCode.OK,
				acceptanceBatchService.getPageByStatus(page, acceptanceStatus, batchStatus, userId, ddBB));
	}

	/**
	 * 新增检验批
	 * 
	 * @param userId
	 * @param tenantId
	 * @param params
	 * @param ddBB
	 * @return
	 */
	@RequestMapping("/acceptanceBatch/addBatch")
	@ResponseBody
	public BaseResult addBatch(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {

		String batchId = (String) params.getParams().get(TableConstants.AcceptanceBatch.ID.name());

		Map<String, Object> oldBatch = acceptanceBatchService.getByID(batchId, ddBB);

		Map<String, Object> noteMap = acceptanceNoteService
				.getByID((String) oldBatch.get(TableConstants.AcceptanceBatch.acceptanceNoteId.name()), ddBB);

		Map<String, Object> batchStatusParamsMap = new HashMap<>();
		batchStatusParamsMap.put(TableConstants.ProcedureBatchStatus.PROCEDURE_ID.name(),
				noteMap.get(TableConstants.AcceptanceNote.procedureId.name()));
		batchStatusParamsMap.put(TableConstants.ProcedureBatchStatus.BATCH_NO.name(),
				oldBatch.get(TableConstants.AcceptanceBatch.batchNo.name()));
		batchStatusParamsMap.put(TableConstants.ProcedureBatchStatus.ACCEPTANCE_NOTE_ID.name(),
				noteMap.get(TableConstants.AcceptanceNote.id.name()));
		Map<String, Object> batchStatus = procedureBatchStatusService.getProcedureBatchStatus(batchStatusParamsMap,
				ddBB);

		if (oldBatch == null || noteMap == null || batchStatus == null) {
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}

		Integer batchTimes = 1 + (Integer) noteMap.get(TableConstants.AcceptanceNote.batchTimes.name());

		Map<String, Object> batchMap = new HashMap<>();
		batchMap.put(TableConstants.TENANT_ID, tenantId);
		batchMap.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		batchMap.put(TableConstants.UPDATE_USER_ID, userId);
		batchMap.put(TableConstants.IS_SEALED, 0);
		batchMap.put(TableConstants.AcceptanceBatch.ACCEPTANCE_NOTE_ID.name(),
				(String) noteMap.get(TableConstants.AcceptanceNote.id.name()));
		batchMap.put(TableConstants.AcceptanceBatch.MIN_PASS_RATIO.name(),
				(Float) oldBatch.get(TableConstants.AcceptanceBatch.minPassRatio.name()));
		batchMap.put(TableConstants.AcceptanceBatch.BATCH_NO.name(), batchTimes);
		batchMap.put(TableConstants.AcceptanceBatch.BATCH_ACCEPTANCE_NO.name(), 1);
		batchMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER);

		batchMap.put(TableConstants.AcceptanceBatch.ACCEPTER.name(),
				(String) oldBatch.get(TableConstants.AcceptanceBatch.accepter.name()));
		batchMap.put(TableConstants.AcceptanceBatch.ACCEPTANCE_PERSON_ID.name(),
				(String) oldBatch.get(TableConstants.AcceptanceBatch.acceptancePersonId.name()));

		Map<String, Object> zjBatchMap = new HashMap<>(batchMap);
		zjBatchMap.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), DataConstants.INSPECTOR_ROLE_ZJ);
		zjBatchMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(), DataConstants.BATCH_STATUS_ID_ZJWYS);

		Map<String, Object> jlBatchMap = new HashMap<>(batchMap);
		jlBatchMap.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), DataConstants.INSPECTOR_ROLE_JL);
		jlBatchMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS_ID.name(), DataConstants.BATCH_STATUS_ID_JLWYS);

		if (DataConstants.INSPECTOR_ROLE_ZJ
				.equals((String) oldBatch.get(TableConstants.AcceptanceBatch.inspectorRole.name()))) {
			Map<String, Object> batchParamsMap = new HashMap<>();
			batchParamsMap.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), DataConstants.INSPECTOR_ROLE_JL);
			batchParamsMap.put(TableConstants.AcceptanceBatch.ACCEPTANCE_NOTE_ID.name(),
					(String) oldBatch.get(TableConstants.AcceptanceBatch.acceptanceNoteId.name()));
			batchParamsMap.put(TableConstants.AcceptanceBatch.BATCH_NO.name(),
					oldBatch.get(TableConstants.AcceptanceBatch.batchNo.name()));
			Map<String, Object> jlAcceptanceBatch = acceptanceBatchService.getAcceptanceBatch(batchParamsMap, ddBB);

			jlBatchMap.put(TableConstants.AcceptanceBatch.ACCEPTER.name(),
					(String) jlAcceptanceBatch.get(TableConstants.AcceptanceBatch.accepter.name()));
			jlBatchMap.put(TableConstants.AcceptanceBatch.ACCEPTANCE_PERSON_ID.name(),
					(String) jlAcceptanceBatch.get(TableConstants.AcceptanceBatch.acceptancePersonId.name()));
		} else if (DataConstants.INSPECTOR_ROLE_JL
				.equals((String) oldBatch.get(TableConstants.AcceptanceBatch.inspectorRole.name()))) {
			Map<String, Object> batchParamsMap = new HashMap<>();
			batchParamsMap.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), DataConstants.INSPECTOR_ROLE_ZJ);
			batchParamsMap.put(TableConstants.AcceptanceBatch.ACCEPTANCE_NOTE_ID.name(),
					(String) oldBatch.get(TableConstants.AcceptanceBatch.acceptanceNoteId.name()));
			batchParamsMap.put(TableConstants.AcceptanceBatch.BATCH_NO.name(),
					oldBatch.get(TableConstants.AcceptanceBatch.batchNo.name()));
			Map<String, Object> zjAcceptanceBatch = acceptanceBatchService.getAcceptanceBatch(batchParamsMap, ddBB);

			zjBatchMap.put(TableConstants.AcceptanceBatch.ACCEPTER.name(),
					(String) zjAcceptanceBatch.get(TableConstants.AcceptanceBatch.accepter.name()));
			zjBatchMap.put(TableConstants.AcceptanceBatch.ACCEPTANCE_PERSON_ID.name(),
					(String) zjAcceptanceBatch.get(TableConstants.AcceptanceBatch.acceptancePersonId.name()));
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

		acceptanceBatchService.addAcceptanceBatch(zjBatchMap, ddBB);

		acceptanceBatchService.addAcceptanceBatch(jlBatchMap, ddBB);

		// 加入批次状态
		Map<String, Object> batchStatusMap = new HashMap<>();
		batchStatusMap.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		batchStatusMap.put(TableConstants.UPDATE_USER_ID, userId);
		batchStatusMap.put(TableConstants.IS_SEALED, 0);
		batchStatusMap.put(TableConstants.ProcedureBatchStatus.PROCEDURE_ID.name(),
				(String) noteMap.get(TableConstants.AcceptanceNote.procedureId.name()));
		batchStatusMap.put(TableConstants.ProcedureBatchStatus.BATCH_NO.name(), batchTimes);
		batchStatusMap.put(TableConstants.ProcedureBatchStatus.ACCEPTANCE_NOTE_ID.name(),
				(String) noteMap.get(TableConstants.AcceptanceNote.id.name()));
		batchStatusMap.put(TableConstants.ProcedureBatchStatus.STATEMENT_ID.name(),
				(String) batchStatus.get(TableConstants.ProcedureBatchStatus.statementId.name()));
		procedureBatchStatusService.addProcedureBatchStatus(batchStatusMap, ddBB);

		Map<String, Object> newNote = new LinkedHashMap<>();
		newNote.put(TableConstants.AcceptanceNote.BATCH_TIMES.name(), batchTimes);
		newNote.put(TableConstants.AcceptanceNote.ID.name(),
				(String) noteMap.get(TableConstants.AcceptanceNote.id.name()));
		acceptanceNoteService.updateAcceptanceNote(newNote, ddBB);

		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/acceptanceBatch/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {

		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		map.put(TableConstants.IS_SEALED, 0);
		acceptanceBatchService.addAcceptanceBatch(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/acceptanceBatch/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = acceptanceBatchService.deleteAcceptanceBatch(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/acceptanceBatch/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = acceptanceBatchService.updateAcceptanceBatch(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/acceptanceBatch/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, acceptanceBatchService.getAcceptanceBatch(map, ddBB));
	}

	@RequestMapping("/acceptanceBatch/getList")
	@ResponseBody
	public BaseResult getList(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, acceptanceBatchService.getAcceptanceBatchList(map, ddBB));
	}

	@RequestMapping("/acceptanceBatch/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, acceptanceBatchService.getAcceptanceBatchPage(page, ddBB));
	}
	
	/**
	 * 更新工序类型部位进度
	 * @param ddBB
	 * @param procedureId
	 * @param regionType
	 * @param regionId
	 */
	private void updateProcedureScheduleRelate(String ddBB,String userId,String procedureId,String regionType,String regionId){
		if((DataConstants.REGION_ROOM_TYPE_VAL.equals(regionType)&&StringUtils.isNotBlank(regionId))||
				(DataConstants.REGION_FLOOR_TYPE_VAL.equals(regionType)&&StringUtils.isNotBlank(regionId))){
			Map<String, Object> procedure=procedureInfoService.getByID(procedureId, ddBB);
			Map<String, Object> procedureType=procedureTypeService.getByID((String)procedure.get(TableConstants.ProcedureInfo.procedureTypeId.name()), ddBB);
			String idTree=(String) procedureType.get(TableConstants.ProcedureType.idTree.name());
			if(idTree!=null){
				String procedureTypeId=idTree.split(TableConstants.SEPARATE_TREE)[0];
				
				Map<String, Object> params=new HashMap<>();
				params.put(TableConstants.ProcedureScheduleRelate.PROCEDURE_TYPE_ID.name(), procedureTypeId);
				params.put(TableConstants.ProcedureScheduleRelate.ROOM_ID.name(), regionId);
				Map<String, Object> procedureSchedule=procedureScheduleRelateService.getProcedureScheduleRelate(params, ddBB);
				if(procedureSchedule==null){
					procedureSchedule=new HashMap<>();
					procedureSchedule.put(TableConstants.ProcedureScheduleRelate.PROCEDURE_TYPE_ID.name(), procedureTypeId);
					procedureSchedule.put(TableConstants.ProcedureScheduleRelate.ROOM_ID.name(), regionId);
					procedureSchedule.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
					procedureSchedule.put(TableConstants.UPDATE_USER_ID, userId);
					procedureScheduleRelateService.addProcedureScheduleRelate(procedureSchedule, ddBB);
				}
			}
		}
	}
	
	private String addNoteBatch(String ddBB,String tenantId,String projectPeriodId,String inspectorRole,
			String userId,String regionType,String regionId,String procedureId){
		Map<String, Object> projectPeriod = projectPeriodService.getByID(projectPeriodId, ddBB);
		Map<String, Object> projectInfo = projectInfoService.getByID((String)projectPeriod.get(TableConstants.ProjectPeriod.projectId.name()), ddBB);
		Map<String, Object> user = userService.getByID(userId, ddBB);
		
		
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
			period = projectPeriod;
			regionMinName=(String) period.get(TableConstants.ProjectPeriod.periodName.name());
			regionName=(String) period.get(TableConstants.ProjectPeriod.periodName.name())+TableConstants.SEPARATE_TREE+DataConstants.REGION_PERIOD_KEY;
		} else if (DataConstants.REGION_BUILDING_TYPE_VAL.equals(regionType)) {
			period = projectBuildingService.getByID(regionId, ddBB);
			regionMinName=(String) period.get(TableConstants.ProjectBuilding.buildingName.name());
			regionName=(String) period.get(TableConstants.ProjectBuilding.buildingName.name())+TableConstants.SEPARATE_TREE+DataConstants.REGION_BUILDING_KEY;
		} else if (DataConstants.REGION_FLOOR_TYPE_VAL.equals(regionType)) {
			period = projectHouseholdService.getByID(regionId, ddBB);
			regionMinName=(String) period.get(TableConstants.ProjectHousehold.roomName.name());
			regionName=((String) period.get(TableConstants.ProjectHousehold.nameTree.name())).replace(projectPeriod.get(TableConstants.ProjectPeriod.periodName.name())+TableConstants.SEPARATE_TREE, "");
		} else if (DataConstants.REGION_ROOM_TYPE_VAL.equals(regionType)) {
			period = projectHouseholdService.getByID(regionId, ddBB);
			regionMinName=(String) period.get(TableConstants.ProjectHousehold.roomName.name());
			regionName=((String) period.get(TableConstants.ProjectHousehold.nameTree.name())).replace(projectPeriod.get(TableConstants.ProjectPeriod.periodName.name())+TableConstants.SEPARATE_TREE, "");
		} else {
			logger.info("部位类型不正常");
			return null;
		}
		if(period==null){
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
			nmap.put(TableConstants.AcceptanceNote.TEAM_INSPECTOR_CHECKED.name(), 0);
			nmap.put(TableConstants.AcceptanceNote.TEAM_INSPECTOR_CHECK_TIME.name(),
					DateUtil.convertDateTimeToString(new Date(), null));
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

			map.put(TableConstants.AcceptanceNote.TEAM_INSPECTOR_CHECKED.name(), 0);
			map.put(TableConstants.AcceptanceNote.TEAM_INSPECTOR_CHECK_TIME.name(),
					DateUtil.convertDateTimeToString(new Date(), null));

			acceptanceNoteService.addAcceptanceNote(map, ddBB);
			acceptanceNoteId=(String) map.get(TableConstants.AcceptanceNote.ID.name());
		}
		
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
		batchStatusMap.put(TableConstants.ProcedureBatchStatus.STATEMENT_ID.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_ID_WBY);
		procedureBatchStatusService.addProcedureBatchStatus(batchStatusMap, ddBB);
		
		if (DataConstants.INSPECTOR_ROLE_ZJ.equals(inspectorRole)) {
			return (String) zjBatchMap.get(TableConstants.AcceptanceBatch.ID.name());
		}else if(DataConstants.INSPECTOR_ROLE_JL.equals(inspectorRole)){
			return (String) jlBatchMap.get(TableConstants.AcceptanceBatch.ID.name());
		}else{
			return null;
		}
		
	}
}
