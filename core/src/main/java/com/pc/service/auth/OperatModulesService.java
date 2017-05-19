package com.pc.service.auth;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.privilege.OperateModulesDao;
import com.pc.dao.privilege.OperatePrivilegesDao;
import com.pc.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OperatModulesService extends BaseService {
	
	@Autowired
	private OperateModulesDao operateModulesDao;
	
	public List<Map<String, Object>> getOperateeModulesList(Map<String, Object> params, String ddBB) {
    	params.put("omTableName", ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_MODULES);
    	return operateModulesDao.queryOperateModulesListInTab(params);
	}

	/**
	 * 添加操作权限模块
	 * 
	 * @param params
	 */
	public void addOperatModules(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_MODULES);
	}

	/**
	 * 更新操作权限模块
	 * 
	 * @param params
	 * @return
	 */
	public boolean updateOperatModules(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_MODULES) > 0;
	}

	/**
	 * 获取操作权限模块详情
	 * 
	 * @param params
	 * @return
	 */
	public Map<String, Object> getOperatModules(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_MODULES);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 删除操作权限模块
	 * 
	 * @param params
	 * @return
	 */
	public boolean deleteOperatModules(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_MODULES) > 0;
	}

	/**
	 * 获取操作权限模块列表
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getOperatModulesList(Map<String, Object> params, String ddBB) {
		return queryList(params, null, ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_MODULES);
	}

	public Page getOperatModulesPage(Page<Map<String, Object>> page, String ddBB) {
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_MODULES);
	}
}
