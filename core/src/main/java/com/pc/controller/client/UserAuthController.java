package com.pc.controller.client;

import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.ColumnConstants;
import com.pc.core.ColumnProcess;
import com.pc.core.DataConstants;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.service.auth.DataTypeService;
import com.pc.service.auth.UserDataPrivilegesService;
import com.pc.service.auth.UserOperatPrivilegesService;
import com.pc.service.procedure.impl.ProcedureTypeService;
import com.pc.util.TreeUtil;
import com.pc.vo.ParamsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/client")
public class UserAuthController extends BaseController {
	@Autowired
	private UserOperatPrivilegesService userOperatPrivilegesService;

	@Autowired
	private UserDataPrivilegesService userDataPrivilegesService;

	@Autowired
	private DataTypeService dataTypeService;

    @Autowired
    private ProcedureTypeService procedureTypeService;

	@RequestMapping("/userAuth/getUserOperatPrivileges")
	@ResponseBody
	public BaseResult getUserOperatPrivileges(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		return new BaseResult(ReturnCode.OK, userOperatPrivilegesService.getUserOperatPrivileges(userId, ddBB));
	}

	/**
	 * 根据数据类型获取用户下所有权限
	 * 
	 * @param userId
	 *            用户编号
	 * @param tenantId
	 *            租户编号
	 * @param params
	 *            参数内容
	 * @return String 数据库语句
	 */
	@RequestMapping("/userAuth/getUserPrivilegesByDataTypeSign")
	@ResponseBody
	public BaseResult getUserPrivilegesByDataTypeSign(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {

		String dataTypeSignID = (String) params.getParams().get(ColumnConstants.DATA_TYPE_ID);

		String dataTypeSignName = (String) params.getParams().get(ColumnConstants.DATA_TYPE_NAME);

		if (dataTypeSignID == null) {
			if (dataTypeSignName != null) {
				Map<String, Object> paramsT = new HashMap<String, Object>();

				paramsT.put(ColumnConstants.DATA_TYPE_NAME, dataTypeSignName);

				Map<String, Object> data = dataTypeService.getDataType(paramsT, ddBB);

				if (data != null) {
					dataTypeSignID = (String) data.get(ColumnProcess.encryptVal(ColumnConstants.ID));
				}
			}
		}

		return new BaseResult(ReturnCode.OK, userDataPrivilegesService.getUserPrivileges(dataTypeSignID, userId));
	}

	/**
	 * 根据数据类型名称获取数据类型ID
	 * 
	 * @param dataTypeSignName
	 *            数据类型名称
	 * @param ddBB
	 *            数据库名称
	 * @return String 数据库语句
	 */
	public String getDataTypeSignIDByName(String dataTypeSignName, String ddBB) {
		String dataTypeSignID = null;

		if (dataTypeSignName != null) {
			Map<String, Object> paramsT = new HashMap<String, Object>();

			paramsT.put(ColumnConstants.DATA_TYPE_NAME, dataTypeSignName);

			Map<String, Object> data = dataTypeService.getDataType(paramsT, ddBB);

			if (data != null) {
				dataTypeSignID = (String) data.get(ColumnProcess.encryptVal(ColumnConstants.ID));
			}
		}

		return dataTypeSignID;
	}

	/**
	 * 根据数据类型获取用户下所有权限
	 * 
	 * @param userId
	 *            用户编号
	 * @param tenantId
	 *            租户编号
	 * @param params
	 *            参数内容
	 * @return String 数据库语句
	 */
	@RequestMapping("/userAuth/getRegionsTree")
	@ResponseBody
	public BaseResult getRegionsTree(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		String dataTypeSignID = null;

		List<Map<String, Object>> listProjectPeriods = null;
		List<Map<String, Object>> listBuildings = null;
		List<Map<String, Object>> listFloors = null;
		List<Map<String, Object>> listRooms = null;


		// 期
		String dataTypeSignName = DataConstants.REGION_PERIOD_TYPE;

		if (dataTypeSignName != null) {
			dataTypeSignID = getDataTypeSignIDByName(dataTypeSignName, ddBB);
		}

		if (dataTypeSignID != null) {
			listProjectPeriods = userDataPrivilegesService.getUserPrivileges(dataTypeSignID, userId);
		}

		// 栋
		dataTypeSignName = DataConstants.REGION_BUILDING_TYPE;

		if (dataTypeSignName != null) {
			dataTypeSignID = getDataTypeSignIDByName(dataTypeSignName, ddBB);
		}

		if (dataTypeSignID != null) {
			listBuildings = userDataPrivilegesService.getUserPrivileges(dataTypeSignID, userId);
		}

		// 层
		dataTypeSignName = DataConstants.REGION_FLOOR_TYPE;

		if (dataTypeSignName != null) {
			dataTypeSignID = getDataTypeSignIDByName(dataTypeSignName, ddBB);
		}

		if (dataTypeSignID != null) {
			listFloors = userDataPrivilegesService.getUserPrivileges(dataTypeSignID, userId);
		}

		// 户
		dataTypeSignName = DataConstants.REGION_ROOM_TYPE;

		if (dataTypeSignName != null) {
			dataTypeSignID = getDataTypeSignIDByName(dataTypeSignName, ddBB);
		}

		if (dataTypeSignID != null) {
			listRooms = userDataPrivilegesService.getUserPrivileges(dataTypeSignID, userId);
		}

		return new BaseResult(ReturnCode.OK, TreeUtil.getRegionTrees(true,null,listProjectPeriods, listBuildings, listFloors, listRooms));
	}

    @RequestMapping("/userAuth/getProcedureTree")
    @ResponseBody
    public BaseResult getProcedureTree(@RequestAttribute(Constants.USER_ID) String userId,
                                       @RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
                                       @RequestAttribute String ddBB) {


        List<Map<String, Object>> procedureList = null;

        String dataTypeSignID = getDataTypeSignIDByName(DataConstants.PROCEDURE_DATA_TYPE, ddBB);
        if (dataTypeSignID != null) {
            procedureList = userDataPrivilegesService.getUserPrivileges(dataTypeSignID, userId);
        }

        List<Map<String, Object>> procedureTypeList = procedureTypeService.getProcedureTypeList(ParamsMap.newMap(TableConstants.TENANT_ID, tenantId)
                .addParams(TableConstants.IS_SEALED, 0).addParams(TableConstants.IS_VALID, 0), ddBB);



        return new BaseResult(ReturnCode.OK);
    }
}
