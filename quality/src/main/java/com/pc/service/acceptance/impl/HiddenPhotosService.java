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
public class HiddenPhotosService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

     
    public void addHiddenPhotos(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.HIDDEN_PHOTOS);
	}

	public Page getHiddenPhotosPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.HIDDEN_PHOTOS);

	}

	public boolean updateHiddenPhotos(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.HIDDEN_PHOTOS) > 0;
	}

	public Map<String, Object> getHiddenPhotos(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.HIDDEN_PHOTOS);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.HIDDEN_PHOTOS,id);
	}
	
    public List<Map<String, Object>> getHiddenPhotosList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.HIDDEN_PHOTOS);
		return list;
	}

	public boolean deleteHiddenPhotos(Map<String, Object> params, String ddBB) {
		return delete(params, ddBB + TableConstants.SEPARATE + TableConstants.HIDDEN_PHOTOS) > 0;
	}
}