package com.pc.service.auth;

import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.dao.auth.AuthDao;
import com.pc.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class FuncRolePrivilegesService extends BaseService {
	@Autowired
	private AuthDao authDao;

	/**
	 * 查询角色权限
	 * 
	 * @param orid
	 * @return
	 */
	public List<Map<String, Object>> getFuncRolePrivileges(String orid, String ddBB) {
		Map<String, Object> paramsMap = ParamsMap.newMap("funcRoleId", orid)
				.addParams("fpTableName", ddBB + TableConstants.SEPARATE + TableConstants.FUNC_PRIVILEGES).addParams(
						"frprTableName", ddBB + TableConstants.SEPARATE + TableConstants.FUNC_ROLE_PRIVILEGES_RELATE);
		return authDao.queryFuncRolePrivilegesTab(paramsMap);

	}

	/**
	 * 添加角色权限
	 * 
	 * @param params
	 */
	public void addFuncRolePrivileges(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.FUNC_ROLE_PRIVILEGES_RELATE);
	}

	/**
	 * 删除角色权限
	 * 
	 * @param params
	 * @return
	 */
	public boolean deleteFuncRolePrivileges(Map<String, Object> params, String ddBB) {
		return delete(params, ddBB + TableConstants.SEPARATE + TableConstants.FUNC_ROLE_PRIVILEGES_RELATE) > 0;
	}

	public void addFuncRolePrivilegesList(List<Map<String, Object>> list, String ddBB) {
		if (list.size() > 0) {
			deleteFuncRolePrivileges(ParamsMap.newMap(TableConstants.FuncRolePrivilegesRelate.FUNC_ROLE_ID.name(),
					list.get(0).get(TableConstants.FuncRolePrivilegesRelate.FUNC_ROLE_ID)), ddBB);
			for (Map<String, Object> map : list) {
				Map<String, Object> obj = new LinkedHashMap<>();
				obj.put(TableConstants.FuncRolePrivilegesRelate.FUNC_ROLE_ID.name(), map.get("funcRoleId"));
				obj.put(TableConstants.FuncRolePrivilegesRelate.FUNC_PRIVILEGE_ID.name(), map.get("funcPrivilegeId"));
				addFuncRolePrivileges(obj, ddBB);
			}
		}
	}

}
