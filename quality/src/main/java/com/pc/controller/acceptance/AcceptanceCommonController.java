package com.pc.controller.acceptance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.DataConstants;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.service.acceptance.impl.AcceptanceBatchService;
import com.pc.service.acceptance.impl.AcceptanceNoteService;
import com.pc.service.acceptance.impl.HiddenPhotosRecordService;
import com.pc.service.auth.DataPrivilegeTypeService;
import com.pc.service.organization.impl.TeamInfoService;
import com.pc.service.project.impl.HouseholdChartAreaService;
import com.pc.service.project.impl.ProjectHouseholdService;
import com.pc.service.project.impl.UnitChartService;
import com.pc.service.user.UserService;
import com.pc.vo.ParamsVo;

@Controller
@RequestMapping("/client")
public class AcceptanceCommonController extends BaseController {
	@Autowired
	private AcceptanceBatchService acceptanceBatchService;

	@Autowired
	private AcceptanceNoteService acceptanceNoteService;

	@Autowired
	private UserService userService;

	@Autowired
	private TeamInfoService teamInfoService;

	@Autowired
	private DataPrivilegeTypeService dataPrivilegeTypeService;

	@Autowired
	private UnitChartService unitChartService;

	@Autowired
	private ProjectHouseholdService projectHouseholdService;

	@Autowired
	private HouseholdChartAreaService householdChartAreaService;

	@Autowired
	private HiddenPhotosRecordService hiddenPhotosRecordService;

	private Logger logger = LogManager.getLogger(this.getClass());

	@RequestMapping("/user/getInspectorList")
	@ResponseBody
	public BaseResult getInspectorList(@RequestHeader(Constants.TENANT_ID) String tenantId,@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		String procedureId = (String) params.getParams().get(TableConstants.AcceptanceNote.PROCEDURE_ID.name());
		String projectPeriodId = (String) params.getParams()
				.get(TableConstants.AcceptanceNote.PROJECT_PERIOD_ID.name());
		if (StringUtils.isBlank(projectPeriodId) || StringUtils.isBlank(projectPeriodId)) {
			return new BaseResult(ReturnCode.REQUEST_PARAMS_MISSING_ERROR);
		}
		Map<String, Object> map = new LinkedHashMap<>();
		map.put(TableConstants.User.tenantId.name(), tenantId);
		map.put(TableConstants.User.postId.name(), DataConstants.INSPECTOR_ROLE_ZJ);
		map.put(TableConstants.AcceptanceNote.procedureId.name(), procedureId);
		map.put(TableConstants.AcceptanceNote.projectPeriodId.name(), projectPeriodId);
		String gDataTypeId = (String) dataPrivilegeTypeService.getDataPrivilegeType(
				ParamsMap.newMap(TableConstants.DataPrivilegeType.TABLE_NAME.name(), TableConstants.PROJECT_HOUSEHOLD),
				ddBB).get(TableConstants.DataPrivilegeType.id.name());
		map.put("gDataTypeId", gDataTypeId);
		String pDataTypeId = (String) dataPrivilegeTypeService.getDataPrivilegeType(
				ParamsMap.newMap(TableConstants.DataPrivilegeType.TABLE_NAME.name(), TableConstants.PROCEDURE_INFO),
				ddBB).get(TableConstants.DataPrivilegeType.id.name());
		map.put("pDataTypeId", pDataTypeId);
		return new BaseResult(ReturnCode.OK, userService.getUserListByPostAuth(map, ddBB));
	}

	@RequestMapping("/user/getSupervisorList")
	@ResponseBody
	public BaseResult getSupervisorList(@RequestHeader(Constants.TENANT_ID) String tenantId,@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		String noteId = (String) params.getParams().get(TableConstants.AcceptanceBatch.ACCEPTANCE_NOTE_ID.name());
		if (StringUtils.isBlank(noteId)) {
			return new BaseResult(ReturnCode.REQUEST_PARAMS_MISSING_ERROR);
		}
		Map<String, Object> acceptanceNote = acceptanceNoteService.getByID(noteId, ddBB);
		if (acceptanceNote == null) {
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}

		String procedureId = (String) acceptanceNote.get(TableConstants.AcceptanceNote.procedureId.name());
		String projectPeriodId = (String) acceptanceNote.get(TableConstants.AcceptanceNote.projectPeriodId.name());

		Map<String, Object> map = new LinkedHashMap<>();
		map.put(TableConstants.User.tenantId.name(), tenantId);
		map.put(TableConstants.User.postId.name(), DataConstants.INSPECTOR_ROLE_JL);
		map.put(TableConstants.AcceptanceNote.procedureId.name(), procedureId);
		map.put(TableConstants.AcceptanceNote.projectPeriodId.name(), projectPeriodId);
		String gDataTypeId = (String) dataPrivilegeTypeService.getDataPrivilegeType(
				ParamsMap.newMap(TableConstants.DataPrivilegeType.TABLE_NAME.name(), TableConstants.PROJECT_HOUSEHOLD),
				ddBB).get(TableConstants.DataPrivilegeType.id.name());
		map.put("gDataTypeId", gDataTypeId);
		String pDataTypeId = (String) dataPrivilegeTypeService.getDataPrivilegeType(
				ParamsMap.newMap(TableConstants.DataPrivilegeType.TABLE_NAME.name(), TableConstants.PROCEDURE_INFO),
				ddBB).get(TableConstants.DataPrivilegeType.id.name());
		map.put("pDataTypeId", pDataTypeId);
		return new BaseResult(ReturnCode.OK, userService.getUserListByPostAuth(map, ddBB));
	}

	/**
	 * 报验获取班组
	 * 
	 * @param tenantId
	 * @param params
	 * @param ddBB
	 * @return
	 */
	@RequestMapping("/team/getProjectTeamList")
	@ResponseBody
	public BaseResult getProjectTeamList(@RequestHeader(Constants.TENANT_ID) String tenantId,
			@EncryptProcess() ParamsVo params, @RequestAttribute String ddBB) {
		String projectPeriodId = (String) params.getParams()
				.get(TableConstants.ProjectPartnerRelate.PROJECT_PERIOD_ID.name());
		String batchId = (String) params.getParams().get(TableConstants.AcceptanceBatch.ID.name());

		if (StringUtils.isBlank(projectPeriodId) || StringUtils.isBlank(batchId)) {
			return new BaseResult(ReturnCode.REQUEST_PARAMS_MISSING_ERROR);
		}

		Map<String, Object> acceptanceBatch = acceptanceBatchService.getByID(batchId, ddBB);
		Map<String, Object> acceptanceNote = acceptanceNoteService
				.getByID((String) acceptanceBatch.get(TableConstants.AcceptanceBatch.acceptanceNoteId.name()), ddBB);

		Map<String, Object> map = new LinkedHashMap<>();
		map.put(TableConstants.TeamInfo.tenantId.name(), tenantId);
		map.put(TableConstants.ProjectPartnerRelate.projectPeriodId.name(), projectPeriodId);
		map.put(TableConstants.TeamProcedureRelate.procedureId.name(),
				(String) acceptanceNote.get(TableConstants.AcceptanceNote.procedureId.name()));
		return new BaseResult(ReturnCode.OK, teamInfoService.getTeamListByProject(map, ddBB));
	}

	/**
	 * 获取户型图信息
	 * 
	 * @param tenantId
	 * @param ddBB
	 * @param params
	 * @return
	 */
	@RequestMapping("/householdChart/getHouseholdChartInfo")
	@ResponseBody
	public BaseResult getHouseholdChart(@RequestHeader(Constants.TENANT_ID) String tenantId,
			@RequestAttribute String ddBB, @EncryptProcess() ParamsVo params) {

		String projectHouseholdId = (String) params.getParams().get(TableConstants.ProjectHousehold.ID.name());
		if (projectHouseholdId == null) {
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}
		Map<String, Object> projectHousehold = projectHouseholdService.getByID(projectHouseholdId, ddBB);
		if (projectHousehold == null) {
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}

		String householdChartId = (String) projectHousehold
				.get(TableConstants.ProjectHousehold.householdChartId.name());
		if (householdChartId == null) {
			return new BaseResult(ReturnCode.NO_HOUSEHOLD_CHART);
		}

		Map<String, Object> householdChart = unitChartService.getByID(householdChartId, ddBB);
		if (householdChart == null) {
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}

		List<Map<String, Object>> householdChartAreaList = householdChartAreaService.getHouseholdAreaInfoByChart(
				ParamsMap.newMap(TableConstants.HouseholdChartArea.householdChartId.name(), householdChartId), ddBB);

		List<Map<String, Object>> hiddenPhotosRecordList = hiddenPhotosRecordService.getHiddenPhotosRecordList(
				ParamsMap.newMap(TableConstants.HiddenPhotosRecord.ROOM_ID.name(), projectHouseholdId), ddBB);
		Map<String, Integer> tempMap = new HashMap<>();
		for (Map<String, Object> hiddenPhotosRecord : hiddenPhotosRecordList) {
			tempMap.put((String) hiddenPhotosRecord.get(TableConstants.HiddenPhotosRecord.householdAreaId.name()),
					(Integer) hiddenPhotosRecord.get(TableConstants.HiddenPhotosRecord.photoNum.name()));
		}

		List<Map<String, Object>> qtList = new ArrayList<>();
		List<Map<String, Object>> lbList = new ArrayList<>();
		List<Map<String, Object>> dpList = new ArrayList<>();
		for (Map<String, Object> info : householdChartAreaList) {
			Integer photoNum = tempMap.get((String) info.get(TableConstants.HouseholdChartAreaType.id.name()));
			if (photoNum == null) {
				photoNum = 0;
			}
			info.put(TableConstants.HiddenPhotosRecord.photoNum.name(), photoNum);
			if (DataConstants.HOUSEHOLD_CHART_AREA_TYPE_QT.equals(
					(String) info.get(TableConstants.HouseholdChartAreaType.householdChartAreaTypeName.name()))) {
				qtList.add(info);
			} else if (DataConstants.HOUSEHOLD_CHART_AREA_TYPE_LB.equals(
					(String) info.get(TableConstants.HouseholdChartAreaType.householdChartAreaTypeName.name()))) {
				lbList.add(info);
			} else if (DataConstants.HOUSEHOLD_CHART_AREA_TYPE_DP.equals(
					(String) info.get(TableConstants.HouseholdChartAreaType.householdChartAreaTypeName.name()))) {
				dpList.add(info);
			} else {
				logger.info("户型图分区类型不存在");
			}
		}

		householdChart.put("qtHouseholdChartAreaList", qtList);
		householdChart.put("lbHouseholdChartAreaList", lbList);
		householdChart.put("dpHouseholdChartAreaList", dpList);

		return new BaseResult(ReturnCode.OK, householdChart);
	}
}
