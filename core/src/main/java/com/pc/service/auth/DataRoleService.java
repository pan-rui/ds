package com.pc.service.auth;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by wady on 2017/3/28.
 */
@Service
public class DataRoleService extends BaseService {
	public void addDataRole(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_ROLE);
	}

	public Page getDataRolePage(Page<Map<String, Object>> page, String ddBB) {
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.DATA_ROLE);
	}

	public boolean updateDataRole(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_ROLE) > 0;
	}

	public Map<String, Object> getDataRole(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.DATA_ROLE);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public List<Map<String, Object>> getDataRoleList(Map<String, Object> params, String ddBB) {
		return queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.DATA_ROLE);
	}

	public boolean deleteDataRole(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_ROLE) > 0;
	}
}
