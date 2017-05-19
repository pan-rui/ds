package com.pc.service.procedure.impl;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GeneralItemService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

     
    public void addGeneralItem(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.GENERAL_ITEM);
	}

	public Page getGeneralItemPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.GENERAL_ITEM);

	}

	public boolean updateGeneralItem(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.GENERAL_ITEM) > 0;
	}

	public Map<String, Object> getGeneralItem(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.GENERAL_ITEM);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.GENERAL_ITEM,id);
	}
	
    public List<Map<String, Object>> getGeneralItemList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.GENERAL_ITEM);
		return list;
	}

	public boolean deleteGeneralItem(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.GENERAL_ITEM) > 0;
	}
}