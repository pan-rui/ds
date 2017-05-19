package com.pc.service.auth;

import com.pc.core.Page;
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
public class DataPrivilegesService extends BaseService {

	@Autowired
	private AuthDao authDao;

    public void addDataPrivileges(Map<String, Object> params) {
        add(params, TableConstants.DATA_PRIVILEGES);
    }

	public void addDataPrivileges(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES);
	}

	public Page getDataPrivilegesPage(Page<Map<String, Object>> page, String ddBB) {
		page.setResults(authDao.queryDataPrivilegesTypePageInTab(page, ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES, ddBB + TableConstants.SEPARATE + TableConstants.DATA_TYPE));
		return page;
		//		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES);
	}

	public boolean updateDataPrivileges(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES) > 0;
	}

	public Map<String, Object> getDataPrivileges(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public List<Map<String, Object>> getDataPrivilegesList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES);
		return list;
	}

	public boolean deleteDataPrivileges(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_PRIVILEGES) > 0;
	}
    public Page getDataPrivilegesPage(Page<Map<String, Object>> page) {
        return queryPage(page, TableConstants.DATA_PRIVILEGES);
    }
    /**
     * 获取数据权限
     * @param params 参数
     * @return Map 获取单条数据
     */
    public boolean updateDataPrivileges(Map<String, Object> params) {
        return update(params, TableConstants.DATA_PRIVILEGES) > 0;
    }
    /**
     * 获取数据权限
     * @param params 参数
     * @return Map 获取单条数据
     */
    public Map<String, Object> getDataPrivileges(Map<String, Object> params) {
        List<Map<String, Object>> list = queryList(params, null, TableConstants.DATA_PRIVILEGES);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    /**
     * 逻辑删除数据权限
     * @param params 参数
     * @return true:逻辑删除成功，0：逻辑删除失败
     */
    public boolean deleteDataPrivileges(Map<String, Object> params) {
        return deleteByState(params, TableConstants.DATA_PRIVILEGES) > 0;
    }
}
