package com.pc.controller.user;

import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.user.TokenService;
import com.pc.service.user.UserService;
import com.pc.util.DateUtil;
import com.pc.vo.ParamsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class UserController extends BaseController {
	@Autowired
	private UserService userService;

	@Autowired
	private TokenService tokenService;


	@RequestMapping("/user/getPage")
	@ResponseBody
	public BaseResult getUserPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, userService.getUserPage(page, ddBB));
	}

	@RequestMapping("/user/getList")
	@ResponseBody
	public BaseResult getUserList(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		return new BaseResult(ReturnCode.OK, userService.getUserList(map, ddBB));
	}

	@RequestMapping("/user/edit")
	@ResponseBody
	public BaseResult editUser(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = userService.updateUser(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/user/add")
	@ResponseBody
	public BaseResult addUser(@RequestHeader(Constants.TENANT_ID) String tenantId,
			@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		map.put(TableConstants.IS_SEALED, 0);
		map.put(TableConstants.User.REGISTER_TIME.name(), DateUtil.convertDateTimeToString(new Date(), null));
		userService.addUser(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/user/delete")
	@ResponseBody
	public BaseResult deleteUser(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = userService.deleteUser(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/user/get")
	@ResponseBody
	public BaseResult getUser(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		return new BaseResult(ReturnCode.OK, userService.getUser(map, ddBB));
	}
	
	@RequestMapping("")
	public BaseResult index(HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.getWriter().write("鹏城欢迎您!");
		response.getWriter().close();
		return null;
	}

	@RequestMapping("/cleanColumns")
	@ResponseBody
	public BaseResult bb(HttpSession session, @RequestAttribute String ddBB) {
//		logger.debug("in............clearColumns cache....");
		if (ddBB.equals("dems")) {
			tokenService.clearAllCache();
			baseImpl.initColumns();
			return new BaseResult(ReturnCode.OK);
		}
		return new BaseResult(ReturnCode.FAIL);
	}

}
