package com.pc.controller.auth;

import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.auth.DataUserRoleRelateService;
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
 * @Description: 用户数据角色
 * @Author: wady (2017-03-28 20:43)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-28
 */
@Controller
@RequestMapping("/admin")
public class UserDataRoleController extends BaseController {
	@Autowired
	private DataUserRoleRelateService dataUserRoleService;

	@RequestMapping("/userDataRole/getUserRoles")
	@ResponseBody
	public BaseResult getRolesByUser(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		return new BaseResult(ReturnCode.OK, dataUserRoleService.getUserDataRoles(params.getId(), ddBB));
	}

	@RequestMapping("/userDataRole/add")
	@ResponseBody
	public BaseResult addUserDataRoles(@RequestAttribute(Constants.USER_ID) String userId,
			@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		dataUserRoleService.addDataUserRoleRelate(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/userDataRole/delete")
	@ResponseBody
	public BaseResult deleteUserDataRoles(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		boolean b = dataUserRoleService.deleteDataUserRoleRelate(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/userDataRole/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		return new BaseResult(ReturnCode.OK, dataUserRoleService.getUserDataRolePrivilegesPage(page, tenantId, ddBB));
	}

}
