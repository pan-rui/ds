package com.pc.controller.project;


import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.project.impl.ProjectRegionProcedureRelateService;
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
public class ProjectRegionProcedureRelateController extends BaseController {
	@Autowired
	private ProjectRegionProcedureRelateService projectRegionProcedureRelateService;

	private Logger logger = LogManager.getLogger(this.getClass());
	
	
	@RequestMapping("/projectRegionProcedureRelate/addList")
	@ResponseBody
	public BaseResult addList(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		
        List<Map<String, Object>> list = (List<Map<String, Object>>) params.getDatas();
        String projectPeriodId=(String) params.getParams().get(TableConstants.ProjectRegionProcedureRelate.PROJECT_PERIOD_ID.name());
        String regionTypeId=(String) params.getParams().get(TableConstants.ProjectRegionProcedureRelate.REGION_TYPE_ID.name());
        
        Map<String, Object> delMap=new HashMap<>();
        delMap.put(TableConstants.ProjectRegionProcedureRelate.PROJECT_PERIOD_ID.name(), projectPeriodId);
        delMap.put(TableConstants.ProjectRegionProcedureRelate.REGION_TYPE_ID.name(), regionTypeId);
        projectRegionProcedureRelateService.deleteProjectRegionProcedureRelate(delMap, ddBB);
        
        for(Map<String, Object> map:list){
        	map.put(TableConstants.TENANT_ID, tenantId);
    		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
    		map.put(TableConstants.UPDATE_USER_ID, userId);
    		map.put(TableConstants.ProjectRegionProcedureRelate.PROJECT_PERIOD_ID.name(), projectPeriodId);
    		map.put(TableConstants.ProjectRegionProcedureRelate.REGION_TYPE_ID.name(), regionTypeId);
    		map.put(TableConstants.ProjectRegionProcedureRelate.PROCEDURE_ID.name(), (String)map.get(TableConstants.ProcedureInfo.ID.name()));
    		map.put(TableConstants.ProjectRegionProcedureRelate.ID.name(), UUID.randomUUID().toString().replace("-", ""));
        }
        if(list!=null&&list.size()>0){
        	projectRegionProcedureRelateService.addRoleDataPrivilegesRelateList(list, ddBB);
        }
		return new BaseResult(ReturnCode.OK);
	}
	
	@RequestMapping("/projectRegionProcedureRelate/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		
                Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		projectRegionProcedureRelateService.addProjectRegionProcedureRelate(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/projectRegionProcedureRelate/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		boolean b = projectRegionProcedureRelateService.deleteProjectRegionProcedureRelate(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/projectRegionProcedureRelate/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = projectRegionProcedureRelateService.updateProjectRegionProcedureRelate(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/projectRegionProcedureRelate/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, projectRegionProcedureRelateService.getProjectRegionProcedureRelate(map, ddBB));
	}

	@RequestMapping("/projectRegionProcedureRelate/getList")
	@ResponseBody
	public BaseResult getList(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		return new BaseResult(ReturnCode.OK, projectRegionProcedureRelateService.getProjectRegionProcedureRelateList(map, ddBB));
	}

	@RequestMapping("/projectRegionProcedureRelate/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, projectRegionProcedureRelateService.getProjectRegionProcedureRelatePage(page, ddBB));
	}
}
