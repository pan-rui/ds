package com.pc.controller.auth;

import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.auth.FuncRoleService;
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
public class FuncRoleController extends BaseController {

	@Autowired
	private FuncRoleService funcRoleService;

	/**
	 * 分页获取角色
	 * 
	 * @param tenantId
	 * @param page
	 * @return
	 */
	@RequestMapping("/funcRole/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_VALID, 0);
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, funcRoleService.getFuncRolePage(page, ddBB));
	}

	@RequestMapping("/funcRole/getList")
	@ResponseBody
	public BaseResult getList(@EncryptProcess ParamsVo params, @RequestHeader(Constants.TENANT_ID) String tenantId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_VALID, 0);
		map.put(TableConstants.IS_SEALED, 0);
		return new BaseResult(ReturnCode.OK, funcRoleService.getFuncRoleList(map, ddBB));
	}

	/**
	 * 添加角色
	 * 
	 * @param userId
	 * @param tenantId
	 * @param params
	 * @return
	 */
	@RequestMapping("/funcRole/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		map.put(TableConstants.IS_VALID, 0);
		map.put(TableConstants.IS_SEALED, 0);
		funcRoleService.addFuncRole(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	/**
	 * 修改角色
	 * 
	 * @param userId
	 * @param params
	 * @return
	 */
	@RequestMapping("/funcRole/edit")
	@ResponseBody
	public BaseResult edit(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = funcRoleService.updateFuncRole(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	/**
	 * 删除角色
	 * 
	 * @param userId
	 * @param params
	 * @return
	 */
	@RequestMapping("/funcRole/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = funcRoleService.deleteFuncRole(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	/**
	 * 获取角色详情
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("/funcRole/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, funcRoleService.getFuncRole(map, ddBB));
	}

}
