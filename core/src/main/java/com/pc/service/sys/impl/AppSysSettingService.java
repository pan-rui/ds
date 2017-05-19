package com.pc.service.sys.impl;

import java.util.List;
import java.util.Map;
 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.sys.AppSysSettingDao;
import com.pc.service.BaseService;

@Service
public class AppSysSettingService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

     
    public void addAppSysSetting(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.APP_SYS_SETTING);
	}

	public Page getAppSysSettingPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.APP_SYS_SETTING);

	}

	public boolean updateAppSysSetting(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.APP_SYS_SETTING) > 0;
	}

	public Map<String, Object> getAppSysSetting(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.APP_SYS_SETTING);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

        public List<Map<String, Object>> getAppSysSettingList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.APP_SYS_SETTING);
		return list;
	}
        public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.APP_SYS_SETTING,id);
	}


	public boolean deleteAppSysSetting(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.APP_SYS_SETTING) > 0;
	}
}