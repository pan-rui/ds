package com.pc.service.auth;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OperatRoleService extends BaseService {

	/**
	 * 添加操作权限角色
	 * 
	 * @param params
	 */
	public void addOperatRole(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_ROLE);
	}

	/**
	 * 分页获取操作权限角色
	 * 
	 * @param page
	 * @return
	 */
	public Page getOperatRolePage(Page<Map<String, Object>> page, String ddBB) {
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_ROLE);
	}

	/**
	 * 更新操作权限角色
	 * 
	 * @param params
	 * @return
	 */
	public boolean updateOperatRole(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_ROLE) > 0;
	}

	/**
	 * 获取操作权限角色详情
	 * 
	 * @param params
	 * @return
	 */
	public Map<String, Object> getOperatRole(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_ROLE);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public List<Map<String, Object>> getOperatRoleList(Map<String, Object> params, String ddBB) {
		return queryList(params, null, ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_ROLE);
	}

	/**
	 * 删除操作权限角色
	 * 
	 * @param params
	 * @return
	 */
	public boolean deleteOperatRole(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_ROLE) > 0;
	}

}
