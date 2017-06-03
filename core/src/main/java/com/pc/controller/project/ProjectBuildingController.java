package com.pc.controller.project;


import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.DataConstants;
import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.project.impl.ProjectBuildingService;
import com.pc.service.project.impl.ProjectPeriodService;
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
public class ProjectBuildingController extends BaseController {
	@Autowired
	private ProjectBuildingService projectBuildingService;

	@Autowired
	private ProjectPeriodService projectPeriodService;

	private Logger logger = LogManager.getLogger(this.getClass());
	
	@RequestMapping("/projectBuilding/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		String idTree = "";
		String nameTree = "";
		String buildingId = null;
		Map<String, Object> paramsBuilding = null;
		Map<String, Object> buildingExist = null;

		String projectPeriodId = null;
		String buildingUnit = null;
		String projectPeriodName = null;
		String buildingName = null;
		int buildingSno = 1;
		Map<String, Object> projectPeriod = null;

		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		map.put(TableConstants.IS_SEALED, 0);

		// 期信息
		if(map.containsKey(TableConstants.ProjectBuilding.PROJECT_PERIOD_ID.name())) {
			projectPeriodId = (String)map.get(TableConstants.ProjectBuilding.PROJECT_PERIOD_ID.name());
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
			else
			{
				return new BaseResult(ReturnCode.BA_PERIOD_ID_NOT_EXIST_ERROR);
			}
		}
		else
		{
			return new BaseResult(ReturnCode.BA_PERIOD_ID_MISSING_ERROR);
		}

		// 栋单位
		if(buildingUnit == null)
		{
			buildingUnit = DataConstants.REGION_BUILDING_TYPE;
		}

		{
			paramsBuilding = new HashMap<>();

			if(map.containsKey(TableConstants.ProjectBuilding.BUILDING_NAME.name())) {
				buildingName = (String)map.get(TableConstants.ProjectBuilding.BUILDING_NAME.name());
			}
			else
			{
				return new BaseResult(ReturnCode.BA_BUILDING_NAME_MISSING_ERROR);
			}

			paramsBuilding.put(TableConstants.TENANT_ID, tenantId);
			paramsBuilding.put(TableConstants.IS_SEALED, 0);
			paramsBuilding.put(TableConstants.ProjectBuilding.BUILDING_NAME.name(), buildingName);
			paramsBuilding.put(TableConstants.ProjectBuilding.PROJECT_PERIOD_ID.name(), projectPeriodId);

			buildingExist = projectBuildingService.getProjectBuilding(paramsBuilding, ddBB);
			if (buildingExist != null && !buildingExist.isEmpty()) {
				return new BaseResult(ReturnCode.BA_BUILDING_NAME_EXIST_ERROR);
			}

			buildingId = UUID.randomUUID().toString().replace("-", "");
			map.put(TableConstants.ProjectBuilding.ID.name(), buildingId);

			paramsBuilding.clear();

			paramsBuilding.put(TableConstants.TENANT_ID, tenantId);
			paramsBuilding.put(TableConstants.ProjectBuilding.PROJECT_PERIOD_ID.name(), projectPeriodId);

			buildingExist = projectBuildingService.getLatestProjectBuildingDetail(paramsBuilding, ddBB);

			if(buildingExist != null
					&& buildingExist.containsKey(TableConstants.ProjectBuilding.buildingSno.name())
					&& buildingExist.get(TableConstants.ProjectBuilding.buildingSno.name()) != null)
			{
				buildingSno = (int)buildingExist.get(TableConstants.ProjectBuilding.buildingSno.name()) + 1;
			}
			//buildingSno
			map.put(TableConstants.ProjectBuilding.BUILDING_SNO.name(), buildingSno);

			//projectPeriodId
			if (projectPeriodId != null) {
				if (projectPeriodName != null) {
					nameTree = String.format("%s%s%s", projectPeriodName, TableConstants.SEPARATE_TREE, buildingName);
				} else {
					nameTree = String.format("%s", buildingName);
				}
				idTree = String.format("%s%s%s", projectPeriodId, TableConstants.SEPARATE_TREE, buildingId);

				map.put(TableConstants.ProjectBuilding.ID_TREE.name(), idTree);
				map.put(TableConstants.ProjectBuilding.NAME_TREE.name(), nameTree);
			}
		}

		projectBuildingService.addProjectBuilding(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/projectBuilding/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = projectBuildingService.deleteProjectBuilding(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/projectBuilding/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = projectBuildingService.updateProjectBuilding(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/projectBuilding/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, projectBuildingService.getProjectBuilding(map, ddBB));
	}

	@RequestMapping("/projectBuilding/getList")
	@ResponseBody
	public BaseResult getList(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		//return new BaseResult(ReturnCode.OK, projectBuildingService.getProjectBuildingList(map, ddBB));
		return new BaseResult(ReturnCode.OK, projectBuildingService.getProjectBuildingDetailList(map, ddBB));
	}

	@RequestMapping("/projectBuilding/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);
		//return new BaseResult(ReturnCode.OK, projectBuildingService.getProjectBuildingPage(page, ddBB));
		return new BaseResult(ReturnCode.OK, projectBuildingService.getProjectBuildingDetailPage(page, ddBB));
	}
}
