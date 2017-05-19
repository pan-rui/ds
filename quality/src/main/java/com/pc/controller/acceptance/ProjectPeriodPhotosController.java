package com.pc.controller.acceptance;

 
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import com.pc.core.Page;
import com.pc.core.ParamsMap;
import com.pc.util.DateUtil;
import com.pc.util.ImgUtil;
import com.pc.vo.ParamsVo;

import com.pc.core.TableConstants;
 
import com.pc.service.acceptance.impl.ProjectPeriodPhotosService;

/**
 * @Description: 
 * @Author: wady (2017-03-28 20:19)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-28
 */
@Controller
@RequestMapping("/client")
public class ProjectPeriodPhotosController extends BaseController {
	@Autowired
	private ProjectPeriodPhotosService projectPeriodPhotosService;

	private Logger logger = LogManager.getLogger(this.getClass());
	
	
	@RequestMapping("/projectPeriodPhotos/addList")
	@ResponseBody
	public BaseResult addList(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		String projectPeriodId=(String) params.getParams().get(TableConstants.ProjectPeriodPhotos.PROJECT_PERIOD_ID.name());
        List<Map<String, Object>> list = (List<Map<String, Object>>) params.getParams().get(TableConstants.PROJECT_PERIOD_PHOTOS);
        if(list==null||StringUtils.isBlank(projectPeriodId)){
        	return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
        }
        
        Map<String, Object> qmap = new LinkedHashMap<>();
		qmap.put(TableConstants.ProjectPeriodPhotos.PROJECT_PERIOD_ID.name(), projectPeriodId);
		int size=projectPeriodPhotosService.getProjectPeriodPhotosList(qmap, ddBB).size()+1;
        
        for(Map<String, Object> map:list){
        	String p=(String) map.get(TableConstants.ProjectPeriodPhotos.PATH.name());
        	if(p.contains(ImgUtil.TEMP_PATH)){
        		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
        		map.put(TableConstants.UPDATE_USER_ID, userId);
        		map.put(TableConstants.IS_SEALED, 0);
        		String path = ImgUtil.saveAndUpdateFile(null,p, null,
    					ImgUtil.ACCEPTANCE_PERIOD_IMG_PATH, projectPeriodId);
        		map.put(TableConstants.ProjectPeriodPhotos.PATH.name(), path);
        		map.put(TableConstants.ProjectPeriodPhotos.PROJECT_PERIOD_ID.name(), projectPeriodId);
        		map.put(TableConstants.ProjectPeriodPhotos.SQNO.name(), size);
        		projectPeriodPhotosService.addProjectPeriodPhotos(map, ddBB);
        		size++;
        	}
        }
		
		return new BaseResult(ReturnCode.OK);
	}
	
	@RequestMapping("/projectPeriodPhotos/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		
                Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		map.put(TableConstants.IS_SEALED, 0); 
		projectPeriodPhotosService.addProjectPeriodPhotos(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/projectPeriodPhotos/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = projectPeriodPhotosService.deleteProjectPeriodPhotos(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/projectPeriodPhotos/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = projectPeriodPhotosService.updateProjectPeriodPhotos(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/projectPeriodPhotos/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, projectPeriodPhotosService.getProjectPeriodPhotos(map, ddBB));
	}

	@RequestMapping("/projectPeriodPhotos/getList")
	@ResponseBody
	public BaseResult getList(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.IS_SEALED, 0);
		return new BaseResult(ReturnCode.OK, projectPeriodPhotosService.getProjectPeriodPhotosList(map, ddBB));
	}

	@RequestMapping("/projectPeriodPhotos/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, projectPeriodPhotosService.getProjectPeriodPhotosPage(page, ddBB));
	}
}
