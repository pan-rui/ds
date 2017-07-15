package com.pc.service.acceptance.impl;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.acceptance.AcceptanceGeneralItemDao;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AcceptanceGeneralItemService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());
    
    @Autowired
    private AcceptanceGeneralItemDao acceptanceGeneralItemDao;
    
    public List<Map<String, Object>> getAcceptanceGeneralDetailRecord(String procedureId, String zjBantchId, String jlBantchId, String jfBantchId, String yfBantchId, String qtBantchId, String ddBB) {
    	Map<String, Object> params=new HashMap<>();
    	params.put("giTableName", ddBB + TableConstants.SEPARATE + TableConstants.GENERAL_ITEM);
    	params.put("agiTableName", ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_GENERAL_ITEM);
		params.put(TableConstants.DominantItem.procedureId.name(), procedureId);
		params.put("zjBantchId", zjBantchId);
		params.put("jlBantchId", jlBantchId);
		params.put("jfBantchId", jfBantchId);
		params.put("yfBantchId", yfBantchId);
		params.put("qtBantchId", qtBantchId);
		return acceptanceGeneralItemDao.queryAcceptanceGeneralDetailRecordInTab(params);
	}
     
    public void addAcceptanceGeneralItem(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_GENERAL_ITEM);
	}

	public Page getAcceptanceGeneralItemPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_GENERAL_ITEM);

	}

	public boolean updateAcceptanceGeneralItem(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_GENERAL_ITEM) > 0;
	}

	public Map<String, Object> getAcceptanceGeneralItem(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_GENERAL_ITEM);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_GENERAL_ITEM,id);
	}
	
    public List<Map<String, Object>> getAcceptanceGeneralItemList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_GENERAL_ITEM);
		return list;
	}

	public boolean deleteAcceptanceGeneralItem(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_GENERAL_ITEM) > 0;
	}
}