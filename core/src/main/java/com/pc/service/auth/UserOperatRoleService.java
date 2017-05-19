package com.pc.service.auth;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.auth.AuthDao;
import com.pc.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserOperatRoleService extends BaseService {
	@Autowired
	private AuthDao authDao;

	/**
	 * 添加用户角色
	 * 
	 * @param params
	 */
	public void addUserOperatRole(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.USER_OPERATE_ROLE_RELATE);
	}

	/**
	 * 删除用户角色
	 * 
	 * @param params
	 * @return
	 */
	public boolean deleteUserOperatRole(Map<String, Object> params, String ddBB) {
		return delete(params, ddBB + TableConstants.SEPARATE + TableConstants.USER_OPERATE_ROLE_RELATE) > 0;
	}

	/**
	 * 查询用户角色列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getUserOperatRoles(String userId, String ddBB) {
		return authDao.queryUserOperatRolesTab(userId, ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_ROLE,
				ddBB + TableConstants.SEPARATE + TableConstants.USER_OPERATE_ROLE_RELATE);
	}

	/**
	 * 查询用户的权限（来自角色）
	 * 
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getUserOperatRolePrivileges(String userId, String ddBB) {
		return authDao.queryUserOperatPrivilegesByRoleTab(userId,
				ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_ROLE_PRIVILEGES_RELATE,
				ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_PRIVILEGES,
				ddBB + TableConstants.SEPARATE + TableConstants.USER_OPERATE_ROLE_RELATE);
	}

	public Page<Map<String, Object>> getUserOperatRolePrivilegesPage(Page<Map<String, Object>> page, String tenantId, String ddBB) {
		page.setResults(authDao.queryUserOperatPrivilegesRolePageInTab(page, tenantId,
				ddBB + TableConstants.SEPARATE + TableConstants.USER,
				ddBB + TableConstants.SEPARATE + TableConstants.USER_OPERATE_ROLE_RELATE,
				ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_ROLE));
		return page;
	}

}
