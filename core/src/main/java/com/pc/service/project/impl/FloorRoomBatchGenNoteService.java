package com.pc.service.project.impl;

import java.util.List;
import java.util.Map;
 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.project.FloorRoomBatchGenNoteDao;
import com.pc.service.BaseService;

@Service
public class FloorRoomBatchGenNoteService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

     
    public void addFloorRoomBatchGenNote(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.FLOOR_ROOM_BATCH_GEN_NOTE);
	}

	public Page getFloorRoomBatchGenNotePage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.FLOOR_ROOM_BATCH_GEN_NOTE);

	}

	public boolean updateFloorRoomBatchGenNote(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.FLOOR_ROOM_BATCH_GEN_NOTE) > 0;
	}

	public Map<String, Object> getFloorRoomBatchGenNote(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.FLOOR_ROOM_BATCH_GEN_NOTE);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

        public List<Map<String, Object>> getFloorRoomBatchGenNoteList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.FLOOR_ROOM_BATCH_GEN_NOTE);
		return list;
	}

	public boolean deleteFloorRoomBatchGenNote(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.FLOOR_ROOM_BATCH_GEN_NOTE) > 0;
	}
}