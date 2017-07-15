package com.pc.controller.labor;

 
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pc.controller.BaseController;
import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.core.Page;
import com.pc.core.ParamsMap;
import com.pc.util.DateUtil;
import com.pc.vo.ParamsVo;

import com.pc.core.TableConstants;
import com.pc.service.labor.impl.LaborPersonInfoService;
import com.pc.service.labor.impl.LaborProjectPersonInfoService;
import com.pc.service.project.impl.ProjectPeriodService;

/**
 * @Description: 
 * @Author: wady (2017-03-28 20:19)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-28
 */
@Controller
@RequestMapping("/admin")
public class LaborProjectPersonInfoController extends BaseController {
	@Autowired
	private LaborProjectPersonInfoService laborProjectPersonInfoService;
	
	@Autowired
	private LaborPersonInfoService laborPersonInfoService;
	
	@Autowired
	private ProjectPeriodService projectPeriodService;

	private Logger logger = LogManager.getLogger(this.getClass());
	
	
	@RequestMapping("/laborProjectPersonInfo/getDetailPage")
	@ResponseBody
	public BaseResult getDetailPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, laborProjectPersonInfoService.getLaborProjectPersonInfoDetailPage(page,ddBB));
	}
	
	@RequestMapping("/laborProjectPersonInfo/getLast")
	@ResponseBody
	public BaseResult getLast(@RequestAttribute(Constants.USER_ID) String userId, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put(TableConstants.LaborPersonInfo.USER_ID.name(), userId);
		Map<String, Object> laborPersonInfo=laborPersonInfoService.getLaborPersonInfo(map, ddBB);
    	if(laborPersonInfo==null){
    		return  new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
    	}
		
    	List<Map<String, Object>> list=laborProjectPersonInfoService.getLaborProjectList(ddBB, (String) laborPersonInfo.get(TableConstants.LaborPersonInfo.id.name()));
    	
		return new BaseResult(ReturnCode.OK, list);
	}
	
	@RequestMapping("/laborProjectPersonInfo/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		
        Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		
		map.put(TableConstants.IS_SEALED, 0); 
		laborProjectPersonInfoService.addLaborProjectPersonInfo(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/laborProjectPersonInfo/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = laborProjectPersonInfoService.deleteLaborProjectPersonInfo(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/laborProjectPersonInfo/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = laborProjectPersonInfoService.updateLaborProjectPersonInfo(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/laborProjectPersonInfo/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, laborProjectPersonInfoService.getLaborProjectPersonInfo(map, ddBB));
	}

	@RequestMapping("/laborProjectPersonInfo/getList")
	@ResponseBody
	public BaseResult getList(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, laborProjectPersonInfoService.getLaborProjectPersonInfoList(map, ddBB));
	}

	@RequestMapping("/laborProjectPersonInfo/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, laborProjectPersonInfoService.getLaborProjectPersonInfoPage(page, ddBB));
	}
}
