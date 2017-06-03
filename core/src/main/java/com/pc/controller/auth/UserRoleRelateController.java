package com.pc.controller.auth;


import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.auth.UserRoleRelateService;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 
 * @Author: wady (2017-03-28 20:19)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-28
 */
@Controller
@RequestMapping("/admin")
public class UserRoleRelateController extends BaseController {
	@Autowired
	private UserRoleRelateService userRoleRelateService;

	private Logger logger = LogManager.getLogger(this.getClass());
	
	
	@RequestMapping("/userRoleRelate/getUserListByRole")
	@ResponseBody
	public BaseResult getUserListByRole(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		page.setParams(map);
		userRoleRelateService.getUserListByRole(page, ddBB);
		return new BaseResult(ReturnCode.OK, page);
	}
	
	@RequestMapping("/userRoleRelate/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		String uid=(String) params.getParams().get(TableConstants.UserRoleRelate.USER_ID.name());
		String rid=(String) params.getParams().get(TableConstants.UserRoleRelate.ROLE_ID.name());
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(TableConstants.UserRoleRelate.USER_ID.name(), uid);
        map.put(TableConstants.UserRoleRelate.ROLE_ID.name(), rid);
        map.put(TableConstants.TENANT_ID, tenantId);
        List<Map<String, Object>> list=userRoleRelateService.getUserRoleRelateList(map, ddBB);
        if(list.size()==0){
        	map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
    		map.put(TableConstants.UPDATE_USER_ID, userId);
    		userRoleRelateService.addUserRoleRelate(map, ddBB);
        }
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/userRoleRelate/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		boolean b = userRoleRelateService.deleteUserRoleRelate(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/userRoleRelate/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = userRoleRelateService.updateUserRoleRelate(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/userRoleRelate/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, userRoleRelateService.getUserRoleRelate(map, ddBB));
	}

	@RequestMapping("/userRoleRelate/getList")
	@ResponseBody
	public BaseResult getList(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, userRoleRelateService.getUserRoleRelateList(map, ddBB));
	}

	@RequestMapping("/userRoleRelate/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, userRoleRelateService.getUserRoleRelatePage(page, ddBB));
	}
}
