package com.pc.controller.project;

 
import java.util.*;

import com.pc.core.DataConstants;
import com.pc.service.project.impl.ProjectBuildingService;
import com.pc.service.project.impl.ProjectPeriodService;
import com.pc.util.NumberUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pc.controller.BaseController;
import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.core.Page;
import com.pc.core.ParamsMap;
import com.pc.util.DateUtil;
import com.pc.vo.ParamsVo;

import com.pc.core.TableConstants;
 
import com.pc.service.project.impl.BuildingBatchGenNoteService;

/**
 * @Description: 
 * @Author: wady (2017-03-28 20:19)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-28
 */
@Controller
@RequestMapping("/admin")
public class BuildingBatchGenNoteController extends BaseController {
	@Autowired
	private BuildingBatchGenNoteService buildingBatchGenNoteService;

	@Autowired
	private ProjectBuildingService projectBuildingService;

	@Autowired
	private ProjectPeriodService projectPeriodService;

	private Logger logger = LogManager.getLogger(this.getClass());
	
	@RequestMapping("/buildingBatchGenNote/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		String projectPeriodId = null;
		String projectPeriodName = null;
		String buildingUnit = null;
		List<Map<String, Object>> buildingList = null;
		Map<String, Object> projectPeriod = null;
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		/*map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);*/
		/*map.put(TableConstants.IS_VALID, 0);*/
		map.put(TableConstants.IS_SEALED, 0); 
		buildingBatchGenNoteService.addBuildingBatchGenNote(map, ddBB);

		// 期信息, projectPeriodId
		{
			if(map.containsKey(TableConstants.BuildingBatchGenNote.PROJECT_PERIOD_ID.name())) {
				projectPeriodId = (String)map.get(TableConstants.BuildingBatchGenNote.PROJECT_PERIOD_ID.name());
				projectPeriod = projectPeriodService.getByID(projectPeriodId, ddBB);

				if(projectPeriod != null && !projectPeriod.isEmpty())
				{
					if(projectPeriod.containsKey(TableConstants.ProjectPeriod.buildingUnit.name()))
					{
						buildingUnit = (String)projectPeriod.get(TableConstants.ProjectPeriod.buildingUnit.name());
					}

					if(projectPeriod.containsKey(TableConstants.ProjectPeriod.periodName.name()))
					{
						projectPeriodName = (String)projectPeriod.get(TableConstants.ProjectPeriod.periodName.name());
					}
				}
			}

			if(buildingUnit == null)
			{
				buildingUnit = DataConstants.REGION_BUILDING_TYPE;
			}
		}

		// 栋信息, building
		{
			// 栋命名规则 buildingNameRule 1,2
			// 栋开始编号 buildingNo, buildingName
			// 栋负责人编号 buildingManagerId
			// 栋数buildingSize
			//总建筑面积 totalArea
			// 商用面积 commercialArea
			// 建筑面积 officeArea
			// 总高度 height
			// 总层数 totalFloor
			// 层高  floorHeight
			// 是否含屋面 isContainRoof
			String buildId = null;
			String buildingNameRule = null;
			int buildingNo = 0;
			String buildingName = null;
			String buildingNewName = null;
			int buildingSize = 0;
			String buildingManagerId = null;
			float totalArea = 0.0f;
			float commercialArea = 0.0f;
			float officeArea = 0.0f;
			float height = 0.0f;
			int totalFloor = 0;
			float floorHeight = 0.0f;
			int isContainRoof = 0;
			Map<String, Object> building = null;
			String idTree = "";
			String nameTree = "";
			Map<String, Object> paramsBuilding = null;
			Map<String, Object> buildingExist = null;

			if(map.containsKey(TableConstants.BuildingBatchGenNote.BUILDING_NAME_RULE.name())) {
				buildingNameRule = (String)map.get(TableConstants.BuildingBatchGenNote.BUILDING_NAME_RULE.name());

				if(buildingNameRule == null)
				{
					return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
				}
			}
			if(map.containsKey(TableConstants.BuildingBatchGenNote.BUILDING_NO.name())) {
				buildingNo = (Integer)map.get(TableConstants.BuildingBatchGenNote.BUILDING_NO.name());
			}
			else
			{
				buildingNo = 1;
			}
			if(map.containsKey(TableConstants.BuildingBatchGenNote.BUILDING_SIZE.name())
					&& map.get(TableConstants.BuildingBatchGenNote.BUILDING_SIZE.name()) != null) {
				buildingSize = (Integer)map.get(TableConstants.BuildingBatchGenNote.BUILDING_SIZE.name());
			}
			if(map.containsKey(TableConstants.BuildingBatchGenNote.BUILDING_MANAGER_ID.name())
					&& map.get(TableConstants.BuildingBatchGenNote.BUILDING_MANAGER_ID.name()) != null) {
				buildingManagerId = (String)map.get(TableConstants.BuildingBatchGenNote.BUILDING_MANAGER_ID.name());
			}
			if(map.containsKey(TableConstants.BuildingBatchGenNote.TOTAL_AREA.name())
					&& map.get(TableConstants.BuildingBatchGenNote.TOTAL_AREA.name()) != null) {
				totalArea = Float.valueOf(String.valueOf(map.get(TableConstants.BuildingBatchGenNote.TOTAL_AREA.name())));
			}
			if(map.containsKey(TableConstants.BuildingBatchGenNote.COMMERCIAL_AREA.name())
					&& map.get(TableConstants.BuildingBatchGenNote.COMMERCIAL_AREA.name()) != null) {
				commercialArea = Float.valueOf(String.valueOf(map.get(TableConstants.BuildingBatchGenNote.COMMERCIAL_AREA.name())));
			}
			if(map.containsKey(TableConstants.BuildingBatchGenNote.OFFICE_AREA.name())
					&& map.get(TableConstants.BuildingBatchGenNote.OFFICE_AREA.name()) != null) {
				officeArea = Float.valueOf(String.valueOf(map.get(TableConstants.BuildingBatchGenNote.OFFICE_AREA.name())));
			}
			if(map.containsKey(TableConstants.BuildingBatchGenNote.HEIGHT.name())
					&& map.get(TableConstants.BuildingBatchGenNote.HEIGHT.name()) != null) {
				height = Float.valueOf(String.valueOf(map.get(TableConstants.BuildingBatchGenNote.HEIGHT.name())));
			}
			if(map.containsKey(TableConstants.BuildingBatchGenNote.TOTAL_FLOOR.name())
					&& map.get(TableConstants.BuildingBatchGenNote.TOTAL_FLOOR.name()) != null) {
				totalFloor = (Integer)map.get(TableConstants.BuildingBatchGenNote.TOTAL_FLOOR.name());
			}
			if(map.containsKey(TableConstants.BuildingBatchGenNote.FLOOR_HEIGHT.name())
					&& map.get(TableConstants.BuildingBatchGenNote.FLOOR_HEIGHT.name()) != null) {
				floorHeight = (Integer)map.get(TableConstants.BuildingBatchGenNote.FLOOR_HEIGHT.name());
			}
			if(map.containsKey(TableConstants.BuildingBatchGenNote.IS_CONTAIN_ROOF.name())
					&& map.get(TableConstants.BuildingBatchGenNote.IS_CONTAIN_ROOF.name()) != null) {
				isContainRoof = (Integer)map.get(TableConstants.BuildingBatchGenNote.IS_CONTAIN_ROOF.name());
			}

			buildingList = new ArrayList<>();

			if(buildingSize > 0) {
				for (int index = 0; index < buildingSize; index++) {
					paramsBuilding = new HashMap<>();

					paramsBuilding.put(TableConstants.TENANT_ID, tenantId);
					paramsBuilding.put(TableConstants.ProjectBuilding.BUILDING_SNO.name(), buildingNo + index);
					paramsBuilding.put(TableConstants.ProjectBuilding.PROJECT_PERIOD_ID.name(), projectPeriodId);

					buildingExist = projectBuildingService.getProjectBuilding(paramsBuilding, ddBB);
					if(buildingExist != null && !buildingExist.isEmpty())
					{
						paramsBuilding.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
						paramsBuilding.put(TableConstants.SEALED_USER_ID, userId);

						projectBuildingService.deleteProjectBuilding(paramsBuilding,ddBB);
					}

					building = new HashMap<>();

					buildId = UUID.randomUUID().toString().replace("-", "");
					building.put(TableConstants.ProjectBuilding.ID.name(), buildId);

					if(buildingNameRule.compareTo("0") == 0)
					{
						String prefix = DataConstants.REGION_BUILDING_RULE_A_Z.substring(buildingNo + index - 1, buildingNo + index);

						buildingNewName = String.format("%s%s", prefix, buildingUnit);
					}
					else
					{
						buildingNewName = String.format("%d%s", buildingNo + index, buildingUnit);
					}

					// buildingName
					building.put(TableConstants.ProjectBuilding.BUILDING_NAME.name(), buildingNewName);

					// buildingManagerId
					if(buildingManagerId != null) {
						building.put(TableConstants.ProjectBuilding.BUILDING_MANAGER_ID.name(), buildingManagerId);
					}

					//buildingSno
					building.put(TableConstants.ProjectBuilding.BUILDING_SNO.name(), buildingNo + index);

					//projectPeriodId
					if(projectPeriodId != null) {
						building.put(TableConstants.ProjectBuilding.PROJECT_PERIOD_ID.name(), projectPeriodId);

						if(projectPeriodName != null)
						{
							nameTree = String.format("%s%s%s", projectPeriodName, TableConstants.SEPARATE_TREE, buildingNewName);
						}
						else
						{
							nameTree = String.format("%s", buildingNewName);
						}
						idTree = String.format("%s%s%s", projectPeriodId, TableConstants.SEPARATE_TREE, buildId);

						building.put(TableConstants.ProjectBuilding.ID_TREE.name(), idTree);
						building.put(TableConstants.ProjectBuilding.NAME_TREE.name(), nameTree);
					}

					building.put(TableConstants.ProjectBuilding.TOTAL_AREA.name(), totalArea);
					building.put(TableConstants.ProjectBuilding.COMMERCIAL_AREA.name(), commercialArea);
					building.put(TableConstants.ProjectBuilding.OFFICE_AREA.name(), officeArea);
					building.put(TableConstants.ProjectBuilding.HEIGHT.name(), height);
					building.put(TableConstants.ProjectBuilding.TOTAL_FLOOR.name(), totalFloor);
					building.put(TableConstants.ProjectBuilding.FLOOR_HEIGHT.name(), floorHeight);
					building.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
					building.put(TableConstants.UPDATE_USER_ID, userId);
					building.put(TableConstants.TENANT_ID, tenantId);
					building.put(TableConstants.IS_SEALED, 0);

					buildingList.add(building);
				}
			}

			if(buildingList != null && buildingList.size() > 0) {
				projectBuildingService.insertBuildingsBatch(buildingList, ddBB);
			}
		}

		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/buildingBatchGenNote/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = buildingBatchGenNoteService.deleteBuildingBatchGenNote(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/buildingBatchGenNote/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = buildingBatchGenNoteService.updateBuildingBatchGenNote(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/buildingBatchGenNote/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, buildingBatchGenNoteService.getBuildingBatchGenNote(map, ddBB));
	}

	@RequestMapping("/buildingBatchGenNote/getList")
	@ResponseBody
	public BaseResult getList(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, buildingBatchGenNoteService.getBuildingBatchGenNoteList(map, ddBB));
	}

	@RequestMapping("/buildingBatchGenNote/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_VALID, 0);
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, buildingBatchGenNoteService.getBuildingBatchGenNotePage(page, ddBB));
	}
}
