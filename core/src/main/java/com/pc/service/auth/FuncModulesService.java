package com.pc.service.auth;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FuncModulesService extends BaseService {

	/**
	 * 添加模块
	 * 
	 * @param params
	 */
	public void addFuncModules(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.FUNC_MODULES);
	}

	/**
	 * 更新模块
	 * 
	 * @param params
	 * @return
	 */
	public boolean updateFuncModules(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.FUNC_MODULES) > 0;
	}

	/**
	 * 获取模块详情
	 * 
	 * @param params
	 * @return
	 */
	public Map<String, Object> getFuncModules(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.FUNC_MODULES);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 删除模块
	 * 
	 * @param params
	 * @return
	 */
	public boolean deleteFuncModules(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.FUNC_MODULES) > 0;
	}

	/**
	 * 获取模块列表
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getFuncModulesList(Map<String, Object> params, String ddBB) {
		return queryList(params, null, ddBB + TableConstants.SEPARATE + TableConstants.FUNC_MODULES);
	}
	
	
	public Page getFuncModulesPage(Page<Map<String, Object>> page, String ddBB) {
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.FUNC_MODULES);
	}
}
