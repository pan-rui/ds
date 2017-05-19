package com.pc.service.project.impl;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.project.ProjectPartnerRelateDao;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectPartnerRelateService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());
    @Autowired
	private ProjectPartnerRelateDao projectPartnerRelateDao;
    
    public Page getPageByTeamProject(Page page, String ddBB) {
		Map<String, Object> map=new HashMap<String, Object>(page.getParams());
		map.put("pprTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PARTNER_RELATE);
		map.put("pTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD);
		map.put("tTableName", ddBB + TableConstants.SEPARATE + TableConstants.TEAM_INFO);
		map.put("page", page);
		if(map.containsKey(TableConstants.ProjectPeriod.PERIOD_NAME.name())){
			map.put(TableConstants.ProjectPeriod.PERIOD_NAME.name(), "%"+map.get(TableConstants.ProjectPeriod.PERIOD_NAME.name())+"%");
		}
		page.setResults(projectPartnerRelateDao.queryProjectPartnerPageInTab(map));
		return page;
	}
     
    public void addProjectPartnerRelate(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PARTNER_RELATE);
	}

	public Page getProjectPartnerRelatePage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PARTNER_RELATE);

	}

	public boolean updateProjectPartnerRelate(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PARTNER_RELATE) > 0;
	}

	public Map<String, Object> getProjectPartnerRelate(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PARTNER_RELATE);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PARTNER_RELATE,id);
	}
	
    public List<Map<String, Object>> getProjectPartnerRelateList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PARTNER_RELATE);
		return list;
	}

	public boolean deleteProjectPartnerRelate(Map<String, Object> params, String ddBB) {
		return delete(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PARTNER_RELATE) > 0;
	}
}