package com.pc.controller.auth;

import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.TableConstants;
import com.pc.service.auth.UserOperatPrivilegesService;
import com.pc.util.DateUtil;
import com.pc.vo.ParamsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class UserOperatPrivilegesController extends BaseController {

	@Autowired
	private UserOperatPrivilegesService userOperatPrivilegesService;

	/**
	 * 获取用户独立操作权限
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("/userOperatPrivileges/getUserPrivileges")
	@ResponseBody
	public BaseResult getByUser(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		return new BaseResult(ReturnCode.OK,
				userOperatPrivilegesService.queryUserOperatPrivilegesByUser(params.getId(), ddBB));
	}

	/**
	 * 添加用户独立操作权限
	 * 
	 * @param userId
	 * @param params
	 * @return
	 */
	@RequestMapping("/userOperatPrivileges/add")
	@ResponseBody
	public BaseResult addUserOperatPrivileges(@RequestAttribute(Constants.USER_ID) String userId,
			@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UserOperatePrivilegesRelate.USER_OPERATE_PRIVILEGES_ORIGIN.name(), 1);
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		map.put(TableConstants.CREATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.CREATE_USER_ID, userId);
		userOperatPrivilegesService.addUserOperatPrivileges(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	/**
	 * 删除用户独立操作权限
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("/userOperatPrivileges/delete")
	@ResponseBody
	public BaseResult deleteUserOperatPrivileges(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		boolean b = userOperatPrivilegesService.deleteUserOperatPrivileges(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

}
