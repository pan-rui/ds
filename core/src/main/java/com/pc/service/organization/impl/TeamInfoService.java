package com.pc.service.organization.impl;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.organization.TeamInfoDao;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TeamInfoService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());
    
    @Autowired
	private TeamInfoDao teamInfoDao;

     
    public void addTeamInfo(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.TEAM_INFO);
	}

	public Page getTeamInfoPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.TEAM_INFO);

	}

	public boolean updateTeamInfo(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.TEAM_INFO) > 0;
	}

	public Map<String, Object> getTeamInfo(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.TEAM_INFO);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.TEAM_INFO,id);
	}
	
    public List<Map<String, Object>> getTeamInfoList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.TEAM_INFO);
		return list;
	}

	public boolean deleteTeamInfo(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.TEAM_INFO) > 0;
	}
	
	public List<Map<String, Object>> getTeamListByProject(Map<String, Object> map, String ddBB) {
		map.put("tTableName", ddBB + TableConstants.SEPARATE + TableConstants.TEAM_INFO);
		map.put("pprTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PARTNER_RELATE);
		
		map.put("tprTableName", ddBB + TableConstants.SEPARATE + TableConstants.TEAM_PROCEDURE_RELATE);
		return teamInfoDao.queryTeamsInTab(map);
	}
}