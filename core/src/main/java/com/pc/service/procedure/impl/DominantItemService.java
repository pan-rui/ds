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
public class DominantItemService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

     
    public void addDominantItem(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.DOMINANT_ITEM);
	}

	public Page getDominantItemPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.DOMINANT_ITEM);

	}

	public boolean updateDominantItem(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.DOMINANT_ITEM) > 0;
	}

	public Map<String, Object> getDominantItem(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.DOMINANT_ITEM);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.DOMINANT_ITEM,id);
	}
	
    public List<Map<String, Object>> getDominantItemList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.DOMINANT_ITEM);
		return list;
	}

	public boolean deleteDominantItem(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.DOMINANT_ITEM) > 0;
	}
}