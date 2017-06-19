package com.pc.service.project.impl;

import com.pc.core.Page;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.dao.project.ProjectBuildingDao;
import com.pc.service.BaseService;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectBuildingService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
    private ProjectBuildingDao projectBuildingDao;
	
	
	public List<Map<String, Object>> getProjectBuildingListByUser(Map<String, Object> params, String ddBB) {
		params.put("pbTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
		params.put("phTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
		
		params.put("rdprTableName", ddBB + TableConstants.SEPARATE + TableConstants.ROLE_DATA_PRIVILEGES_RELATE);
		params.put("dpTableName", ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES_INFO);
		params.put("rTableName", ddBB + TableConstants.SEPARATE + TableConstants.ROLES_INFO);
		params.put("urrTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER_ROLE_RELATE);
		
		return projectBuildingDao.queryProjectBuildingListByUserInTab(params);
	}
     
    public void addProjectBuilding(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
	}

	public Page getProjectBuildingPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);

	}

	public boolean updateProjectBuilding(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING) > 0;
	}

	public Map<String, Object> getProjectBuilding(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING,id);
	}

    public List<Map<String, Object>> getProjectBuildingList(Map<String, Object> params, String ddBB) {
    	Map<String, Object> orderMap=new HashMap<>();
    	orderMap.put(TableConstants.ProjectBuilding.BUILDING_SNO.name(), TableConstants.ORDER_BY_ASC);
		List<Map<String, Object>> list = queryList(params, orderMap,
				ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
		return list;
	}

	public boolean deleteProjectBuilding(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING) > 0;
	}

	public List<Map<String, Object>> getProjectBuildingDetailList(Map<String, Object> params, String ddBB) {
		params.put("projectBuildingTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
		params.put("userTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER);
		List<Map<String, Object>> list = projectBuildingDao.queryProjectBuildingDetailListInTab(params);
		return list;
	}

	public Page getProjectBuildingDetailPage(Page<Map<String, Object>> page, String ddBB) {

		Map<String, Object> paramsMap = ParamsMap.newMap("page", page);
		paramsMap.put("projectBuildingTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
		paramsMap.put("userTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER);
		paramsMap.putAll(page.getParams());

		page.setResults(projectBuildingDao.queryProjectBuildingDetailPageInTab(paramsMap));

		return page;
	}

	public boolean insertBuildingsBatch(List<Map<String, Object>> buildingList, String ddBB) {
		Map<String, Object> paramsMap = new LinkedMap();
		paramsMap.put("projectBuildingTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
		paramsMap.put("list", buildingList);

		return projectBuildingDao.insertBatch(paramsMap) > 0;
	}

	public Map<String, Object> getLatestProjectBuildingDetail(Map<String, Object> params, String ddBB) {
		params.put("projectBuildingTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
		params.put("userTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER);
		Map<String, Object> map = projectBuildingDao.queryLatestProjectBuildingDetailListInTab(params);
		return map;
	}

}