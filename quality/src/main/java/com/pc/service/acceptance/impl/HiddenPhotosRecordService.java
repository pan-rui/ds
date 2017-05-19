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
public class HiddenPhotosRecordService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

     
    public void addHiddenPhotosRecord(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.HIDDEN_PHOTOS_RECORD);
	}

	public Page getHiddenPhotosRecordPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.HIDDEN_PHOTOS_RECORD);

	}

	public boolean updateHiddenPhotosRecord(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.HIDDEN_PHOTOS_RECORD) > 0;
	}

	public Map<String, Object> getHiddenPhotosRecord(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.HIDDEN_PHOTOS_RECORD);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.HIDDEN_PHOTOS_RECORD,id);
	}
	
    public List<Map<String, Object>> getHiddenPhotosRecordList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.HIDDEN_PHOTOS_RECORD);
		return list;
	}

	public boolean deleteHiddenPhotosRecord(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.HIDDEN_PHOTOS_RECORD) > 0;
	}
}