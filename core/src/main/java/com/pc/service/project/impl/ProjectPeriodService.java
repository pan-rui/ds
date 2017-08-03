package com.pc.service.project.impl;

import com.pc.core.Page;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.dao.project.ProjectPeriodDao;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectPeriodService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private ProjectPeriodDao projectPeriodDao;
	
	public List<Map<String, Object>> getProjectPeriodListByUser(Map<String, Object> params, String ddBB) {
		params.put("ppTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD);
		params.put("pbTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
		params.put("phTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
		
		params.put("rdprTableName", ddBB + TableConstants.SEPARATE + TableConstants.ROLE_DATA_PRIVILEGES_RELATE);
		params.put("dpTableName", ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES_INFO);
		params.put("rTableName", ddBB + TableConstants.SEPARATE + TableConstants.ROLES_INFO);
		params.put("urrTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER_ROLE_RELATE);
		
		return projectPeriodDao.queryProjectPeriodListByUserInTab(params);
	}
     
    public void addProjectPeriod(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD);
	}

	public Page getProjectPeriodPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD);

	}

	public boolean updateProjectPeriod(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD) > 0;
	}
	
	public boolean updateProjectPeriodTree(Map<String, Object> params, String ddBB) {
		return updateTree(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD) > 0;
	}

	public Map<String, Object> getProjectPeriod(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD,id);
	}

    public List<Map<String, Object>> getProjectPeriodList(Map<String, Object> params, String ddBB) {
    	Map<String, Object> orderMap=new HashMap<>();
    	orderMap.put(TableConstants.ProjectPeriod.PERIOD_NAME.name(), TableConstants.ORDER_BY_ASC);
		List<Map<String, Object>> list = queryList(params, orderMap,
				ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD);
		return list;
	}

	public boolean deleteProjectPeriod(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD) > 0;
	}

	public List<Map<String, Object>> getProjectPeriodDetailList(Map<String, Object> params, String ddBB) {
		params.put("projectPeriodTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD);
		params.put("orgTableName", ddBB + TableConstants.SEPARATE + TableConstants.ORGANIZATION_INFO);
		params.put("userTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER);
		params.put("projectTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_INFO);
		List<Map<String, Object>> list = projectPeriodDao.queryProjectPeriodDetailListInTab(params);
		return list;
	}


	public Page getProjectPeriodDetailPage(Page<Map<String, Object>> page, String ddBB) {

		Map<String, Object> paramsMap = ParamsMap.newMap("page", page);
		paramsMap.put("projectPeriodTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD);
		paramsMap.put("orgTableName", ddBB + TableConstants.SEPARATE + TableConstants.ORGANIZATION_INFO);
		paramsMap.put("userTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER);
		paramsMap.put("projectTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_INFO);
		paramsMap.putAll(page.getParams());

		page.setResults(projectPeriodDao.queryProjectPeriodDetailPageInTab(paramsMap));

		return page;
	}
}