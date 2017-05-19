package com.pc.service.tenant.impl;

import java.util.List;
import java.util.Map;
 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.tenant.TenantSystemSettingDao;
import com.pc.service.BaseService;

@Service
public class TenantSystemSettingService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

     
    public void addTenantSystemSetting(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.TENANT_SYSTEM_SETTING);
	}

	public Page getTenantSystemSettingPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.TENANT_SYSTEM_SETTING);

	}

	public boolean updateTenantSystemSetting(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.TENANT_SYSTEM_SETTING) > 0;
	}

	public Map<String, Object> getTenantSystemSetting(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.TENANT_SYSTEM_SETTING);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

        public List<Map<String, Object>> getTenantSystemSettingList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.TENANT_SYSTEM_SETTING);
		return list;
	}

	public boolean deleteTenantSystemSetting(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.TENANT_SYSTEM_SETTING) > 0;
	}
}