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
public class DataUserPrivilegesRelateService extends BaseService {
	@Autowired
	private AuthDao authDao;

	public void addDataUserPrivilegesRelate(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.USER_DATA_PRIVILEGES_RELATE);
	}

	public Page getDataUserPrivilegesRelatePage(Page<Map<String, Object>> page, String ddBB) {
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.USER_DATA_PRIVILEGES_RELATE);
	}

	public boolean updateDataUserPrivilegesRelate(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.USER_DATA_PRIVILEGES_RELATE) > 0;
	}

	public List<Map<String, Object>> getDataUserPrivilegesRelate(Map<String, Object> params, String ddBB) {
		return queryList(params, null, ddBB + TableConstants.SEPARATE + TableConstants.USER_DATA_PRIVILEGES_RELATE);
	}

	public boolean deleteDataUserPrivilegesRelate(Map<String, Object> params, String ddBB) {
		return delete(params, ddBB + TableConstants.SEPARATE + TableConstants.USER_DATA_PRIVILEGES_RELATE) > 0;
	}

	/**
	 * 获取用户独立授权
	 * 
	 * @param userId
	 * @param ddBB
	 * @return
	 */
	public List<Map<String, Object>> queryUserDataPrivilegesByUser(String userId, String ddBB) {
		return authDao.queryUserDataPrivilegesByUserTab(userId,
				ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES,
				ddBB + TableConstants.SEPARATE + TableConstants.USER_DATA_PRIVILEGES_RELATE);
	}

	public Page getUserDataPrivilegesPage(String tenantId, Page page, String ddBB) {
		Map<String, Object> paramsMap = ParamsMap.newMap("page", page).addParams("tenantId", tenantId)
				.addParams("uTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER)
				.addParams("dpTableName", ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES)
				.addParams("dsTableName", ddBB + TableConstants.SEPARATE + TableConstants.DATA_SCENE)
				.addParams("dtTableName", ddBB + TableConstants.SEPARATE + TableConstants.DATA_TYPE).addParams(
						"udprTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER_DATA_PRIVILEGES_RELATE);
		page.setResults(authDao.queryDataPrivilegesUserPageInTab(paramsMap));
		return page;
	}

}
