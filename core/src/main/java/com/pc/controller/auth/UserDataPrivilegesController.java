package com.pc.controller.auth;

import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.auth.DataUserPrivilegesRelateService;
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
 * @Description: 用户数据权限
 * @Author: wady (2017-03-28 20:50)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-28
 */
@Controller
@RequestMapping("/admin")
public class UserDataPrivilegesController extends BaseController {
	@Autowired
	private DataUserPrivilegesRelateService dataUserPrivilegesRelateService;

	/**
	 * 获取用户独立权限
	 * 
	 * @param params
	 * @param ddBB
	 * @return
	 */
	@RequestMapping("/userDataPrivileges/getUserPrivileges")
	@ResponseBody
	public BaseResult getPrivilegesByUser(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		return new BaseResult(ReturnCode.OK,
				dataUserPrivilegesRelateService.queryUserDataPrivilegesByUser(params.getId(), ddBB));
	}

	@RequestMapping("/userDataPrivileges/add")
	@ResponseBody
	public BaseResult addUserDataPrivileges(@RequestAttribute(Constants.USER_ID) String userId,
			@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		map.put(TableConstants.CREATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.CREATE_USER_ID, userId);
		dataUserPrivilegesRelateService.addDataUserPrivilegesRelate(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/userDataPrivileges/delete")
	@ResponseBody
	public BaseResult deleteUserDataPrivileges(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		boolean b = dataUserPrivilegesRelateService.deleteDataUserPrivilegesRelate(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/userDataPrivileges/getPage")
	@ResponseBody
	public BaseResult getUserDataPrivilegesPage(@RequestHeader(Constants.TENANT_ID) String tenantId,
			@EncryptProcess Page page, @RequestAttribute String ddBB) {
		return new BaseResult(ReturnCode.OK,
				dataUserPrivilegesRelateService.getUserDataPrivilegesPage(tenantId, page, ddBB));
	}
}
