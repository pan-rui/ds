package com.pc.controller.sys;


import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.sys.impl.AppInfoService;
import com.pc.service.sys.impl.PublishPhotosService;
import com.pc.service.tenant.impl.UpdateVesionInfoService;
import com.pc.util.DateUtil;
import com.pc.util.ImgUtil;
import com.pc.vo.ParamsVo;
import org.apache.commons.lang.StringUtils;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Description: 
 * @Author: wady (2017-03-28 20:19)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-28
 */
@Controller
@RequestMapping("/admin")
public class AppInfoController extends BaseController {
	@Autowired
	private AppInfoService appInfoService;
	
	@Autowired
	private UpdateVesionInfoService updateVesionInfoService;
	
	@Autowired
	private PublishPhotosService publishPhotosService;

	private Logger logger = LogManager.getLogger(this.getClass());
	
	@RequestMapping("/appInfo/edit")
	@ResponseBody
	public BaseResult edit(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		String appName=(String) params.getParams().get(TableConstants.AppInfo.NAME.name());
		String appRemark=(String) params.getParams().get(TableConstants.AppInfo.REMARK.name());
		String appType=(String) params.getParams().get(TableConstants.AppInfo.APP_TYPE.name());
		String appIcon=(String) params.getParams().get(TableConstants.AppInfo.APP_ICON.name());
		String appPackageName=(String) params.getParams().get(TableConstants.AppInfo.APP_PACKAGE_NAME.name());
		String appIosPath=(String) params.getParams().get(TableConstants.AppInfo.APP_IOS_PATH.name());
		String appAndroidPath=(String) params.getParams().get(TableConstants.AppInfo.APP_ANDROID_PATH.name());
		String appId=(String) params.getParams().get(TableConstants.AppInfo.ID.name());
		
		String versionCode=(String) params.getParams().get(TableConstants.UpdateVesionInfo.VERSION_CODE.name());
		String versionName=(String) params.getParams().get(TableConstants.UpdateVesionInfo.VERSION_NAME.name());
		String fileSize=(String) params.getParams().get(TableConstants.UpdateVesionInfo.FILE_SIZE.name());
		String updateType=(String) params.getParams().get(TableConstants.UpdateVesionInfo.UPDATE_TYPE.name());
		String updateContent=(String) params.getParams().get(TableConstants.UpdateVesionInfo.UPDATE_CONTENT.name());
		
		String fileName=(String) params.getParams().get("FILE_NAME");
		List<Map<String, Object>> photos=(List<Map<String, Object>>) params.getParams().get(TableConstants.PUBLISH_PHOTOS);
		
		if(StringUtils.isBlank(versionCode)||StringUtils.isBlank(versionName)||StringUtils.isBlank(appId)){
			return new BaseResult(ReturnCode.REQUEST_PARAMS_MISSING_ERROR);
		}
		
		Map<String, Object> appInfo=appInfoService.getByID(appId, ddBB);
		if(appInfo==null){
			return new BaseResult(ReturnCode.REQUEST_PARAMS_VERIFY_ERROR);
		}
		String versionId=(String) appInfo.get(TableConstants.AppInfo.latestVersionId.name());
		Map<String, Object> versionInfo=null;
		if(versionId!=null){
			versionInfo=updateVesionInfoService.getByID(versionId, ddBB);
		}
		
		boolean isUpdateVersion=true;
		
		if(versionInfo!=null&&versionCode.equals((String)versionInfo.get(TableConstants.UpdateVesionInfo.versionCode.name()))&&
				versionName.equals((String)versionInfo.get(TableConstants.UpdateVesionInfo.versionName.name()))){
			isUpdateVersion=false;
		}else{
			versionId=UUID.randomUUID().toString().replace("-", "");
		}
		
        Map<String, Object> updateAppInfo = new LinkedHashMap<>();
        updateAppInfo.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
        updateAppInfo.put(TableConstants.UPDATE_USER_ID, userId);
        updateAppInfo.put(TableConstants.AppInfo.NAME.name(), appName);
        updateAppInfo.put(TableConstants.AppInfo.REMARK.name(), appRemark);
        updateAppInfo.put(TableConstants.AppInfo.APP_IOS_PATH.name(), appIosPath);
        updateAppInfo.put(TableConstants.AppInfo.APP_PACKAGE_NAME.name(), appPackageName);
        updateAppInfo.put(TableConstants.AppInfo.LATEST_VERSION_ID.name(), versionId);
        if(appIcon.contains(ImgUtil.TEMP_PATH)){
        	String iocn = ImgUtil.saveAndUpdateFile(null,appIcon, null,ImgUtil.APK_PATH, appId);
        	updateAppInfo.put(TableConstants.AppInfo.APP_ICON.name(), iocn);
        }
        String path = appAndroidPath;
        if(StringUtils.isNotBlank(appAndroidPath)&&appAndroidPath.contains(ImgUtil.TEMP_APK_PATH)){
			path = ImgUtil.saveAndUpdateFile(fileName,appAndroidPath, null,ImgUtil.APK_PATH, appId+"/"+versionId);
			updateAppInfo.put(TableConstants.AppInfo.APP_ANDROID_PATH.name(), path);
        }
        updateAppInfo.put(TableConstants.AppInfo.ID.name(), appId);
        appInfoService.updateAppInfo(updateAppInfo, ddBB);
		
		if(isUpdateVersion){
			Map<String, Object> newVersionInfo = new LinkedHashMap<>();
			newVersionInfo.put(TableConstants.TENANT_ID, tenantId);
			newVersionInfo.put(TableConstants.UpdateVesionInfo.PUBLISH_TIME.name(), DateUtil.convertDateTimeToString(new Date(), null));
			newVersionInfo.put(TableConstants.UpdateVesionInfo.PUBLISH_PERSON.name(), userId);
			newVersionInfo.put(TableConstants.IS_SEALED, 0); 
			newVersionInfo.put(TableConstants.UpdateVesionInfo.NAME.name(), appName);
			newVersionInfo.put(TableConstants.UpdateVesionInfo.APP_ID.name(), appId);
			newVersionInfo.put(TableConstants.UpdateVesionInfo.VERSION_CODE.name(), versionCode);
			newVersionInfo.put(TableConstants.UpdateVesionInfo.VERSION_NAME.name(), versionName);
			newVersionInfo.put(TableConstants.UpdateVesionInfo.FILE_SIZE.name(), fileSize.replace("MB", ""));
			newVersionInfo.put(TableConstants.UpdateVesionInfo.UPDATE_TYPE.name(), updateType);
			newVersionInfo.put(TableConstants.UpdateVesionInfo.UPDATE_CONTENT.name(), updateContent);
			newVersionInfo.put(TableConstants.UpdateVesionInfo.DOWNLOAD_COUNT.name(), 0);
			newVersionInfo.put(TableConstants.UpdateVesionInfo.DOWNLOAD_URL.name(), path);
			newVersionInfo.put(TableConstants.UpdateVesionInfo.ID.name(), versionId);
			updateVesionInfoService.addUpdateVesionInfo(newVersionInfo, ddBB);
		}else{
			Map<String, Object> updateVersionInfo = new LinkedHashMap<>();
			updateVersionInfo.put(TableConstants.UpdateVesionInfo.PUBLISH_TIME.name(), DateUtil.convertDateTimeToString(new Date(), null));
			updateVersionInfo.put(TableConstants.UpdateVesionInfo.PUBLISH_PERSON.name(), userId);
			updateVersionInfo.put(TableConstants.UpdateVesionInfo.NAME.name(), appName);
			updateVersionInfo.put(TableConstants.UpdateVesionInfo.VERSION_CODE.name(), versionCode);
			updateVersionInfo.put(TableConstants.UpdateVesionInfo.VERSION_NAME.name(), versionName);
			updateVersionInfo.put(TableConstants.UpdateVesionInfo.FILE_SIZE.name(), fileSize);
			updateVersionInfo.put(TableConstants.UpdateVesionInfo.UPDATE_TYPE.name(), updateType);
			updateVersionInfo.put(TableConstants.UpdateVesionInfo.UPDATE_CONTENT.name(), updateContent);
			updateVersionInfo.put(TableConstants.UpdateVesionInfo.DOWNLOAD_URL.name(), path);
			updateVersionInfo.put(TableConstants.UpdateVesionInfo.ID.name(), versionId);
			updateVesionInfoService.updateUpdateVesionInfo(updateVersionInfo, ddBB);
		}
		
		List<Map<String, Object>> list=new ArrayList<>();
		for(Map<String, Object> map:photos){
			String picPath=(String) map.get(TableConstants.PublishPhotos.PHOTO_PATH.name());
			if(StringUtils.isNotBlank(picPath)&&picPath.contains(ImgUtil.TEMP_PATH)){
				String p=ImgUtil.saveAndUpdateFile(null,picPath, null,ImgUtil.APK_PATH, appId+"/"+versionId);
				Map<String, Object> photo = new LinkedHashMap<>();
				photo.put(TableConstants.PublishPhotos.UPLOAD_TIME.name(), DateUtil.convertDateTimeToString(new Date(), null));
				photo.put(TableConstants.PublishPhotos.UPGRADE_ID.name(), versionId);
				photo.put(TableConstants.PublishPhotos.PHOTO_PATH.name(), p);
				photo.put(TableConstants.PublishPhotos.ID.name(), UUID.randomUUID().toString().replace("-", ""));
				list.add(photo);
			}
		}
		if(list.size()>0){
			publishPhotosService.addPublishPhotosList(list, ddBB);
		}
		
		return new BaseResult(ReturnCode.OK);
	}
	
	@RequestMapping("/appInfo/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		String appName=(String) params.getParams().get(TableConstants.AppInfo.NAME.name());
		String appRemark=(String) params.getParams().get(TableConstants.AppInfo.REMARK.name());
		String appType=(String) params.getParams().get(TableConstants.AppInfo.APP_TYPE.name());
		String appIcon=(String) params.getParams().get(TableConstants.AppInfo.APP_ICON.name());
		String appPackageName=(String) params.getParams().get(TableConstants.AppInfo.APP_PACKAGE_NAME.name());
		String appIosPath=(String) params.getParams().get(TableConstants.AppInfo.APP_IOS_PATH.name());
		String appAndroidPath=(String) params.getParams().get(TableConstants.AppInfo.APP_ANDROID_PATH.name());
		String appId=UUID.randomUUID().toString().replace("-", "");
		
		String versionCode=(String) params.getParams().get(TableConstants.UpdateVesionInfo.VERSION_CODE.name());
		String versionName=(String) params.getParams().get(TableConstants.UpdateVesionInfo.VERSION_NAME.name());
		String fileSize=(String) params.getParams().get(TableConstants.UpdateVesionInfo.FILE_SIZE.name());
		String updateType=(String) params.getParams().get(TableConstants.UpdateVesionInfo.UPDATE_TYPE.name());
		String updateContent=(String) params.getParams().get(TableConstants.UpdateVesionInfo.UPDATE_CONTENT.name());
		String versionId=UUID.randomUUID().toString().replace("-", "");
		
		String fileName=(String) params.getParams().get("FILE_NAME");
		List<Map<String, Object>> photos=(List<Map<String, Object>>) params.getParams().get(TableConstants.PUBLISH_PHOTOS);
		
        Map<String, Object> appInfo = new LinkedHashMap<>();
        appInfo.put(TableConstants.TENANT_ID, tenantId);
        appInfo.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
        appInfo.put(TableConstants.UPDATE_USER_ID, userId);
        appInfo.put(TableConstants.IS_SEALED, 0); 
        appInfo.put(TableConstants.AppInfo.NAME.name(), appName);
        appInfo.put(TableConstants.AppInfo.REMARK.name(), appRemark);
    	appInfo.put(TableConstants.AppInfo.APP_TYPE.name(), appType);
    	appInfo.put(TableConstants.AppInfo.APP_IOS_PATH.name(), appIosPath);
        appInfo.put(TableConstants.AppInfo.APP_PACKAGE_NAME.name(), appPackageName);
        appInfo.put(TableConstants.AppInfo.PUBLISH_TIMES.name(), 0);
        appInfo.put(TableConstants.AppInfo.DOWNLOAD_TIMES.name(), 0);
        appInfo.put(TableConstants.AppInfo.TOP_FIXED_SQNO.name(), 1);
        appInfo.put(TableConstants.AppInfo.LATEST_VERSION_ID.name(), versionId);
        String iocn = ImgUtil.saveAndUpdateFile(null,appIcon, null,ImgUtil.APK_PATH, appId);
		appInfo.put(TableConstants.AppInfo.APP_ICON.name(), iocn);
		String path = ImgUtil.saveAndUpdateFile(fileName,appAndroidPath, null,ImgUtil.APK_PATH, appId+"/"+versionId);
		appInfo.put(TableConstants.AppInfo.APP_ANDROID_PATH.name(), path);
        appInfo.put(TableConstants.AppInfo.ID.name(), appId);
		appInfoService.addAppInfo(appInfo, ddBB);
		
		Map<String, Object> versionInfo = new LinkedHashMap<>();
		versionInfo.put(TableConstants.TENANT_ID, tenantId);
		versionInfo.put(TableConstants.UpdateVesionInfo.PUBLISH_TIME.name(), DateUtil.convertDateTimeToString(new Date(), null));
		versionInfo.put(TableConstants.UpdateVesionInfo.PUBLISH_PERSON.name(), userId);
		versionInfo.put(TableConstants.IS_SEALED, 0); 
		versionInfo.put(TableConstants.UpdateVesionInfo.NAME.name(), appName);
		versionInfo.put(TableConstants.UpdateVesionInfo.APP_ID.name(), appId);
		versionInfo.put(TableConstants.UpdateVesionInfo.VERSION_CODE.name(), versionCode);
		versionInfo.put(TableConstants.UpdateVesionInfo.VERSION_NAME.name(), versionName);
		versionInfo.put(TableConstants.UpdateVesionInfo.FILE_SIZE.name(), fileSize);
		versionInfo.put(TableConstants.UpdateVesionInfo.UPDATE_TYPE.name(), updateType);
		versionInfo.put(TableConstants.UpdateVesionInfo.UPDATE_CONTENT.name(), updateContent);
		versionInfo.put(TableConstants.UpdateVesionInfo.DOWNLOAD_COUNT.name(), 0);
		versionInfo.put(TableConstants.UpdateVesionInfo.DOWNLOAD_URL.name(), path);
		versionInfo.put(TableConstants.UpdateVesionInfo.ID.name(), versionId);
		updateVesionInfoService.addUpdateVesionInfo(versionInfo, ddBB);
		
		List<Map<String, Object>> list=new ArrayList<>();
		for(Map<String, Object> map:photos){
			String picPath=(String) map.get(TableConstants.PublishPhotos.PHOTO_PATH.name());
			if(StringUtils.isNotBlank(picPath)&&picPath.contains(ImgUtil.TEMP_PATH)){
				String p=ImgUtil.saveAndUpdateFile(null,picPath, null,ImgUtil.APK_PATH, appId+"/"+versionId);
				Map<String, Object> photo = new LinkedHashMap<>();
				photo.put(TableConstants.PublishPhotos.UPLOAD_TIME.name(), DateUtil.convertDateTimeToString(new Date(), null));
				photo.put(TableConstants.PublishPhotos.UPGRADE_ID.name(), versionId);
				photo.put(TableConstants.PublishPhotos.PHOTO_PATH.name(), p);
				photo.put(TableConstants.PublishPhotos.ID.name(), UUID.randomUUID().toString().replace("-", ""));
				list.add(photo);
			}
		}
		publishPhotosService.addPublishPhotosList(list, ddBB);
		
		
		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/appInfo/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = appInfoService.deleteAppInfo(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}
	
	@RequestMapping("/appInfo/toTop")
	@ResponseBody
	public BaseResult toTop(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		boolean b = appInfoService.updateAppInfo(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}


	@RequestMapping("/appInfo/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		String id=(String) params.getParams().get(TableConstants.AppInfo.ID.name());
		Map<String, Object> appInfo=appInfoService.getByID(id, ddBB);
		
		Map<String, Object> updateVesionInfo=updateVesionInfoService.getByID((String)appInfo.get(TableConstants.AppInfo.latestVersionId.name()), ddBB);
		appInfo.put(TableConstants.UpdateVesionInfo.updateContent.name(), updateVesionInfo.get(TableConstants.UpdateVesionInfo.updateContent.name()));
		appInfo.put(TableConstants.UpdateVesionInfo.updateType.name(), updateVesionInfo.get(TableConstants.UpdateVesionInfo.updateType.name()));
		appInfo.put(TableConstants.UpdateVesionInfo.versionCode.name(), updateVesionInfo.get(TableConstants.UpdateVesionInfo.versionCode.name()));
		appInfo.put(TableConstants.UpdateVesionInfo.versionName.name(), updateVesionInfo.get(TableConstants.UpdateVesionInfo.versionName.name()));
		appInfo.put(TableConstants.UpdateVesionInfo.fileSize.name(), updateVesionInfo.get(TableConstants.UpdateVesionInfo.fileSize.name()));
		
		Map<String, Object> pmap = new LinkedHashMap<>();
		pmap.put(TableConstants.PublishPhotos.UPGRADE_ID.name(), updateVesionInfo.get(TableConstants.UpdateVesionInfo.id.name()));
		List<Map<String, Object>> photos=publishPhotosService.getPublishPhotosList(pmap, ddBB);
		appInfo.put("photos", photos);
		
		return new BaseResult(ReturnCode.OK, appInfo);
	}

	@RequestMapping("/appInfo/getList")
	@ResponseBody
	public BaseResult getList(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		if(map.containsKey(TableConstants.AppInfo.NAME.name())){
			map.put(TableConstants.AppInfo.NAME.name(), "%"+map.get(TableConstants.AppInfo.NAME.name())+"%");
		}
		return new BaseResult(ReturnCode.OK, appInfoService.getAppInfoDeatilList(map, ddBB));
	}

	@RequestMapping("/appInfo/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		if(map.containsKey(TableConstants.AppInfo.NAME.name())){
			map.put(TableConstants.AppInfo.NAME.name(), "%"+map.get(TableConstants.AppInfo.NAME.name())+"%");
		}
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, appInfoService.getAppInfoDeatilPage(page, ddBB));
	}
}
