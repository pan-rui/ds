package com.pc.controller.labor;

 
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pc.controller.BaseController;
import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.core.DataConstants;
import com.pc.core.Page;
import com.pc.core.ParamsMap;
import com.pc.util.DateUtil;
import com.pc.vo.ParamsVo;

import com.pc.core.TableConstants;
 
import com.pc.service.labor.impl.LaborAttendanceDetailRecordService;
import com.pc.service.labor.impl.LaborDeviceInfoService;
import com.pc.service.labor.impl.LaborProjectInfoService;

/**
 * @Description: 
 * @Author: wady (2017-03-28 20:19)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-28
 */
@Controller
@RequestMapping("/admin")
public class LaborAttendanceDetailRecordController extends BaseController {
	@Autowired
	private LaborAttendanceDetailRecordService laborAttendanceDetailRecordService;
	
	@Autowired
	private LaborProjectInfoService laborProjectInfoService;
	
	@Autowired
	private LaborDeviceInfoService laborDeviceInfoService;

	private Logger logger = LogManager.getLogger(this.getClass());
	
	
	@RequestMapping("/laborAttendanceDetailRecord/addList")
	@ResponseBody
	public BaseResult addList(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		List<Map<String, Object>> list=(List<Map<String, Object>>) params.getParams().get("LIST");
		String projectId=(String) params.getParams().get(TableConstants.LaborAttendanceDetailRecord.PROJECT_ID.name());
		
		Map<String, Object> project=laborProjectInfoService.getByID(projectId, ddBB);
		
		if(project==null||list==null||list.size()==0){
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}
		
		Map<String, Object> device=laborDeviceInfoService.getLaborDeviceInfoByProject(projectId, ddBB);
		
		List<Map<String, Object>> attendanceList=new ArrayList<Map<String,Object>>();
		for(Map<String, Object> map:list){
			Map<String, Object> attendance = new LinkedHashMap<>();
			attendance.put(TableConstants.TENANT_ID, tenantId);
			attendance.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
			attendance.put(TableConstants.UPDATE_USER_ID, userId);
			attendance.put(TableConstants.IS_SEALED, 0);
			attendance.put(TableConstants.LaborAttendanceDetailRecord.IS_SYNCHRO.name(), DataConstants.NOT_SYNCHRO);
			attendance.put(TableConstants.LaborAttendanceDetailRecord.PROJECT_ID.name(), projectId);
			attendance.put(TableConstants.LaborAttendanceDetailRecord.DEVICE_ID.name(), device.get(TableConstants.LaborDeviceInfo.id.name()));
			
			attendance.put(TableConstants.LaborAttendanceDetailRecord.PERSON_CARDID.name(), map.get(TableConstants.LaborAttendanceDetailRecord.PERSON_CARDID.name()));
			attendance.put(TableConstants.LaborAttendanceDetailRecord.PERSON_TYPE.name(), map.get(TableConstants.LaborAttendanceDetailRecord.PERSON_TYPE.name()));
			attendance.put(TableConstants.LaborAttendanceDetailRecord.PASSED_TIME.name(), map.get(TableConstants.LaborAttendanceDetailRecord.PASSED_TIME.name()));
			attendance.put(TableConstants.LaborAttendanceDetailRecord.DIRECTION.name(), map.get(TableConstants.LaborAttendanceDetailRecord.DIRECTION.name()));
			attendance.put(TableConstants.LaborAttendanceDetailRecord.WAY.name(), map.get(TableConstants.LaborAttendanceDetailRecord.WAY.name()));
			attendance.put(TableConstants.LaborAttendanceDetailRecord.SITE_PHOTO.name(), map.get(TableConstants.LaborAttendanceDetailRecord.SITE_PHOTO.name()));
			
			attendance.put(TableConstants.LaborAttendanceDetailRecord.ID.name(), UUID.randomUUID().toString().replace("-", ""));
			attendanceList.add(attendance);
		}
        
		laborAttendanceDetailRecordService.addLaborAttendanceDetailRecordList(attendanceList, ddBB);
		return new BaseResult(ReturnCode.OK);
	}
	
	@RequestMapping("/laborAttendanceDetailRecord/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		
                Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		map.put(TableConstants.IS_SEALED, 0); 
		laborAttendanceDetailRecordService.addLaborAttendanceDetailRecord(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/laborAttendanceDetailRecord/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = laborAttendanceDetailRecordService.deleteLaborAttendanceDetailRecord(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/laborAttendanceDetailRecord/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = laborAttendanceDetailRecordService.updateLaborAttendanceDetailRecord(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/laborAttendanceDetailRecord/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, laborAttendanceDetailRecordService.getLaborAttendanceDetailRecord(map, ddBB));
	}

	@RequestMapping("/laborAttendanceDetailRecord/getList")
	@ResponseBody
	public BaseResult getList(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, laborAttendanceDetailRecordService.getLaborAttendanceDetailRecordList(map, ddBB));
	}

	@RequestMapping("/laborAttendanceDetailRecord/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, laborAttendanceDetailRecordService.getLaborAttendanceDetailRecordPage(page, ddBB));
	}
}
