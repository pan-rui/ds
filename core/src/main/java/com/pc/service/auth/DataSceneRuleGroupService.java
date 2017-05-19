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
public class DataSceneRuleGroupService extends BaseService {
	public void addDataSceneRuleGroup(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_SCENE_RULE_GROUP);
	}

	public Page getDataSceneRuleGroupPage(Page<Map<String, Object>> page, String ddBB) {
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.DATA_SCENE_RULE_GROUP);
	}

	public boolean updateDataSceneRuleGroup(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_SCENE_RULE_GROUP) > 0;
	}

	public Map<String, Object> getDataSceneRuleGroup(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.DATA_SCENE_RULE_GROUP);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public List<Map<String, Object>> getDataSceneRuleGroupList(Map<String, Object> params, String ddBB) {
		return queryList(params, null, ddBB + TableConstants.SEPARATE + TableConstants.DATA_SCENE_RULE_GROUP);
	}

	public boolean deleteDataSceneRuleGroup(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_SCENE_RULE_GROUP) > 0;
	}
}
