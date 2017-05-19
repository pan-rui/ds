package com.pc.controller.auth;

import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.service.auth.OperatRolePrivilegesService;
import com.pc.vo.ParamsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class OperatRolePrivilegesController extends BaseController {

	@Autowired
	private OperatRolePrivilegesService operatRolePrivilegesService;

	/**
	 * 获取操作角色权限
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("/operatRolePrivileges/get")
	@ResponseBody
	public BaseResult getOperatRolePrivileges(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		return new BaseResult(ReturnCode.OK, operatRolePrivilegesService.getOperatRolePrivileges(params.getId(), ddBB));
	}

	/**
	 * 添加操作角色权限
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("/operatRolePrivileges/add")
	@ResponseBody
	public BaseResult addOperatRolePrivileges(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		operatRolePrivilegesService.addOperatRolePrivileges(params.getParams(), ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	/**
	 * 删除操作角色权限
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("/operatRolePrivileges/delete")
	@ResponseBody
	public BaseResult deleteOperatRolePrivileges(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		return new BaseResult(ReturnCode.OK,
				operatRolePrivilegesService.deleteOperatRolePrivileges(params.getParams(), ddBB));
	}

	@RequestMapping("/operatRolePrivileges/addList")
	@ResponseBody
	public BaseResult addOperatRolePrivilegesList(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		operatRolePrivilegesService.addOperatRolePrivilegesList(params.getDatas(), ddBB);
		return new BaseResult(ReturnCode.OK);
	}

}
