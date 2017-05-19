package com.pc.controller.auth;

import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.auth.UserOperatRoleService;
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

@Controller
@RequestMapping("/admin")
public class UserOperatRoleController extends BaseController {

	@Autowired
	private UserOperatRoleService userOperatRoleService;

	/**
	 * 添加用户操作角色
	 * 
	 * @param userId
	 * @param params
	 * @return
	 */
	@RequestMapping("/userOperatRole/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		userOperatRoleService.addUserOperatRole(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	/**
	 * 删除用户操作角色
	 * 
	 * @param userId
	 * @param params
	 * @return
	 */
	@RequestMapping("/userOperatRole/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		boolean b = userOperatRoleService.deleteUserOperatRole(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/userOperatRole/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		return new BaseResult(ReturnCode.OK,
				userOperatRoleService.getUserOperatRolePrivilegesPage(page, tenantId, ddBB));
	}

	/**
	 * 获取用户操作角色列表
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("/userOperatRole/getRoles")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		return new BaseResult(ReturnCode.OK, userOperatRoleService.getUserOperatRoles(params.getId(), ddBB));
	}

	/**
	 * 获取用户角色权限（只包含来自角色的）
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("/userOperatRole/getUserPrivileges")
	@ResponseBody
	public BaseResult getOperatRolePrivileges(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		return new BaseResult(ReturnCode.OK, userOperatRoleService.getUserOperatRolePrivileges(params.getId(), ddBB));
	}

}
