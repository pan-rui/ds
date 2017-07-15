package com.pc.controller.user;

import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.service.organization.impl.CompanyService;
import com.pc.service.organization.impl.OrganizationInfoService;
import com.pc.service.organization.impl.PartnerInfoService;
import com.pc.service.organization.impl.PostInfoService;
import com.pc.service.user.TokenService;
import com.pc.service.user.UserService;
import com.pc.vo.ParamsVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
public class LoginController extends BaseController {
    private static final Logger logger = LogManager.getLogger(LoginController.class);
    @Autowired
	private UserService userService;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private PostInfoService postInfoService;

	@Autowired
	private OrganizationInfoService organizationInfoService;
	@Autowired
	private PartnerInfoService partnerInfoService;
	@Autowired
	private CompanyService companyService;

	@Value("#{config[sessionExpireTime]}")
	private int sessionExpireTime;

	@RequestMapping("/login")
	@ResponseBody
	public BaseResult login(HttpServletRequest request, @RequestBody ParamsVo params) {
		String tenantId = String.valueOf(params.getParams().get("tenantId"));
		String phone = String.valueOf(params.getParams().get("phone"));
		String password = String.valueOf(params.getParams().get("password"));

//		tokenService.clearAllCache();
//		baseImpl.initColumns();

		Boolean isAdmin = Boolean.valueOf(String.valueOf(params.getParams().getOrDefault("admin", "true")));
		String ddBB = null;
		List<Map<String, Object>> tentants = baseImpl.getSystemValue("dems-" + TableConstants.TENANT, List.class);
		Optional<Map<String, Object>> tentant = tentants.stream().filter(map -> {
			return map.get("id").equals(tenantId);
		}).findFirst();
		if (tentant.isPresent()) {
			ddBB = (String) tentant.get().get("dbName");
		} else {
			return new BaseResult(ReturnCode.NO_AUTH);
		}
		Map<String, Object> map = userService.getUserByPhone(phone, tenantId, ddBB);
		if (map != null) {
			if (!password.equals(map.get(TableConstants.User.pwd.name()))) {
				return new BaseResult(ReturnCode.LOGIN_PWD_ERROR);
			}
			String token = tokenService.getToken(phone, tenantId);
			map.put("token", token);
			if (map.get(TableConstants.User.postId.name()) != null) {
				String postName = (String) postInfoService
						.getByID((String) map.get(TableConstants.User.postId.name()), ddBB)
						.get(TableConstants.PostInfo.postName.name());
				map.put(TableConstants.PostInfo.postName.name(), postName);
			}

			if (map.get(TableConstants.User.ownOrgType.name()) != null
					&& Integer.valueOf((String) map.get(TableConstants.User.ownOrgType.name())) == 1) {
				map.put("orgName",
						(String) organizationInfoService
								.getByID((String) map.get(TableConstants.User.ownOrgId.name()), ddBB)
								.get(TableConstants.OrganizationInfo.organizationName.name()));
				map.put("companyName",
						(String) companyService.getByID((String) map.get(TableConstants.User.companyId.name()), ddBB)
								.get(TableConstants.Company.corporateName.name()));
			} else if (map.get(TableConstants.User.ownOrgType.name()) != null
					&& Integer.valueOf((String) map.get(TableConstants.User.ownOrgType.name())) == 2) {
				map.put("companyName",
						(String) partnerInfoService.getByID((String) map.get(TableConstants.User.ownOrgId.name()), ddBB)
								.get(TableConstants.PartnerInfo.partnerName.name()));
			}

			String userId = (String) map.get(TableConstants.User.id.name());
            tokenService.cacheUserByToken(token, userId, phone, tenantId, ddBB);

			loadAuthorization(phone, password, tenantId, ddBB);
            com.pc.core.Constants.setCacheOnExpire(
                    (String) SecurityUtils.getSubject().getSession().getId(),
                    tenantId + TableConstants.SEPARATE_SPLIT + ddBB + TableConstants.SEPARATE_SPLIT + phone, sessionExpireTime);
            if (isAdmin) {
				request.getSession().setAttribute("admin", map);
			}

			return new BaseResult(ReturnCode.OK, map);
		} else {
			return new BaseResult(ReturnCode.LOGIN_PHONE_ERROR);
		}

	}

	@RequestMapping("/loginOut")
	@ResponseBody
	public BaseResult loginOut(@RequestHeader(Constants.TENANT_ID) String tenantId,
			@RequestAttribute(Constants.USER_PHONE) String phone,
			@RequestHeader(Constants.USER_TOKEN) String userToken) {
		String ddBB = null;
		List<Map<String, Object>> tentants = baseImpl.getSystemValue("dems-" + TableConstants.TENANT, List.class);
		Optional<Map<String, Object>> tentant = tentants.stream().filter(map -> {
			return map.get("id").equals(tenantId);
		}).findFirst();
		if (tentant.isPresent()) {
			ddBB = (String) tentant.get().get("dbName");
		} else {
			return new BaseResult(ReturnCode.NO_AUTH);
		}
		Map<String, Object> map = userService.getUserByPhone(phone, tenantId, ddBB);
		if (map != null) {
			String token = tokenService.getTokenToCheck(phone, tenantId);

			if (token == null || !userToken.equals(token)) {
				return new BaseResult(ReturnCode.FAIL);
			}

			tokenService.delToken(phone, tenantId);
			tokenService.delUserByToken(token);
			SecurityUtils.getSecurityManager().logout(SecurityUtils.getSubject());

			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	public void loadAuthorization(String username, String password, String tenantId, String ddBB) {
		String realm = ((RealmSecurityManager) SecurityUtils.getSecurityManager()).getRealms().iterator().next()
				.getName();
		SubjectContext subjectContext = new DefaultSubjectContext();
		subjectContext.setAuthenticated(true);
		subjectContext.setAuthenticationToken(new UsernamePasswordToken(username, password, tenantId));
		PrincipalCollection principalCollection = new SimplePrincipalCollection(
				ParamsMap.newMap("ddBB", ddBB).addParams("tenantId",tenantId).addParams("username", username), realm);
		subjectContext.setAuthenticationInfo(new SimpleAuthenticationInfo(principalCollection, password));
		subjectContext.setSubject(SecurityUtils.getSubject());
		SecurityUtils.getSecurityManager().createSubject(subjectContext);
	}
}
