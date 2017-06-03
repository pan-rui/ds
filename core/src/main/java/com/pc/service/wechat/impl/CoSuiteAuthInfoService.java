package com.pc.service.wechat.impl;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CoSuiteAuthInfoService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

     
    public void addCoSuiteAuthInfo(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.CO_SUITE_AUTH_INFO);
	}

	public Page getCoSuiteAuthInfoPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.CO_SUITE_AUTH_INFO);

	}

	public boolean updateCoSuiteAuthInfo(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.CO_SUITE_AUTH_INFO) > 0;
	}

	public Map<String, Object> getCoSuiteAuthInfo(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.CO_SUITE_AUTH_INFO);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

        public List<Map<String, Object>> getCoSuiteAuthInfoList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.CO_SUITE_AUTH_INFO);
		return list;
	}
        public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.CO_SUITE_AUTH_INFO,id);
	}


	public boolean deleteCoSuiteAuthInfo(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.CO_SUITE_AUTH_INFO) > 0;
	}
}