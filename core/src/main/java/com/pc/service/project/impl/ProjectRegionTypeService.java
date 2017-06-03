package com.pc.service.project.impl;

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
public class ProjectRegionTypeService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

     
    public void addProjectRegionType(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_REGION_TYPE);
	}

	public Page getProjectRegionTypePage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_REGION_TYPE);

	}
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_REGION_TYPE,id);
	}
	public boolean updateProjectRegionType(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_REGION_TYPE) > 0;
	}

	public Map<String, Object> getProjectRegionType(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_REGION_TYPE);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

        public List<Map<String, Object>> getProjectRegionTypeList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_REGION_TYPE);
		return list;
	}

	public boolean deleteProjectRegionType(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_REGION_TYPE) > 0;
	}

	public String getProjectRegionTypeIdByName(String regionName, String ddBB)
	{
		String regionTypeId = null;
		Map<String, Object> regionType = null;
		Map<String, Object> params  = new HashMap<String, Object>();
		params.put(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(), regionName);
		params.put(TableConstants.IS_SEALED, 0);

		regionType = getProjectRegionType(params, ddBB);

		if(regionType != null && regionType.containsKey(TableConstants.ProjectRegionType.id.name()))
		{
			regionTypeId = (String)regionType.get(TableConstants.ProjectRegionType.id.name());
		}

		return regionTypeId;
	}

}