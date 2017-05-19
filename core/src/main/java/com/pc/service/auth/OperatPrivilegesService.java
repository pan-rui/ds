package com.pc.service.auth;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.privilege.OperatePrivilegesDao;
import com.pc.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OperatPrivilegesService extends BaseService {
	
	@Autowired
	private OperatePrivilegesDao operatePrivilegesDao;
	
	public List<Map<String, Object>> getOperatePrivilegesList(Map<String, Object> params, String ddBB) {
    	params.put("opTableName", ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_PRIVILEGES);
    	return operatePrivilegesDao.queryOperatePrivilegesListInTab(params);
	}

	/**
	 * 添加操作权限资源
	 * 
	 * @param params
	 */
	public void addOperatPrivilege(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_PRIVILEGES);
	}

	/**
	 * 分页获取操作权限资源
	 * 
	 * @param page
	 * @return
	 */
	public Page getOperatPrivilegePage(Page<Map<String, Object>> page, String ddBB) {
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_PRIVILEGES);
	}

	/**
	 * 更新操作资源权限
	 * 
	 * @param params
	 * @return
	 */
	public boolean updateOperatPrivilege(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_PRIVILEGES) > 0;
	}

	/**
	 * 获取操作资源权限列表
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getOperatPrivileges(Map<String, Object> params, String ddBB) {
		return queryList(params, null, ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_PRIVILEGES);
	}

	/**
	 * 获取操作资源权限详情
	 * 
	 * @param params
	 * @return
	 */
	public Map<String, Object> getOperatPrivilege(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = getOperatPrivileges(params, ddBB);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 删除操作资源权限
	 * 
	 * @param params
	 * @return
	 */
	public boolean deleteOperatPrivilege(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_PRIVILEGES) > 0;
	}

}
