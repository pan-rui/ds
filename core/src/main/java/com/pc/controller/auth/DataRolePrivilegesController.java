package com.pc.controller.auth;

import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.auth.DataRolePrivilegesRelateService;
import com.pc.util.DateUtil;
import com.pc.vo.ParamsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description: 数据角色权限
 * @Author: wady (2017-03-28 20:52)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-28
 */
@Controller
@RequestMapping("/admin")
public class DataRolePrivilegesController extends BaseController {
	@Autowired
	private DataRolePrivilegesRelateService dataRolePrivilegesRelateService;


	@RequestMapping("/dataRolePrivileges/getRolePrivileges")
	@ResponseBody
	public BaseResult getPrivilegesByRole(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		return new BaseResult(ReturnCode.OK,
				dataRolePrivilegesRelateService.getDataRolePrivileges(params.getId(), ddBB));
	}

	@RequestMapping("/dataRolePrivileges/add")
	@ResponseBody
	public BaseResult addDataRolePrivileges(@RequestAttribute(Constants.USER_ID) String userId,
			@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		dataRolePrivilegesRelateService.addDataRolePrivilegesRelate(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/dataRolePrivileges/delete")
	@ResponseBody
	public BaseResult deleteDataRolePrivileges(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		boolean b = dataRolePrivilegesRelateService.deleteDataRolePrivilegesRelate(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/dataRolePrivileges/getPage")
	@ResponseBody
	public BaseResult getOperatRolePrivilegesPage(@RequestHeader(Constants.TENANT_ID) String tenantId,
			@EncryptProcess Page page, @RequestAttribute String ddBB) {
		return new BaseResult(ReturnCode.OK,
				dataRolePrivilegesRelateService.getOperatRolePrivilegesPage(tenantId, page, ddBB));
	}
}
