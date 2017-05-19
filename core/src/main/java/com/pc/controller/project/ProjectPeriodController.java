package com.pc.controller.project;


import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.project.impl.ProjectPeriodService;
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
public class ProjectPeriodController extends BaseController {
	@Autowired
	private ProjectPeriodService projectPeriodService;

	private Logger logger = LogManager.getLogger(this.getClass());

	public final static String MANAGER_NAME = "MANAGER_NAME";
	
	@RequestMapping("/projectPeriod/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		
                Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		map.put(TableConstants.IS_SEALED, 0); 
		projectPeriodService.addProjectPeriod(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/projectPeriod/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = projectPeriodService.deleteProjectPeriod(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/projectPeriod/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = projectPeriodService.updateProjectPeriod(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/projectPeriod/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, projectPeriodService.getProjectPeriod(map, ddBB));
	}

	@RequestMapping("/projectPeriod/getList")
	@ResponseBody
	public BaseResult getList(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		//return new BaseResult(ReturnCode.OK, projectPeriodService.getProjectPeriodList(map, ddBB));
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		if(map != null) {
			if (map.containsKey(TableConstants.ProjectInfo.PROJECT_NAME.name())) {
				map.put(TableConstants.ProjectInfo.PROJECT_NAME.name(), String.format("%%%s%%", map.get(TableConstants.ProjectInfo.PROJECT_NAME.name())));
			}
			if (map.containsKey(TableConstants.ProjectPeriod.PERIOD_NAME.name())) {
				map.put(TableConstants.ProjectPeriod.PERIOD_NAME.name(), String.format("%%%s%%", map.get(TableConstants.ProjectPeriod.PERIOD_NAME.name())));
			}
			if (map.containsKey(MANAGER_NAME)) {
				map.put(MANAGER_NAME, String.format("%%%s%%", map.get(MANAGER_NAME)));
			}
			if (map.containsKey(TableConstants.OrganizationInfo.ORGANIZATION_NAME.name())) {
				map.put(TableConstants.OrganizationInfo.ORGANIZATION_NAME.name(), String.format("%%%s%%", map.get(TableConstants.OrganizationInfo.ORGANIZATION_NAME.name())));
			}
		}

		return new BaseResult(ReturnCode.OK, projectPeriodService.getProjectPeriodDetailList(map, ddBB));
	}

	@RequestMapping("/projectPeriod/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);

		if(page.getParams() != null) {
			if (page.getParams().containsKey(TableConstants.ProjectInfo.PROJECT_NAME.name())) {
				page.getParams().put(TableConstants.ProjectInfo.PROJECT_NAME.name(), String.format("%%%s%%", page.getParams().get(TableConstants.ProjectInfo.PROJECT_NAME.name())));
			}
			if (page.getParams().containsKey(TableConstants.ProjectPeriod.PERIOD_NAME.name())) {
				page.getParams().put(TableConstants.ProjectPeriod.PERIOD_NAME.name(), String.format("%%%s%%", page.getParams().get(TableConstants.ProjectPeriod.PERIOD_NAME.name())));
			}
			if (page.getParams().containsKey(MANAGER_NAME)) {
				page.getParams().put(MANAGER_NAME, String.format("%%%s%%", page.getParams().get(MANAGER_NAME)));
			}
			if (page.getParams().containsKey(TableConstants.OrganizationInfo.ORGANIZATION_NAME.name())) {
				page.getParams().put(TableConstants.OrganizationInfo.ORGANIZATION_NAME.name(), String.format("%%%s%%", page.getParams().get(TableConstants.OrganizationInfo.ORGANIZATION_NAME.name())));
			}
		}

		//return new BaseResult(ReturnCode.OK, projectPeriodService.getProjectPeriodPage(page, ddBB));
		return new BaseResult(ReturnCode.OK, projectPeriodService.getProjectPeriodDetailPage(page, ddBB));
	}
}
