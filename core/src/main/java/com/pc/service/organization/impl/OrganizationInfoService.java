package com.pc.service.organization.impl;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.organization.OrganizationInfoDao;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrganizationInfoService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());
    
    @Autowired
	private OrganizationInfoDao organizationInfoDao;
    
    public List<Map<String, Object>> getProjectOrganizationInfoList(Map<String, Object> params, String ddBB) {
    	params.put("oTableName", ddBB + TableConstants.SEPARATE + TableConstants.ORGANIZATION_INFO);
    	params.put("ppTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD);
		return organizationInfoDao.queryProjectOrganizationInfoListInTab(params);
	}
     
    public void addOrganizationInfo(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.ORGANIZATION_INFO);
	}

	public Page getOrganizationInfoPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.ORGANIZATION_INFO);

	}

	public boolean updateOrganizationInfo(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.ORGANIZATION_INFO) > 0;
	}

	public Map<String, Object> getOrganizationInfo(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.ORGANIZATION_INFO);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.ORGANIZATION_INFO,id);
	}

    public List<Map<String, Object>> getOrganizationInfoList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.ORGANIZATION_INFO);
		return list;
	}

	public boolean deleteOrganizationInfo(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.ORGANIZATION_INFO) > 0;
	}
}