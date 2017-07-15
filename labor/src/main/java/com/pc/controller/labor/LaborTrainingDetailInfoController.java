package com.pc.controller.labor;

 
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.pc.util.ExcelUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
 
import com.pc.service.labor.impl.LaborTrainingDetailInfoService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 
 * @Author: wady (2017-03-28 20:19)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-28
 */
@Controller
@RequestMapping("/admin")
public class LaborTrainingDetailInfoController extends BaseController {
	@Autowired
	private LaborTrainingDetailInfoService laborTrainingDetailInfoService;
	@Autowired
	private ExcelUtils excelUtils;

	private Logger logger = LogManager.getLogger(this.getClass());
	
	
	@RequestMapping("/laborTrainingDetailInfo/getDetailPage")
	@ResponseBody
	public BaseResult getDetailPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, laborTrainingDetailInfoService.getLaborTrainingDetailPage(page, ddBB));
	}
	
	@RequestMapping("/laborTrainingDetailInfo/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		
                Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		
		map.put(TableConstants.IS_SEALED, 0); 
		laborTrainingDetailInfoService.addLaborTrainingDetailInfo(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/laborTrainingDetailInfo/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = laborTrainingDetailInfoService.deleteLaborTrainingDetailInfo(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/laborTrainingDetailInfo/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = laborTrainingDetailInfoService.updateLaborTrainingDetailInfo(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/laborTrainingDetailInfo/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, laborTrainingDetailInfoService.getLaborTrainingDetailInfo(map, ddBB));
	}

	@RequestMapping("/laborTrainingDetailInfo/getList")
	@ResponseBody
	public BaseResult getList(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, laborTrainingDetailInfoService.getLaborTrainingDetailInfoList(map, ddBB));
	}

	@RequestMapping("/laborTrainingDetailInfo/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, laborTrainingDetailInfoService.getLaborTrainingDetailInfoPage(page, ddBB));
	}
	@RequestMapping(value = "/laborTrainingDetailInfo/import",method = RequestMethod.POST)
	@ResponseBody
	public BaseResult importData(HttpServletRequest request, @RequestAttribute String ddBB, @RequestHeader(Constants.TENANT_ID) String tenantId, @RequestAttribute(Constants.USER_ID) String userId, @RequestParam String projectId, MultipartFile file) {
		List fields = Arrays.asList("","","EDU_COURSENAME","EDU_ORGANIZATION","EDU_TEACHER","EDU_ADDR","","EDU_CLASSHOUR","EDU_CONTENT");
		try {
			excelUtils.importTraining(file,ddBB+TableConstants.SEPARATE+TableConstants.LABOR_TRAINING_INFO,fields,tenantId,userId,projectId);
		} catch (IOException e) {
			e.printStackTrace();
			return new BaseResult(1, e.getMessage());
		}
		return new BaseResult(0, "OK");
	}
}
