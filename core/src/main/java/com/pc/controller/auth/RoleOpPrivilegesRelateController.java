package com.pc.controller.auth;

 
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pc.controller.BaseController;
import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.core.DataConstants;
import com.pc.core.Page;
import com.pc.util.DateUtil;
import com.pc.vo.ParamsVo;

import com.pc.core.TableConstants;

import com.pc.service.auth.RoleOpPrivilegesRelateService;

/**
 * @Description: 
 * @Author: wady (2017-03-28 20:19)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-28
 */
@Controller
@RequestMapping("/admin")
public class RoleOpPrivilegesRelateController extends BaseController {
	@Autowired
	private RoleOpPrivilegesRelateService roleOpPrivilegesRelateService;

	private Logger logger = LogManager.getLogger(this.getClass());
	
	@RequestMapping("/roleOpPrivilegesRelate/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		
        Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		roleOpPrivilegesRelateService.addRoleOpPrivilegesRelate(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/roleOpPrivilegesRelate/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		boolean b = roleOpPrivilegesRelateService.deleteRoleOpPrivilegesRelate(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/roleOpPrivilegesRelate/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = roleOpPrivilegesRelateService.updateRoleOpPrivilegesRelate(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/roleOpPrivilegesRelate/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, roleOpPrivilegesRelateService.getRoleOpPrivilegesRelate(map, ddBB));
	}

	@RequestMapping("/roleOpPrivilegesRelate/getList")
	@ResponseBody
	public BaseResult getList(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, roleOpPrivilegesRelateService.getRoleOpPrivilegesRelateList(map, ddBB));
	}

	@RequestMapping("/roleOpPrivilegesRelate/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, roleOpPrivilegesRelateService.getRoleOpPrivilegesRelatePage(page, ddBB));
	}
	
	@RequestMapping("/roleOpPrivilegesRelate/addList")
	@ResponseBody
	public BaseResult addroleOpPrivilegesList(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		List<Map<String, Object>> list = params.getDatas();
		String roleId = (String) params.getParams().get(TableConstants.RoleOpPrivilegesRelate.ROLE_ID.name());
		
		Map<String, Object> paramsMap=new HashMap<String, Object>();
		paramsMap.put(TableConstants.RoleOpPrivilegesRelate.ROLE_ID.name(), roleId);
		roleOpPrivilegesRelateService.deleteRoleOpPrivilegesRelate(paramsMap, ddBB);
		
		for (Map<String, Object> obj : list) {
			obj.put(TableConstants.RoleOpPrivilegesRelate.ROLE_ID.name(), roleId);
			roleOpPrivilegesRelateService.addRoleOpPrivilegesRelate(obj, ddBB);
		}
		
		return new BaseResult(ReturnCode.OK);
	}
	
	@RequestMapping("/roleOpPrivilegesRelate/getPrivileges")
	@ResponseBody
	public BaseResult getPrivileges(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		return new BaseResult(ReturnCode.OK, roleOpPrivilegesRelateService.getPrivileges(params.getParams(), ddBB));
	}
	
}
