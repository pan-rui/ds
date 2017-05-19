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
public class UserFuncRoleService extends BaseService {
	@Autowired
	private AuthDao authDao;

	/**
	 * 添加用户角色
	 * 
	 * @param params
	 */
	public void addUserFuncRole(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.USER_FUNC_ROLE_RELATE);
	}

	/**
	 * 删除用户角色
	 * 
	 * @param params
	 * @return
	 */
	public boolean deleteUserFuncRole(Map<String, Object> params, String ddBB) {
		return delete(params, ddBB + TableConstants.SEPARATE + TableConstants.USER_FUNC_ROLE_RELATE) > 0;
	}

	/**
	 * 查询用户角色列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getUserFuncRoles(String userId, String ddBB) {
		Map<String, Object> paramsMap = ParamsMap.newMap("userId", userId)
				.addParams("frTableName", ddBB + TableConstants.SEPARATE + TableConstants.FUNC_ROLE).addParams(
						"ufrrTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER_FUNC_ROLE_RELATE);
		return authDao.queryUserFuncRolesTab(paramsMap);
	}


}
