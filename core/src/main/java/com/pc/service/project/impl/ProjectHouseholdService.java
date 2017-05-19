package com.pc.service.project.impl;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pc.core.DataConstants;
import com.pc.core.Page;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.dao.project.ProjectHouseholdDao;
import com.pc.service.BaseService;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectHouseholdService extends BaseService {
	private Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private ProjectHouseholdDao projectHouseholdDao;

	@Autowired
	private ProjectRegionTypeService projectRegionTypeService;
	
	public List<Map<String, Object>> getProjectRegionTree(Map<String, Object> params, String ddBB) {
		params.put("pTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_INFO);
		params.put("ppTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD);
		params.put("pbTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
		params.put("phTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
		return projectHouseholdDao.queryProjectRegionTreeInTab(params);
	}
	
	public List<Map<String, Object>> getProjectRoomListByUser(Map<String, Object> params, String ddBB) {
		params.put("pbTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
		params.put("phTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
		
		params.put("rdprTableName", ddBB + TableConstants.SEPARATE + TableConstants.ROLE_DATA_PRIVILEGES_RELATE);
		params.put("dpTableName", ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES_INFO);
		params.put("rTableName", ddBB + TableConstants.SEPARATE + TableConstants.ROLES_INFO);
		params.put("urrTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER_ROLE_RELATE);
		
		return projectHouseholdDao.queryProjectRoomListByUserInTab(params);
	}
	
	public List<Map<String, Object>> getProjectFloorListByUser(Map<String, Object> params, String ddBB) {
		params.put("pbTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
		params.put("phTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
		
		params.put("rdprTableName", ddBB + TableConstants.SEPARATE + TableConstants.ROLE_DATA_PRIVILEGES_RELATE);
		params.put("dpTableName", ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES_INFO);
		params.put("rTableName", ddBB + TableConstants.SEPARATE + TableConstants.ROLES_INFO);
		params.put("urrTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER_ROLE_RELATE);
		
		return projectHouseholdDao.queryProjectFloorListByUserInTab(params);
	}

	public void addProjectHousehold(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
	}

	public Page getProjectHouseholdPage(Page<Map<String, Object>> page, String ddBB) {

		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);

	}

	public boolean updateProjectHousehold(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD) > 0;
	}

	public Map<String, Object> getProjectHousehold(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD, id);
	}

	public List<Map<String, Object>> getProjectHouseholdList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
		return list;
	}
	
	public List<Map<String, Object>> getProjectHouseholdList(Map<String, Object> params, Map<String, Object> orederParams, String ddBB) {
		List<Map<String, Object>> list = queryList(params, orederParams,
				ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
		return list;
	}

	public List<Map<String, Object>> getRoomList(Map<String, Object> params, String ddBB) {
		params.put("phTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
		params.put("pbTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
		params.put("regionTypeId",projectRegionTypeService
						.getProjectRegionType(ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
								DataConstants.REGION_ROOM_TYPE), ddBB).get(TableConstants.ProjectRegionType.id.name()));
		return projectHouseholdDao.queryRoomsInTab(params);
	}

	public List<Map<String, Object>> getFloorList(Map<String, Object> params, String ddBB) {
		params.put("phTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
		params.put("pbTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
		params.put("regionTypeId",projectRegionTypeService
						.getProjectRegionType(ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
								DataConstants.REGION_FLOOR_TYPE), ddBB).get(TableConstants.ProjectRegionType.id.name()));
		return projectHouseholdDao.queryFloorsInTab(params);
	}

	public boolean deleteProjectHousehold(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD) > 0;
	}

	public List<Map<String, Object>> getHouseholdsByRegion(Map<String, Object> params, String ddBB) {
		params.put("phTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
		params.put("pbTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
		params.put("rtTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_REGION_TYPE);

		return projectHouseholdDao.queryHouseholdsByRegionInTab(params);
	}

	public Page getHouseholdsPageByRegion(Page<Map<String, Object>> page, String ddBB) {

		Map<String, Object> paramsMap = ParamsMap.newMap("page", page);
		paramsMap.put("phTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
		paramsMap.put("pbTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
		paramsMap.put("rtTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_REGION_TYPE);
		paramsMap.putAll(page.getParams());

		page.setResults(projectHouseholdDao.queryHouseholdsPageByRegionInTab(paramsMap));

		return page;
	}
	public boolean insertHouseholdBatch(List<Map<String, Object>> householdList, String ddBB) {
		Map<String, Object> paramsMap = new LinkedMap();
		paramsMap.put("phTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
		paramsMap.put("list", householdList);

		return projectHouseholdDao.insertBatch(paramsMap) > 0;
	}

	public boolean updateHouseholdBatch(Map<String, Object> householdMap, String ddBB) {
		householdMap.put("phTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);

		return projectHouseholdDao.updateBatch(householdMap) > 0;
	}
}