package com.pc.service.user;

import com.pc.core.Page;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.dao.privilege.UserDao;
import com.pc.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService {
	@Autowired
	private UserDao userDao;
	
	public List<Map<String, Object>> getUserListByPostAuth(Map<String, Object> params, String ddBB) {
		params.put("uTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER);
		
		params.put("pTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROCEDURE_INFO);
		
		params.put("pbTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_BUILDING);
		params.put("phTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_HOUSEHOLD);
		
		params.put("rdprTableName", ddBB + TableConstants.SEPARATE + TableConstants.ROLE_DATA_PRIVILEGES_RELATE);
		params.put("dpTableName", ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES_INFO);
		params.put("rTableName", ddBB + TableConstants.SEPARATE + TableConstants.ROLES_INFO);
		params.put("urrTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER_ROLE_RELATE);
		
		return userDao.queryUserListByPostAuthInTab(params);
	}

	public Map<String, Object> getUserByPhone(String phone, String tenantId, String ddBB) {
		List<Map<String, Object>> list = queryList(
				ParamsMap.newMap(TableConstants.User.PHONE.name(), phone).addParams(TableConstants.User.TENANT_ID.name(), tenantId)
						.addParams(TableConstants.User.IS_SEALED.name(), 0),
				null, ddBB + TableConstants.SEPARATE + TableConstants.USER);
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	public Map<String, Object> getUser(Map<String, Object> map, String ddBB) {
		List<Map<String, Object>> list = queryList(map, null, ddBB + TableConstants.SEPARATE + TableConstants.USER);
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.USER,id);
	}

	public boolean updateUser(Map<String, Object> map, String ddBB) {
		return update(map, ddBB + TableConstants.SEPARATE + TableConstants.USER) > 0;
	}

	public Page<Map<String, Object>> getUserPage(Page page, String ddBB) {
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.USER);
	}

	public List<Map<String, Object>> getUserList(Map<String, Object> params, String ddBB) {
		return queryList(params, null, ddBB + TableConstants.SEPARATE + TableConstants.USER);
	}

	public void addUser(Map<String, Object> map, String ddBB) {
		add(map, ddBB + TableConstants.SEPARATE + TableConstants.USER);
	}

	public boolean deleteUser(Map<String, Object> map, String ddBB) {
		return deleteByState(map, ddBB + TableConstants.SEPARATE + TableConstants.USER) > 0;
	}

}
