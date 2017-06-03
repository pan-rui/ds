package com.pc.service.organization.impl;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.organization.TeamUserRelateDao;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeamUserRelateService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());
    
    @Autowired
    private TeamUserRelateDao teamUserRelateDao;
    
    public Page getUserPageByTeam(Page page, String ddBB) {
    	Map<String, Object> params = new LinkedHashMap<>(page.getParams());
    	params.put("uTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER);
    	params.put("turTableName", ddBB + TableConstants.SEPARATE + TableConstants.TEAM_USER_RELATE);
    	params.put("tTableName", ddBB + TableConstants.SEPARATE + TableConstants.TEAM_INFO);
    	params.put("pTableName", ddBB + TableConstants.SEPARATE + TableConstants.PARTNER_INFO);
    	page.setResults(teamUserRelateDao.queryUserTeamPageInTab(page,params));
		return page;
	}
     
    public void addTeamUserRelate(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.TEAM_USER_RELATE);
	}

	public Page getTeamUserRelatePage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.TEAM_USER_RELATE);

	}

	public boolean updateTeamUserRelate(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.TEAM_USER_RELATE) > 0;
	}

	public Map<String, Object> getTeamUserRelate(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.TEAM_USER_RELATE);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.TEAM_USER_RELATE,id);
	}
	
    public List<Map<String, Object>> getTeamUserRelateList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.TEAM_USER_RELATE);
		return list;
	}

	public boolean deleteTeamUserRelate(Map<String, Object> params, String ddBB) {
		return delete(params, ddBB + TableConstants.SEPARATE + TableConstants.TEAM_USER_RELATE) > 0;
	}
}