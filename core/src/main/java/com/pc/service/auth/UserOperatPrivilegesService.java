package com.pc.service.auth;

import com.pc.core.TableConstants;
import com.pc.dao.auth.AuthDao;
import com.pc.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserOperatPrivilegesService extends BaseService {
	@Autowired
	private AuthDao authDao;

	/**
	 * 添加用户权限，独立授权
	 * 
	 * @param params
	 */
	public void addUserOperatPrivileges(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.USER_OPERATE_PRIVILEGES_RELATE);
	}

	/**
	 * 删除用户独立授权权限
	 * 
	 * @param params
	 */
	public boolean deleteUserOperatPrivileges(Map<String, Object> params, String ddBB) {
		return delete(params, ddBB + TableConstants.SEPARATE + TableConstants.USER_OPERATE_PRIVILEGES_RELATE) > 0;
	}

	/**
	 * 查询用户的权限，独立授权
	 * 
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> queryUserOperatPrivilegesByUser(String userId, String ddBB) {
		return authDao.queryUserOperatPrivilegesByUserTab(userId,
				ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_PRIVILEGES,
				ddBB + TableConstants.SEPARATE + TableConstants.USER_OPERATE_PRIVILEGES_RELATE);
	}

	/**
	 * 查询用户的权限，全部
	 * 
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getUserOperatPrivileges(String userId, String ddBB) {
		return authDao.queryUserOperatPrivilegesTab(userId,
				ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_ROLE_PRIVILEGES_RELATE,
				ddBB + TableConstants.SEPARATE + TableConstants.USER_OPERATE_ROLE_RELATE,
				ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_PRIVILEGES,
				ddBB + TableConstants.SEPARATE + TableConstants.USER_OPERATE_PRIVILEGES_RELATE);
	}

}
