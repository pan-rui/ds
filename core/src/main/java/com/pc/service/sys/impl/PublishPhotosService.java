package com.pc.service.sys.impl;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PublishPhotosService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

     
    public void addPublishPhotos(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.PUBLISH_PHOTOS);
	}
    
    public void addPublishPhotosList(List<Map<String, Object>> list, String ddBB) {
		addList(list, ddBB + TableConstants.SEPARATE + TableConstants.PUBLISH_PHOTOS);
	}
    
	public Page getPublishPhotosPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.PUBLISH_PHOTOS);

	}

	public boolean updatePublishPhotos(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.PUBLISH_PHOTOS) > 0;
	}

	public Map<String, Object> getPublishPhotos(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PUBLISH_PHOTOS);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public List<Map<String, Object>> getPublishPhotosList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PUBLISH_PHOTOS);
		return list;
	}
        public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.PUBLISH_PHOTOS,id);
	}


	public boolean deletePublishPhotos(Map<String, Object> params, String ddBB) {
		return delete(params, ddBB + TableConstants.SEPARATE + TableConstants.PUBLISH_PHOTOS) > 0;
	}
}