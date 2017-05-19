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
public class OperatRolePrivilegesService extends BaseService {
	@Autowired
	private AuthDao authDao;

	/**
	 * 查询角色权限
	 * 
	 * @param orid
	 * @return
	 */
	public List<Map<String, Object>> getOperatRolePrivileges(String orid, String ddBB) {
		return authDao.queryOperatRolePrivilegesTab(orid, ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_ROLE,
				ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_ROLE_PRIVILEGES_RELATE,
				ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_PRIVILEGES);
	}

	/**
	 * 添加角色权限
	 * 
	 * @param params
	 */
	public void addOperatRolePrivileges(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_ROLE_PRIVILEGES_RELATE);
	}

	/**
	 * 删除角色权限
	 * 
	 * @param params
	 * @return
	 */
	public boolean deleteOperatRolePrivileges(Map<String, Object> params, String ddBB) {
		return delete(params, ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_ROLE_PRIVILEGES_RELATE) > 0;
	}

	public void addOperatRolePrivilegesList(List<Map<String, Object>> list, String ddBB) {
		if (list.size() > 0) {
			deleteOperatRolePrivileges(
					ParamsMap.newMap(TableConstants.OperateRolePrivilegesRelate.OPERATE_ROLE_ID.name(),
							list.get(0).get(TableConstants.OperateRolePrivilegesRelate.OPERATE_ROLE_ID)),
					ddBB);
			for (Map<String, Object> map : list) {
				Map<String, Object> obj = new LinkedHashMap<>();
				obj.put(TableConstants.OperateRolePrivilegesRelate.OPERATE_ROLE_ID.name(), map.get("operateRoleId"));
				obj.put(TableConstants.OperateRolePrivilegesRelate.OPERATE_PRIVILEGE_ID.name(),
						map.get("operatePrivilegeId"));
				addOperatRolePrivileges(obj, ddBB);
			}
		}
	}

}
