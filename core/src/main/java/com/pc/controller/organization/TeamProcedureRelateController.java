package com.pc.controller.organization;


import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.organization.impl.TeamProcedureRelateService;
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

import java.util.ArrayList;
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
public class TeamProcedureRelateController extends BaseController {
	@Autowired
	private TeamProcedureRelateService teamProcedureRelateService;

	private Logger logger = LogManager.getLogger(this.getClass());
	
	
	@RequestMapping("/teamProcedureRelate/addList")
	@ResponseBody
	public BaseResult addList(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		
		List<Map<String, Object>> list = params.getDatas();
		String teamId = (String) params.getParams().get(TableConstants.TeamProcedureRelate.TEAM_ID.name());
		
		Map<String, Object> paramsMap=new HashMap<String, Object>();
		paramsMap.put(TableConstants.TeamProcedureRelate.TEAM_ID.name(), teamId);
		teamProcedureRelateService.deleteTeamProcedureRelate(paramsMap, ddBB);
		
		List<Map<String, Object>> tprList=new ArrayList<>();
        for(Map<String, Object> map:list){
        	LinkedHashMap<String, Object> obj=new LinkedHashMap<>();
        	obj.put(TableConstants.TeamProcedureRelate.PROCEDURE_ID.name(), map.get(TableConstants.TeamProcedureRelate.PROCEDURE_ID.name()));
        	obj.put(TableConstants.TeamProcedureRelate.TEAM_ID.name(), teamId);
        	obj.put(TableConstants.TENANT_ID, tenantId);
        	obj.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
        	obj.put(TableConstants.UPDATE_USER_ID, userId);
        	obj.put(TableConstants.IS_SEALED, 0);
        	obj.put(TableConstants.TeamProcedureRelate.ID.name(), UUID.randomUUID().toString().replace("-", ""));
        	tprList.add(obj);
        }
        teamProcedureRelateService.addTeamProcedureRelateList(tprList, ddBB);
		return new BaseResult(ReturnCode.OK);
	}
	
	@RequestMapping("/teamProcedureRelate/getProcedureListByTeam")
	@ResponseBody
	public BaseResult getProcedureListByTeam(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		return new BaseResult(ReturnCode.OK, teamProcedureRelateService.getProcedureListByTeam(map, ddBB));
	}
	
	@RequestMapping("/teamProcedureRelate/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		
                Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		map.put(TableConstants.IS_SEALED, 0); 
		teamProcedureRelateService.addTeamProcedureRelate(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/teamProcedureRelate/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = teamProcedureRelateService.deleteTeamProcedureRelate(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/teamProcedureRelate/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = teamProcedureRelateService.updateTeamProcedureRelate(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/teamProcedureRelate/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, teamProcedureRelateService.getTeamProcedureRelate(map, ddBB));
	}

	@RequestMapping("/teamProcedureRelate/getList")
	@ResponseBody
	public BaseResult getList(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, teamProcedureRelateService.getTeamProcedureRelateList(map, ddBB));
	}

	@RequestMapping("/teamProcedureRelate/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, teamProcedureRelateService.getTeamProcedureRelatePage(page, ddBB));
	}
}
