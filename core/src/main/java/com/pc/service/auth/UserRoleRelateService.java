package com.pc.service.auth;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.privilege.UserRoleRelateDao;
import com.pc.service.BaseService;

@Service
public class UserRoleRelateService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());
    @Autowired
	private UserRoleRelateDao userRoleRelateDao;
    
    public Page getUserListByRole(Page page, String ddBB) {
    	Map<String, Object> params = new LinkedHashMap<>(page.getParams());
    	if(params.containsKey(TableConstants.User.REAL_NAME.name())){
    		params.put(TableConstants.User.REAL_NAME.name(), "%"+params.get(TableConstants.User.REAL_NAME.name())+"%");
    	}
    	params.put("uTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER);
    	params.put("urrTableName", ddBB + TableConstants.SEPARATE + TableConstants.USER_ROLE_RELATE);
    	params.put("pTableName", ddBB + TableConstants.SEPARATE + TableConstants.POST_INFO);
    	params.put("page", page);
    	page.setResults(userRoleRelateDao.queryUserRoleRelatePageInTab(params));
		return page;
	}
     
    public void addUserRoleRelate(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.USER_ROLE_RELATE);
	}

	public Page getUserRoleRelatePage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.USER_ROLE_RELATE);

	}

	public boolean updateUserRoleRelate(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.USER_ROLE_RELATE) > 0;
	}

	public Map<String, Object> getUserRoleRelate(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.USER_ROLE_RELATE);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.USER_ROLE_RELATE,id);
	}
	
    public List<Map<String, Object>> getUserRoleRelateList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.USER_ROLE_RELATE);
		return list;
	}

	public boolean deleteUserRoleRelate(Map<String, Object> params, String ddBB) {
		return delete(params, ddBB + TableConstants.SEPARATE + TableConstants.USER_ROLE_RELATE) > 0;
	}
}