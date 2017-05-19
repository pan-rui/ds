package com.pc.controller.procedure;

import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.procedure.impl.ProcedureTypeService;
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
public class ProcedureTypeController extends BaseController {
	@Autowired
	private ProcedureTypeService procedureTypeService;

	private Logger logger = LogManager.getLogger(this.getClass());

	@RequestMapping("/procedureType/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {

		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		map.put(TableConstants.IS_SEALED, 0);

		Map<String, Object> parentMap = null;

		{
			String treeCode = null;
			Map<String, Object> omap = new LinkedHashMap<>();
			omap.put(TableConstants.TENANT_ID, tenantId);
			omap.put(TableConstants.IS_SEALED, 0);
			List<Map<String, Object>> treeList = procedureTypeService.getProcedureTypeList(omap, ddBB);

			String parentID = (String) map.get(TableConstants.ProcedureType.PARENT_ID.name());

			parentMap = TreeUtil.getParentTree(treeList, parentID);
			if(parentMap != null && parentMap.containsKey(TableConstants.ProcedureType.treeCode.name()))
			{
				treeCode =  (String)parentMap.get(TableConstants.ProcedureType.treeCode.name());
			}

			treeCode = TreeUtil.getTreeCode(treeList, treeCode, parentID);

			map.put(TableConstants.OrganizationInfo.TREE_CODE.name(), treeCode);
		}

		{
			String idTree = "";
			String nameTree = "";
			if(parentMap != null)
			{
				if(parentMap.containsKey(TableConstants.ProcedureType.idTree.name()))
				{
					idTree = (String)parentMap.get(TableConstants.ProcedureType.idTree.name());
				}
				if(parentMap.containsKey(TableConstants.ProcedureType.nameTree.name()))
				{
					nameTree = (String)parentMap.get(TableConstants.ProcedureType.nameTree.name());
				}
			}

			if(idTree != null && idTree.length() > 0)
			{
				idTree = String.format("%s%s%s", idTree, TableConstants.SEPARATE_TREE, map.get(TableConstants.ProcedureType.ID.name()));
			}
			else
			{
				idTree = (String)map.get(TableConstants.ProcedureType.ID.name());
			}

			if(nameTree != null && nameTree.length() > 0)
			{
				nameTree = String.format("%s%s%s", nameTree, TableConstants.SEPARATE_TREE, map.get(TableConstants.ProcedureType.PROCEDURE_TYPE_NAME.name()));
			}
			else
			{
				nameTree = (String)map.get(TableConstants.ProcedureType.PROCEDURE_TYPE_NAME.name());
			}

			map.put(TableConstants.ProcedureType.ID_TREE.name(), idTree);
			map.put(TableConstants.ProcedureType.NAME_TREE.name(), nameTree);

			map.put(TableConstants.ProcedureType.IS_LEAF.name(), 1);
			if(parentMap != null)
			{
				int level = 0;
				if(parentMap.containsKey(TableConstants.ProcedureType.level.name())) {
					level = (int) parentMap.get(TableConstants.ProcedureType.level.name());
				}

				map.put(TableConstants.ProcedureType.LEVEL.name(), level + 1);
			}
			else
			{
				map.put(TableConstants.ProcedureType.LEVEL.name(), 1);
			}
		}

		procedureTypeService.addProcedureType(map, ddBB);

		{
			if(parentMap != null && (parentMap.containsKey(TableConstants.ProcedureType.isLeaf.name())
					&& Integer.valueOf((String)parentMap.get(TableConstants.ProcedureType.isLeaf.name())) != 0)) {
				Map<String, Object> parentParam = new LinkedHashMap<>();
				parentParam.put(TableConstants.ProcedureType.ID.name(), parentMap.get(TableConstants.ProcedureType.id.name()));
				parentParam.put(TableConstants.ProcedureType.IS_LEAF.name(), 0);

				procedureTypeService.updateProcedureType(parentParam, ddBB);
			}
		}

		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/procedureType/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = procedureTypeService.deleteProcedureType(map, ddBB);
		if (b) {
			String id = null;
			String parentId = null;
			Map<String, Object> parentMap = new LinkedHashMap<>();

			if (map != null && map.containsKey(TableConstants.ProcedureType.ID.name())) {
				id = (String) map.get(TableConstants.ProcedureType.ID.name());
			}

			Map<String, Object> mapOrg = procedureTypeService.getByID(id, ddBB);

			if (mapOrg != null && mapOrg.containsKey(TableConstants.ProcedureType.parentId.name()))
			{
				parentId = (String)mapOrg.get(TableConstants.ProcedureType.parentId.name());
			}

			if(parentId != null) {

				Map<String, Object> parentParams = new LinkedHashMap<>();
				parentParams.put(TableConstants.ProcedureType.ID.name(), parentId);
				Map<String, Object> data = procedureTypeService.getProcedureType(parentParams, ddBB);
				if(data != null && !data.isEmpty())
				{
					Map<String, Object> parentParam = new LinkedHashMap<>();
					parentParam.put(TableConstants.ProcedureType.ID.name(), parentId);
					parentParam.put(TableConstants.ProcedureType.IS_LEAF.name(), 1);

					procedureTypeService.updateProcedureType(parentParam, ddBB);
				}
			}

			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/procedureType/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = procedureTypeService.updateProcedureType(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/procedureType/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, procedureTypeService.getProcedureType(map, ddBB));
	}

	@RequestMapping("/procedureType/getList")
	@ResponseBody
	public BaseResult getList(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		return new BaseResult(ReturnCode.OK, procedureTypeService.getProcedureTypeList(map, ddBB));
	}

	@RequestMapping("/procedureType/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, procedureTypeService.getProcedureTypePage(page, ddBB));
	}
}
