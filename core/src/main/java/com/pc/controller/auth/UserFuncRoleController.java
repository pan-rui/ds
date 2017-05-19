package com.pc.controller.auth;

import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.TableConstants;
import com.pc.security.UrlFilterService;
import com.pc.service.auth.UserFuncRoleService;
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
public class UserFuncRoleController extends BaseController {

	@Autowired
	private UserFuncRoleService userFuncRoleService;
	@Autowired
	private UrlFilterService urlFilterService;

	/**
	 * 添加用户角色
	 * 
	 * @param userId
	 * @param params
	 * @return
	 */
	@RequestMapping("/userFuncRole/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
//		userFuncRoleService.addUserFuncRole(map, ddBB);
		urlFilterService.addUserFuncRole(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	/**
	 * 删除用户角色
	 * 
	 * @param userId
	 * @param params
	 * @return
	 */
	@RequestMapping("/userFuncRole/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		boolean b = urlFilterService.deleteUserFuncRole(map, ddBB) > 0;
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	/**
	 * 获取用户角色列表
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("/userFuncRole/getRoles")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		return new BaseResult(ReturnCode.OK, userFuncRoleService.getUserFuncRoles(params.getId(), ddBB));
	}

}
