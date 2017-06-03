package com.pc.controller.project;


import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.DataConstants;
import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.project.impl.FloorRoomBatchGenNoteService;
import com.pc.service.project.impl.FloorTypeBatchNoteService;
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

import java.util.ArrayList;
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
public class FloorRoomBatchGenNoteController extends BaseController {
	@Autowired
	private FloorRoomBatchGenNoteService floorRoomBatchGenNoteService;

	@Autowired
	private FloorTypeBatchNoteService floorTypeBatchNoteService;

	@Autowired
	private ProjectPeriodService projectPeriodService;

	@Autowired
	private ProjectBuildingService projectBuildingService;

	@Autowired
	private ProjectHouseholdService projectHouseholdService;

	@Autowired
	private ProjectRegionTypeService projectRegionTypeService;

	private Logger logger = LogManager.getLogger(this.getClass());

	public final static String SPEC_FLOORS = "FLOORS";

	public final static String BEGIN_BUILDING_SNO = "BEGIN_BUILDING_SNO";

	public final static String END_BUILDING_SNO = "END_BUILDING_SNO";
	
	@RequestMapping("/floorRoomBatchGenNote/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		int beginBuildingSno = -1;
		int endBuildingSno = -1;
		int floorNum = 0;
		int roomPerFloorNum = 0;
		int roomNumByFloor = 0;
		int roomNameRule = 1;
		int roomSqNo = 0;
		float totalArea = 0.0f;
		float avariableArea = 0.0f;
		String floorRegionTypeId = null;
		String roomRegionTypeId = null;
		String floorTypeId = null;
		String projectPeriodId = null;
		String projectPeriodName = null;
		String buildingNameTree = null;
		String buildingIdTree = null;
		String buildingName = null;
		String buildingId = null;
		String floorId = null;
		String floorName = null;
		String roomId = null;
		String roomName = null;
		String floorIdTree = "";
		String floorNameTree = "";
		String roomIdTree = "";
		String roomNameTree = "";
		String roomSuffixName = "";
		String noteId = null;
		Map<String, Object> projectPeriod = null;
		Map<String, Object> beginBuilding = null;
		Map<String, Object> endBuilding = null;
		List<Map<String, Object>> listSpecFloors = null;
		List<Map<String, Object>> floors = null;
		List<Map<String, Object>> rooms = null;
		List<Map<String, Object>> households = null;
		Map<String, Object> floor = null;
		Map<String, Object> room = null;
		Map<String, Object> building = null;
		Map<String, Object> mapSpecFloors = null;
		Map<String, Object> specFloor = null;
		List<Map<String, Object>> listBuildings = null;
		Map<String, Object> buildingParams = new LinkedHashMap<>();
		Map<String, Object> tempParams = new LinkedHashMap<>();
		int isContainExternalWall = 0;
		int isContainInternalWall = 0;
		int isContainRoof = 0;

		String floorRange = "";
		String[] floorRanges = null;
		String floorNo = null;

		Map<String, Object> map = new LinkedHashMap<>(params.getParams());

		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);

		noteId = UUID.randomUUID().toString().replace("-", "");

		map.put(TableConstants.FloorRoomBatchGenNote.ID.name(), noteId);

		if(map.containsKey(SPEC_FLOORS))
		{
			if(map.get(SPEC_FLOORS) != null) {
				listSpecFloors = (List<Map<String, Object>>) map.get(SPEC_FLOORS);
			}

			map.remove(SPEC_FLOORS);
		}
		if(map.containsKey(TableConstants.FloorRoomBatchGenNote.TOTAL_FLOOR.name())
				&& map.get(TableConstants.FloorRoomBatchGenNote.TOTAL_FLOOR.name()) != null) {
			floorNum = (int)map.get(TableConstants.FloorRoomBatchGenNote.TOTAL_FLOOR.name());
		}
		if(map.containsKey(TableConstants.FloorRoomBatchGenNote.ROOM_PER_FLOOR.name())
				&& map.get(TableConstants.FloorRoomBatchGenNote.ROOM_PER_FLOOR.name()) != null) {
			roomPerFloorNum = (int)map.get(TableConstants.FloorRoomBatchGenNote.ROOM_PER_FLOOR.name());
		}
		if(map.containsKey(TableConstants.FloorRoomBatchGenNote.ROOM_NAME_RULE.name())
				&& map.get(TableConstants.FloorRoomBatchGenNote.ROOM_NAME_RULE.name()) != null) {
			roomNameRule = (int)map.get(TableConstants.FloorRoomBatchGenNote.ROOM_NAME_RULE.name());
		}

		if(map.containsKey(TableConstants.FloorRoomBatchGenNote.ROOM_SUFFIX_NAME.name())
				&& map.get(TableConstants.FloorRoomBatchGenNote.ROOM_SUFFIX_NAME.name()) != null) {
			roomSuffixName = (String)map.get(TableConstants.FloorRoomBatchGenNote.ROOM_SUFFIX_NAME.name());
		}

		if(map.containsKey(TableConstants.FloorRoomBatchGenNote.ROOM_TOTAL_AREA.name())
				&& map.get(TableConstants.FloorRoomBatchGenNote.ROOM_TOTAL_AREA.name()) != null) {
			totalArea = Float.valueOf(String.valueOf(map.get(TableConstants.FloorRoomBatchGenNote.ROOM_TOTAL_AREA.name())));
		}
		if(map.containsKey(TableConstants.FloorRoomBatchGenNote.ROOM_AVARIABLE_AREA.name())
				&& map.get(TableConstants.FloorRoomBatchGenNote.ROOM_AVARIABLE_AREA.name()) != null) {
			avariableArea = Float.valueOf(String.valueOf(map.get(TableConstants.FloorRoomBatchGenNote.ROOM_AVARIABLE_AREA.name())));
		}

		if(map.containsKey(TableConstants.FloorRoomBatchGenNote.IS_CONTAIN_EXTERNAL_WALL.name())
				&& map.get(TableConstants.FloorRoomBatchGenNote.IS_CONTAIN_EXTERNAL_WALL.name()) != null) {
			isContainExternalWall = (int)map.get(TableConstants.FloorRoomBatchGenNote.IS_CONTAIN_EXTERNAL_WALL.name());
		}
		if(map.containsKey(TableConstants.FloorRoomBatchGenNote.IS_CONTAIN_INTERNAL_WALL.name())
				&& map.get(TableConstants.FloorRoomBatchGenNote.IS_CONTAIN_INTERNAL_WALL.name()) != null) {
			isContainInternalWall = (int)map.get(TableConstants.FloorRoomBatchGenNote.IS_CONTAIN_INTERNAL_WALL.name());
		}
		if(map.containsKey(TableConstants.FloorRoomBatchGenNote.IS_CONTAIN_ROOF.name())
				&& map.get(TableConstants.FloorRoomBatchGenNote.IS_CONTAIN_ROOF.name()) != null) {
			isContainRoof = (int)map.get(TableConstants.FloorRoomBatchGenNote.IS_CONTAIN_ROOF.name());
		}

		// 特殊层处理
		if(listSpecFloors != null && listSpecFloors.size() > 0) {
			mapSpecFloors = new HashMap<>();

			for (int index = 0; index < listSpecFloors.size(); index++) {
				specFloor = listSpecFloors.get(index);

				specFloor.put(TableConstants.TENANT_ID, tenantId);
				specFloor.put(TableConstants.IS_SEALED, 0);
				specFloor.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
				specFloor.put(TableConstants.UPDATE_USER_ID, userId);

				specFloor.put(TableConstants.FloorTypeBatchNote.ID.name(), UUID.randomUUID().toString().replace("-", ""));

				specFloor.put(TableConstants.FloorTypeBatchNote.FLOOR_ROOM_BATCH_NOTE_ID.name(), noteId);

				// 获取所有特殊层信息
				if(specFloor != null && specFloor.containsKey(TableConstants.FloorTypeBatchNote.FLOOR_RANGE.name())
						&&  specFloor.get(TableConstants.FloorTypeBatchNote.FLOOR_RANGE.name()) != null) {
					floorRange = (String)specFloor.get(TableConstants.FloorTypeBatchNote.FLOOR_RANGE.name());
					if(floorRange != null)
					{
						floorRanges = floorRange.split(TableConstants.SEPARATE_SPLIT);
						if(floorRanges != null && floorRanges.length > 0)
						{
							for(int idxRange = 0; idxRange < floorRanges.length; idxRange++)
							{
								if(floorRanges[idxRange] != null) {
									floorNo = floorRanges[idxRange];

									if(!mapSpecFloors.containsKey(floorNo)) {
										mapSpecFloors.put(floorNo, specFloor);
									}
								}
							}
						}
					}
				}
			}
		}

		// 获取期信息
		{
			if(map.containsKey(TableConstants.FloorRoomBatchGenNote.PROJECT_PERIOD_ID.name())) {
				projectPeriodId = (String)map.get(TableConstants.FloorRoomBatchGenNote.PROJECT_PERIOD_ID.name());
				projectPeriod = projectPeriodService.getByID(projectPeriodId, ddBB);

				if(projectPeriod != null && !projectPeriod.isEmpty())
				{
					if(projectPeriod.containsKey(TableConstants.ProjectPeriod.periodName.name()))
					{
						projectPeriodName = (String)projectPeriod.get(TableConstants.ProjectPeriod.periodName.name());
					}
				}
			}
		}
		// 获取起始栋和结束栋,所有需要批量处理的栋信息列表
		{
			if(map.containsKey(TableConstants.FloorRoomBatchGenNote.BUILDING_BEGIN_ID.name())) {
				if(map.get(TableConstants.FloorRoomBatchGenNote.BUILDING_BEGIN_ID.name()) != null) {
					beginBuilding = projectBuildingService.getByID((String)map.get(TableConstants.FloorRoomBatchGenNote.BUILDING_BEGIN_ID.name()), ddBB);
				}
			}
			if(map.containsKey(TableConstants.FloorRoomBatchGenNote.BUILDING_END_ID.name())) {
				if(map.get(TableConstants.FloorRoomBatchGenNote.BUILDING_END_ID.name()) != null) {
					endBuilding = projectBuildingService.getByID((String)map.get(TableConstants.FloorRoomBatchGenNote.BUILDING_END_ID.name()), ddBB);
				}
			}

			if(beginBuilding != null && beginBuilding.containsKey(TableConstants.ProjectBuilding.buildingSno.name())
					&& beginBuilding.get(TableConstants.ProjectBuilding.buildingSno.name()) != null)
			{
				beginBuildingSno = (int)beginBuilding.get(TableConstants.ProjectBuilding.buildingSno.name());
			}

			if(endBuilding != null && endBuilding.containsKey(TableConstants.ProjectBuilding.buildingSno.name())
					&& endBuilding.get(TableConstants.ProjectBuilding.buildingSno.name()) != null)
			{
				endBuildingSno = (int)endBuilding.get(TableConstants.ProjectBuilding.buildingSno.name());
				if(endBuildingSno < beginBuildingSno)
				{
					int temp = beginBuildingSno;
					beginBuildingSno = endBuildingSno;
					endBuildingSno = temp;
				}
			}

            if(beginBuildingSno > -1 && endBuildingSno > -1) {
				buildingParams.put(BEGIN_BUILDING_SNO, beginBuildingSno);
				buildingParams.put(END_BUILDING_SNO, endBuildingSno);
				buildingParams.put(TableConstants.ProjectBuilding.PROJECT_PERIOD_ID.name(), projectPeriodId);
				buildingParams.put(TableConstants.IS_SEALED, 0);

				listBuildings = projectBuildingService.getProjectBuildingDetailList(buildingParams, ddBB);
			}
			else
			{
				new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
			}
		}
		// 保存层户信息
		{
			if(listBuildings != null && listBuildings.size() > 0) {
				// 部位层
				floorRegionTypeId = projectRegionTypeService.getProjectRegionTypeIdByName(DataConstants.REGION_FLOOR_TYPE, ddBB);

				// 部位户
				roomRegionTypeId = projectRegionTypeService.getProjectRegionTypeIdByName(DataConstants.REGION_ROOM_TYPE, ddBB);

				// 批量生成层和户信息
				for (int index = 0; index < listBuildings.size(); index++) {
					building = listBuildings.get(index);
					if(building != null) {
						if(building.containsKey(TableConstants.ProjectBuilding.id.name())) {
							buildingId = (String)building.get(TableConstants.ProjectBuilding.id.name());
						}
						else
						{
							continue;
						}
						if(building.containsKey(TableConstants.ProjectBuilding.nameTree.name())) {
							buildingNameTree = (String)building.get(TableConstants.ProjectBuilding.nameTree.name());
						}
						if(building.containsKey(TableConstants.ProjectBuilding.idTree.name())) {
							buildingIdTree = (String)building.get(TableConstants.ProjectBuilding.idTree.name());
						}
						if(building.containsKey(TableConstants.ProjectBuilding.buildingName.name())) {
							buildingName = (String) building.get(TableConstants.ProjectBuilding.buildingName.name());
						}

						floors = new ArrayList();
						rooms = new ArrayList();
						households = new ArrayList();

						if(isContainRoof == 1)
						{
							floorName =  DataConstants.ROOF_FLOOR;

							floor = ProjectHouseholdController.getFloor(floorName, floorRegionTypeId, buildingId,  tenantId, userId,
									buildingNameTree, null, projectPeriodName, buildingName,
									buildingIdTree, projectPeriodId, floorNum+1);
							floors.add(floor);
						}

						// 部位类型、户/层名、户/层全名、层建筑面积(咱不加)、层可用面积(暂不加)、栋编号、层数、层类型、ID树、名称树、备注
						// 保存层信息
						for(int idxFloor = 1; idxFloor <= floorNum; idxFloor ++ ) {
							floorName =  idxFloor + DataConstants.REGION_FLOOR_TYPE;

							floor = ProjectHouseholdController.getFloor(floorName, floorRegionTypeId, buildingId,  tenantId, userId,
									buildingNameTree, null, projectPeriodName, buildingName,
									buildingIdTree, projectPeriodId, idxFloor);

							if(mapSpecFloors != null && mapSpecFloors.containsKey(String.valueOf(idxFloor))
									&& mapSpecFloors.get(String.valueOf(idxFloor)) != null)
							{
								specFloor = (Map<String, Object>) mapSpecFloors.get(String.valueOf(idxFloor));
								if(specFloor != null
										&& specFloor.containsKey(TableConstants.FloorTypeBatchNote.ROOM_PER_FLOOR.name())
										&& specFloor.get(TableConstants.FloorTypeBatchNote.ROOM_PER_FLOOR.name()) != null)
								{
									roomNumByFloor = Integer.valueOf(String.valueOf(specFloor.get(TableConstants.FloorTypeBatchNote.ROOM_PER_FLOOR.name())));
								}
								else
								{
									roomNumByFloor = roomPerFloorNum;
								}
								if(specFloor != null
										&& specFloor.containsKey(TableConstants.FloorTypeBatchNote.FLOOR_TYPE_ID.name())
										&& specFloor.get(TableConstants.FloorTypeBatchNote.FLOOR_TYPE_ID.name()) != null)
								{
									floor.put(TableConstants.ProjectHousehold.FLOOR_TYPE_ID.name(), String.valueOf(specFloor.get(TableConstants.FloorTypeBatchNote.FLOOR_TYPE_ID.name())));
								}
							}
							else
							{
								roomNumByFloor = roomPerFloorNum;
							}

							if(floor.containsKey(TableConstants.ProjectHousehold.ID_TREE.name()))
							{
								floorIdTree = (String)floor.get(TableConstants.ProjectHousehold.ID_TREE.name());
							}
							if(floor.containsKey(TableConstants.ProjectHousehold.NAME_TREE.name()))
							{
								floorNameTree = (String)floor.get(TableConstants.ProjectHousehold.NAME_TREE.name());
							}
							if(floor.containsKey(TableConstants.ProjectHousehold.ID.name()))
							{
								floorId = (String)floor.get(TableConstants.ProjectHousehold.ID.name());
							}

							if(isContainInternalWall == 1)
							{
								roomName = String.format("%d%s", idxFloor, DataConstants.FLOOR_INTERNAL_WALL_SUFFIX);

								room = ProjectHouseholdController.getRoom(roomRegionTypeId,
										roomName, null, null,
										null, buildingId, idxFloor,
										tenantId, userId, floorNameTree,
										projectPeriodName, buildingName, floorName,
										floorIdTree, projectPeriodId, floorId);

								rooms.add(room);
							}

							if(isContainExternalWall == 1)
							{
								roomName = String.format("%d%s", idxFloor, DataConstants.FLOOR_EXTERNAL_WALL_SUFFIX);

								room = ProjectHouseholdController.getRoom(roomRegionTypeId,
										roomName, null, null,
										null, buildingId, idxFloor,
										tenantId, userId, floorNameTree,
										projectPeriodName, buildingName, floorName,
										floorIdTree, projectPeriodId, floorId);

								rooms.add(room);
							}

							// 部位类型、户/层名、户/层全名、户序列号、户编号、户建筑面积、户可用面积、栋编号、层数、ID树、名称树、备注
							for (int idxRoom = 1; idxRoom <= roomNumByFloor; idxRoom++) {
								// 保存户信息
								String format = String.format("%%d%%0%dd%%s", roomNameRule);
								roomName = String.format(format, idxFloor, idxRoom, roomSuffixName);
								format = String.format("%%d%%0%dd", roomNameRule);
								roomSqNo = Integer.valueOf(String.format(format, idxFloor, idxRoom));

								room = ProjectHouseholdController.getRoom(roomRegionTypeId,
										roomName, roomSqNo, totalArea,
										avariableArea, buildingId, idxFloor,
										tenantId, userId, floorNameTree,
										projectPeriodName, buildingName, floorName,
										floorIdTree, projectPeriodId, floorId);

								rooms.add(room);
							}

							floors.add(floor);
						}

						households.addAll(floors);
						households.addAll(rooms);

						projectHouseholdService.insertHouseholdBatch(households, ddBB);
					}
				}
			}
		}

		// 保存批量验收操作记录
		floorRoomBatchGenNoteService.addFloorRoomBatchGenNote(map, ddBB);

		if(listSpecFloors != null && listSpecFloors.size() > 0) {
			floorTypeBatchNoteService.insertFloorBatchNoteBatch(listSpecFloors, ddBB);
		}

		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/floorRoomBatchGenNote/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = floorRoomBatchGenNoteService.deleteFloorRoomBatchGenNote(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/floorRoomBatchGenNote/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
/*		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);*/
		boolean b = floorRoomBatchGenNoteService.updateFloorRoomBatchGenNote(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/floorRoomBatchGenNote/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, floorRoomBatchGenNoteService.getFloorRoomBatchGenNote(map, ddBB));
	}

	@RequestMapping("/floorRoomBatchGenNote/getList")
	@ResponseBody
	public BaseResult getList(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, floorRoomBatchGenNoteService.getFloorRoomBatchGenNoteList(map, ddBB));
	}

	@RequestMapping("/floorRoomBatchGenNote/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, floorRoomBatchGenNoteService.getFloorRoomBatchGenNotePage(page, ddBB));
	}
}
