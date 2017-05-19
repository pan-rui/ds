package com.pc.controller.client;

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

import com.alibaba.fastjson.JSONObject;
import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.DataConstants;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.service.auth.DataPrivilegeTypeService;
import com.pc.service.auth.DataTypeService;
import com.pc.service.procedure.impl.DominantItemService;
import com.pc.service.procedure.impl.GeneralItemService;
import com.pc.service.procedure.impl.ProcedureInfoService;
import com.pc.service.procedure.impl.ProcedureTypeService;
import com.pc.service.project.impl.ProjectRegionTypeService;
import com.pc.util.TreeUtil;
import com.pc.vo.ParamsVo;

/**
 * @Description:
 * @Author: wady (2017-03-28 20:19)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-28
 */
@Controller
@RequestMapping("/client")
public class ProcedureClientController extends BaseController {
	@Autowired
	private ProcedureInfoService procedureInfoService;

	@Autowired
	private GeneralItemService generalItemService;

	@Autowired
	private DominantItemService dominantItemService;

	@Autowired
	private ProjectRegionTypeService projectRegionTypeService;

	@Autowired
	private ProcedureTypeService procedureTypeService;
	
	@Autowired
	private DataPrivilegeTypeService dataPrivilegeTypeService;

	private Logger logger = LogManager.getLogger(this.getClass());
	
	
	@RequestMapping("/procedureInfo/getListByRegionAndUser")
	@ResponseBody
	public BaseResult getListByRegionAndUser(@RequestAttribute(Constants.USER_ID) String userId, @RequestHeader(Constants.TENANT_ID) String tenantId,
			@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {

		String regionType = (String) params.getParams().get(TableConstants.AcceptanceNote.REGION_TYPE.name());
		String projectPeriodId = (String) params.getParams().get(TableConstants.ProjectRegionProcedureRelate.PROJECT_PERIOD_ID.name());
		if(projectPeriodId==null){
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}
		String regionTypeId = null;
		if (DataConstants.REGION_PERIOD_TYPE_VAL.equals(regionType)) {
			regionTypeId = (String) projectRegionTypeService
					.getProjectRegionType(ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
							DataConstants.REGION_PERIOD_TYPE), ddBB)
					.get(TableConstants.ProjectRegionType.id.name());
		} else if (DataConstants.REGION_BUILDING_TYPE_VAL.equals(regionType)) {
			regionTypeId = (String) projectRegionTypeService
					.getProjectRegionType(ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
							DataConstants.REGION_BUILDING_TYPE), ddBB)
					.get(TableConstants.ProjectRegionType.id.name());
		} else if (DataConstants.REGION_FLOOR_TYPE_VAL.equals(regionType)) {
			regionTypeId = (String) projectRegionTypeService
					.getProjectRegionType(ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
							DataConstants.REGION_FLOOR_TYPE), ddBB)
					.get(TableConstants.ProjectRegionType.id.name());
		} else if (DataConstants.REGION_ROOM_TYPE_VAL.equals(regionType)) {
			regionTypeId = (String) projectRegionTypeService
					.getProjectRegionType(ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
							DataConstants.REGION_ROOM_TYPE), ddBB)
					.get(TableConstants.ProjectRegionType.id.name());
		} else {
			logger.info("部位类型不正常");
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}
		Map<String, Object> dataTypeParams = new LinkedHashMap<>();
		dataTypeParams.put(TableConstants.DataPrivilegeType.TABLE_NAME.name(), TableConstants.PROCEDURE_INFO);
		String dataTypeId=(String) dataPrivilegeTypeService.getDataPrivilegeType(dataTypeParams, ddBB).get(TableConstants.DataPrivilegeType.id.name());
				
		Map<String, Object> map = new LinkedHashMap<>();
		map.put(TableConstants.ProjectRegionProcedureRelate.tenantId.name(),tenantId);
		map.put(TableConstants.ProjectRegionProcedureRelate.projectPeriodId.name(),projectPeriodId);
		map.put(TableConstants.ProjectRegionProcedureRelate.regionTypeId.name(),regionTypeId);
		map.put(TableConstants.UserRoleRelate.userId.name(),userId);
		map.put(TableConstants.DataPrivilegesInfo.dataTypeId.name(),dataTypeId);
		//根据部位类型找到工序
		List<Map<String, Object>> procedureList=procedureInfoService.getListByRegionAndUser(map, ddBB);
		//找到工序的树结构列表
		List<Map<String, Object>> procedureTypeList=procedureTypeService.getProcedureTypeByUser(map, ddBB);

		return new BaseResult(ReturnCode.OK,TreeUtil.getTree(procedureTypeList, procedureList,TableConstants.ProcedureInfo.procedureTypeId.name()));
	}

	@RequestMapping("/procedureInfo/getListByRegion")
	@ResponseBody
	public BaseResult getListByRegion(@RequestHeader(Constants.TENANT_ID) String tenantId,
			@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {

		String regionType = (String) params.getParams().get(TableConstants.AcceptanceNote.REGION_TYPE.name());
		String projectPeriodId = (String) params.getParams().get(TableConstants.ProjectRegionProcedureRelate.PROJECT_PERIOD_ID.name());
		if(projectPeriodId==null){
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}
		String regionTypeId = null;
		if (DataConstants.REGION_PERIOD_TYPE_VAL.equals(regionType)) {
			regionTypeId = (String) projectRegionTypeService
					.getProjectRegionType(ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
							DataConstants.REGION_PERIOD_TYPE), ddBB)
					.get(TableConstants.ProjectRegionType.id.name());
		} else if (DataConstants.REGION_BUILDING_TYPE_VAL.equals(regionType)) {
			regionTypeId = (String) projectRegionTypeService
					.getProjectRegionType(ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
							DataConstants.REGION_BUILDING_TYPE), ddBB)
					.get(TableConstants.ProjectRegionType.id.name());
		} else if (DataConstants.REGION_FLOOR_TYPE_VAL.equals(regionType)) {
			regionTypeId = (String) projectRegionTypeService
					.getProjectRegionType(ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
							DataConstants.REGION_FLOOR_TYPE), ddBB)
					.get(TableConstants.ProjectRegionType.id.name());
		} else if (DataConstants.REGION_ROOM_TYPE_VAL.equals(regionType)) {
			regionTypeId = (String) projectRegionTypeService
					.getProjectRegionType(ParamsMap.newMap(TableConstants.ProjectRegionType.REGION_TYPE_NAME.name(),
							DataConstants.REGION_ROOM_TYPE), ddBB)
					.get(TableConstants.ProjectRegionType.id.name());
		}
		
		Map<String, Object> map = new LinkedHashMap<>();
		map.put(TableConstants.ProjectRegionProcedureRelate.tenantId.name(),tenantId);
		map.put(TableConstants.ProjectRegionProcedureRelate.projectPeriodId.name(),projectPeriodId);
		if(regionTypeId!=null){
			map.put(TableConstants.ProjectRegionProcedureRelate.regionTypeId.name(),regionTypeId);
		}
		
		//根据部位类型找到工序
		List<Map<String, Object>> procedureList=procedureInfoService.getListByRegion(map, ddBB);
		//找到工序的树结构列表
		List<Map<String, Object>> procedureTypeList=procedureTypeService.getProcedureTypeByProjectRegionParams(map, ddBB);

		return new BaseResult(ReturnCode.OK,TreeUtil.getTree(procedureTypeList, procedureList,TableConstants.ProcedureInfo.procedureTypeId.name()));
	}

	/**
	 * 获取工序的一般主控项
	 * 
	 * @param tenantId
	 * @param params
	 * @param ddBB
	 * @return
	 */
	@RequestMapping("/procedureInfo/getItemList")
	@ResponseBody
	public BaseResult getItemList(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);

		JSONObject object = new JSONObject();
		object.put("dominantItem", dominantItemService.getDominantItemList(map, ddBB));
		object.put("generalItem", generalItemService.getGeneralItemList(map, ddBB));

		return new BaseResult(ReturnCode.OK, object);
	}

}
