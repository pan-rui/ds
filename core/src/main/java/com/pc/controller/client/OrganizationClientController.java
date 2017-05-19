package com.pc.controller.client;

import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.TableConstants;
import com.pc.service.auth.DataPrivilegeTypeService;
import com.pc.service.organization.impl.CompanyService;
import com.pc.service.organization.impl.OrganizationInfoService;
import com.pc.service.organization.impl.TeamInfoService;
import com.pc.service.project.impl.ProjectPeriodService;
import com.pc.util.TreeUtil;
import com.pc.vo.ParamsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/client")
public class OrganizationClientController extends BaseController  {
	@Autowired
    private CompanyService companyService;
	
	@Autowired
    private TeamInfoService teamInfoService;
	
	@Autowired
    private DataPrivilegeTypeService dataPrivilegeTypeService;
	
	@Autowired
    private ProjectPeriodService projectPeriodService;
	
	@Autowired
    private OrganizationInfoService organizationInfoService;
	
	
	@RequestMapping("/organization/getOrgProjectTree")
	@ResponseBody
	public BaseResult getProjectDetailList(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @RequestAttribute String ddBB) {
		Map<String, Object> dataTypeParams = new LinkedHashMap<>();
		dataTypeParams.put(TableConstants.DataPrivilegeType.TABLE_NAME.name(), TableConstants.PROJECT_HOUSEHOLD);
		String dataTypeId = (String) dataPrivilegeTypeService.getDataPrivilegeType(dataTypeParams, ddBB)
				.get(TableConstants.DataPrivilegeType.id.name());

		Map<String, Object> params = new HashMap<>();
		params.put(TableConstants.TENANT_ID, tenantId);
		params.put(TableConstants.UserRoleRelate.userId.name(), userId);
		params.put(TableConstants.DataPrivilegesInfo.dataTypeId.name(), dataTypeId);
		
		List<Map<String, Object>> projectOrganizationInfoList=organizationInfoService.getProjectOrganizationInfoList(params, ddBB);
		
		List<Map<String, Object>> projectPeriodList=projectPeriodService.getProjectPeriodListByUser(params, ddBB);
		
		return new BaseResult(ReturnCode.OK,TreeUtil.getTree(projectOrganizationInfoList, projectPeriodList, TableConstants.ProjectPeriod.comId.name()));
	}
	
	@RequestMapping("/company/getPartnerCompanyList")
	@ResponseBody
	public BaseResult getPartnerCompanyList(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess() ParamsVo params,
			@RequestAttribute String ddBB) {
		String projectPeriodId=(String) params.getParams().get(TableConstants.ProjectPartnerRelate.PROJECT_PERIOD_ID.name());
		Map<String, Object> map = new LinkedHashMap<>();
		map.put(TableConstants.Company.tenantId.name(), tenantId);
		return new BaseResult(ReturnCode.OK, companyService.getPartnerCompanyList(projectPeriodId,map, ddBB));
	}
	
}
