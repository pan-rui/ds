package com.pc.controller.auth;

import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.TableConstants;
import com.pc.security.UrlFilterService;
import com.pc.service.auth.FuncRolePrivilegesService;
import com.pc.vo.ParamsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class FuncRolePrivilegesController extends BaseController {

	@Autowired
	private FuncRolePrivilegesService funcRolePrivilegesService;
	@Autowired
	private UrlFilterService urlFilterService;

	/**
	 * 获取角色权限
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("/funcRolePrivileges/get")
	@ResponseBody
	public BaseResult getFuncRolePrivileges(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		return new BaseResult(ReturnCode.OK, funcRolePrivilegesService.getFuncRolePrivileges(params.getId(), ddBB));
	}

	/**
	 * 添加角色权限
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("/funcRolePrivileges/add")
	@ResponseBody
	public BaseResult addFuncRolePrivileges(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		funcRolePrivilegesService.addFuncRolePrivileges(params.getParams(), ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	/**
	 * 删除角色权限
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("/funcRolePrivileges/delete")
	@ResponseBody
	public BaseResult deleteFuncRolePrivileges(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		return new BaseResult(ReturnCode.OK,
				urlFilterService.deleteFuncRolePermiss(params.getParams(), ddBB));
	}

	@RequestMapping("/funcRolePrivileges/addList")
	@ResponseBody
	public BaseResult addFuncRolePrivilegesList(@EncryptProcess ParamsVo params,@RequestAttribute String ddBB) {
//		funcRolePrivilegesService.addFuncRolePrivilegesList(params.getDatas(), ddBB);
		urlFilterService.inserFuncRolePermiss(params.getParams(), ddBB);
		return new BaseResult(ReturnCode.OK);
	}

}
