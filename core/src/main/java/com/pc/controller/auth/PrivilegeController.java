package com.pc.controller.auth;

import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.ColumnConstants;
import com.pc.core.ColumnProcess;
import com.pc.service.auth.DataTypeService;
import com.pc.service.auth.UserDataPrivilegesService;
import com.pc.vo.ParamsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 权限访问接口
 * @Author: wady (2017-03-30 17:49)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-30
 */
@Controller
public class PrivilegeController extends BaseController {

	@Autowired
	private UserDataPrivilegesService userDataPrivilegesService;

	@Autowired
	private DataTypeService dataTypeService;

	/**
	 * 根据数据类型获取用户下所有权限执行语句
	 * 
	 * @param userId
	 *            权限编号
	 * @param tenantId
	 *            租户编号
	 * @param params
	 *            参数内容
	 * @return String 数据库语句
	 */
	@RequestMapping("/privileges/getUserPrivilegesSQLByDataTypeSign")
	@ResponseBody
	public BaseResult getUserPrivilegesSQLByDataTypeSign(@RequestAttribute(Constants.USER_ID) String userId,
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

		return new BaseResult(ReturnCode.OK, userDataPrivilegesService.getUserPrivilegesSQL(dataTypeSignID, userId));
	}

	/**
	 * 根据数据类型获取用户下所有权限
	 * 
	 * @param userId
	 *            权限编号
	 * @param tenantId
	 *            租户编号
	 * @param params
	 *            参数内容
	 * @return String 数据库语句
	 */
	@RequestMapping("/privileges/getUserPrivilegesByDataTypeSign")
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
	 * 根据数据类型获取用户下独立权限执行语句，根据用户权限表直接授权的数据的执行语句
	 * 
	 * @param userId
	 *            权限编号
	 * @param tenantId
	 *            租户编号
	 * @param params
	 *            参数内容
	 * @return String 数据库语句
	 */
	@RequestMapping("/privileges/getPrivilegesSQLByDataTypeSign")
	@ResponseBody
	public BaseResult getPrivilegesSQLByDataTypeSign(@RequestAttribute(Constants.USER_ID) String userId,
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

		return new BaseResult(ReturnCode.OK,
				userDataPrivilegesService.getUserPrivilegesSQLByUP(dataTypeSignID, userId));
	}

	/**
	 * 根据数据类型获取用户权限表授权内容
	 * 
	 * @param userId
	 *            权限编号
	 * @param tenantId
	 *            租户编号
	 * @param params
	 *            参数内容
	 * @return String 数据库语句
	 */
	@RequestMapping("/privileges/getPrivilegesByDataTypeSign")
	@ResponseBody
	public BaseResult getPrivilegesByDataTypeSign(@RequestAttribute(Constants.USER_ID) String userId,
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

		return new BaseResult(ReturnCode.OK, userDataPrivilegesService.getUserPrivilegesByUP(dataTypeSignID, userId));
	}

	/**
	 * 根据数据类型获取用户角色权限表授权内容数据库执行语句
	 * 
	 * @param userId
	 *            权限编号
	 * @param tenantId
	 *            租户编号
	 * @param params
	 *            参数内容
	 * @return String 数据库语句
	 */
	@RequestMapping("/privileges/getPrivilegesSQLByDataTypeSignRole")
	@ResponseBody
	public BaseResult getPrivilegesSQLByDataTypeSignRole(@RequestAttribute(Constants.USER_ID) String userId,
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

		return new BaseResult(ReturnCode.OK,
				userDataPrivilegesService.getUserPrivilegesSQLByURP(dataTypeSignID, userId));
	}

	/**
	 * 根据数据类型获取用户角色权限表授权数据
	 * 
	 * @param userId
	 *            权限编号
	 * @param tenantId
	 *            租户编号
	 * @param params
	 *            参数内容
	 * @return String 数据库语句
	 */
	@RequestMapping("/privileges/getPrivilegesByDataTypeSignRole")
	@ResponseBody
	public BaseResult getPrivilegesByDataTypeSignRole(@RequestAttribute(Constants.USER_ID) String userId,
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

		return new BaseResult(ReturnCode.OK, userDataPrivilegesService.getUserPrivilegesByURP(dataTypeSignID, userId));
	}
}
