package com.pc.service.project.impl;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.project.FloorTypeBatchNoteDao;
import com.pc.service.BaseService;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FloorTypeBatchNoteService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private FloorTypeBatchNoteDao floorTypeBatchNoteDao;

    public void addFloorTypeBatchNote(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.FLOOR_TYPE_BATCH_NOTE);
	}

	public Page getFloorTypeBatchNotePage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.FLOOR_TYPE_BATCH_NOTE);

	}

	public boolean updateFloorTypeBatchNote(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.FLOOR_TYPE_BATCH_NOTE) > 0;
	}

	public Map<String, Object> getFloorTypeBatchNote(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.FLOOR_TYPE_BATCH_NOTE);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

        public List<Map<String, Object>> getFloorTypeBatchNoteList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.FLOOR_TYPE_BATCH_NOTE);
		return list;
	}

	public boolean deleteFloorTypeBatchNote(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.FLOOR_TYPE_BATCH_NOTE) > 0;
	}

	public boolean insertFloorBatchNoteBatch(List<Map<String, Object>> floorBatchNoteList, String ddBB) {
		Map<String, Object> paramsMap = new LinkedMap();
		paramsMap.put("ftbnTableName", ddBB + TableConstants.SEPARATE + TableConstants.FLOOR_TYPE_BATCH_NOTE);
		paramsMap.put("list", floorBatchNoteList);

		return floorTypeBatchNoteDao.insertBatch(paramsMap) > 0;
	}
}