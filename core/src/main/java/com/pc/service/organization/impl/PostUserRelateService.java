package com.pc.service.organization.impl;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PostUserRelateService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

     
    public void addPostUserRelate(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.POST_USER_RELATE);
	}

	public Page getPostUserRelatePage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.POST_USER_RELATE);

	}

	public boolean updatePostUserRelate(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.POST_USER_RELATE) > 0;
	}

	public Map<String, Object> getPostUserRelate(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.POST_USER_RELATE);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.POST_USER_RELATE,id);
	}

    public List<Map<String, Object>> getPostUserRelateList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.POST_USER_RELATE);
		return list;
	}

	public boolean deletePostUserRelate(Map<String, Object> params, String ddBB) {
		return delete(params, ddBB + TableConstants.SEPARATE + TableConstants.POST_USER_RELATE) > 0;
	}
}