package com.pc.controller.acceptance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import com.alibaba.fastjson.JSONObject;
import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.DataConstants;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.core.TableConstants.ProjectPartnerRelate;
import com.pc.service.acceptance.impl.AcceptanceBatchService;
import com.pc.service.acceptance.impl.AcceptanceNoteService;
import com.pc.service.acceptance.impl.HiddenPhotosRecordService;
import com.pc.service.auth.DataPrivilegeTypeService;
import com.pc.service.organization.impl.CompanyService;
import com.pc.service.organization.impl.PostInfoService;
import com.pc.service.organization.impl.TeamInfoService;
import com.pc.service.procedure.impl.ProcedureTypeService;
import com.pc.service.project.impl.HouseholdChartAreaService;
import com.pc.service.project.impl.ProjectHouseholdService;
import com.pc.service.project.impl.ProjectPartnerRelateService;
import com.pc.service.project.impl.UnitChartService;
import com.pc.service.user.UserService;
import com.pc.util.DateUtil;
import com.pc.util.WeatherUtil;
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
	
	@Autowired
	private ProjectPartnerRelateService projectPartnerRelateService;
	
	@Autowired
	private PostInfoService postInfoService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private ProcedureTypeService procedureTypeService;

	private Logger logger = LogManager.getLogger(this.getClass());
	
	@RequestMapping("/acceptanceNote/getProjectAcceptanceDetailCount")
	@ResponseBody
	public BaseResult getProjectAcceptanceDetailCount(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId,
			@RequestAttribute String ddBB,@EncryptProcess ParamsVo params) {
		String projectPeriodId=(String) params.getParams().get(TableConstants.AcceptanceNote.PROJECT_PERIOD_ID.name());
		String beginTime = (String) params.getParams().get("BEGIN_TIME");
		String endTime = (String) params.getParams().get("END_TIME");
		if(StringUtils.isBlank(projectPeriodId)){
			return new BaseResult(ReturnCode.REQUEST_PARAMS_MISSING_ERROR);
		}
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(TableConstants.AcceptanceNote.PROJECT_PERIOD_ID.name(), projectPeriodId);
		
		if(StringUtils.isNotBlank(beginTime)){
			paramsMap.put("beginTime", beginTime+" 00:00:00");
		}
		if(StringUtils.isNotBlank(endTime)){
			paramsMap.put("endTime", endTime+" 23:59:59");
		}
		
		Map<String, Object> typeParams=new HashMap<>();
		typeParams.put(TableConstants.TENANT_ID, tenantId);
    	typeParams.put(TableConstants.ProcedureType.PROCEDURE_TYPE_NAME.name(), DataConstants.PROCEDURE_TYPE_TJ);
    	Map<String, Object> ztType=procedureTypeService.getProcedureType(typeParams, ddBB);
    	if(ztType!=null){
    		paramsMap.put("ztTypeId", ztType.get("id"));
    	}else{
    		paramsMap.put("ztTypeId", "");
    	}
    	typeParams.put(TableConstants.ProcedureType.PROCEDURE_TYPE_NAME.name(), DataConstants.PROCEDURE_TYPE_ZX);
    	Map<String, Object> zxType=procedureTypeService.getProcedureType(typeParams, ddBB);
    	if(zxType!=null){
    		paramsMap.put("zxTypeId", zxType.get("id"));
    	}else{
    		paramsMap.put("zxTypeId", "");
    	}
    	typeParams.put(TableConstants.ProcedureType.PROCEDURE_TYPE_NAME.name(), DataConstants.PROCEDURE_TYPE_DJ);
    	Map<String, Object> djType=procedureTypeService.getProcedureType(typeParams, ddBB);
    	if(djType!=null){
    		paramsMap.put("djTypeId", djType.get("id"));
    	}else{
    		paramsMap.put("djTypeId", "");
    	}
    	
    	Map<String, Object> result = new HashMap<String, Object>();
    	
    	paramsMap.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), DataConstants.INSPECTOR_ROLE_ZJ);
    	List<Map<String, Object>> zjList=acceptanceNoteService.getProjectAcceptanceDetailCount(paramsMap, ddBB);
    	result.put("zjList", zjList);
    	
    	paramsMap.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), DataConstants.INSPECTOR_ROLE_JL);
    	List<Map<String, Object>> jlList=acceptanceNoteService.getProjectAcceptanceDetailCount(paramsMap, ddBB);
    	result.put("jlList", jlList);
    	
    	paramsMap.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), DataConstants.INSPECTOR_ROLE_JF);
    	List<Map<String, Object>> jfList=acceptanceNoteService.getProjectAcceptanceDetailCount(paramsMap, ddBB);
    	result.put("jfList", jfList);
    	
    	paramsMap.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), DataConstants.INSPECTOR_ROLE_YF);
    	List<Map<String, Object>> yfList=acceptanceNoteService.getProjectAcceptanceDetailCount(paramsMap, ddBB);
    	result.put("yfList", yfList);
    	
    	paramsMap.remove(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name());
    	List<Map<String, Object>> qtList=acceptanceNoteService.getProjectAcceptanceDetailCount(paramsMap, ddBB);
    	result.put("qtList", qtList);
    	
		return new BaseResult(ReturnCode.OK,result);
	}
	
	@RequestMapping("/acceptanceNote/getProjectAcceptanceCount")
	@ResponseBody
	public BaseResult getProjectAcceptanceCount(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId,
			@RequestAttribute String ddBB,@EncryptProcess ParamsVo params) {
		String beginTime = (String) params.getParams().get("BEGIN_TIME");
		String endTime = (String) params.getParams().get("END_TIME");
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(beginTime)){
			paramsMap.put("beginTime", beginTime+" 00:00:00");
		}
		if(StringUtils.isNotBlank(endTime)){
			paramsMap.put("endTime", endTime+" 23:59:59");
		}
		paramsMap.put(TableConstants.TENANT_ID, tenantId);
		
		return new BaseResult(ReturnCode.OK,acceptanceNoteService.getProjectAcceptanceCount(paramsMap, ddBB));
	}
	
	@RequestMapping("/acceptanceNote/getTeamAcceptanceCountByProject")
	@ResponseBody
	public BaseResult getTeamAcceptanceCountByProject(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId,
			@RequestAttribute String ddBB,@EncryptProcess ParamsVo params) {
		String projectPeriodId=(String) params.getParams().get(TableConstants.AcceptanceNote.PROJECT_PERIOD_ID.name());
		String beginTime = (String) params.getParams().get("BEGIN_TIME");
		String endTime = (String) params.getParams().get("END_TIME");
		List<Map<String, Object>> userList = (List<Map<String, Object>>) params.getParams()
				.get(TableConstants.USER);
		if(StringUtils.isBlank(projectPeriodId)||userList==null||userList.size()==0){
			return new BaseResult(ReturnCode.REQUEST_PARAMS_MISSING_ERROR);
		}
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(TableConstants.AcceptanceNote.PROJECT_PERIOD_ID.name(), projectPeriodId);
		paramsMap.put("list", userList);
		if(StringUtils.isNotBlank(beginTime)){
			paramsMap.put("beginTime", beginTime+" 00:00:00");
		}
		if(StringUtils.isNotBlank(endTime)){
			paramsMap.put("endTime", endTime+" 23:59:59");
		}
		
		return new BaseResult(ReturnCode.OK,acceptanceNoteService.getTeamAcceptanceCountByProject(paramsMap, ddBB));
	}
	
	@RequestMapping("/acceptanceNote/getTeamInspectorList")
	@ResponseBody
	public BaseResult getTeamInspectorList(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId,
			@RequestAttribute String ddBB,@EncryptProcess ParamsVo params) {
		String projectPeriodId=(String) params.getParams().get(TableConstants.AcceptanceNote.PROJECT_PERIOD_ID.name());
		if(StringUtils.isBlank(projectPeriodId)){
			return new BaseResult(ReturnCode.REQUEST_PARAMS_MISSING_ERROR);
		}
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(TableConstants.AcceptanceNote.PROJECT_PERIOD_ID.name(), projectPeriodId);
		return new BaseResult(ReturnCode.OK,acceptanceNoteService.getTeamInspectorList(paramsMap, ddBB));
	}
	
	@RequestMapping("/common/getWeather")
	@ResponseBody
	public BaseResult getWeather(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId,
			@RequestAttribute String ddBB,@EncryptProcess ParamsVo params) {
		
		String projectPeriodId=(String) params.getParams().get(TableConstants.AcceptanceNote.PROJECT_PERIOD_ID.name());
		String beginTime=DateUtil.getMonthmorning()+" 00:00:00";
		
		Integer countTimes=0;
		
		Map<String, Object> user=userService.getByID(userId, ddBB);
		String postId=(String) user.get(TableConstants.User.postId.name());
		if(DataConstants.INSPECTOR_ROLE_BZ.equals(postId)){
			Map<String, Object> paramsMap = new LinkedHashMap<>();
			paramsMap.put(TableConstants.AcceptanceNote.PROJECT_PERIOD_ID.name(), projectPeriodId);
			paramsMap.put(TableConstants.AcceptanceNote.TEAM_INSPECTOR_ID.name(), userId);
			paramsMap.put("beginTime", beginTime);
			countTimes=acceptanceNoteService.getAcceptanceNoteByMonth(paramsMap, ddBB).size();
		}else{
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put(TableConstants.AcceptanceNote.PROJECT_PERIOD_ID.name(), projectPeriodId);
			paramsMap.put(TableConstants.AcceptanceBatch.ACCEPTANCE_PERSON_ID.name(), userId);
			paramsMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER);
			paramsMap.put("beginTime", beginTime);
			countTimes=acceptanceNoteService.getUserAcceptanceCountByMonth(paramsMap, ddBB).size();
		}
		
		String cityName = (String) params.getParams().get("CITY_NAME");
		JSONObject result = WeatherUtil.getWeather(cityName);
		result.put("countTimes", countTimes);
		
		return new BaseResult(ReturnCode.OK,result);
	}
	
	@RequestMapping("/acceptanceNote/getUserAcceptanceCountByMonth")
	@ResponseBody
	public BaseResult getUserAcceptanceCountByMonth(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId,
			@RequestAttribute String ddBB,@EncryptProcess ParamsVo params) {
		String projectPeriodId=(String) params.getParams().get(TableConstants.AcceptanceNote.PROJECT_PERIOD_ID.name());
		String beginTime=DateUtil.getMonthmorning()+" 00:00:00";
		if(StringUtils.isBlank(projectPeriodId)){
			return new BaseResult(ReturnCode.REQUEST_PARAMS_MISSING_ERROR);
		}
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(TableConstants.AcceptanceNote.PROJECT_PERIOD_ID.name(), projectPeriodId);
		paramsMap.put(TableConstants.AcceptanceBatch.ACCEPTANCE_PERSON_ID.name(), userId);
		paramsMap.put(TableConstants.AcceptanceBatch.BATCH_STATUS.name(), DataConstants.ACCEPTANCE_BATCH_STATUS_MASTER);
		paramsMap.put("beginTime", beginTime);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("countTimes", acceptanceNoteService.getUserAcceptanceCountByMonth(paramsMap, ddBB).size());
		return new BaseResult(ReturnCode.OK,result);
	}
	
	@RequestMapping("/acceptanceNote/getUserAcceptanceStatisticsByPost")
	@ResponseBody
	public BaseResult getUserAcceptanceStatisticsByPost(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		List<Map<String, Object>> userList = (List<Map<String, Object>>) params.getParams()
				.get(TableConstants.USER);
		String projectPeriodId=(String) params.getParams().get(TableConstants.AcceptanceNote.PROJECT_PERIOD_ID.name());
		String inspectorRole=(String) params.getParams().get("INSPECTOR_ROLE");
		String checkRole=(String) params.getParams().get("CHECK_ROLE");
		
		String beginTime = (String) params.getParams().get("BEGIN_TIME");
		String endTime = (String) params.getParams().get("END_TIME");

		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("list", userList);
		paramsMap.put("inspectorRole", inspectorRole);
		paramsMap.put("checkRole", checkRole);
		paramsMap.put("projectPeriodId", projectPeriodId);
		if(StringUtils.isNotBlank(beginTime)){
			paramsMap.put("beginTime", beginTime+" 00:00:00");
		}
		if(StringUtils.isNotBlank(endTime)){
			paramsMap.put("endTime", endTime+" 23:59:59");
		}

		return new BaseResult(ReturnCode.OK,acceptanceNoteService.getUserAcceptanceStatisticsByPost(paramsMap, ddBB));
	}
	
	@RequestMapping("/acceptanceNote/getPostUserList")
	@ResponseBody
	public BaseResult getPostUserList(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId,
			@RequestAttribute String ddBB,@EncryptProcess ParamsVo params) {
		String projectPeriodId=(String) params.getParams().get(TableConstants.AcceptanceNote.PROJECT_PERIOD_ID.name());
		String inspectorRole=(String) params.getParams().get(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name());
		if(StringUtils.isBlank(inspectorRole)||StringUtils.isBlank(projectPeriodId)){
			return new BaseResult(ReturnCode.REQUEST_PARAMS_MISSING_ERROR);
		}
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(TableConstants.AcceptanceNote.PROJECT_PERIOD_ID.name(), projectPeriodId);
		paramsMap.put(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name(), inspectorRole);
		return new BaseResult(ReturnCode.OK,userService.getPostUserListByProject(paramsMap, ddBB));
	}
	
	@RequestMapping("/acceptanceNote/getCompanyAcceptanceStatisticsRanking")
	@ResponseBody
	public BaseResult getCompanyAcceptanceStatisticsRanking(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		String type = (String) params.getParams().get("TYPE");
		
		String beginTime = DateUtil.getMonthFirstDate();
		String endTime = DateUtil.getMonthLastDate();
		
		if("quarter".equals(type)){
			beginTime = DateUtil.getQuarterFirstDate();
			endTime = DateUtil.getQuarterLastDate();
		}else if("year".equals(type)){
			beginTime = DateUtil.getYearFirstDate();
			endTime = DateUtil.getYearLastDate();
		}
		
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put(TableConstants.TENANT_ID, tenantId);
		paramsMap.put("inspectorRole", DataConstants.INSPECTOR_ROLE_YF);
		paramsMap.put("beginTime", beginTime+" 00:00:00");
		paramsMap.put("endTime", endTime+" 23:59:59");

		return new BaseResult(ReturnCode.OK,acceptanceNoteService.getCompanyAcceptanceStatisticsRanking(paramsMap, ddBB));
	}
	
	@RequestMapping("/acceptanceNote/getCompanyAcceptanceStatistics")
	@ResponseBody
	public BaseResult getCompanyAcceptanceStatistics(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		List<Map<String, Object>> companyList = (List<Map<String, Object>>) params.getParams()
				.get(TableConstants.COMPANY);
		String beginTime = (String) params.getParams().get("BEGIN_TIME");
		String endTime = (String) params.getParams().get("END_TIME");

		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("list", companyList);
		paramsMap.put("inspectorRole", DataConstants.INSPECTOR_ROLE_YF);
		if(StringUtils.isNotBlank(beginTime)){
			paramsMap.put("beginTime", beginTime+" 00:00:00");
		}
		if(StringUtils.isNotBlank(endTime)){
			paramsMap.put("endTime", endTime+" 23:59:59");
		}
		paramsMap.put(TableConstants.TENANT_ID, tenantId);

		return new BaseResult(ReturnCode.OK,acceptanceNoteService.getCompanyAcceptanceStatisticsRanking(paramsMap, ddBB));
	}
	
	@RequestMapping("/acceptanceNote/getCompanyList")
	@ResponseBody
	public BaseResult getCompanyList(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId,
			@RequestAttribute String ddBB) {
		return new BaseResult(ReturnCode.OK,companyService.getCompanyListByAcceptance(tenantId, ddBB));
	}
	
	@RequestMapping("/acceptanceNote/getTeamAcceptanceStatisticsRanking")
	@ResponseBody
	public BaseResult getTeamAcceptanceStatisticsRanking(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		String type = (String) params.getParams().get("TYPE");
		String projectPeriodId = (String) params.getParams().get(TableConstants.ProjectPartnerRelate.PROJECT_PERIOD_ID.name());
		
		String beginTime = DateUtil.getMonthFirstDate();
		String endTime = DateUtil.getMonthLastDate();
		
		if("quarter".equals(type)){
			beginTime = DateUtil.getQuarterFirstDate();
			endTime = DateUtil.getQuarterLastDate();
		}else if("year".equals(type)){
			beginTime = DateUtil.getYearFirstDate();
			endTime = DateUtil.getYearLastDate();
		}
		
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("projectPeriodId", projectPeriodId);
		paramsMap.put("inspectorRole", DataConstants.INSPECTOR_ROLE_ZJ);
		paramsMap.put("beginTime", beginTime+" 00:00:00");
		paramsMap.put("endTime", endTime+" 23:59:59");

		return new BaseResult(ReturnCode.OK,acceptanceNoteService.getTeamAcceptanceStatisticsRanking(paramsMap, ddBB));
	}
	
	@RequestMapping("/acceptanceNote/getTeamAcceptanceStatistics")
	@ResponseBody
	public BaseResult getTeamAcceptanceStatistics(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		List<Map<String, Object>> teamList = (List<Map<String, Object>>) params.getParams()
				.get(TableConstants.TEAM_INFO);
		String projectPeriodId = (String) params.getParams().get(TableConstants.ProjectPartnerRelate.PROJECT_PERIOD_ID.name());
		String beginTime = (String) params.getParams().get("BEGIN_TIME");
		String endTime = (String) params.getParams().get("END_TIME");

		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("list", teamList);
		paramsMap.put("inspectorRole", DataConstants.INSPECTOR_ROLE_ZJ);
		if(StringUtils.isNotBlank(beginTime)){
			paramsMap.put("beginTime", beginTime+" 00:00:00");
		}
		if(StringUtils.isNotBlank(endTime)){
			paramsMap.put("endTime", endTime+" 23:59:59");
		}
		paramsMap.put("projectPeriodId", projectPeriodId);

		return new BaseResult(ReturnCode.OK,acceptanceNoteService.getTeamAcceptanceStatisticsRanking(paramsMap, ddBB));
	}
	
	@RequestMapping("/acceptanceNote/getTeamListByProject")
	@ResponseBody
	public BaseResult getTeamListByProject(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		String projectPeriodId = (String) params.getParams().get(TableConstants.ProjectPartnerRelate.PROJECT_PERIOD_ID.name());

		return new BaseResult(ReturnCode.OK,projectPartnerRelateService.getTeamListByProject(projectPeriodId, ddBB));
	}
	
	@RequestMapping("/acceptanceNote/getPostInfoList")
	@ResponseBody
	public BaseResult getPostInfoList(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId,
			@RequestAttribute String ddBB) {
		
		Map<String, Object> params=new HashMap<>();
		params.put(TableConstants.IS_SEALED, 0);
		return new BaseResult(ReturnCode.OK,postInfoService.getPublishPostInfoList(params, ddBB));
	}
	
	@RequestMapping("/acceptanceNote/getProjectAcceptanceStatisticsRanking")
	@ResponseBody
	public BaseResult getProjectAcceptanceStatisticsRanking(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		List<Map<String, Object>> inspectorRoleList = (List<Map<String, Object>>) params.getParams().get(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name());
		
		String type = (String) params.getParams().get("TYPE");
		
		String beginTime = DateUtil.getMonthFirstDate();
		String endTime = DateUtil.getMonthLastDate();
		
		if("quarter".equals(type)){
			beginTime = DateUtil.getQuarterFirstDate();
			endTime = DateUtil.getQuarterLastDate();
		}else if("year".equals(type)){
			beginTime = DateUtil.getYearFirstDate();
			endTime = DateUtil.getYearLastDate();
		}
		
		
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("tenantId", tenantId);
		paramsMap.put("inspectorRoleList", inspectorRoleList);
		paramsMap.put("beginTime", beginTime+" 00:00:00");
		paramsMap.put("endTime", endTime+" 23:59:59");

		return new BaseResult(ReturnCode.OK,acceptanceNoteService.getProjectAcceptanceStatisticsRanking(paramsMap, ddBB));
	}

	@RequestMapping("/acceptanceNote/getProjectAcceptanceStatistics")
	@ResponseBody
	public BaseResult getProjectAcceptanceStatistics(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		List<Map<String, Object>> projectList = (List<Map<String, Object>>) params.getParams()
				.get(TableConstants.PROJECT_PERIOD);
		List<Map<String, Object>> inspectorRoleList = (List<Map<String, Object>>) params.getParams().get(TableConstants.AcceptanceBatch.INSPECTOR_ROLE.name());
		String beginTime = (String) params.getParams().get("BEGIN_TIME");
		String endTime = (String) params.getParams().get("END_TIME");

		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("list", projectList);
		paramsMap.put("inspectorRoleList", inspectorRoleList);
		paramsMap.put("beginTime", beginTime+" 00:00:00");
		paramsMap.put("endTime", endTime+" 23:59:59");
		paramsMap.put("tenantId", tenantId);	

		return new BaseResult(ReturnCode.OK,acceptanceNoteService.getProjectAcceptanceStatisticsRanking(paramsMap, ddBB));
	}

	@RequestMapping("/user/getInspectorList")
	@ResponseBody
	public BaseResult getInspectorList(@RequestHeader(Constants.TENANT_ID) String tenantId,
			@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		String procedureId = (String) params.getParams().get(TableConstants.AcceptanceNote.PROCEDURE_ID.name());
		String projectPeriodId = (String) params.getParams()
				.get(TableConstants.AcceptanceNote.PROJECT_PERIOD_ID.name());
		String regionId = (String) params.getParams().get(TableConstants.AcceptanceNote.REGION_ID.name());
		String regionType = (String) params.getParams().get(TableConstants.AcceptanceNote.REGION_TYPE.name());
		if (StringUtils.isBlank(projectPeriodId) || StringUtils.isBlank(projectPeriodId)
				|| StringUtils.isBlank(regionId)|| StringUtils.isBlank(regionType)) {
			return new BaseResult(ReturnCode.REQUEST_PARAMS_MISSING_ERROR);
		}
		Map<String, Object> map = new LinkedHashMap<>();
		map.put(TableConstants.User.tenantId.name(), tenantId);
		map.put(TableConstants.User.postId.name(), DataConstants.INSPECTOR_ROLE_ZJ);
		map.put(TableConstants.AcceptanceNote.procedureId.name(), procedureId);
		map.put(TableConstants.AcceptanceNote.projectPeriodId.name(), projectPeriodId);
		map.put(TableConstants.AcceptanceNote.regionId.name(), regionId);
		map.put(TableConstants.AcceptanceNote.regionType.name(), regionType);
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
	public BaseResult getSupervisorList(@RequestHeader(Constants.TENANT_ID) String tenantId,
			@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
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
		String regionId = (String) acceptanceNote.get(TableConstants.AcceptanceNote.regionId.name());
		String regionType = (String) acceptanceNote.get(TableConstants.AcceptanceNote.regionType.name());
		
		Map<String, Object> map = new LinkedHashMap<>();
		map.put(TableConstants.User.tenantId.name(), tenantId);
		map.put(TableConstants.User.postId.name(), DataConstants.INSPECTOR_ROLE_JL);
		map.put(TableConstants.AcceptanceNote.procedureId.name(), procedureId);
		map.put(TableConstants.AcceptanceNote.projectPeriodId.name(), projectPeriodId);
		map.put(TableConstants.AcceptanceNote.regionId.name(), regionId);
		map.put(TableConstants.AcceptanceNote.regionType.name(), regionType);
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
