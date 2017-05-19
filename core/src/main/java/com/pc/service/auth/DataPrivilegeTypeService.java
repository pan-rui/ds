package com.pc.service.auth;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataPrivilegeTypeService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

     
    public void addDataPrivilegeType(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGE_TYPE);
	}

	public Page getDataPrivilegeTypePage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGE_TYPE);

	}

	public boolean updateDataPrivilegeType(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGE_TYPE) > 0;
	}

	public Map<String, Object> getDataPrivilegeType(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGE_TYPE);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGE_TYPE,id);
	}
	
	public List<Map<String, Object>> getDataPrivilegeTypeList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGE_TYPE);
		return list;
	}

	public boolean deleteDataPrivilegeType(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGE_TYPE) > 0;
	}
}