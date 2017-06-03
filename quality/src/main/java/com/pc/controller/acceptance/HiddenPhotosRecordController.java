package com.pc.controller.acceptance;


import com.alibaba.fastjson.JSONObject;
import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.DataConstants;
import com.pc.core.Page;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.service.acceptance.impl.HiddenPhotosRecordService;
import com.pc.service.acceptance.impl.HiddenPhotosService;
import com.pc.service.project.impl.HouseholdChartAreaService;
import com.pc.service.project.impl.ProjectHouseholdService;
import com.pc.service.project.impl.UnitChartService;
import com.pc.util.DateUtil;
import com.pc.util.ImgUtil;
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

/**
 * @Description: 
 * @Author: wady (2017-03-28 20:19)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-28
 */
@Controller
@RequestMapping("/client")
public class HiddenPhotosRecordController extends BaseController {
	@Autowired
	private HiddenPhotosRecordService hiddenPhotosRecordService;
	
	@Autowired
	private HiddenPhotosService hiddenPhotosService;
	
	@Autowired
	private ProjectHouseholdService projectHouseholdService;
	
	@Autowired
	private UnitChartService unitChartService;

	@Autowired
	private HouseholdChartAreaService householdChartAreaService;

	private Logger logger = LogManager.getLogger(this.getClass());
	
	
	@RequestMapping("/hiddenPhotosRecord/getHiddenPhotos")
	@ResponseBody
	public BaseResult getHiddenPhotos(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		String projectHouseholdId = (String) params.getParams().get(TableConstants.HiddenPhotosRecord.ROOM_ID.name());
		String householdAreaId = (String) params.getParams().get(TableConstants.HiddenPhotosRecord.HOUSEHOLD_AREA_ID.name());
		if(projectHouseholdId==null||householdAreaId==null){
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}
		Map<String, Object> hiddenPhotosRecord=hiddenPhotosRecordService.getHiddenPhotosRecord(ParamsMap.newMap(TableConstants.HiddenPhotosRecord.ROOM_ID.name(), projectHouseholdId).
				addParams(TableConstants.HiddenPhotosRecord.HOUSEHOLD_AREA_ID.name(), householdAreaId), ddBB);
		
		JSONObject jsonObject=new JSONObject();
		if(hiddenPhotosRecord==null){
			jsonObject.put("ztPhotos", new ArrayList<>());
			jsonObject.put("fbPhotos", new ArrayList<>());
			
			return new BaseResult(ReturnCode.OK,jsonObject);
		}
		
		List<Map<String, Object>> ztPhotos=hiddenPhotosService.getHiddenPhotosList(ParamsMap
				.newMap(TableConstants.HiddenPhotos.HIDDEN_PHOTO_RECORD_ID.name(), (String)hiddenPhotosRecord.get(TableConstants.HiddenPhotosRecord.id.name()))
				.addParams(TableConstants.HiddenPhotos.HIDDEN_PHOTO_TYPE.name(), DataConstants.HIDDEN_PHOTO_TYPE_ZT), ddBB);
		
		List<Map<String, Object>> fbPhotos=hiddenPhotosService.getHiddenPhotosList(ParamsMap
				.newMap(TableConstants.HiddenPhotos.HIDDEN_PHOTO_RECORD_ID.name(), (String)hiddenPhotosRecord.get(TableConstants.HiddenPhotosRecord.id.name()))
				.addParams(TableConstants.HiddenPhotos.HIDDEN_PHOTO_TYPE.name(), DataConstants.HIDDEN_PHOTO_TYPE_FB), ddBB);
		
		jsonObject.put("ztPhotos", ztPhotos);
		jsonObject.put("fbPhotos", fbPhotos);
		
		return new BaseResult(ReturnCode.OK,jsonObject);
	}
	
	@RequestMapping("/hiddenPhotosRecord/addHiddenPhotos")
	@ResponseBody
	public BaseResult addHiddenPhotos(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		String projectHouseholdId = (String) params.getParams().get(TableConstants.HiddenPhotosRecord.ROOM_ID.name());
		String householdAreaId = (String) params.getParams().get(TableConstants.HiddenPhotosRecord.HOUSEHOLD_AREA_ID.name());
		if(projectHouseholdId==null||householdAreaId==null){
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}
		Map<String, Object> projectHousehold=projectHouseholdService.getByID(projectHouseholdId, ddBB);
		if(projectHousehold==null){
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}
		
		Map<String, Object> hiddenPhotosRecord=hiddenPhotosRecordService.getHiddenPhotosRecord(ParamsMap.newMap(TableConstants.HiddenPhotosRecord.ROOM_ID.name(), projectHouseholdId).
				addParams(TableConstants.HiddenPhotosRecord.HOUSEHOLD_AREA_ID.name(), householdAreaId), ddBB);
		
		String hiddenPhotosRecordId=null;
		int photoNum=0;
		if(hiddenPhotosRecord==null){
			hiddenPhotosRecord=new HashMap<String, Object>();
			hiddenPhotosRecord.put(TableConstants.TENANT_ID, tenantId);
			hiddenPhotosRecord.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
			hiddenPhotosRecord.put(TableConstants.UPDATE_USER_ID, userId);
			hiddenPhotosRecord.put(TableConstants.IS_SEALED, 0);
			hiddenPhotosRecord.put(TableConstants.HiddenPhotosRecord.HOUSEHOLD_AREA_ID.name(), householdAreaId);
			hiddenPhotosRecord.put(TableConstants.HiddenPhotosRecord.ROOM_ID.name(), projectHouseholdId);
			hiddenPhotosRecord.put(TableConstants.HiddenPhotosRecord.PHOTO_NUM.name(), 0);
			hiddenPhotosRecordService.addHiddenPhotosRecord(hiddenPhotosRecord, ddBB);
			hiddenPhotosRecordId=(String) hiddenPhotosRecord.get(TableConstants.HiddenPhotosRecord.ID.name());
		}else{
			hiddenPhotosRecordId=(String) hiddenPhotosRecord.get(TableConstants.HiddenPhotosRecord.id.name());
			photoNum=(Integer)hiddenPhotosRecord.get(TableConstants.HiddenPhotosRecord.photoNum.name());
		}
		
		List<Map<String, Object>> ztPhotos=(List<Map<String, Object>>) params.getParams().get("ZT_PHOTOS");
		List<Map<String, Object>> fbPhotos=(List<Map<String, Object>>) params.getParams().get("FB_PHOTOS");
		
		String path=null;
		
		String idTree=((String) projectHousehold.get(TableConstants.ProjectHousehold.idTree.name())).replace(">", "/");
		for(Map<String, Object> photo:ztPhotos){
			String p=(String)photo.get(TableConstants.HiddenPhotos.ATTACH_PATH.name());
        	if(p.contains(ImgUtil.TEMP_PATH)){
        		path=ImgUtil.saveAndUpdateFile(null,p, null, ImgUtil.ACCEPTANCE_IMG_PATH, idTree+ImgUtil.HIDDEEN_ACCEPTANCE_IMG_PATH);
    			if(path!=null){
    				photo.put(TableConstants.HiddenPhotos.HIDDEN_PHOTO_RECORD_ID.name(), hiddenPhotosRecordId);
    				photo.put(TableConstants.HiddenPhotos.HIDDEN_PHOTO_TYPE.name(), DataConstants.HIDDEN_PHOTO_TYPE_ZT);
    				photo.put(TableConstants.HiddenPhotos.ATTACH_PATH.name(), path);
    				photo.put(TableConstants.HiddenPhotos.UPLOAD_TIME.name(), DateUtil.convertDateTimeToString(new Date(), null));
    				hiddenPhotosService.addHiddenPhotos(photo, ddBB);
    				photoNum++;
    			}
        	}
		}
		
		for(Map<String, Object> photo:fbPhotos){
			String p=(String)photo.get(TableConstants.HiddenPhotos.ATTACH_PATH.name());
        	if(p.contains(ImgUtil.TEMP_PATH)){
				path=ImgUtil.saveAndUpdateFile(null,p, null, ImgUtil.ACCEPTANCE_IMG_PATH, idTree+ImgUtil.HIDDEEN_ACCEPTANCE_IMG_PATH);
				if(path!=null){
					photo.put(TableConstants.HiddenPhotos.HIDDEN_PHOTO_RECORD_ID.name(), hiddenPhotosRecordId);
					photo.put(TableConstants.HiddenPhotos.HIDDEN_PHOTO_TYPE.name(), DataConstants.HIDDEN_PHOTO_TYPE_FB);
					photo.put(TableConstants.HiddenPhotos.ATTACH_PATH.name(), path);
					photo.put(TableConstants.HiddenPhotos.UPLOAD_TIME.name(), DateUtil.convertDateTimeToString(new Date(), null));
					hiddenPhotosService.addHiddenPhotos(photo, ddBB);
					photoNum++;
				}
        	}
		}
		
		HashMap<String, Object> updateHiddenPhotosRecord=new HashMap<String, Object>();
		updateHiddenPhotosRecord.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		updateHiddenPhotosRecord.put(TableConstants.UPDATE_USER_ID, userId);
		updateHiddenPhotosRecord.put(TableConstants.HiddenPhotosRecord.PHOTO_NUM.name(), photoNum);
		updateHiddenPhotosRecord.put(TableConstants.HiddenPhotosRecord.ID.name(), hiddenPhotosRecordId);
		hiddenPhotosRecordService.updateHiddenPhotosRecord(updateHiddenPhotosRecord, ddBB);
		
		return new BaseResult(ReturnCode.OK);
	}
	
	@RequestMapping("/hiddenPhotosRecord/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		
                Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		map.put(TableConstants.IS_SEALED, 0); 
		hiddenPhotosRecordService.addHiddenPhotosRecord(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/hiddenPhotosRecord/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = hiddenPhotosRecordService.deleteHiddenPhotosRecord(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/hiddenPhotosRecord/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = hiddenPhotosRecordService.updateHiddenPhotosRecord(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/hiddenPhotosRecord/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, hiddenPhotosRecordService.getHiddenPhotosRecord(map, ddBB));
	}

	@RequestMapping("/hiddenPhotosRecord/getList")
	@ResponseBody
	public BaseResult getList(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, hiddenPhotosRecordService.getHiddenPhotosRecordList(map, ddBB));
	}

	@RequestMapping("/hiddenPhotosRecord/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, hiddenPhotosRecordService.getHiddenPhotosRecordPage(page, ddBB));
	}
}
