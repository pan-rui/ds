package com.pc.controller.project;


import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.DataConstants;
import com.pc.core.Page;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.service.project.impl.ProjectBuildingService;
import com.pc.service.project.impl.ProjectHouseholdService;
import com.pc.service.project.impl.ProjectPeriodService;
import com.pc.service.project.impl.ProjectRegionTypeService;
import com.pc.util.DateUtil;
import com.pc.vo.ParamsVo;
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
import java.util.UUID;

/**
 * @Description: 
 * @Author: wady (2017-03-28 20:19)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-28
 */
@Controller
@RequestMapping("/admin")
public class ProjectHouseholdController extends BaseController {
	@Autowired
	private ProjectHouseholdService projectHouseholdService;

	@Autowired
	private ProjectBuildingService projectBuildingService;
	
	@Autowired
	private ProjectPeriodService projectPeriodService;

	@Autowired
	private ProjectRegionTypeService projectRegionTypeService;

	private Logger logger = LogManager.getLogger(this.getClass());

	public final static String ROOM_IDS_UPPER = "ROOM_IDS";

	public final static String ROOM_IDS_LOWER = "roomIds";
	
	
	@RequestMapping("/projectHousehold/getProjectRegionTree")
	@ResponseBody
	public BaseResult getProjectRegionTree(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new HashMap<>();
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put("floorTypeId", projectRegionTypeService
				.getProjectRegionType( ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
				DataConstants.REGION_FLOOR_TYPE), ddBB).get(TableConstants.ProjectRegionType.id.name()));
		map.put("roomTypeId", projectRegionTypeService
				.getProjectRegionType( ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
				DataConstants.REGION_ROOM_TYPE), ddBB).get(TableConstants.ProjectRegionType.id.name()));
		return new BaseResult(ReturnCode.OK, projectHouseholdService.getProjectRegionTree(map, ddBB));
	}
	
	@RequestMapping("/projectHousehold/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		int floor = 0;
		int roomSqNo = 0;
		Float totalArea = null;
		Float avariableArea = null;
		String floorId = null;
		String floorName = null;
		String floorIdTree = null;
		String floorNameTree = null;
		String regionTypeId = null;
		String regionTypeName = null;
		String roomName = null;
		String buildingId = null;
		String buildingName = null;
		String buildingIdTree = null;
		String buildingNameTree = null;
		String projectPeriodName = null;
		String projectPeriodId = null;
		Map<String, Object> mapBuilding = null;
		Map<String, Object> mapFloor = null;
		Map<String, Object> household = null;
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		map.put(TableConstants.IS_SEALED, 0);

		if(map.containsKey(TableConstants.ProjectHousehold.ROOM_NAME.name())
				&& map.get(TableConstants.ProjectHousehold.ROOM_NAME.name()) != null)
		{
			roomName = (String)map.get(TableConstants.ProjectHousehold.ROOM_NAME.name());
		}
		else
		{
			return new BaseResult(ReturnCode.HA_ROOM_NAME_MISSING_ERROR);
		}
		if(map.containsKey(TableConstants.ProjectHousehold.FLOOR.name())
				&& map.get(TableConstants.ProjectHousehold.FLOOR.name()) != null)
		{
			floor = Integer.valueOf((String)map.get(TableConstants.ProjectHousehold.FLOOR.name()));
		}
		else
		{
			return new BaseResult(ReturnCode.HA_FLOOR_MISSING_ERROR);
		}

		if(map.containsKey(TableConstants.ProjectHousehold.REGION_TYPE_ID.name())
				&& map.get(TableConstants.ProjectHousehold.REGION_TYPE_ID.name()) != null) {
			regionTypeId = map.get(TableConstants.ProjectHousehold.REGION_TYPE_ID.name())+"";

			Map<String,Object> mapRegionType =  projectRegionTypeService.getByID(regionTypeId, ddBB);
			if(mapRegionType != null
					&& mapRegionType.containsKey(TableConstants.ProjectRegionType.regionTypeName.name())
					&& mapRegionType.get(TableConstants.ProjectRegionType.regionTypeName.name()) != null)
			{
				regionTypeName = (String)mapRegionType.get(TableConstants.ProjectRegionType.regionTypeName.name());
			}
		}
		else
		{
			if(map.containsKey(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name())
					&& map.get(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name()) != null) {
				regionTypeName = (String) map.get(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name());

				regionTypeId = projectRegionTypeService.getProjectRegionTypeIdByName(regionTypeName, ddBB);
			}
		}

		if(regionTypeId == null)
		{
			return new BaseResult(ReturnCode.HA_REGION_MISSING_ERROR);
		}

		if(map.containsKey(TableConstants.ProjectHousehold.CONSTRACTION_AREA.name())
				&& map.get(TableConstants.ProjectHousehold.CONSTRACTION_AREA.name()) != null)
		{
			totalArea = Float.valueOf(String.valueOf(map.get(TableConstants.ProjectHousehold.CONSTRACTION_AREA.name())));
		}
		if(map.containsKey(TableConstants.ProjectHousehold.USABLE_AREA.name())
				&& map.get(TableConstants.ProjectHousehold.USABLE_AREA.name()) != null)
		{
			avariableArea = Float.valueOf(String.valueOf(map.get(TableConstants.ProjectHousehold.USABLE_AREA.name())));
		}

		if(map.containsKey(TableConstants.ProjectHousehold.ROOM.name())
				&& map.get(TableConstants.ProjectHousehold.ROOM.name()) != null)
		{
			roomSqNo = Integer.valueOf(String.valueOf(map.get(TableConstants.ProjectHousehold.ROOM.name())));
		}
		else
		{
			return new BaseResult(ReturnCode.HA_ROOM_MISSING_ERROR);
		}
		// 栋期信息
		if(map.containsKey(TableConstants.ProjectHousehold.BUILDING_ID.name())
				&& map.get(TableConstants.ProjectHousehold.BUILDING_ID.name()) != null)
		{
			buildingId = (String)map.get(TableConstants.ProjectHousehold.BUILDING_ID.name());

			mapBuilding = projectBuildingService.getByID(buildingId, ddBB);

			if(mapBuilding != null)
			{
				if(mapBuilding.containsKey(TableConstants.ProjectBuilding.nameTree.name())
						&& mapBuilding.get(TableConstants.ProjectBuilding.nameTree.name()) != null)
				{
					buildingNameTree = (String)mapBuilding.get(TableConstants.ProjectBuilding.nameTree.name());
				}

				if(mapBuilding.containsKey(TableConstants.ProjectBuilding.idTree.name())
						&& mapBuilding.get(TableConstants.ProjectBuilding.idTree.name()) != null)
				{
					buildingIdTree = (String)mapBuilding.get(TableConstants.ProjectBuilding.idTree.name());
				}

				if(mapBuilding.containsKey(TableConstants.ProjectBuilding.buildingName.name())
						&& mapBuilding.get(TableConstants.ProjectBuilding.buildingName.name()) != null)
				{
					buildingName = (String)mapBuilding.get(TableConstants.ProjectBuilding.buildingName.name());
				}

				if(mapBuilding.containsKey(TableConstants.ProjectBuilding.projectPeriodId.name())
						&& mapBuilding.get(TableConstants.ProjectBuilding.projectPeriodId.name()) != null)
				{
					projectPeriodId = (String)mapBuilding.get(TableConstants.ProjectBuilding.projectPeriodId.name());
					projectPeriodName=(String) projectPeriodService.getByID(projectPeriodId, ddBB).get(TableConstants.ProjectPeriod.periodName.name());
				}
			}
		}
		else
		{
			return new BaseResult(ReturnCode.BA_BUILDING_ID_MISSING_ERROR);
		}



		if(regionTypeName != null && regionTypeName.compareTo(DataConstants.REGION_FLOOR_TYPE) == 0) {
			household = getFloor(roomName, regionTypeId, buildingId, tenantId, userId,
					buildingNameTree, null, projectPeriodName, buildingName,
					buildingIdTree, projectPeriodId, floor);
		}
		else if(regionTypeName != null && regionTypeName.compareTo(DataConstants.REGION_ROOM_TYPE) == 0) {
			// 层信息
			if(map.containsKey(TableConstants.ProjectHousehold.FLOOR_ID.name())
					&& map.get(TableConstants.ProjectHousehold.FLOOR_ID.name()) != null)
			{
				floorId = (String)map.get(TableConstants.ProjectHousehold.FLOOR_ID.name());

				mapFloor = projectHouseholdService.getByID(floorId, ddBB);

				if(mapFloor != null)
				{
					if(mapFloor.containsKey(TableConstants.ProjectHousehold.nameTree.name())
							&& mapFloor.get(TableConstants.ProjectHousehold.nameTree.name()) != null)
					{
						floorNameTree = (String)mapFloor.get(TableConstants.ProjectHousehold.nameTree.name());
					}

					if(mapFloor.containsKey(TableConstants.ProjectHousehold.idTree.name())
							&& mapFloor.get(TableConstants.ProjectHousehold.idTree.name()) != null)
					{
						floorIdTree = (String)mapFloor.get(TableConstants.ProjectHousehold.idTree.name());
					}

					if(mapFloor.containsKey(TableConstants.ProjectHousehold.roomName.name())
							&& mapFloor.get(TableConstants.ProjectHousehold.roomName.name()) != null)
					{
						floorName = (String)mapFloor.get(TableConstants.ProjectHousehold.roomName.name());
					}
				}
			}
			else
			{
				return new BaseResult(ReturnCode.BA_FLOOR_ID_MISSING_ERROR);
			}

			household = getRoom(regionTypeId,
					roomName, roomSqNo, totalArea,
					avariableArea, buildingId, floor, tenantId, userId,floorNameTree,
					projectPeriodName, buildingName, floorName,
					floorIdTree, projectPeriodId,  floorId);
		}

		projectHouseholdService.addProjectHousehold(household, ddBB);

		return new BaseResult(ReturnCode.OK);
	}

	public static Map<String, Object> getRoom(String roomRegionTypeId,
											  String roomName, Integer roomSqNo, Float totalArea,
											  Float avariableArea, String buildingId, int idxFloor,
											  String tenantId, String userId, String floorNameTree,
											  String projectPeriodName, String buildingName, String floorName,
											  String floorIdTree, String projectPeriodId, String floorId)
	{
		Map<String, Object> room = new HashMap<>();

		String roomId = UUID.randomUUID().toString().replace("-", "");

		room.put(TableConstants.ProjectHousehold.ID.name(), roomId);
		room.put(TableConstants.ProjectHousehold.REGION_TYPE_ID.name(), roomRegionTypeId);
		room.put(TableConstants.ProjectHousehold.ROOM_NAME.name(), roomName);
		room.put(TableConstants.ProjectHousehold.ROOM_FULL_NAME.name(), String.format("%s%s%s%s", projectPeriodName, buildingName, floorName, roomName));
		room.put(TableConstants.ProjectHousehold.FLOOR_ID.name(), floorId);

		if(roomSqNo != null) {
			room.put(TableConstants.ProjectHousehold.ROOM.name(), roomSqNo);
		}
		if(totalArea != null && totalArea > 0)
		{
			room.put(TableConstants.ProjectHousehold.CONSTRACTION_AREA.name(), totalArea);
		}
		if(avariableArea != null && avariableArea > 0)
		{
			room.put(TableConstants.ProjectHousehold.USABLE_AREA.name(), avariableArea);
		}
		room.put(TableConstants.ProjectHousehold.BUILDING_ID.name(), buildingId);
		room.put(TableConstants.ProjectHousehold.FLOOR.name(), idxFloor);
		room.put(TableConstants.TENANT_ID, tenantId);
		room.put(TableConstants.IS_SEALED, 0);
		room.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		room.put(TableConstants.UPDATE_USER_ID, userId);

		String roomNameTree = "";
		String roomIdTree = "";
		if(floorNameTree != null)
		{
			roomNameTree = String.format("%s%s%s", floorNameTree, TableConstants.SEPARATE_TREE, roomName);
		}
		else
		{
			roomNameTree = String.format("%s%s%s%s%s%s%s", projectPeriodName, TableConstants.SEPARATE_TREE, buildingName, TableConstants.SEPARATE_TREE, floorName,  TableConstants.SEPARATE_TREE, roomName);
		}
		if(floorIdTree != null) {
			roomIdTree = String.format("%s%s%s", floorIdTree, TableConstants.SEPARATE_TREE, roomId);
		}
		else
		{
			roomIdTree = String.format("%s%s%s%s%s%s%s", projectPeriodId, TableConstants.SEPARATE_TREE, buildingId, TableConstants.SEPARATE_TREE, floorId, TableConstants.SEPARATE_TREE, roomId);
		}

		room.put(TableConstants.ProjectHousehold.ID_TREE.name(), roomIdTree);
		room.put(TableConstants.ProjectHousehold.NAME_TREE.name(), roomNameTree);

		return room;
	}

	public static Map<String, Object> getFloor(String floorName, String floorRegionTypeId, String buildingId,  String tenantId, String userId,
											   String buildingNameTree, String floorTypeId, String projectPeriodName, String buildingName,
											   String buildingIdTree, String projectPeriodId, Integer idxFloor)
	{
		String floorId = null;
		String floorNameTree = null;
		String floorIdTree = null;
		Map<String, Object> floor = new HashMap<>();

		floorId = UUID.randomUUID().toString().replace("-", "");

		floor.put(TableConstants.ProjectHousehold.ID.name(), floorId);

		floor.put(TableConstants.ProjectHousehold.REGION_TYPE_ID.name(), floorRegionTypeId);
		floor.put(TableConstants.ProjectHousehold.ROOM_NAME.name(), floorName);
		floor.put(TableConstants.ProjectHousehold.ROOM_FULL_NAME.name(), String.format("%s%s%s", projectPeriodName, buildingName, floorName));
		floor.put(TableConstants.ProjectHousehold.BUILDING_ID.name(), buildingId);
		floor.put(TableConstants.ProjectHousehold.FLOOR_ID.name(), floorId);
		if(idxFloor != null) {
			floor.put(TableConstants.ProjectHousehold.FLOOR.name(), idxFloor);
		}
		// TODO:
		if(floorTypeId != null)
		{
			floor.put(TableConstants.ProjectHousehold.FLOOR_TYPE_ID.name(), floorTypeId);
		}
		floor.put(TableConstants.TENANT_ID, tenantId);
		floor.put(TableConstants.IS_SEALED, 0);
		floor.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		floor.put(TableConstants.UPDATE_USER_ID, userId);

		floorNameTree = "";
		floorIdTree = "";
		if(buildingNameTree != null)
		{
			floorNameTree = String.format("%s%s%s", buildingNameTree, TableConstants.SEPARATE_TREE, floorName);
		}
		else
		{
			floorNameTree = String.format("%s%s%s%s%s", projectPeriodName, TableConstants.SEPARATE_TREE, buildingName, TableConstants.SEPARATE_TREE, floorName);
		}
		if(buildingIdTree != null) {
			floorIdTree = String.format("%s%s%s", buildingIdTree, TableConstants.SEPARATE_TREE, floorId);
		}
		else
		{
			floorIdTree = String.format("%s%s%s%s%s", projectPeriodId, TableConstants.SEPARATE_TREE, buildingId, TableConstants.SEPARATE_TREE, floorId);
		}

		floor.put(TableConstants.ProjectHousehold.ID_TREE.name(), floorIdTree);
		floor.put(TableConstants.ProjectHousehold.NAME_TREE.name(), floorNameTree);

		return floor;
	}

	@RequestMapping("/projectHousehold/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = projectHouseholdService.deleteProjectHousehold(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/projectHousehold/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = projectHouseholdService.updateProjectHousehold(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/projectHousehold/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, projectHouseholdService.getProjectHousehold(map, ddBB));
	}

	@RequestMapping("/projectHousehold/getList")
	@ResponseBody
	public BaseResult getList(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		return new BaseResult(ReturnCode.OK, projectHouseholdService.getProjectHouseholdList(map, ddBB));
	}

	@RequestMapping("/projectHousehold/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, projectHouseholdService.getProjectHouseholdPage(page, ddBB));
	}

	@RequestMapping("/projectHousehold/getHouseholdList")
	@ResponseBody
	public BaseResult getHouseholdList(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		return new BaseResult(ReturnCode.OK, projectHouseholdService.getHouseholdsByRegion(map, ddBB));
	}

	@RequestMapping("/projectHousehold/getHouseholdPage")
	@ResponseBody
	public BaseResult getHouseholdPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
							  @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, projectHouseholdService.getHouseholdsPageByRegion(page, ddBB));
	}

	@RequestMapping("/projectHousehold/updateBatch")
	@ResponseBody
	public BaseResult updateBatch(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
									   @RequestAttribute String ddBB) {
		String roomIds = null;
		String[] rooms = null;
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
/*		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);*/

		if(map.containsKey(ROOM_IDS_UPPER))
		{
			roomIds = (String)map.get(ROOM_IDS_UPPER);

			if(roomIds != null)
			{
				rooms = roomIds.split(",");
				List list = java.util.Arrays.asList(rooms);

				map.put(ROOM_IDS_UPPER, list);
			}
		}

		return new BaseResult(ReturnCode.OK, projectHouseholdService.updateHouseholdBatch(map, ddBB));
	}

}
