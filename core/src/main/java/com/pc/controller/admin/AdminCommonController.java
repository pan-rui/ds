package com.pc.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.TableConstants;
import com.pc.util.ApkUtil;
import com.pc.util.ImgUtil;
import com.pc.vo.ParamsVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminCommonController extends BaseController {

	@RequestMapping("/common/getTables")
	@ResponseBody
	public BaseResult getTables(@EncryptProcess ParamsVo pv) {
		return new BaseResult(ReturnCode.OK, TableConstants.Tables.values());
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
	
	@RequestMapping("/file/uploadApk")
	@ResponseBody
	public BaseResult uploadApk(@RequestParam("file") CommonsMultipartFile file) throws IOException {
		new File(ImgUtil.BASE_PATH + ImgUtil.TEMP_APK_PATH).mkdirs();
		String fileName = file.getOriginalFilename();
		String path = ImgUtil.TEMP_APK_PATH + UUID.randomUUID().toString() + "." + fileName.split("\\.")[1];
		File newFile = new File(ImgUtil.BASE_PATH + path);
		file.transferTo(newFile);
		
		Map<String, Object> map=ApkUtil.getApkInfo(ImgUtil.BASE_PATH + path);
		map.put("path", path);
		double fileSize=new BigDecimal(file.getSize()/1024.0/1024).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
		map.put("fileSize", fileSize);
		
		map.put("fileName", fileName);
		
		return new BaseResult(ReturnCode.OK, map);
	}
	
}
