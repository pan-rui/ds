package com.pc.service.project.impl;

import com.pc.core.Page;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.dao.project.ProjectInfoDao;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProjectInfoService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private ProjectInfoDao projectInfoDao;
	
	public List<Map<String, Object>> getProjectInfoListByUser(Map<String, Object> params, String ddBB) {
		params.put("pTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_INFO);
		params.put("ppTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD);
		params.put("pbTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
		params.put("phTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
		
		params.put("rdprTableName", ddBB + TableConstants.SEPARATE + TableConstants.ROLE_DATA_PRIVILEGES_RELATE);
		params.put("dpTableName", ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES_INFO);
		params.put("rTableName", ddBB + TableConstants.SEPARATE + TableConstants.ROLES_INFO);
		params.put("urrTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER_ROLE_RELATE);
		
		return projectInfoDao.queryProjectInfoListByUserInTab(params);
	}
     
    public void addProjectInfo(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_INFO);
	}

	public Page getProjectInfoPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_INFO);

	}

	public boolean updateProjectInfo(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_INFO) > 0;
	}

	public Map<String, Object> getProjectInfo(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_INFO);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_INFO,id);
	}
	
    public List<Map<String, Object>> getProjectInfoList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_INFO);
		return list;
	}

	public List<Map<String, Object>> getProjectDetailList(Map<String, Object> params, String ddBB) {
		params.put("companyTableName", ddBB + TableConstants.SEPARATE + TableConstants.COMPANY);
		params.put("projectTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_INFO);
		List<Map<String, Object>> list = projectInfoDao.queryProjectDetailListInTab(params);
		return list;
	}


	public Page getProjectDetailPage(Page<Map<String, Object>> page, String ddBB) {

		Map<String, Object> paramsMap = ParamsMap.newMap("page", page);
		paramsMap.put("companyTableName", ddBB + TableConstants.SEPARATE + TableConstants.COMPANY);
		paramsMap.put("projectTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_INFO);
		paramsMap.putAll(page.getParams());

		page.setResults(projectInfoDao.queryProjectDetailPageInTab(paramsMap));

		return page;
	}

	public boolean deleteProjectInfo(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_INFO) > 0;
	}
}