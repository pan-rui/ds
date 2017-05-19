package com.pc.controller.organization;

import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.organization.impl.CompanyService;
import com.pc.service.organization.impl.OrganizationInfoService;
import com.pc.util.DateUtil;
import com.pc.util.TreeUtil;
import com.pc.vo.ParamsVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @Description:
 * @Author: wady (2017-03-28 20:19)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-28
 */
@Controller
@RequestMapping("/admin")
public class OrganizationInfoController extends BaseController {
	@Autowired
	private OrganizationInfoService organizationInfoService;

	@Autowired
	private CompanyService companyService;

	private Logger logger = LogManager.getLogger(this.getClass());
	

	@RequestMapping("/organizationInfo/getTree")
	@ResponseBody
	public BaseResult getTree(@RequestHeader(Constants.TENANT_ID) String tenantId, @RequestAttribute String ddBB) {
		Map<String, Object> omap = new LinkedHashMap<>();
		omap.put(TableConstants.TENANT_ID, tenantId);
		omap.put(TableConstants.OrganizationInfo.IS_ENABLED.name(), 0);
		omap.put(TableConstants.IS_SEALED, 0);
		List<Map<String, Object>> treeList = organizationInfoService.queryList(omap, null,
				ddBB + TableConstants.SEPARATE + TableConstants.ORGANIZATION_INFO);

		Map<String, Object> cmap = new LinkedHashMap<>();
		cmap.put(TableConstants.TENANT_ID, tenantId);
		cmap.put(TableConstants.IS_SEALED, 0);
		List<Map<String, Object>> companyList = companyService.queryList(cmap, null,
				ddBB + TableConstants.SEPARATE + TableConstants.COMPANY);
		Map<String, Object> companyMap = new LinkedHashMap<>();
		for (Map<String, Object> map : companyList) {
			companyMap.put((String) map.get(TableConstants.Company.id.name()), map);
		}

		List<Map<String, Object>> tree = TreeUtil.getTree(treeList, TableConstants.OrganizationInfo.parentId.name(),
				TableConstants.OrganizationInfo.corporateId.name(),  TableConstants.OrganizationInfo.id.name(), companyMap, "company", "list");
		return new BaseResult(ReturnCode.OK, tree);
	}

	@RequestMapping("/organizationInfo/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {

		String corporateId = null;
		Map<String, Object> commap = null;
		Map<String, Object> paramsmMap = params.getParams();

		String datestr = DateUtil.convertDateTimeToString(new Date(), null);

		if(paramsmMap.containsKey(ORGAIZATION_PARAM_KEY.COMPANY.name())) {
			commap = (Map) paramsmMap.get(ORGAIZATION_PARAM_KEY.COMPANY.name());

			if (commap != null) {
				corporateId = companyService.companyAdd(userId, tenantId, commap, ddBB, datestr);
			}
		}

		// 组织信息新增
		Map<String, Object> orgmap = new LinkedHashMap<>((Map) paramsmMap.get(ORGAIZATION_PARAM_KEY.ORG.name()));
		orgmap.put(TableConstants.TENANT_ID, tenantId);

		String orgid = UUID.randomUUID().toString().replace("-", "");

		orgmap.put(TableConstants.OrganizationInfo.ID.name(), orgid);
		orgmap.put(TableConstants.CREATE_TIME, datestr);
		orgmap.put(TableConstants.CREATE_USER_ID, userId);
		orgmap.put(TableConstants.UPDATE_TIME, datestr);
		orgmap.put(TableConstants.UPDATE_USER_ID, userId);
		orgmap.put(TableConstants.OrganizationInfo.IS_ENABLED.name(), 0);
		orgmap.put(TableConstants.IS_SEALED, 0);
		if ("1".compareTo(String.valueOf(orgmap.get(TableConstants.OrganizationInfo.IS_ENTITY.name()))) == 0
				&& orgmap.get(TableConstants.OrganizationInfo.CORPORATE_ID.name()) == null && corporateId != null) {
			orgmap.put(TableConstants.OrganizationInfo.CORPORATE_ID.name(), corporateId);
		}

		Map<String, Object> parentMap = null;

		{
			String treeCode = null;
			Map<String, Object> omap = new LinkedHashMap<>();
			omap.put(TableConstants.TENANT_ID, tenantId);
			omap.put(TableConstants.OrganizationInfo.IS_ENABLED.name(), 0);
			omap.put(TableConstants.IS_SEALED, 0);
			List<Map<String, Object>> treeList = organizationInfoService.queryList(omap, null,
					ddBB + TableConstants.SEPARATE + TableConstants.ORGANIZATION_INFO);

			String parentID = (String) orgmap.get(TableConstants.OrganizationInfo.PARENT_ID.name());

			parentMap = TreeUtil.getParentTree(treeList, parentID);
			if(parentMap != null && parentMap.containsKey(TableConstants.OrganizationInfo.treeCode.name()))
			{
				treeCode =  (String)parentMap.get(TableConstants.OrganizationInfo.treeCode.name());
			}

			treeCode = TreeUtil.getTreeCode(treeList, treeCode, parentID);

			orgmap.put(TableConstants.OrganizationInfo.TREE_CODE.name(), treeCode);
		}

		{
			String idTree = "";
			String nameTree = "";
			if(parentMap != null)
			{
				if(parentMap.containsKey(TableConstants.OrganizationInfo.idTree.name()))
				{
					idTree = (String)parentMap.get(TableConstants.OrganizationInfo.idTree.name());
				}
				if(parentMap.containsKey(TableConstants.OrganizationInfo.nameTree.name()))
				{
					nameTree = (String)parentMap.get(TableConstants.OrganizationInfo.nameTree.name());
				}
			}

			if(idTree != null && idTree.length() > 0)
			{
				idTree = String.format("%s%s%s", idTree, TableConstants.SEPARATE_TREE, orgmap.get(TableConstants.OrganizationInfo.ID.name()));
			}
			else
			{
				idTree = (String)orgmap.get(TableConstants.OrganizationInfo.ID.name());
			}

			if(nameTree != null && nameTree.length() > 0)
			{
				nameTree = String.format("%s%s%s", nameTree, TableConstants.SEPARATE_TREE, orgmap.get(TableConstants.OrganizationInfo.ORGANIZATION_NAME.name()));
			}
			else
			{
				nameTree = (String)orgmap.get(TableConstants.OrganizationInfo.ORGANIZATION_NAME.name());
			}

			orgmap.put(TableConstants.OrganizationInfo.ID_TREE.name(), idTree);
			orgmap.put(TableConstants.OrganizationInfo.NAME_TREE.name(), nameTree);

			orgmap.put(TableConstants.OrganizationInfo.IS_LEAF.name(), 1);
			if(parentMap != null)
			{
				int level = 0;
				if(parentMap.containsKey(TableConstants.OrganizationInfo.level.name())) {
					level = (int) parentMap.get(TableConstants.OrganizationInfo.level.name());
				}

				orgmap.put(TableConstants.OrganizationInfo.LEVEL.name(), level + 1);
			}
			else
			{
				orgmap.put(TableConstants.OrganizationInfo.LEVEL.name(), 1);
			}
		}

		organizationInfoService.addOrganizationInfo(orgmap, ddBB);

		{
			if(parentMap != null && (parentMap.containsKey(TableConstants.OrganizationInfo.isLeaf.name()) && Integer.valueOf((String)parentMap.get(TableConstants.OrganizationInfo.isLeaf.name())) != 0)) {
				Map<String, Object> parentParam = new LinkedHashMap<>();
				parentParam.put(TableConstants.OrganizationInfo.ID.name(), parentMap.get(TableConstants.OrganizationInfo.id.name()));
				parentParam.put(TableConstants.OrganizationInfo.IS_LEAF.name(), 0);

				organizationInfoService.updateOrganizationInfo(parentParam, ddBB);
			}
		}

		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/organizationInfo/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = organizationInfoService.deleteOrganizationInfo(map, ddBB);
		if (b) {
				String id = null;
				String parentId = null;
				Map<String, Object> parentMap = new LinkedHashMap<>();

				if (map != null && map.containsKey(TableConstants.OrganizationInfo.ID.name())) {
					id = (String) map.get(TableConstants.OrganizationInfo.ID.name());
				}

				Map<String, Object> mapOrg = organizationInfoService.getByID(id, ddBB);

				if (mapOrg != null && mapOrg.containsKey(TableConstants.OrganizationInfo.parentId.name()))
				{
					parentId = (String)mapOrg.get(TableConstants.OrganizationInfo.parentId.name());
				}

				if(parentId != null) {
					Map<String, Object> parentParams = new LinkedHashMap<>();
					parentParams.put(TableConstants.OrganizationInfo.ID.name(), parentId);
					Map<String, Object> data = organizationInfoService.getOrganizationInfo(parentParams, ddBB);
					if(data != null && !data.isEmpty())
					{
						Map<String, Object> parentParam = new LinkedHashMap<>();
						parentParam.put(TableConstants.OrganizationInfo.ID.name(), parentId);
						parentParam.put(TableConstants.OrganizationInfo.IS_LEAF.name(), 1);

						organizationInfoService.updateOrganizationInfo(parentParam, ddBB);
					}
				}

			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/organizationInfo/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		
		Map<String, Object> paramsmMap = params.getParams();
		if(paramsmMap.containsKey(ORGAIZATION_PARAM_KEY.COMPANY.name())) {
			Map<String, Object> commap = (Map) paramsmMap.get(ORGAIZATION_PARAM_KEY.COMPANY.name());
			commap.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
			commap.put(TableConstants.UPDATE_USER_ID, userId);
			companyService.updateCompany(commap, ddBB);
		}
		
		Map<String, Object> orgmap = (Map) paramsmMap.get(ORGAIZATION_PARAM_KEY.ORG.name());
		if(orgmap!=null){
			orgmap.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
			orgmap.put(TableConstants.UPDATE_USER_ID, userId);
			boolean b = organizationInfoService.updateOrganizationInfo(orgmap, ddBB);
		}
		
		return new BaseResult(ReturnCode.OK);

	}

	@RequestMapping("/organizationInfo/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, organizationInfoService.getOrganizationInfo(map, ddBB));
	}

	@RequestMapping("/organizationInfo/getList")
	@ResponseBody
	public BaseResult getList(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_ENABLED, 0);
		map.put(TableConstants.IS_SEALED, 0);
		return new BaseResult(ReturnCode.OK, organizationInfoService.getOrganizationInfoList(map, ddBB));
	}

	@RequestMapping("/organizationInfo/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_ENABLED, 0);
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, organizationInfoService.getOrganizationInfoPage(page, ddBB));
	}

    public enum ORGAIZATION_PARAM_KEY {
        COMPANY,    //检验批ID
        company,    //检验批ID
        ORG,
        org
    }// ;
}
