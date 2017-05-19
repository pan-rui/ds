package com.pc.service.auth;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.auth.AuthDao;
import com.pc.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by wady on 2017/3/28.
 */
@Service
public class DataUserRoleRelateService extends BaseService {
	@Autowired
	private AuthDao authDao;

	public void addDataUserRoleRelate(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.USER_DATA_ROLE_RELATE);
	}

	public Page getDataUserRoleRelatePage(Page<Map<String, Object>> page, String ddBB) {
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.USER_DATA_ROLE_RELATE);
	}

	public boolean updateDataUserRoleRelate(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.USER_DATA_ROLE_RELATE) > 0;
	}

	public List<Map<String, Object>> getDataUserRoleRelate(Map<String, Object> params, String ddBB) {
		return queryList(params, null, ddBB + TableConstants.SEPARATE + TableConstants.USER_DATA_ROLE_RELATE);
	}

	public boolean deleteDataUserRoleRelate(Map<String, Object> params, String ddBB) {
		return delete(params, ddBB + TableConstants.SEPARATE + TableConstants.USER_DATA_ROLE_RELATE) > 0;
	}

	/**
	 * 获取用户角色
	 * 
	 * @param params
	 * @param ddBB
	 * @return
	 */
	public List<Map<String, Object>> getUserDataRoles(String userId, String ddBB) {
		return authDao.queryUserDataRolesTab(userId, ddBB + TableConstants.SEPARATE + TableConstants.DATA_ROLE,
				ddBB + TableConstants.SEPARATE + TableConstants.USER_DATA_ROLE_RELATE);
	}
	
	public Page<Map<String, Object>> getUserDataRolePrivilegesPage(Page<Map<String, Object>> page, String tenantId, String ddBB) {
		page.setResults(authDao.queryUserDataPrivilegesRolePageInTab(page, tenantId,
				ddBB + TableConstants.SEPARATE + TableConstants.USER,
				ddBB + TableConstants.SEPARATE + TableConstants.USER_DATA_ROLE_RELATE,
				ddBB + TableConstants.SEPARATE + TableConstants.DATA_ROLE));
		return page;
	}
}
