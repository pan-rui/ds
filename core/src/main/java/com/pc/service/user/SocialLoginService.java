package com.pc.service.user;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SocialLoginService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

     
    public void addSocialLogin(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.SOCIAL_LOGIN);
	}

	public Page getSocialLoginPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.SOCIAL_LOGIN);

	}

	public boolean updateSocialLogin(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.SOCIAL_LOGIN) > 0;
	}


	public Map<String, Object> getSocialLogin(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.SOCIAL_LOGIN);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.SOCIAL_LOGIN,id);
	}
	
    public List<Map<String, Object>> getSocialLoginList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.SOCIAL_LOGIN);
		return list;
	}

	public boolean deleteSocialLogin(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.SOCIAL_LOGIN) > 0;
	}
}