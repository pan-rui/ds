package com.pc.controller.auth;

import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.BaseService;
import com.pc.util.DateUtil;
import com.pc.vo.ParamsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class TenantRoleController extends BaseController {

@Autowired
private BaseService baseService;
    private String tableN=TableConstants.TENANT_ROLE;

	/**
	 * 添加操作权限资源
	 * 
	 * @param userId
	 * @param tenantId
	 * @param params
	 * @return
	 */
	@RequestMapping("/tenantRole/add")
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
		baseService.add(map, ddBB+TableConstants.SEPARATE+tableN);
//        urlFilterService.addFuncPermiss(map, ddBB);
        return new BaseResult(ReturnCode.OK);
	}

	/**
	 * 删除操作权限资源
	 * 
	 * @param userId
	 * @param params
	 * @return
	 */
	@RequestMapping("/tenantRole/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @RequestHeader(Constants.TENANT_ID) String tenantId,@EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		map.put(TableConstants.IS_VALID, "1");
		map.put(TableConstants.IS_SEALED, "1");
		int b = baseService.update(map, ddBB+TableConstants.SEPARATE+tableN);
//        boolean b = urlFilterService.deleteFuncPermiss(map, ddBB) > 0;
        if (b>0) {
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
	@RequestMapping("/tenantRole/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestHeader(Constants.TENANT_ID) String tenantId,@RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		map.put(TableConstants.TENANT_ID, tenantId);
		int b = baseService.update(map, ddBB+TableConstants.SEPARATE+tableN);
//        boolean b = urlFilterService.updateFuncPermiss(map, ddBB) > 0;
        if (b>0) {
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
	@RequestMapping("/tenantRole/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		List<Map<String,Object>> resultList=baseService.queryList(map,null, ddBB+TableConstants.SEPARATE+tableN);
		return new BaseResult(ReturnCode.OK, CollectionUtils.isEmpty(resultList)?null:resultList.get(0));
	}

	/**
	 * 获取操作权限资源列表
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("/tenantRole/getList")
	@ResponseBody
	public BaseResult getList(@EncryptProcess ParamsVo params, @RequestHeader(Constants.TENANT_ID) String tenantId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_VALID, 0);
		map.put(TableConstants.IS_SEALED, 0);
		return new BaseResult(ReturnCode.OK, baseService.queryList(map,null, ddBB+TableConstants.SEPARATE+tableN));
	}

	/**
	 * 分页获取操作权限资源
	 * 
	 * @param tenantId
	 * @param page
	 * @return
	 */
	@RequestMapping("/tenantRole/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_VALID, 0);
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, baseService.queryPage(page, ddBB+TableConstants.SEPARATE+tableN));
	}

}
