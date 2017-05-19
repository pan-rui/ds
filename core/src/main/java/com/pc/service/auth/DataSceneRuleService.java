package com.pc.service.auth;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by wady on 2017/3/28.
 */
@Service
public class DataSceneRuleService extends BaseService {
	public void addDataSceneRule(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_SCENE_RULE);
	}

	public Page getDataSceneRulePage(Page<Map<String, Object>> page, String ddBB) {
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.DATA_SCENE_RULE);
	}

	public boolean updateDataSceneRule(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_SCENE_RULE) > 0;
	}

	public Map<String, Object> getDataSceneRule(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.DATA_SCENE_RULE);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public List<Map<String, Object>> getDataSceneRuleList(Map<String, Object> params, String ddBB) {
		return queryList(params, null, ddBB + TableConstants.SEPARATE + TableConstants.DATA_SCENE_RULE);
	}

	public boolean deleteDataSceneRule(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_SCENE_RULE) > 0;
	}
}
