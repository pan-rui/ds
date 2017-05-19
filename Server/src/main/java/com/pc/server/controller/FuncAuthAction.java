package com.pc.server.controller;

import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.TableConstants;
import com.pc.service.BaseService;
import com.pc.vo.ParamsVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description: 功能权限模块
 * @Author: 潘锐 (2017-03-28 14:35)
 * @version: \$Rev: 2395 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-05-18 10:36:38 +0800 (周四, 18 5月 2017) $
 */
@Controller
@RequestMapping("/auth")
@Deprecated
public class FuncAuthAction extends BaseController {

	@Resource
	public BaseService baseService;

	/**
	 * 添加功能权限模块
	 * 
	 * @param pv
	 *            权限模块表单封装对象
	 * @param ddBB
	 *            操作库名
	 * @return
	 */
	@RequestMapping(value = "/funModules", method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addFunModules(@EncryptProcess ParamsVo pv, @RequestHeader(Constants.TENANT_ID) String tenantId,
			@RequestAttribute String ddBB) {
		pv.getParams().addParams("TENANT_ID", tenantId);
		baseService.add(pv.getParams(), ddBB + TableConstants.SEPARATE + TableConstants.FUNC_MODULES);
		return new BaseResult(ReturnCode.OK);
	}

	/**
	 * 删除功能权限模块
	 * 
	 * @param pv
	 *            权限模块表单封装对象
	 * @param ddBB
	 *            操作库名
	 * @return
	 */
	@RequestMapping(value = "/funModules", method = RequestMethod.DELETE)
	@ResponseBody
	public BaseResult deleteFunModules(@EncryptProcess ParamsVo pv, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		pv.getParams().addParams("DEL_USER_ID", userId).addParams("DEL_TIME", new Date());
		baseService.delete(pv.getParams(), ddBB + TableConstants.SEPARATE + TableConstants.FUNC_MODULES);
		return new BaseResult(ReturnCode.OK);
	}

	/**
	 * 修改功能权限模块
	 * 
	 * @param pv
	 *            权限模块表单封装对象
	 * @param ddBB
	 *            操作库名
	 * @return
	 */
	@RequestMapping(value = "/funModules", method = RequestMethod.PUT)
	@ResponseBody
	public BaseResult updateFunModules(@EncryptProcess ParamsVo pv, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		pv.getParams().addParams("UPDATE_USER_ID", userId).addParams("UPDATE_TIME", new Date());
		int result = baseService.update(pv.getParams(), ddBB + TableConstants.SEPARATE + TableConstants.FUNC_MODULES);
		return result > 0 ? new BaseResult(ReturnCode.OK) : new BaseResult(ReturnCode.FAIL);
	}

	@RequestMapping(value = "/funModules", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult getFunModules(@EncryptProcess ParamsVo pv, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		return new BaseResult(0, baseService.queryList(pv.getParams(), null,
				ddBB + TableConstants.SEPARATE + TableConstants.FUNC_MODULES));
	}

	/**
	 * 添加功能权限
	 * 
	 * @param pv
	 *            权限表单封装对象
	 * @param ddBB
	 *            操作库名
	 * @return
	 */
	@RequestMapping(value = "/funPermission", method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addFunPermission(@EncryptProcess ParamsVo pv, @RequestHeader(Constants.TENANT_ID) String tenantId,
			@RequestAttribute String ddBB) {
		pv.getParams().addParams("TENANT_ID", tenantId);
		baseService.add(pv.getParams(), ddBB + TableConstants.SEPARATE + TableConstants.FUNC_PRIVILEGES);
		return new BaseResult(ReturnCode.OK);
	}

	/**
	 * 删除功能权限
	 * 
	 * @param pv
	 *            权限表单封装对象
	 * @param ddBB
	 *            操作库名
	 * @return
	 */
	@RequestMapping(value = "/funPermission", method = RequestMethod.DELETE)
	@ResponseBody
	public BaseResult deleteFunPermission(@EncryptProcess ParamsVo pv,
			@RequestAttribute(Constants.USER_ID) String userId, @RequestAttribute String ddBB) {
		pv.getParams().addParams("DEL_USER_ID", userId).addParams("DEL_TIME", new Date());
		baseService.delete(pv.getParams(), ddBB + TableConstants.SEPARATE + TableConstants.FUNC_PRIVILEGES);
		return new BaseResult(ReturnCode.OK);
	}

	/**
	 * 修改功能权限
	 * 
	 * @param pv
	 *            权限表单封装对象
	 * @param ddBB
	 *            操作库名
	 * @return
	 */
	@RequestMapping(value = "/funPermission", method = RequestMethod.PUT)
	@ResponseBody
	public BaseResult updateFunPermission(@EncryptProcess ParamsVo pv,
			@RequestAttribute(Constants.USER_ID) String userId, @RequestAttribute String ddBB) {
		pv.getParams().addParams("UPDATE_USER_ID", userId).addParams("UPDATE_TIME", new Date());
		int result = baseService.update(pv.getParams(),
				ddBB + TableConstants.SEPARATE + TableConstants.FUNC_PRIVILEGES);
		return result > 0 ? new BaseResult(ReturnCode.OK) : new BaseResult(ReturnCode.FAIL);
	}

	/**
	 * 添加功能角色
	 * 
	 * @param pv
	 *            角色表单封装对象
	 * @param ddBB
	 *            操作库名
	 * @return
	 */
	@RequestMapping(value = "/funRole", method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addFunRole(@EncryptProcess ParamsVo pv, @RequestAttribute String ddBB) {
		baseService.add(pv.getParams(), ddBB + TableConstants.SEPARATE + TableConstants.FUNC_ROLE);
		return new BaseResult(ReturnCode.OK);
	}

	/**
	 * 删除功能角色
	 * 
	 * @param pv
	 *            角色表单封装对象
	 * @param ddBB
	 *            操作库名
	 * @return
	 */
	@RequestMapping(value = "/funRole", method = RequestMethod.DELETE)
	@ResponseBody
	public BaseResult deleteFunRole(@EncryptProcess ParamsVo pv, @RequestAttribute String ddBB) {
		baseService.delete(pv.getParams(), ddBB + TableConstants.SEPARATE + TableConstants.FUNC_ROLE);
		return new BaseResult(ReturnCode.OK);
	}

	/**
	 * 修改功能角色
	 * 
	 * @param pv
	 *            角色表单封装对象
	 * @param ddBB
	 *            操作库名
	 * @return
	 */
	@RequestMapping(value = "/funRole", method = RequestMethod.PUT)
	@ResponseBody
	public BaseResult updateFunRole(@EncryptProcess ParamsVo pv, @RequestAttribute String ddBB) {
		baseService.update(pv.getParams(), ddBB + TableConstants.SEPARATE + TableConstants.FUNC_ROLE);
		return new BaseResult(ReturnCode.OK);
	}

	/**
	 * 添加功能角色权限
	 * 
	 * @param pv
	 *            角色权限表单封装对象
	 * @param ddBB
	 *            操作库名
	 * @return
	 */
	@RequestMapping(value = "/funRolePermiss", method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addFunRolePermiss(@EncryptProcess ParamsVo pv, @RequestAttribute String ddBB) {
		baseService.add(pv.getParams(), ddBB + TableConstants.SEPARATE + TableConstants.FUNC_ROLE_PRIVILEGES_RELATE);
		return new BaseResult(ReturnCode.OK);
	}

	/**
	 * 添加功能角色权限
	 * 
	 * @param pv
	 *            角色权限表单封装对象
	 * @param ddBB
	 *            操作库名
	 * @return
	 */
	@RequestMapping(value = "/funRolePermiss", method = RequestMethod.DELETE)
	@ResponseBody
	public BaseResult deleteFunRolePermiss(@EncryptProcess ParamsVo pv, @RequestAttribute String ddBB) {
		baseService.delete(pv.getParams(), ddBB + TableConstants.SEPARATE + TableConstants.FUNC_ROLE_PRIVILEGES_RELATE);
		return new BaseResult(ReturnCode.OK);
	}

	/**
	 * 修改功能角色权限
	 * 
	 * @param pv
	 *            角色权限表单封装对象
	 * @param ddBB
	 *            操作库名
	 * @return
	 */
	@RequestMapping(value = "/funRolePermiss", method = RequestMethod.PUT)
	@ResponseBody
	public BaseResult updateFunRolePermiss(@EncryptProcess ParamsVo pv, @RequestAttribute String ddBB) {
		baseService.update(pv.getParams(), ddBB + TableConstants.SEPARATE + TableConstants.FUNC_ROLE_PRIVILEGES_RELATE);
		return new BaseResult(ReturnCode.OK);
	}

	/**
	 * 添加功能用户角色
	 * 
	 * @param pv
	 *            功能角色表单封装对象
	 * @param ddBB
	 *            操作库名
	 * @return
	 */
	@RequestMapping(value = "/funUserRole", method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addFunUserRole(@EncryptProcess ParamsVo pv, @RequestAttribute String ddBB) {
		baseService.add(pv.getParams(), ddBB + TableConstants.SEPARATE + TableConstants.USER_FUNC_ROLE_RELATE);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping(value = "/funUserRole", method = RequestMethod.DELETE)
	@ResponseBody
	public BaseResult deleteFunUserRole(@EncryptProcess ParamsVo pv, @RequestAttribute String ddBB) {
		baseService.delete(pv.getParams(), ddBB + TableConstants.SEPARATE + TableConstants.USER_FUNC_ROLE_RELATE);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping(value = "/funUserRole", method = RequestMethod.PUT)
	@ResponseBody
	public BaseResult updateFunUserRole(@EncryptProcess ParamsVo pv, @RequestAttribute String ddBB) {
		baseService.update(pv.getParams(), ddBB + TableConstants.SEPARATE + TableConstants.USER_FUNC_ROLE_RELATE);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping(value = "/funUserPermiss", method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addFunUserPermiss(@EncryptProcess ParamsVo pv, @RequestAttribute String ddBB) {
		baseService.add(pv.getParams(), ddBB + TableConstants.SEPARATE + TableConstants.USER_FUNC_PRIVILEGES_RELATE);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping(value = "/funUserPermiss", method = RequestMethod.DELETE)
	@ResponseBody
	public BaseResult deleteFunUserPermiss(@EncryptProcess ParamsVo pv, @RequestAttribute String ddBB) {
		baseService.delete(pv.getParams(), ddBB + TableConstants.SEPARATE + TableConstants.USER_FUNC_PRIVILEGES_RELATE);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping(value = "/funUserPermiss", method = RequestMethod.PUT)
	@ResponseBody
	public BaseResult updateFunUserPermiss(@EncryptProcess ParamsVo pv, @RequestAttribute String ddBB) {
		baseService.update(pv.getParams(), ddBB + TableConstants.SEPARATE + TableConstants.USER_FUNC_PRIVILEGES_RELATE);
		return new BaseResult(ReturnCode.OK);
	}

}