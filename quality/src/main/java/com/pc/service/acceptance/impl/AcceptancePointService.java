package com.pc.service.acceptance.impl;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AcceptancePointService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

     
    public void addAcceptancePoint(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_POINT);
	}
    
    public void addAcceptancePointList(List<Map<String, Object>> params, String ddBB) {
		super.addList(params, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_POINT);
	}

	public Page getAcceptancePointPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_POINT);

	}

	public boolean updateAcceptancePoint(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_POINT) > 0;
	}

	public Map<String, Object> getAcceptancePoint(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_POINT);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_POINT,id);
	}

    public List<Map<String, Object>> getAcceptancePointList(Map<String, Object> params,Map<String, Object> orderMap, String ddBB) {
		List<Map<String, Object>> list = queryList(params, orderMap,
				ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_POINT);
		return list;
	}

	public boolean deleteAcceptancePoint(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_POINT) > 0;
	}
}