package com.pc.server.controller;

import com.pc.base.BaseImpl;
import com.pc.base.BaseResult;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.service.user.TokenService;
import com.pc.util.ExcelUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by ThinkPad on 2017/3/22.
 */
@Controller
@RequestMapping("/admin")
public class AdminAction extends BaseController {

	private static final Logger logger = LogManager.getLogger(AdminAction.class);
	@Autowired
	private BaseImpl baseImpl;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private ExcelUtils excelUtils;

	@RequestMapping("/cleans")
	@ResponseBody
	public BaseResult bb(HttpSession session, @RequestAttribute String ddBB,String cN) {
		logger.debug("in............clearColumns cache....");
		if (ddBB.equals("dems")) {
			tokenService.clearAllCache();
			baseImpl.initColumns();
		}
		if ("offline".equals(cN)) {
			tokenService.clearOfflineCache();
		}
			return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/login")
	public String login(HttpServletRequest request) {
		return "login";
	}

	@RequestMapping("testPart")
	public BaseResult testPart(HttpServletRequest request) throws IOException, ServletException {
//		Iterator<Part> parts=request.getParts().iterator();
		Collection<List<MultipartFile>> fileTs = ((DefaultMultipartHttpServletRequest) request).getMultiFileMap().values();
//		excelUtils.importData(TableConstants.PROCEDURE_TYPE, "PROCEDURE_TYPE_NAME", TableConstants.PROCEDURE_INFO, Arrays.<String>asList("PROCEDURE_NAME","REMARK"), "PROCEDURE_TYPE_ID",  fileTs.iterator().next().get(0));
		return new BaseResult(0, "testData");
	}

}
