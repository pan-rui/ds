package com.pc.service.organization.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.organization.TeamProcedureRelateDao;
import com.pc.dao.organization.TeamUserRelateDao;
import com.pc.service.BaseService;

@Service
public class TeamProcedureRelateService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());
    
    @Autowired
    private TeamProcedureRelateDao teamProcedureRelateDao;
    
    public List<Map<String, Object>> getProcedureListByTeam(Map<String, Object> params,String ddBB) {
    	params.put("pTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_INFO);
    	params.put("tprTableName", ddBB + TableConstants.SEPARATE + TableConstants.TEAM_PROCEDURE_RELATE);
    	params.put("tTableName", ddBB + TableConstants.SEPARATE + TableConstants.TEAM_INFO);
		return teamProcedureRelateDao.queryProcedureListByTeamInTab(params);
	}
     
    public void addTeamProcedureRelate(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.TEAM_PROCEDURE_RELATE);
	}
    
    public void addTeamProcedureRelateList(List<Map<String, Object>> list, String ddBB) {
		addList(list, ddBB + TableConstants.SEPARATE + TableConstants.TEAM_PROCEDURE_RELATE);
	}

	public Page getTeamProcedureRelatePage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.TEAM_PROCEDURE_RELATE);

	}

	public boolean updateTeamProcedureRelate(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.TEAM_PROCEDURE_RELATE) > 0;
	}

	public Map<String, Object> getTeamProcedureRelate(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.TEAM_PROCEDURE_RELATE);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

        public List<Map<String, Object>> getTeamProcedureRelateList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.TEAM_PROCEDURE_RELATE);
		return list;
	}

	public boolean deleteTeamProcedureRelate(Map<String, Object> params, String ddBB) {
		return delete(params, ddBB + TableConstants.SEPARATE + TableConstants.TEAM_PROCEDURE_RELATE) > 0;
	}
}