package com.pc.service.sys.impl;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.sys.AppInfoDao;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppInfoService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());
    
    @Autowired
	private AppInfoDao appInfoDao;
    
    public List<Map<String, Object>> getAppInfoDeatilList(Map<String, Object> params, String ddBB) {
    	params.put("aTableName", ddBB + TableConstants.SEPARATE + TableConstants.APP_INFO);
		params.put("uvTableName", ddBB + TableConstants.SEPARATE + TableConstants.UPDATE_VESION_INFO);
		return appInfoDao.queryAppInfoDetailListInTab(params);
	}
    
    public Page getAppInfoDeatilPage(Page page, String ddBB) {
    	Map<String, Object> params=new HashMap<>(page.getParams());
    	params.put("page", page);
    	params.put("aTableName", ddBB + TableConstants.SEPARATE + TableConstants.APP_INFO);
		params.put("uvTableName", ddBB + TableConstants.SEPARATE + TableConstants.UPDATE_VESION_INFO);
		page.setResults(appInfoDao.queryAppInfoDetailPageInTab(params));
		return page;
	}
     
    public void addAppInfo(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.APP_INFO);
	}

	public Page getAppInfoPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.APP_INFO);

	}

	public boolean updateAppInfo(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.APP_INFO) > 0;
	}

	public Map<String, Object> getAppInfo(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.APP_INFO);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

    public List<Map<String, Object>> getAppInfoList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.APP_INFO);
		return list;
	}
    
    public List<Map<String, Object>> getAppInfoDetailList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.APP_INFO);
		return list;
	}
    
    public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.APP_INFO,id);
	}


	public boolean deleteAppInfo(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.APP_INFO) > 0;
	}
}