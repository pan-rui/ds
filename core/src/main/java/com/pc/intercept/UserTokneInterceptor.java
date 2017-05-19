package com.pc.intercept;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.pc.base.BaseImpl;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.core.TableConstants;
import com.pc.service.user.TokenService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class UserTokneInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private TokenService tokenService;
	@Autowired
	private BaseImpl baseImpl;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String tenantId = request.getHeader(Constants.TENANT_ID);
		String userToken = request.getHeader(Constants.USER_TOKEN);
		String appVersion = request.getHeader(Constants.APP_VERSION);

		Map<String, Object> map = (Map<String, Object>) request.getSession().getAttribute("admin");
		if (map != null && (tenantId == null || userToken == null )) {
			tenantId = (String) map.get(TableConstants.User.tenantId.name());
			userToken = (String) map.get("token");
		}

		if (StringUtils.isBlank(userToken) || StringUtils.isBlank(tenantId)) {
			returnMsg(response, new BaseResult(ReturnCode.HEADER_PARAMS_VERIFY_ERROR));
			return false;
		}
		Map<String, String> user = tokenService.getUser(userToken);
		if (user == null) {
			returnMsg(response, new BaseResult(ReturnCode.TOKEN_VERIFY_ERROR));
			return false;
		}

		String token = tokenService.getTokenToCheck(user.get(Constants.USER_PHONE), tenantId);
		// String token = (String)
		// SecurityUtils.getSubject().getSession().getAttribute("token");

		if (!userToken.equals(token)) {
			returnMsg(response, new BaseResult(ReturnCode.TOKEN_VERIFY_ERROR));
			return false;
		}
		request.setAttribute(Constants.USER_ID, user.get(Constants.USER_ID));
		request.setAttribute(Constants.USER_PHONE, user.get(Constants.USER_PHONE));

		/*
		 * List<Map<String, Object>> tentants = baseImpl.getSystemValue("dems-"
		 * + TableConstants.TENANT, List.class); Optional<Map<String, Object>>
		 * tentant = tentants.stream().filter(map -> { return
		 * map.get("id").equals(tenantId); }).findFirst(); if
		 * (tentant.isPresent()) { request.setAttribute("ddBB",
		 * tentant.get().get("dbName")); } else { returnMsg(response, new
		 * BaseResult(ReturnCode.NO_AUTH)); return false; }
		 */
		return true;
	}

	private void returnMsg(HttpServletResponse response, BaseResult result) throws IOException {
		response.setContentType(Constants.APPLICATION_JSON);
		response.setStatus(200);
		response.getOutputStream().write(JSON.toJSONBytes(result, SerializerFeature.WriteEnumUsingToString));
		response.getOutputStream().close();
	}

}
