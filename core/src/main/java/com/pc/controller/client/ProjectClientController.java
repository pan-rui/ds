package com.pc.controller.client;

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
import com.pc.service.auth.DataPrivilegeTypeService;
import com.pc.service.project.impl.HouseholdChartAreaService;
import com.pc.service.project.impl.ProjectBuildingService;
import com.pc.service.project.impl.ProjectHouseholdService;
import com.pc.service.project.impl.ProjectInfoService;
import com.pc.service.project.impl.ProjectPeriodService;
import com.pc.service.project.impl.ProjectRegionProcedureRelateService;
import com.pc.service.project.impl.ProjectRegionTypeService;
import com.pc.service.project.impl.UnitChartService;
import com.pc.util.TreeUtil;
import com.pc.vo.ParamsVo;

/**
 * @Description: 项目对应工序管理
 * @Author: wady (2017-04-10 11:51)
 * @version: \$Rev: 2094 $
 * @UpdateAuthor: \$Author: zhangj $
 * @UpdateDateTime: \$Date: 2017-05-09 20:22:04 +0800 (周二, 09 5月 2017) $
 */
@Controller
@RequestMapping("/client")
public class ProjectClientController extends BaseController {

	private Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private ProjectRegionProcedureRelateService projectRegionProcedureRelateService;

	@Autowired
	private ProjectInfoService projectInfoService;

	@Autowired
	private ProjectPeriodService projectPeriodService;

	@Autowired
	private ProjectBuildingService projectBuildingService;

	@Autowired
	private ProjectHouseholdService projectHouseholdService;

	@Autowired
	private ProjectRegionTypeService projectRegionTypeService;

	@Autowired
	private DataPrivilegeTypeService dataPrivilegeTypeService;
	
	
	@RequestMapping("/procedureInfo/getProjectDetailList")
	@ResponseBody
	public BaseResult getProjectDetailList(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @RequestAttribute String ddBB) {
		Map<String, Object> dataTypeParams = new LinkedHashMap<>();
		dataTypeParams.put(TableConstants.DataPrivilegeType.TABLE_NAME.name(), TableConstants.PROJECT_HOUSEHOLD);
		String dataTypeId = (String) dataPrivilegeTypeService.getDataPrivilegeType(dataTypeParams, ddBB)
				.get(TableConstants.DataPrivilegeType.id.name());

		Map<String, Object> params = new HashMap<>();
		params.put(TableConstants.TENANT_ID, tenantId);
		params.put(TableConstants.UserRoleRelate.userId.name(), userId);
		params.put(TableConstants.DataPrivilegesInfo.dataTypeId.name(), dataTypeId);
		
		List<Map<String, Object>> projectInfoList=projectInfoService.getProjectInfoListByUser(params, ddBB);
		List<Map<String, Object>> projectPeriodList=projectPeriodService.getProjectPeriodListByUser(params, ddBB);
		
		for(Map<String, Object> project:projectInfoList){
			List<Map<String, Object>> list=new ArrayList<>();
			for(Map<String, Object> period:projectPeriodList){
				if(((String)project.get(TableConstants.ProjectInfo.id.name())).equals((String)period.get(TableConstants.ProjectPeriod.projectId.name()))){
					list.add(period);
				}
			}
			project.put("list", list);
		}
		
		return new BaseResult(ReturnCode.OK, projectInfoList);
	}

	@RequestMapping("/procedureInfo/getProjectPeriodByUser")
	@ResponseBody
	public BaseResult getListByRegionAndUser(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @RequestAttribute String ddBB) {
		Map<String, Object> dataTypeParams = new LinkedHashMap<>();
		dataTypeParams.put(TableConstants.DataPrivilegeType.TABLE_NAME.name(), TableConstants.PROJECT_HOUSEHOLD);
		String dataTypeId = (String) dataPrivilegeTypeService.getDataPrivilegeType(dataTypeParams, ddBB)
				.get(TableConstants.DataPrivilegeType.id.name());

		Map<String, Object> params = new HashMap<>();
		params.put(TableConstants.TENANT_ID, tenantId);
		params.put(TableConstants.UserRoleRelate.userId.name(), userId);
		params.put(TableConstants.DataPrivilegesInfo.dataTypeId.name(), dataTypeId);
		
		return new BaseResult(ReturnCode.OK, projectPeriodService.getProjectPeriodListByUser(params, ddBB));
	}

	@RequestMapping("/projectTree/getAllRegionsTree")
	@ResponseBody
	public BaseResult getAllProjectTree(@RequestHeader(Constants.TENANT_ID) String tenantId,
			@RequestAttribute String ddBB, @EncryptProcess() ParamsVo params) {
		String projectPeriodId = (String) params.getParams()
				.get(TableConstants.ProjectPartnerRelate.PROJECT_PERIOD_ID.name());
		
		boolean isContains=true;
		String isContainsString = (String) params.getParams()
				.get("IS_CONTAINS");
		if(isContainsString!=null&&StringUtils.isNotBlank(isContainsString)){
			isContains=Boolean.valueOf(isContainsString);
		}
		
		if (projectPeriodId != null) {
			List<Map<String, Object>> buildingList = projectBuildingService
					.getProjectBuildingList(ParamsMap.newMap(TableConstants.ProjectBuilding.TENANT_ID.name(), tenantId)
							.addParams(TableConstants.ProjectBuilding.PROJECT_PERIOD_ID.name(), projectPeriodId)
							.addParams(TableConstants.ProjectBuilding.IS_SEALED.name(), 0), ddBB);
			
			Map<String, Object> floorMap=new HashMap<>();
			floorMap.put(TableConstants.ProjectBuilding.projectPeriodId.name(), projectPeriodId);
			floorMap.put(TableConstants.ProjectHousehold.regionTypeId.name(),projectRegionTypeService
					.getProjectRegionType(ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
							DataConstants.REGION_FLOOR_TYPE), ddBB).get(TableConstants.ProjectRegionType.id.name()));
			List<Map<String, Object>> floorList = projectHouseholdService.getFloorList(floorMap, ddBB);
			
			Map<String, Object> roomMap=new HashMap<>();
			roomMap.put(TableConstants.ProjectBuilding.projectPeriodId.name(), projectPeriodId);
			roomMap.put(TableConstants.ProjectHousehold.regionTypeId.name(),projectRegionTypeService
					.getProjectRegionType(ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
							DataConstants.REGION_ROOM_TYPE), ddBB).get(TableConstants.ProjectRegionType.id.name()));
			List<Map<String, Object>> roomList = projectHouseholdService.getRoomList(roomMap, ddBB);
			return new BaseResult(ReturnCode.OK,
					TreeUtil.getRegionTrees(isContains,null, null, buildingList, floorList, roomList));
		} else {
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put(TableConstants.TENANT_ID, tenantId);
			paramsMap.put(TableConstants.IS_SEALED, 0);

			List<Map<String, Object>> projectList = projectInfoService.getProjectInfoList(paramsMap, ddBB);
			List<Map<String, Object>> projectPeriodList = projectPeriodService.getProjectPeriodList(paramsMap, ddBB);
			List<Map<String, Object>> projectBuildingList = projectBuildingService.getProjectBuildingList(paramsMap,
					ddBB);

			Map<String, Object> floorMap = new HashMap<String, Object>(paramsMap);
			floorMap.put(TableConstants.ProjectHousehold.REGION_TYPE_ID.name(),
					projectRegionTypeService.getProjectRegionType(
									ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
											DataConstants.REGION_FLOOR_TYPE),ddBB).get(TableConstants.ProjectRegionType.id.name()));
			List<Map<String, Object>> projectFloorList = projectHouseholdService.getProjectHouseholdList(floorMap,ddBB);

			Map<String, Object> roomMap = new HashMap<String, Object>(paramsMap);
			roomMap.put(TableConstants.ProjectHousehold.REGION_TYPE_ID.name(),
					projectRegionTypeService.getProjectRegionType(ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
											DataConstants.REGION_ROOM_TYPE),ddBB).get(TableConstants.ProjectRegionType.id.name()));
			List<Map<String, Object>> projectRoomList = projectHouseholdService.getProjectHouseholdList(roomMap, ddBB);

			return new BaseResult(ReturnCode.OK, TreeUtil.getRegionTrees(isContains,projectList, projectPeriodList,
					projectBuildingList, projectFloorList, projectRoomList));
		}
	}

	@RequestMapping("/projectTree/getRegionsTreeByUser")
	@ResponseBody
	public BaseResult getRegionsTreeByUser(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @RequestAttribute String ddBB,
			@EncryptProcess() ParamsVo params) {
		String projectPeriodId = (String) params.getParams()
				.get(TableConstants.ProjectPartnerRelate.PROJECT_PERIOD_ID.name());
		
		Map<String, Object> dataTypeParams = new LinkedHashMap<>();
		dataTypeParams.put(TableConstants.DataPrivilegeType.TABLE_NAME.name(), TableConstants.PROJECT_HOUSEHOLD);
		String dataTypeId = (String) dataPrivilegeTypeService.getDataPrivilegeType(dataTypeParams, ddBB)
				.get(TableConstants.DataPrivilegeType.id.name());
		
		boolean isContains=true;
		String isContainsString = (String) params.getParams()
				.get("IS_CONTAINS");
		if(isContainsString!=null&&StringUtils.isNotBlank(isContainsString)){
			isContains=Boolean.valueOf(isContainsString);
		}
		
		Map<String, Object> paramsMap = new LinkedHashMap<>();
		paramsMap.put(TableConstants.TENANT_ID, tenantId);
		paramsMap.put(TableConstants.UserRoleRelate.userId.name(), userId);
		paramsMap.put(TableConstants.DataPrivilegesInfo.dataTypeId.name(), dataTypeId);
		
		if (projectPeriodId != null) {
			paramsMap.put(TableConstants.ProjectBuilding.projectPeriodId.name(), projectPeriodId);
			
			List<Map<String, Object>> buildingList = projectBuildingService.getProjectBuildingListByUser(paramsMap, ddBB);
			
			Map<String, Object> floorParamsMap = new LinkedHashMap<>(paramsMap);
			floorParamsMap.put(TableConstants.ProjectHousehold.regionTypeId.name(), projectRegionTypeService
					.getProjectRegionType(ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
									DataConstants.REGION_FLOOR_TYPE),ddBB)
					.get(TableConstants.ProjectRegionType.id.name()));
			List<Map<String, Object>> floorList = projectHouseholdService.getProjectFloorListByUser(floorParamsMap, ddBB);
			
			Map<String, Object> roomParamsMap = new LinkedHashMap<>(paramsMap);
			roomParamsMap.put(TableConstants.ProjectHousehold.regionTypeId.name(), projectRegionTypeService
					.getProjectRegionType(ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
									DataConstants.REGION_ROOM_TYPE),ddBB)
					.get(TableConstants.ProjectRegionType.id.name()));
			List<Map<String, Object>> roomList = projectHouseholdService.getProjectRoomListByUser(roomParamsMap, ddBB);
			return new BaseResult(ReturnCode.OK,
					TreeUtil.getRegionTrees(isContains,null, null, buildingList, floorList, roomList));
		} else {

			List<Map<String, Object>> projectList = projectInfoService.getProjectInfoListByUser(paramsMap, ddBB);
			List<Map<String, Object>> projectPeriodList = projectPeriodService.getProjectPeriodListByUser(paramsMap, ddBB);
			
			List<Map<String, Object>> buildingList = projectBuildingService.getProjectBuildingListByUser(paramsMap, ddBB);
			
			Map<String, Object> floorParamsMap = new LinkedHashMap<>(paramsMap);
			floorParamsMap.put(TableConstants.ProjectHousehold.regionTypeId.name(), projectRegionTypeService
					.getProjectRegionType(ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
									DataConstants.REGION_FLOOR_TYPE),ddBB)
					.get(TableConstants.ProjectRegionType.id.name()));
			List<Map<String, Object>> floorList = projectHouseholdService.getProjectFloorListByUser(floorParamsMap, ddBB);
			
			Map<String, Object> roomParamsMap = new LinkedHashMap<>(paramsMap);
			roomParamsMap.put(TableConstants.ProjectHousehold.regionTypeId.name(), projectRegionTypeService
					.getProjectRegionType(ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
									DataConstants.REGION_ROOM_TYPE),ddBB)
					.get(TableConstants.ProjectRegionType.id.name()));
			List<Map<String, Object>> roomList = projectHouseholdService.getProjectRoomListByUser(roomParamsMap, ddBB);

			return new BaseResult(ReturnCode.OK, TreeUtil.getRegionTrees(isContains,projectList, projectPeriodList,
					buildingList, floorList, roomList));
		}

	}

}
