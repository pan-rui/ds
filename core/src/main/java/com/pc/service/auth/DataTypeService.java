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
public class DataTypeService extends BaseService {
	public void addDataType(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_TYPE);
	}

	public Page getDataTypePage(Page<Map<String, Object>> page, String ddBB) {
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.DATA_TYPE);
	}

	public boolean updateDataType(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_TYPE) > 0;
	}

	public List<Map<String, Object>> getDataTypeList(Map<String, Object> params, String ddBB) {
		return queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.DATA_TYPE);
	}
	
	public Map<String, Object> getDataType(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.DATA_TYPE);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	public boolean deleteDataType(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.DATA_TYPE) > 0;
	}
	
	

}
