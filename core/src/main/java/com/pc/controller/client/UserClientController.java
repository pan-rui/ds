package com.pc.controller.client;

import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.TableConstants;
import com.pc.service.user.UserService;
import com.pc.util.DateUtil;
import com.pc.util.ImgUtil;
import com.pc.vo.ParamsVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/client")
public class UserClientController extends BaseController {
	@Autowired
	private UserService userService;
	

	private Logger logger = LogManager.getLogger(this.getClass());

	
	@RequestMapping("/user/mod")
	@ResponseBody
	public BaseResult editUser(@RequestAttribute(Constants.USER_ID) String userId, @RequestBody ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> userMap=userService.getByID(userId, ddBB);
		Map<String, Object> map = new LinkedHashMap<>();
		
		Object npwd=params.getParams().get("npassword");
		Object pwd=params.getParams().get("password");
		Object nimg=params.getParams().get("nimg");
		
		if(pwd!=null&&npwd!=null){
			if(((String)pwd).equals((String)userMap.get(TableConstants.User.pwd.name()))){
				map.put(TableConstants.User.PWD.name(), (String)npwd);
			}else{
				return new BaseResult(ReturnCode.OLD_PWD_ERROR);
			}
		}
		
		if(nimg!=null){
			String userPath=(String)userMap.get(TableConstants.User.phone.name());
			map.put(TableConstants.User.IMG.name(), ImgUtil.saveAndUpdateFile(null,(String)nimg, null, ImgUtil.USER_IMG_PATH, userPath));
		}
		
		map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.UPDATE_USER_ID, userId);
		map.put(TableConstants.User.ID.name(), userId);
		boolean b = userService.updateUser(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}
	
	
}
