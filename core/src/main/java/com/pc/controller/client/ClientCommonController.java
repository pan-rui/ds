package com.pc.controller.client;

import com.alibaba.fastjson.JSONArray;
import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.TableConstants;
import com.pc.service.user.impl.FeedbackInfoService;
import com.pc.util.DateUtil;
import com.pc.util.ImgUtil;
import com.pc.util.WeatherUtil;
import com.pc.vo.ParamsVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

@Controller
@RequestMapping("/client")
public class ClientCommonController extends BaseController {
	
	@Autowired
	private FeedbackInfoService feedbackInfoService;
	
	@RequestMapping("/feedbackInfo/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		
        Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		map.put(TableConstants.IS_SEALED, 0); 
		
		map.put(TableConstants.FeedbackInfo.FEEDBACK_TIME.name(), DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.FeedbackInfo.FEEDBACK_USER_ID.name(), userId);
		
		feedbackInfoService.addFeedbackInfo(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/common/getWeather")
	@ResponseBody
	public BaseResult getWeather(@EncryptProcess ParamsVo pv) {
		String cityName = (String) pv.getParams().get("CITY_NAME");
		return new BaseResult(ReturnCode.OK, WeatherUtil.getWeather(cityName));
	}
	
	@RequestMapping("/img/upload")
	@ResponseBody
	public BaseResult fileUpload(@RequestParam("file") CommonsMultipartFile file) throws IOException {
		new File(ImgUtil.BASE_PATH + ImgUtil.TEMP_PATH).mkdirs();
		String fileName = file.getOriginalFilename();
		String path = ImgUtil.TEMP_PATH + UUID.randomUUID().toString() + "." + fileName.split("\\.")[1];
		File newFile = new File(ImgUtil.BASE_PATH + path);
		file.transferTo(newFile);
		return new BaseResult(ReturnCode.OK, path);
	}
	
	@RequestMapping("/img/uploadFiles")
	@ResponseBody
	public BaseResult uploadImgs(@RequestParam("file") CommonsMultipartFile[] files) throws IOException {
		new File(ImgUtil.BASE_PATH + ImgUtil.TEMP_PATH).mkdirs();
		JSONArray jsonArray=new JSONArray();
		for(CommonsMultipartFile file:files){
			String fileName = file.getOriginalFilename();
			String path = ImgUtil.TEMP_PATH + UUID.randomUUID().toString() + "." + fileName.split("\\.")[1];
			File newFile = new File(ImgUtil.BASE_PATH + path);
			file.transferTo(newFile);
			jsonArray.add(path);
		}
		
		return new BaseResult(ReturnCode.OK, jsonArray);
	}
	
	@RequestMapping("/img/uploadImgs")
	@ResponseBody
	public BaseResult uploadImgs(HttpServletRequest request) throws IOException, ServletException {
		MultiValueMap<String, MultipartFile> fileMap=((DefaultMultipartHttpServletRequest) request).getMultiFileMap();
		if(fileMap==null||fileMap.size()==0){
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}
		new File(ImgUtil.BASE_PATH + ImgUtil.TEMP_PATH).mkdirs();
		JSONArray jsonArray=new JSONArray();
		for(int i=0;i<fileMap.size();i++){
			MultipartFile file=fileMap.get("file"+i).get(0);
			String fileName = file.getOriginalFilename();
			String path = ImgUtil.TEMP_PATH + UUID.randomUUID().toString() + "." + fileName.split("\\.")[1];
			File newFile = new File(ImgUtil.BASE_PATH + path);
			file.transferTo(newFile);
			jsonArray.add(path);
		}
		
		return new BaseResult(ReturnCode.OK, jsonArray);
	}
}
