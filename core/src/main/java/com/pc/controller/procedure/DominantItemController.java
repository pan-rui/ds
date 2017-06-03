package com.pc.controller.procedure;


import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.Page;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.service.procedure.impl.DominantItemService;
import com.pc.util.DateUtil;
import com.pc.util.ExcelUtils;
import com.pc.vo.ParamsVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
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
public class DominantItemController extends BaseController {
	@Autowired
	private DominantItemService dominantItemService;
	@Autowired
	private ExcelUtils excelUtils;
	private Logger logger = LogManager.getLogger(this.getClass());
	
	@RequestMapping("/dominantItem/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		
                Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		map.put(TableConstants.IS_SEALED, 0); 
		dominantItemService.addDominantItem(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/dominantItem/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = dominantItemService.deleteDominantItem(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/dominantItem/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = dominantItemService.updateDominantItem(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/dominantItem/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, dominantItemService.getDominantItem(map, ddBB));
	}

	@RequestMapping("/dominantItem/getList")
	@ResponseBody
	public BaseResult getList(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		return new BaseResult(ReturnCode.OK, dominantItemService.getDominantItemList(map, ddBB));
	}

	@RequestMapping("/dominantItem/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, dominantItemService.getDominantItemPage(page, ddBB));
	}

	@RequestMapping(value = "/dominant/load", method = RequestMethod.POST)
	@ResponseBody
	public BaseResult importItem(@RequestHeader(Constants.TENANT_ID) String tenantId,@RequestAttribute(Constants.USER_ID) String userId,@RequestAttribute String ddBB, MultipartFile file) {			//尚未导入CALC_TYPE
		try {
			ParamsMap paramsMap=ParamsMap.newMap("主控",ddBB+TableConstants.SEPARATE+TableConstants.DOMINANT_ITEM)
					.addParams("一般",ddBB+TableConstants.SEPARATE+TableConstants.GENERAL_ITEM);
			List<String> fields=Arrays.<String>asList( "ITEM_CODE","GRADING_STANDARD","ERROR_RANGE","PASS_TEXT","NO_PASS_TEXT","REMARK");
			if(!file.getOriginalFilename().contains("验收项")) return new BaseResult(350,"文字名可能不包含\"验收项\"等字,请选择正确的文件及文件格式,否则可能导致异常");
			excelUtils.importItem(paramsMap,ddBB+TableConstants.SEPARATE+ TableConstants.PROCEDURE_INFO, "PROCEDURE_ID", fields, file,tenantId,userId);
		} catch (IOException e) {
			e.printStackTrace();
			return new BaseResult(1, e.getMessage());
		}
		return new BaseResult(0, "OK");
	}
}
