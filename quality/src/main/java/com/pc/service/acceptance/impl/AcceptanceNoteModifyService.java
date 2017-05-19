package com.pc.service.acceptance.impl;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AcceptanceNoteModifyService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

     
    public void addAcceptanceNoteModify(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE_MODIFY);
	}

	public Page getAcceptanceNoteModifyPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE_MODIFY);

	}

	public boolean updateAcceptanceNoteModify(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE_MODIFY) > 0;
	}

	public Map<String, Object> getAcceptanceNoteModify(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE_MODIFY);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE_MODIFY,id);
	}
	
    public List<Map<String, Object>> getAcceptanceNoteModifyList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE_MODIFY);
		return list;
	}

	public boolean deleteAcceptanceNoteModify(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.ACCEPTANCE_NOTE_MODIFY) > 0;
	}
}