package com.pc.controller.client;


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
import com.pc.vo.ParamsVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/client")
public class AppClientController extends BaseController {
	@Autowired
	private AppInfoService appInfoService;
	
	@Autowired
	private PublishPhotosService publishPhotosService;
	
	@Autowired
	private UpdateVesionInfoService updateVesionInfoService;

	private Logger logger = LogManager.getLogger(this.getClass());
	
	@RequestMapping("/appInfo/getLastVersion")
	@ResponseBody
	public BaseResult getLastVersion(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		String packageName=(String) params.getParams().get(TableConstants.AppInfo.APP_PACKAGE_NAME.name());
		String versionCode=(String) params.getParams().get(TableConstants.UpdateVesionInfo.VERSION_CODE.name()); 
		
		Map<String, Object> paramsMap=new HashMap<String, Object>();
		paramsMap.put(TableConstants.AppInfo.APP_PACKAGE_NAME.name(), packageName);
		paramsMap.put(TableConstants.IS_SEALED, 0);
		Map<String, Object> appInfo=appInfoService.getAppInfo(paramsMap, ddBB);
		
		Map<String, Object> updateVesionInfo=updateVesionInfoService.getByID((String)appInfo.get(TableConstants.AppInfo.latestVersionId.name()), ddBB);
		appInfo.put(TableConstants.UpdateVesionInfo.updateContent.name(), updateVesionInfo.get(TableConstants.UpdateVesionInfo.updateContent.name()));
		appInfo.put(TableConstants.UpdateVesionInfo.updateType.name(), updateVesionInfo.get(TableConstants.UpdateVesionInfo.updateType.name()));
		appInfo.put(TableConstants.UpdateVesionInfo.versionCode.name(), updateVesionInfo.get(TableConstants.UpdateVesionInfo.versionCode.name()));
		appInfo.put(TableConstants.UpdateVesionInfo.versionName.name(), updateVesionInfo.get(TableConstants.UpdateVesionInfo.versionName.name()));
		appInfo.put(TableConstants.UpdateVesionInfo.fileSize.name(), updateVesionInfo.get(TableConstants.UpdateVesionInfo.fileSize.name()));
		
		if(versionCode.equals((String)updateVesionInfo.get(TableConstants.UpdateVesionInfo.versionCode.name()))){
			return new BaseResult(ReturnCode.APP_IS_LAST_VERSION);
		}else{
			return new BaseResult(ReturnCode.OK, appInfo);
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
		map.put(TableConstants.IS_SEALED, 0);
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, appInfoService.getAppInfoDeatilPage(page, ddBB));
	}
	
	
}
