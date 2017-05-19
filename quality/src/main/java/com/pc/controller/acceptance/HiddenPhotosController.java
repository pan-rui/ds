package com.pc.controller.acceptance;


import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.acceptance.impl.HiddenPhotosRecordService;
import com.pc.service.acceptance.impl.HiddenPhotosService;
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
public class HiddenPhotosController extends BaseController {
	@Autowired
	private HiddenPhotosService hiddenPhotosService;
	
	@Autowired
	private HiddenPhotosRecordService hiddenPhotosRecordService;

	private Logger logger = LogManager.getLogger(this.getClass());
	
	
	@RequestMapping("/hiddenPhotos/deleteList")
	@ResponseBody
	public BaseResult deleteList(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		List<Map<String, Object>> list = (List<Map<String, Object>>) params.getParams().get(TableConstants.HIDDEN_PHOTOS);
		
		if(list==null||list.size()==0){
			return new BaseResult(ReturnCode.OK);
		}
		String hiddenPhotosRecordId=null;
		int i=0;
		for (Map<String, Object> photo:list) {
			Map<String, Object> hiddenPhoto=hiddenPhotosService.getByID((String)photo.get(TableConstants.HiddenPhotos.ID.name()), ddBB);
			if(hiddenPhoto!=null){
				hiddenPhotosService.deleteHiddenPhotos(photo, ddBB);
				i++;
				hiddenPhotosRecordId=(String) hiddenPhoto.get(TableConstants.HiddenPhotos.hiddenPhotoRecordId.name());
			}
		}
		if(hiddenPhotosRecordId!=null){
			Map<String, Object> hiddenPhotosRecord=hiddenPhotosRecordService.getByID(hiddenPhotosRecordId, ddBB);
			Map<String, Object> updateHiddenPhotosRecord=new HashMap<>();
			Integer photoNum=(Integer) hiddenPhotosRecord.get(TableConstants.HiddenPhotosRecord.photoNum.name())-i;
			updateHiddenPhotosRecord.put(TableConstants.HiddenPhotosRecord.PHOTO_NUM.name(), photoNum);
			updateHiddenPhotosRecord.put(TableConstants.HiddenPhotosRecord.ID.name(), hiddenPhotosRecordId);
			hiddenPhotosRecordService.updateHiddenPhotosRecord(updateHiddenPhotosRecord, ddBB);
		}
		
		return new BaseResult(ReturnCode.OK);
	}
	
	@RequestMapping("/hiddenPhotos/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		
        Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		hiddenPhotosService.addHiddenPhotos(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/hiddenPhotos/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		boolean b = hiddenPhotosService.deleteHiddenPhotos(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/hiddenPhotos/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		boolean b = hiddenPhotosService.updateHiddenPhotos(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/hiddenPhotos/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, hiddenPhotosService.getHiddenPhotos(map, ddBB));
	}

	@RequestMapping("/hiddenPhotos/getList")
	@ResponseBody
	public BaseResult getList(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, hiddenPhotosService.getHiddenPhotosList(map, ddBB));
	}

	@RequestMapping("/hiddenPhotos/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, hiddenPhotosService.getHiddenPhotosPage(page, ddBB));
	}
}
