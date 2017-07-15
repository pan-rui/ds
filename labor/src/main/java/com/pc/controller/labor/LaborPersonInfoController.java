package com.pc.controller.labor;

 
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.pc.util.ExcelUtils;
import com.pc.util.ImgUtil;
import com.pc.util.ZipUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
 
import com.pc.service.labor.impl.LaborPersonInfoService;
import com.pc.service.labor.impl.LaborProjectPersonInfoService;
import com.pc.service.labor.impl.LaborProjectPersonnelContractRelateService;
import com.pc.task.PushLaborDataTask;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 
 * @Author: wady (2017-03-28 20:19)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-28
 */
@Controller
@RequestMapping("/admin")
public class LaborPersonInfoController extends BaseController {
	@Autowired
	private LaborPersonInfoService laborPersonInfoService;
	@Autowired
	private LaborProjectPersonInfoService laborProjectPersonInfoService;
	@Autowired
	private LaborProjectPersonnelContractRelateService laborProjectPersonnelContractRelateService;
	@Autowired
	private ExcelUtils excelUtils;
	@Autowired
	private PushLaborDataTask pushLaborDataTask;

	private Logger logger = LogManager.getLogger(this.getClass());
	
	@RequestMapping("/laborPersonInfo/pushData")
	@ResponseBody
	public BaseResult pushData(@RequestAttribute String ddBB) throws IOException {
		pushLaborDataTask.pushProjectData("10fc6cf1c451440096ee57587a857f85");
		return new BaseResult(ReturnCode.OK);
	}
	
	@RequestMapping("/laborPersonInfo/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		
        Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		
		map.put(TableConstants.IS_SEALED, 0); 
		laborPersonInfoService.addLaborPersonInfo(map, ddBB);
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/laborPersonInfo/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = laborPersonInfoService.deleteLaborPersonInfo(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/laborPersonInfo/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = laborPersonInfoService.updateLaborPersonInfo(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}

	}

	@RequestMapping("/laborPersonInfo/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, laborPersonInfoService.getLaborPersonInfo(map, ddBB));
	}

	@RequestMapping("/laborPersonInfo/getList")
	@ResponseBody
	public BaseResult getList(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, laborPersonInfoService.getLaborPersonInfoList(map, ddBB));
	}

	@RequestMapping("/laborPersonInfo/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, laborPersonInfoService.getLaborPersonInfoPage(page, ddBB));
	}

	@RequestMapping(value = "/laborPersonInfo/import",method = RequestMethod.POST)
	@ResponseBody
	public BaseResult importData(HttpServletRequest request,@RequestAttribute String ddBB, @RequestHeader(Constants.TENANT_ID) String tenantId, @RequestAttribute(Constants.USER_ID) String userId, @RequestParam String projectId, MultipartFile file) {
		List fields = Arrays.asList("EMP_NAME", "ID_CODE", "", "", "EMP_PHONE", "EMP_NATIVE_PROVINCE", "HOME_ADDR", "EMP_NATION", "EMP_BIRTHDATE","USER_EDUCATION","EMP_NATIVEPLACE","","","","","","","","","","","","","","","","","","","HAS_CERTIFICATE","CERTIFICATE_NAME","","","","","","","","","","ID_AGENCY","ID_VALIDDATE","");
		try {
			excelUtils.importPerson(file,ddBB+TableConstants.SEPARATE+TableConstants.LABOR_PERSON_INFO,fields,tenantId,userId,projectId);
		} catch (IOException e) {
			e.printStackTrace();
			return new BaseResult(1, e.getMessage());
		}
		return new BaseResult(0, "OK");
	}

	
	/**
	 * 
	 * @param file 压缩文件
	 * @param projectId 项目编号
	 * @param fileType 1为身份证正面或反面照片，3为身份证相片，4为现场采集照片，5为合同
	 * @param ddBB
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/laborPersonInfo/uploadFile")
	@ResponseBody
	public BaseResult fileUpload(@RequestParam("file") CommonsMultipartFile file,@RequestParam("projectId") String projectId,
			@RequestParam("fileType") String fileType, @RequestAttribute String ddBB) throws IOException {
		if(StringUtils.isBlank(fileType)||StringUtils.isBlank(projectId)){
			return new BaseResult(ReturnCode.REQUEST_PARAMS_MISSING_ERROR);
		}
		
		new File(ImgUtil.PROJECT_FILE_PATH + projectId + "/" + fileType +"/").mkdirs();
		String fileName = file.getOriginalFilename();
		String path = projectId +"/"+ fileType +"/" + UUID.randomUUID().toString() + "." + fileName.split("\\.")[1];
		File newFile = new File(ImgUtil.PROJECT_FILE_PATH + path);
		file.transferTo(newFile);
		
		//解压目录
		ZipUtil.unzip(ImgUtil.PROJECT_FILE_PATH + path);
		
		List<Map<String, Object>> idcodeList=new ArrayList<>();
		File [] projectFiles=new File(ImgUtil.PROJECT_FILE_PATH + projectId + "/" + fileType).listFiles();
		
		List<Map<String, Object>> list=null;
		if("5".equals(fileType)){
			list=laborProjectPersonnelContractRelateService.getLaborContractListByFile(projectId, ddBB);
		}else{
			list=laborProjectPersonInfoService.getLaborPersonListByFileType(projectId, fileType, ddBB);
		}
		
		for(Map<String, Object> obj:list){
			String idcard=(String) obj.get(TableConstants.LaborPersonInfo.idCode.name());
			String empName=(String) obj.get(TableConstants.LaborPersonInfo.empName.name());
			String fileMinName=null;
			if("5".equals(fileType)){
				fileMinName=(String) obj.get(TableConstants.LaborProjectPersonnelContractRelate.contractNo.name());
			}else if("1".equals(fileType)){
				fileMinName=idcard+"_";
			}else{
				fileMinName=idcard+"_"+fileType+".";
			}
			boolean b=false;
			boolean b1=false;
			boolean b2=false;
			for (File projectFile:projectFiles) {
				if(projectFile.getName().startsWith(fileMinName)){
					Map<String, Object> map=new HashMap<>();
					Map<String, Object> pmap=new HashMap<>();
					if("1".equals(fileType)&&projectFile.getName().startsWith(fileMinName+"1.")){
						map.put(TableConstants.LaborPersonInfo.IDPHOTO_SCAN.name(), projectId+"/"+fileType+"/"+projectFile.getName());
						map.put(TableConstants.LaborPersonInfo.ID.name(), obj.get(TableConstants.LaborProjectPersonInfo.personId.name()));
						laborPersonInfoService.updateLaborPersonInfo(map, ddBB);
						
						pmap.put(TableConstants.LaborProjectPersonInfo.IS_SYNCHRO.name(), DataConstants.NOT_SYNCHRO);
						pmap.put(TableConstants.LaborProjectPersonInfo.ID.name(), obj.get(TableConstants.LaborProjectPersonInfo.id.name()));
						laborProjectPersonInfoService.updateLaborProjectPersonInfo(pmap, ddBB);
						
						b1=true;
						if(b2){
							b=true;
							break;
						}
					}else if("1".equals(fileType)&&projectFile.getName().startsWith(fileMinName+"2.")){
						map.put(TableConstants.LaborPersonInfo.IDPHOTO_SCAN2.name(), projectId+"/"+fileType+"/"+projectFile.getName());
						map.put(TableConstants.LaborPersonInfo.ID.name(), obj.get(TableConstants.LaborProjectPersonInfo.personId.name()));
						laborPersonInfoService.updateLaborPersonInfo(map, ddBB);
						
						pmap.put(TableConstants.LaborProjectPersonInfo.IS_SYNCHRO.name(), DataConstants.NOT_SYNCHRO);
						pmap.put(TableConstants.LaborProjectPersonInfo.ID.name(), obj.get(TableConstants.LaborProjectPersonInfo.id.name()));
						laborProjectPersonInfoService.updateLaborProjectPersonInfo(pmap, ddBB);
						
						b2=true;
						if(b1){
							b=true;
							break;
						}
					}else if("3".equals(fileType)){
						map.put(TableConstants.LaborPersonInfo.ID_PHOTO.name(), projectId+"/"+fileType+"/"+projectFile.getName());
						map.put(TableConstants.LaborPersonInfo.ID.name(), obj.get(TableConstants.LaborProjectPersonInfo.personId.name()));
						laborPersonInfoService.updateLaborPersonInfo(map, ddBB);
						
						pmap.put(TableConstants.LaborProjectPersonInfo.IS_SYNCHRO.name(), DataConstants.NOT_SYNCHRO);
						pmap.put(TableConstants.LaborProjectPersonInfo.ID.name(), obj.get(TableConstants.LaborProjectPersonInfo.id.name()));
						laborProjectPersonInfoService.updateLaborProjectPersonInfo(pmap, ddBB);
						
						b=true;
						break;
					}else if("4".equals(fileType)){
						map.put(TableConstants.LaborProjectPersonInfo.IS_SYNCHRO.name(), DataConstants.NOT_SYNCHRO);
						map.put(TableConstants.LaborProjectPersonInfo.FACEPHOTO.name(), projectId+"/"+fileType+"/"+projectFile.getName());
						map.put(TableConstants.LaborProjectPersonInfo.ID.name(), obj.get(TableConstants.LaborProjectPersonInfo.id.name()));
						laborProjectPersonInfoService.updateLaborProjectPersonInfo(map, ddBB);
						
						b=true;
						break;
					}else if("5".equals(fileType)){
						map.put(TableConstants.LaborProjectPersonnelContractRelate.IS_SYNCHRO.name(), DataConstants.NOT_SYNCHRO);
						map.put(TableConstants.LaborProjectPersonnelContractRelate.CONTRACT_FILE.name(), projectId+"/"+fileType+"/"+projectFile.getName());
						map.put(TableConstants.LaborProjectPersonnelContractRelate.ID.name(), obj.get(TableConstants.LaborProjectPersonnelContractRelate.id.name()));
						laborProjectPersonnelContractRelateService.updateLaborProjectPersonnelContractRelate(map, ddBB);
						
						b=true;
						break;
					}
				}
			}
			if(b1&b2){
				b=true;
			}
			if(!b){
				Map<String, Object> user=new HashMap<>();
				user.put("idcard", idcard);
				user.put("empName", empName);
				idcodeList.add(user);
			}
		}
		
		return new BaseResult(ReturnCode.OK, idcodeList);
	}
}
