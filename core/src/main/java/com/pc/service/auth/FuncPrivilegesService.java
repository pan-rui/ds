package com.pc.service.auth;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FuncPrivilegesService extends BaseService {
	public void addFuncPrivileges(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.FUNC_PRIVILEGES);
	}

	public Page getFuncPrivilegesPage(Page<Map<String, Object>> page, String ddBB) {
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.FUNC_PRIVILEGES);
	}

	public boolean updateFuncPrivileges(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.FUNC_PRIVILEGES) > 0;
	}

	public Map<String, Object> getFuncPrivileges(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.FUNC_PRIVILEGES);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public List<Map<String, Object>> getFuncPrivilegesList(Map<String, Object> params, String ddBB) {
		return queryList(params, null, ddBB + TableConstants.SEPARATE + TableConstants.FUNC_PRIVILEGES);
	}

	public boolean deleteFuncPrivileges(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.FUNC_PRIVILEGES) > 0;
	}
}
