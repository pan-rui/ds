package com.pc.service.acceptance.impl;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectPeriodPhotosService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

     
    public void addProjectPeriodPhotos(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD_PHOTOS);
	}

	public Page getProjectPeriodPhotosPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD_PHOTOS);

	}

	public boolean updateProjectPeriodPhotos(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD_PHOTOS) > 0;
	}

	public Map<String, Object> getProjectPeriodPhotos(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD_PHOTOS);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

    public List<Map<String, Object>> getProjectPeriodPhotosList(Map<String, Object> params, String ddBB) {
		Map<String, Object> orderMap=new HashMap<>();
		orderMap.put(TableConstants.ProjectPeriodPhotos.UPDATE_TIME.name(), TableConstants.ORDER_BY_DESC);
        	List<Map<String, Object>> list = queryList(params, orderMap,
				ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD_PHOTOS);
		return list;
	}
        public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD_PHOTOS,id);
	}


	public boolean deleteProjectPeriodPhotos(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD_PHOTOS) > 0;
	}
}