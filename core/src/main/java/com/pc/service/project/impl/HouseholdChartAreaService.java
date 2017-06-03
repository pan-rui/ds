package com.pc.service.project.impl;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.project.HouseholdChartAreaDao;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HouseholdChartAreaService extends BaseService {
	private Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private HouseholdChartAreaDao householdChartAreaDao;

	public List<Map<String, Object>> getHouseholdAreaInfoByChart(Map<String, Object> params, String ddBB) {
		params.put("hcaTableName", ddBB + TableConstants.SEPARATE + TableConstants.HOUSEHOLD_CHART_AREA);
		params.put("hcatTableName", ddBB + TableConstants.SEPARATE + TableConstants.HOUSEHOLD_CHART_AREA_TYPE);
		return householdChartAreaDao.queryHouseholdAreaInfoByChartInTab(params);
	}

	public void addHouseholdChartArea(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.HOUSEHOLD_CHART_AREA);
	}

	public Page getHouseholdChartAreaPage(Page<Map<String, Object>> page, String ddBB) {

		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.HOUSEHOLD_CHART_AREA);

	}

	public boolean updateHouseholdChartArea(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.HOUSEHOLD_CHART_AREA) > 0;
	}

	public Map<String, Object> getHouseholdChartArea(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.HOUSEHOLD_CHART_AREA);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.HOUSEHOLD_CHART_AREA, id);
	}

	public List<Map<String, Object>> getHouseholdChartAreaList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.HOUSEHOLD_CHART_AREA);
		return list;
	}

	public boolean deleteHouseholdChartArea(Map<String, Object> params, String ddBB) {
		return delete(params, ddBB + TableConstants.SEPARATE + TableConstants.HOUSEHOLD_CHART_AREA) > 0;
	}
}