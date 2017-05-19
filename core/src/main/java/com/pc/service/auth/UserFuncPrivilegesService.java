package com.pc.service.auth;

import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.dao.auth.AuthDao;
import com.pc.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserFuncPrivilegesService extends BaseService {
	@Autowired
	private AuthDao authDao;

	/**
	 * 添加用户权限，独立授权
	 * 
	 * @param params
	 */
	public void addUserFuncPrivileges(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.USER_FUNC_PRIVILEGES_RELATE);
	}

	/**
	 * 删除用户独立授权权限
	 * 
	 * @param params
	 */
	public boolean deleteUserFuncPrivileges(Map<String, Object> params, String ddBB) {
		return delete(params, ddBB + TableConstants.SEPARATE + TableConstants.USER_FUNC_PRIVILEGES_RELATE) > 0;
	}

	/**
	 * 查询用户的权限，独立授权
	 * 
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> queryUserFuncPrivilegesByUser(String userId, String ddBB) {
		Map<String, Object> paramsMap = ParamsMap.newMap("userId", userId)
				.addParams("fpTableName", ddBB + TableConstants.SEPARATE + TableConstants.FUNC_PRIVILEGES).addParams(
						"ufprTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER_FUNC_PRIVILEGES_RELATE);
		return authDao.queryUserFuncPrivilegesByUserTab(paramsMap);
	}

}
