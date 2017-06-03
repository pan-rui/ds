package com.pc.controller.auth;

import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.auth.OperatModulesService;
import com.pc.service.auth.OperatPrivilegesService;
import com.pc.util.DateUtil;
import com.pc.vo.ParamsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class OperatPrivilegesController extends BaseController {

	@Autowired
	private OperatPrivilegesService operatPrivilegesService;
	@Autowired
	private OperatModulesService operatModulesService;
	

	@RequestMapping("/operatPrivileges/getOperatPrivilegesTree")
	@ResponseBody
	public BaseResult getDataPrivilegesTree(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @RequestAttribute String ddBB) {
		
        Map<String, Object> map = new LinkedHashMap<>();
		map.put(TableConstants.TENANT_ID, tenantId);
		
		List<Map<String, Object>> result=new ArrayList<Map<String,Object>>();
		result.addAll(operatPrivilegesService.getOperatePrivilegesList(map, ddBB));
		result.addAll(operatModulesService.getOperateeModulesList(map, ddBB));
		
		return new BaseResult(ReturnCode.OK,result);
	}

	/**
	 * 添加操作权限资源
	 * 
	 * @param userId
	 * @param tenantId
	 * @param params
	 * @return
	 */
	@RequestMapping("/operatPrivileges/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put("OPERATE_PRIVILEGES_CREATE_TIME", DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		map.put(TableConstants.IS_VALID, 0);
		map.put(TableConstants.IS_SEALED, 0);
		operatPrivilegesService.addOperatPrivilege(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	/**
	 * 删除操作权限资源
	 * 
	 * @param userId
	 * @param params
	 * @return
	 */
	@RequestMapping("/operatPrivileges/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = operatPrivilegesService.deleteOperatPrivilege(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	/**
	 * 修改操作权限资源
	 * 
	 * @param params
	 * @param userId
	 * @return
	 */
	@RequestMapping("/operatPrivileges/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = operatPrivilegesService.updateOperatPrivilege(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	/**
	 * 获取操作权限资源详情
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("/operatPrivileges/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, operatPrivilegesService.getOperatPrivilege(map, ddBB));
	}

	/**
	 * 获取操作权限资源列表
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("/operatPrivileges/getList")
	@ResponseBody
	public BaseResult getList(@EncryptProcess ParamsVo params, @RequestHeader(Constants.TENANT_ID) String tenantId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_VALID, 0);
		map.put(TableConstants.IS_SEALED, 0);
		return new BaseResult(ReturnCode.OK, operatPrivilegesService.getOperatPrivileges(map, ddBB));
	}

	/**
	 * 分页获取操作权限资源
	 * 
	 * @param tenantId
	 * @param page
	 * @return
	 */
	@RequestMapping("/operatPrivileges/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_VALID, 0);
		map.put(TableConstants.IS_SEALED, 0);
		if(map.containsKey(TableConstants.OperatePrivileges.OPERATE_PRIVILEGES_NAME.name())){
			map.put(TableConstants.OperatePrivileges.OPERATE_PRIVILEGES_NAME.name(), "%"+map.get(TableConstants.OperatePrivileges.OPERATE_PRIVILEGES_NAME.name())+"%");
		}
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, operatPrivilegesService.getOperatPrivilegePage(page, ddBB));
	}

}
