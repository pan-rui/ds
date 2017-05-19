package com.pc.controller.acceptance;


import java.util.ArrayList;
import java.util.HashMap;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.DataConstants;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.service.acceptance.impl.ProcedureScheduleRelateService;
import com.pc.service.procedure.impl.ProcedureTypeService;
import com.pc.service.project.impl.ProjectBuildingService;
import com.pc.service.project.impl.ProjectHouseholdService;
import com.pc.service.project.impl.ProjectRegionTypeService;
import com.pc.util.TreeUtil;
import com.pc.vo.ParamsVo;


@Controller
@RequestMapping("/client")
public class ProcedureScheduleRelateController extends BaseController {
	@Autowired
	private ProcedureScheduleRelateService procedureScheduleRelateService;
	
	@Autowired
	private ProjectBuildingService projectBuildingService;
	
	@Autowired
	private ProjectHouseholdService projectHouseholdService;
	
	@Autowired
	private ProjectRegionTypeService projectRegionTypeService;
	
	@Autowired
	private ProcedureTypeService procedureTypeService;

	private Logger logger = LogManager.getLogger(this.getClass());
	
	@RequestMapping("/procedureScheduleRelate/getProcedureSchedule")
	@ResponseBody
	public BaseResult getProcedureSchedule(@RequestHeader(Constants.TENANT_ID) String tenantId,
			@RequestAttribute String ddBB, @EncryptProcess() ParamsVo params) {
		String buildingId = (String) params.getParams()
				.get(TableConstants.ProjectHousehold.BUILDING_ID.name());
		List<Map<String, Object>> procedureIdList = (List<Map<String, Object>>) params.getParams()
				.get(TableConstants.PROCEDURE_INFO);
		
		if(StringUtils.isBlank(buildingId)||procedureIdList==null||procedureIdList.isEmpty()){
			return new BaseResult(ReturnCode.REQUEST_PARAMS_MISSING_ERROR);
		}
		
		Map floorParams=new HashMap<>();
		floorParams.put(TableConstants.TENANT_ID, tenantId);
		floorParams.put(TableConstants.IS_SEALED, 0);
		floorParams.put(TableConstants.ProjectHousehold.BUILDING_ID.name(), buildingId);
		floorParams.put(TableConstants.ProjectHousehold.REGION_TYPE_ID.name(),projectRegionTypeService
				.getProjectRegionType(ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
						DataConstants.REGION_FLOOR_TYPE), ddBB).get(TableConstants.ProjectRegionType.id.name()));
		Map orderFloorParams=new HashMap<>();
		orderFloorParams.put(TableConstants.ProjectHousehold.FLOOR.name(), TableConstants.ORDER_BY_ASC);
		
		Map relateParams=new HashMap<>();
		relateParams.put(TableConstants.ProjectHousehold.tenantId.name(), tenantId);
		relateParams.put(TableConstants.AcceptanceNote.regionType.name(), DataConstants.REGION_ROOM_TYPE_VAL);
		relateParams.put(TableConstants.ProjectHousehold.regionTypeId.name(),projectRegionTypeService
				.getProjectRegionType(ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
						DataConstants.REGION_ROOM_TYPE), ddBB).get(TableConstants.ProjectRegionType.id.name()));
		relateParams.put(TableConstants.ProjectHousehold.buildingId.name(), buildingId);
		
		JSONArray result=new JSONArray();
		for(Map<String, Object> procedureId:procedureIdList){
			JSONObject obj=new JSONObject();
			
			String pid=(String)procedureId.get(TableConstants.ProcedureInfo.ID.name());
			relateParams.put(TableConstants.AcceptanceNote.procedureId.name(),pid);
			List<Map<String, Object>> roomList=procedureScheduleRelateService.getBuildingProcedureScheduleList(relateParams, ddBB);
			
			List<Map<String, Object>> floorList=projectHouseholdService.getProjectHouseholdList(floorParams, orderFloorParams, ddBB);
			
			for(Map<String, Object> floor:floorList){
				List<Map<String, Object>> rooms=new ArrayList<>();
				for(Map<String, Object> room:roomList){
					if(((String)room.get(TableConstants.ProjectHousehold.floorId.name())).equals((String)floor.get(TableConstants.ProjectHousehold.id.name()))){
						rooms.add(room);
					}
				}
				floor.put(DataConstants.REGION_ROOM_LIST_TYPE_KEY, rooms);
			}
			
			obj.put(TableConstants.AcceptanceNote.procedureId.name(), pid);
			obj.put(DataConstants.REGION_FLOOR_LIST_TYPE_KEY, floorList);
			result.add(obj);
		}
		
		return new BaseResult(ReturnCode.OK,result);
	}
	
	/**
	 * 获取整个项目树进度
	 * @param tenantId
	 * @param ddBB
	 * @param params
	 * @return
	 */
	@RequestMapping("/procedureScheduleRelate/getScheduleTree")
	@ResponseBody
	public BaseResult getScheduleTree(@RequestHeader(Constants.TENANT_ID) String tenantId,
			@RequestAttribute String ddBB, @EncryptProcess() ParamsVo params) {
		String projectPeriodId = (String) params.getParams()
				.get(TableConstants.ProjectPartnerRelate.PROJECT_PERIOD_ID.name());
		
		if(StringUtils.isBlank(projectPeriodId)){
			new BaseResult(ReturnCode.REQUEST_PARAMS_MISSING_ERROR);
		}
		
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
		
		Map<String, Object> tjParams=new HashMap<>();
		tjParams.put(TableConstants.ProjectBuilding.projectPeriodId.name(), projectPeriodId);
		tjParams.put(TableConstants.ProjectHousehold.regionTypeId.name(),(String)projectRegionTypeService
				.getProjectRegionType(ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
						DataConstants.REGION_ROOM_TYPE), ddBB).get(TableConstants.ProjectRegionType.id.name()));
		tjParams.put(TableConstants.ProcedureScheduleRelate.procedureTypeId.name(), (String)procedureTypeService.getProcedureType(ParamsMap
				.newMap(TableConstants.ProcedureType.NAME_TREE.name(),DataConstants.PROCEDURE_TYPE_TJ)
				.addParams(TableConstants.ProcedureType.PROCEDURE_TYPE_NAME.name(),DataConstants.PROCEDURE_TYPE_TJ), ddBB)
				.get(TableConstants.ProcedureType.id.name()));
		List<Map<String, Object>> tjRoomList=procedureScheduleRelateService.getProcedureScheduleRoomidList(tjParams, ddBB);
		
		Map<String, Object> zxParams=new HashMap<>();
		zxParams.put(TableConstants.ProjectBuilding.projectPeriodId.name(), projectPeriodId);
		zxParams.put(TableConstants.ProjectHousehold.regionTypeId.name(),(String)projectRegionTypeService
				.getProjectRegionType(ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
						DataConstants.REGION_ROOM_TYPE), ddBB).get(TableConstants.ProjectRegionType.id.name()));
		zxParams.put(TableConstants.ProcedureScheduleRelate.procedureTypeId.name(), (String)procedureTypeService.getProcedureType(ParamsMap
				.newMap(TableConstants.ProcedureType.NAME_TREE.name(),DataConstants.PROCEDURE_TYPE_ZX)
				.addParams(TableConstants.ProcedureType.PROCEDURE_TYPE_NAME.name(),DataConstants.PROCEDURE_TYPE_ZX), ddBB)
				.get(TableConstants.ProcedureType.id.name()));
		List<Map<String, Object>> zxRoomList=procedureScheduleRelateService.getProcedureScheduleRoomidList(zxParams, ddBB);
		
		for(Map<String, Object> room:roomList){
			if(tjRoomList.contains(room)){
				room.put("tjSchedule", "1");
			}else{
				room.put("tjSchedule", "0");
			}
			if(zxRoomList.contains(room)){
				room.put("zxSchedule", "1");
			}else{
				room.put("zxSchedule", "0");
			}
		}
		
		return new BaseResult(ReturnCode.OK,TreeUtil.getRegionTrees(false, null, null, buildingList, floorList, roomList));
		
	}

}
