package com.pc.service.tenant.impl;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class UpdateVesionInfoService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

     
    public void addUpdateVesionInfo(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.UPDATE_VESION_INFO);
	}

	public Page getUpdateVesionInfoPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.UPDATE_VESION_INFO);

	}

	public boolean updateUpdateVesionInfo(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.UPDATE_VESION_INFO) > 0;
	}

	public Map<String, Object> getUpdateVesionInfo(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.UPDATE_VESION_INFO);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.UPDATE_VESION_INFO,id);
	}

	public List<Map<String, Object>> getUpdateVesionInfoList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.UPDATE_VESION_INFO);
		return list;
	}

	public boolean deleteUpdateVesionInfo(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.UPDATE_VESION_INFO) > 0;
	}
}