package com.pc.controller.auth;


import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.auth.DataPrivilegesInfoService;
import com.pc.service.auth.RoleDataPrivilegesRelateService;
import com.pc.util.DateUtil;
import com.pc.vo.ParamsVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Description: 
 * @Author: wady (2017-03-28 20:19)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-28
 */
@Controller
@RequestMapping("/admin")
public class RoleDataPrivilegesRelateController extends BaseController {
	@Autowired
	private RoleDataPrivilegesRelateService roleDataPrivilegesRelateService;
	
	@Autowired
	private DataPrivilegesInfoService dataPrivilegesInfoService;

	private Logger logger = LogManager.getLogger(this.getClass());
	
	
	@RequestMapping("/roleDataPrivilegesRelate/addList")
	@ResponseBody
	public BaseResult addList(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		
        List<Map<String, Object>> list = (List<Map<String, Object>>) params.getDatas();
        String roleId=(String) params.getParams().get(TableConstants.RoleDataPrivilegesRelate.ROLE_ID.name());
        String dataTypeId=(String) params.getParams().get(TableConstants.DataPrivilegesInfo.DATA_TYPE_ID.name());
        
        Map<String, Object> delMap=new HashMap<>();
        delMap.put(TableConstants.RoleDataPrivilegesRelate.roleId.name(), roleId);
        delMap.put(TableConstants.DataPrivilegesInfo.dataTypeId.name(), dataTypeId);
        roleDataPrivilegesRelateService.deleteRoleDataPrivilegesRelateList(delMap, ddBB);
        
        Map<String, Object> dataPrivilegesParams=new HashMap<>();
        dataPrivilegesParams.put(TableConstants.DataPrivilegesInfo.DATA_TYPE_ID.name(), dataTypeId);
        dataPrivilegesParams.put(TableConstants.TENANT_ID, tenantId);
		List<Map<String, Object>> dataPrivilegesList = dataPrivilegesInfoService.getDataPrivilegesInfoList(dataPrivilegesParams,ddBB);
		Map<String, Object> dataPrivilegesListMap=new HashMap<>(); 
		for(Map<String, Object> dataPrivileges:dataPrivilegesList){
			dataPrivilegesListMap.put((String)dataPrivileges.get(TableConstants.DataPrivilegesInfo.dataId.name()), (String)dataPrivileges.get(TableConstants.DataPrivilegesInfo.id.name()));
		}
        
        for(Map<String, Object> map:list){
        	map.put(TableConstants.TENANT_ID, tenantId);
    		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
    		map.put(TableConstants.UPDATE_USER_ID, userId);
    		map.put(TableConstants.RoleDataPrivilegesRelate.ROLE_ID.name(), roleId);
    		map.put(TableConstants.RoleDataPrivilegesRelate.DATA_PRIVILEGE_ID.name(), dataPrivilegesInfoService.getDataPrivilegesInfoId(dataPrivilegesListMap,tenantId,(String) map.get(TableConstants.DataPrivilegesInfo.ID.name()), dataTypeId, ddBB));
    		map.put(TableConstants.RoleDataPrivilegesRelate.ID.name(), UUID.randomUUID().toString().replace("-", ""));
        }
        if(list!=null&&list.size()>0){
        	roleDataPrivilegesRelateService.addRoleDataPrivilegesRelateList(list, ddBB);
        }
		return new BaseResult(ReturnCode.OK);
	}
	
	@RequestMapping("/roleDataPrivilegesRelate/getDataPrivilegesList")
	@ResponseBody
	public BaseResult getDataPrivilegesList(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		return new BaseResult(ReturnCode.OK, roleDataPrivilegesRelateService.getDataPrivilegesList(params.getParams(), ddBB));
	}
	
	@RequestMapping("/roleDataPrivilegesRelate/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		
        Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		roleDataPrivilegesRelateService.addRoleDataPrivilegesRelate(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/roleDataPrivilegesRelate/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		boolean b = roleDataPrivilegesRelateService.deleteRoleDataPrivilegesRelate(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/roleDataPrivilegesRelate/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = roleDataPrivilegesRelateService.updateRoleDataPrivilegesRelate(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/roleDataPrivilegesRelate/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, roleDataPrivilegesRelateService.getRoleDataPrivilegesRelate(map, ddBB));
	}

	@RequestMapping("/roleDataPrivilegesRelate/getList")
	@ResponseBody
	public BaseResult getList(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, roleDataPrivilegesRelateService.getRoleDataPrivilegesRelateList(map, ddBB));
	}

	@RequestMapping("/roleDataPrivilegesRelate/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, roleDataPrivilegesRelateService.getRoleDataPrivilegesRelatePage(page, ddBB));
	}
}
