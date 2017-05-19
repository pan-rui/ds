package com.pc.service.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.privilege.RoleDataPrivilegesRelateDao;
import com.pc.service.BaseService;

@Service
public class RoleDataPrivilegesRelateService extends BaseService {
	private Logger logger = LogManager.getLogger(this.getClass());
	@Autowired
	private RoleDataPrivilegesRelateDao roleDataPrivilegesRelateDao;

	public void addRoleDataPrivilegesRelateList(List<Map<String, Object>> list, String ddBB) {
		Map<String, Object> map = new HashMap<>();
		map.put("list", list);
		map.put("rdprTableName", ddBB + TableConstants.SEPARATE + TableConstants.ROLE_DATA_PRIVILEGES_RELATE);
		roleDataPrivilegesRelateDao.insertList(map);
	}

	public List<Map<String, Object>> getDataPrivilegesList(Map<String, Object> params, String ddBB) {
		params.put("rdprTableName", ddBB + TableConstants.SEPARATE + TableConstants.ROLE_DATA_PRIVILEGES_RELATE);
		params.put("dpTableName", ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES_INFO);
		return roleDataPrivilegesRelateDao.queryDataPrivilegesListInTab(params);
	}

	public void addRoleDataPrivilegesRelate(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.ROLE_DATA_PRIVILEGES_RELATE);
	}

	public Page getRoleDataPrivilegesRelatePage(Page<Map<String, Object>> page, String ddBB) {

		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.ROLE_DATA_PRIVILEGES_RELATE);

	}

	public boolean updateRoleDataPrivilegesRelate(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.ROLE_DATA_PRIVILEGES_RELATE) > 0;
	}

	public Map<String, Object> getRoleDataPrivilegesRelate(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.ROLE_DATA_PRIVILEGES_RELATE);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.ROLE_DATA_PRIVILEGES_RELATE, id);
	}

	public List<Map<String, Object>> getRoleDataPrivilegesRelateList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.ROLE_DATA_PRIVILEGES_RELATE);
		return list;
	}

	public boolean deleteRoleDataPrivilegesRelate(Map<String, Object> params, String ddBB) {
		return delete(params, ddBB + TableConstants.SEPARATE + TableConstants.ROLE_DATA_PRIVILEGES_RELATE) > 0;
	}
	
	public int deleteRoleDataPrivilegesRelateList(Map<String, Object> params, String ddBB) {
		params.put("dpTableName", ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES_INFO);
		params.put("rdprTableName", ddBB + TableConstants.SEPARATE + TableConstants.ROLE_DATA_PRIVILEGES_RELATE);
		return roleDataPrivilegesRelateDao.deleteList(params);
	}
}