package com.pc.service.project.impl;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.project.ProjectRegionProcedureRelateDao;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectRegionProcedureRelateService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());
    
    @Autowired
	private ProjectRegionProcedureRelateDao projectRegionProcedureRelateDao;
    
    public void addRoleDataPrivilegesRelateList(List<Map<String, Object>> list, String ddBB) {
		Map<String, Object> map = new HashMap<>();
		map.put("list", list);
		map.put("prprTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_REGION_PROCEDURE_RELATE);
		projectRegionProcedureRelateDao.insertList(map);
	}
     
    public void addProjectRegionProcedureRelate(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_REGION_PROCEDURE_RELATE);
	}

	public Page getProjectRegionProcedureRelatePage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_REGION_PROCEDURE_RELATE);

	}

	public boolean updateProjectRegionProcedureRelate(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_REGION_PROCEDURE_RELATE) > 0;
	}

	public Map<String, Object> getProjectRegionProcedureRelate(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_REGION_PROCEDURE_RELATE);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_REGION_PROCEDURE_RELATE,id);
	}
	
    public List<Map<String, Object>> getProjectRegionProcedureRelateList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_REGION_PROCEDURE_RELATE);
		return list;
	}

	public boolean deleteProjectRegionProcedureRelate(Map<String, Object> params, String ddBB) {
		return delete(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_REGION_PROCEDURE_RELATE) > 0;
	}
}