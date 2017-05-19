package com.pc.service.auth;

import com.pc.core.Page;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.dao.auth.AuthDao;
import com.pc.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by wady on 2017/3/28.
 */
@Service
public class DataRolePrivilegesRelateService extends BaseService {
	@Autowired
	private AuthDao authDao;

	public void addDataRolePrivilegesRelate(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_ROLE_PRIVILEGES_RELATE);
	}

	public Page getDataRolePrivilegesRelatePage(Page<Map<String, Object>> page, String ddBB) {
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.DATA_ROLE_PRIVILEGES_RELATE);
	}

	public boolean updateDataRolePrivilegesRelate(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_ROLE_PRIVILEGES_RELATE) > 0;
	}

	public List<Map<String, Object>> getDataRolePrivilegesRelate(Map<String, Object> params, String ddBB) {
		return queryList(params, null, ddBB + TableConstants.SEPARATE + TableConstants.DATA_ROLE_PRIVILEGES_RELATE);
	}

	public boolean deleteDataRolePrivilegesRelate(Map<String, Object> params, String ddBB) {
		return delete(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_ROLE_PRIVILEGES_RELATE) > 0;
	}

	/**
	 * 获取角色权限
	 * 
	 * @param orid
	 * @param ddBB
	 * @return
	 */
	public List<Map<String, Object>> getDataRolePrivileges(String drid, String ddBB) {
		return authDao.queryDataRolePrivilegesTab(drid,
				ddBB + TableConstants.SEPARATE + TableConstants.DATA_ROLE_PRIVILEGES_RELATE,
				ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES);
	}

	public Page getOperatRolePrivilegesPage(String tenantId, Page page, String ddBB) {
		Map<String, Object> paramsMap = ParamsMap.newMap("page", page).addParams("tenantId", tenantId)
				.addParams("drTableName", ddBB + TableConstants.SEPARATE + TableConstants.DATA_ROLE)
				.addParams("dpTableName", ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES)
				.addParams("dsTableName", ddBB + TableConstants.SEPARATE + TableConstants.DATA_SCENE)
				.addParams("dtTableName", ddBB + TableConstants.SEPARATE + TableConstants.DATA_TYPE).addParams(
						"drprTableName", ddBB + TableConstants.SEPARATE + TableConstants.DATA_ROLE_PRIVILEGES_RELATE);
		page.setResults(authDao.queryDataPrivilegesRolePageInTab(paramsMap));
		return page;
	}

}
