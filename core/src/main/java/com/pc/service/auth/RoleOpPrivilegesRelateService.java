package com.pc.service.auth;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.privilege.RoleOpPrivilegesRelateDao;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoleOpPrivilegesRelateService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());
    @Autowired
	private RoleOpPrivilegesRelateDao roleOpPrivilegesRelateDao;
     
    public void addRoleOpPrivilegesRelate(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.ROLE_OP_PRIVILEGES_RELATE);
	}

	public Page getRoleOpPrivilegesRelatePage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.ROLE_OP_PRIVILEGES_RELATE);

	}

	public boolean updateRoleOpPrivilegesRelate(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.ROLE_OP_PRIVILEGES_RELATE) > 0;
	}

	public Map<String, Object> getRoleOpPrivilegesRelate(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.ROLE_OP_PRIVILEGES_RELATE);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.ROLE_OP_PRIVILEGES_RELATE,id);
	}
	
    public List<Map<String, Object>> getRoleOpPrivilegesRelateList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.ROLE_OP_PRIVILEGES_RELATE);
		return list;
	}

	public boolean deleteRoleOpPrivilegesRelate(Map<String, Object> params, String ddBB) {
		return delete(params, ddBB + TableConstants.SEPARATE + TableConstants.ROLE_OP_PRIVILEGES_RELATE) > 0;
	}
	
	public List<Map<String, Object>> getPrivileges(Map<String, Object> params, String ddBB) {
		params.put("opTableName", ddBB + TableConstants.SEPARATE + TableConstants.OPERATE_PRIVILEGES);
		params.put("roprTableName", ddBB + TableConstants.SEPARATE + TableConstants.ROLE_OP_PRIVILEGES_RELATE);
		return roleOpPrivilegesRelateDao.queryPrivilegesListInTab(params);
	}
	
}