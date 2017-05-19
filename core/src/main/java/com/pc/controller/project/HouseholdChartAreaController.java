package com.pc.controller.project;


import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.project.impl.HouseholdChartAreaService;
import com.pc.service.project.impl.UnitChartService;
import com.pc.util.DateUtil;
import com.pc.vo.ParamsVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 
 * @Author: wady (2017-03-28 20:19)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-28
 */
@Controller
@RequestMapping("/admin")
public class HouseholdChartAreaController extends BaseController {
	@Autowired
	private HouseholdChartAreaService householdChartAreaService;
	
	@Autowired
	private UnitChartService unitChartService;

	private Logger logger = LogManager.getLogger(this.getClass());
	
	@RequestMapping("/householdChartArea/getChartAreaList")
	@ResponseBody
	public BaseResult getChartAreaList(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		return new BaseResult(ReturnCode.OK, householdChartAreaService.getHouseholdChartAreaList(map, ddBB));
	}
	
	@RequestMapping("/householdChartArea/modChartAreaList")
	@ResponseBody
	public BaseResult addList(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		
		List<Map<String, Object>> householdChartAreaList=(List<Map<String, Object>>) params.getParams().get(TableConstants.HOUSEHOLD_CHART_AREA);
		if(householdChartAreaList==null||householdChartAreaList.size()==0){
			return new BaseResult(ReturnCode.OK);
		}
		
		String householdChartId=(String) householdChartAreaList.get(0).get(TableConstants.HouseholdChartArea.HOUSEHOLD_CHART_ID.name());
		
		Map<String, Object> delMap = new LinkedHashMap<>();
		delMap.put(TableConstants.HouseholdChartArea.HOUSEHOLD_CHART_ID.name(), householdChartId);
		householdChartAreaService.deleteHouseholdChartArea(delMap, ddBB);
		
		for(Map<String, Object> householdChartArea:householdChartAreaList){
			householdChartArea.put(TableConstants.TENANT_ID, tenantId);
			householdChartArea.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
			householdChartArea.put(TableConstants.UPDATE_USER_ID, userId);
			householdChartArea.put(TableConstants.IS_SEALED, 0);
			householdChartAreaService.addHouseholdChartArea(householdChartArea, ddBB);
		}
		
		return new BaseResult(ReturnCode.OK);
	}
	
	@RequestMapping("/householdChartArea/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		
                Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		map.put(TableConstants.IS_SEALED, 0); 
		householdChartAreaService.addHouseholdChartArea(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/householdChartArea/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = householdChartAreaService.deleteHouseholdChartArea(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/householdChartArea/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = householdChartAreaService.updateHouseholdChartArea(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/householdChartArea/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, householdChartAreaService.getHouseholdChartArea(map, ddBB));
	}

	@RequestMapping("/householdChartArea/getList")
	@ResponseBody
	public BaseResult getList(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		return new BaseResult(ReturnCode.OK, householdChartAreaService.getHouseholdChartAreaList(map, ddBB));
	}

	@RequestMapping("/householdChartArea/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, householdChartAreaService.getHouseholdChartAreaPage(page, ddBB));
	}
}
