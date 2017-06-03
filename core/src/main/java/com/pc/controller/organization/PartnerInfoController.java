package com.pc.controller.organization;

import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.organization.impl.CompanyService;
import com.pc.service.organization.impl.PartnerInfoService;
import com.pc.util.DateUtil;
import com.pc.vo.ParamsVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.LinkedHashMap;
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
public class PartnerInfoController extends BaseController {
	@Autowired
	private PartnerInfoService partnerInfoService;

	@Autowired
	private CompanyService companyService;

	private Logger logger = LogManager.getLogger(this.getClass());

	@RequestMapping("/partnerInfo/add")
	@ResponseBody
	public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
			@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {

		String corporateId = null;
		Map<String, Object> commap = null;
		Map<String, Object> paramsmMap = params.getParams();

		String datestr = DateUtil.convertDateTimeToString(new Date(), null);

		if(paramsmMap.containsKey(PARTNER_PARAM_KEY.COMPANY.name())) {
			commap = new LinkedHashMap<>((Map) paramsmMap.get(PARTNER_PARAM_KEY.COMPANY.name()));
			corporateId = companyService.companyAdd(userId, tenantId, commap, ddBB, datestr);
		}

		// 合作伙伴信息新增
		Map<String, Object> partnerMap = new LinkedHashMap<>((Map) paramsmMap.get(PARTNER_PARAM_KEY.PARTNER.name()));
		partnerMap.put(TableConstants.TENANT_ID, tenantId);

		String orgid = UUID.randomUUID().toString().replace("-", "");

		partnerMap.put(TableConstants.OrganizationInfo.ID.name(), orgid);
		partnerMap.put(TableConstants.TENANT_ID, tenantId);
		partnerMap.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		partnerMap.put(TableConstants.UPDATE_USER_ID, userId);
		partnerMap.put(TableConstants.IS_SEALED, 0);

		if ("1".compareTo(String.valueOf( partnerMap.get(TableConstants.OrganizationInfo.IS_ENTITY.name()))) == 0
				&&  partnerMap.get(TableConstants.OrganizationInfo.CORPORATE_ID.name()) == null && corporateId != null) {
			 partnerMap.put(TableConstants.OrganizationInfo.CORPORATE_ID.name(), corporateId);
		}

		partnerInfoService.addPartnerInfo(partnerMap, ddBB);

		return new BaseResult(ReturnCode.OK);
	}

	@RequestMapping("/partnerInfo/delete")
	@ResponseBody
	public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		map.put(TableConstants.SEALED_USER_ID, userId);
		boolean b = partnerInfoService.deletePartnerInfo(map, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/partnerInfo/edit")
	@ResponseBody
	public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
						   @RequestHeader(Constants.TENANT_ID) String tenantId,
			@RequestAttribute String ddBB) {

		String corporateId = null;
		Map<String, Object> commap = null;
		Map<String, Object> paramsmMap = params.getParams();

		String datestr = DateUtil.convertDateTimeToString(new Date(), null);

		commap = (Map) paramsmMap.get(PARTNER_PARAM_KEY.COMPANY.name());

		if(commap != null) {
			corporateId = companyService.companyAdd(userId, tenantId, commap, ddBB, datestr);
		}

		// 合作伙伴信息新增
		Map<String, Object> partnerMap = new LinkedHashMap<>((Map) paramsmMap.get(PARTNER_PARAM_KEY.PARTNER.name()));
		if(corporateId!=null){
			partnerMap.put(TableConstants.PartnerInfo.CORPORATE_ID.name(), corporateId);
		}
		partnerMap.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
		partnerMap.put(TableConstants.UPDATE_USER_ID, userId);
		
		boolean b = partnerInfoService.updatePartnerInfo(partnerMap, ddBB);
		if (b) {
			return new BaseResult(ReturnCode.OK);
		} else {
			return new BaseResult(ReturnCode.FAIL);
		}
	}

	@RequestMapping("/partnerInfo/get")
	@ResponseBody
	public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		return new BaseResult(ReturnCode.OK, partnerInfoService.getPartnerInfo(map, ddBB));
	}

	@RequestMapping("/partnerInfo/getList")
	@ResponseBody
	public BaseResult getList(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(params.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		return new BaseResult(ReturnCode.OK, partnerInfoService.getPartnerInfoList(map, ddBB));
	}

	@RequestMapping("/partnerInfo/getPage")
	@ResponseBody
	public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
			@RequestAttribute String ddBB) {
		Map<String, Object> map = new LinkedHashMap<>(page.getParams());
		map.put(TableConstants.TENANT_ID, tenantId);
		map.put(TableConstants.IS_SEALED, 0);
		if(map.containsKey(TableConstants.PartnerInfo.PARTNER_NAME.name())){
			map.put(TableConstants.PartnerInfo.PARTNER_NAME.name(), "%"+map.get(TableConstants.PartnerInfo.PARTNER_NAME.name())+"%");
		}
		page.setParams(map);
		return new BaseResult(ReturnCode.OK, partnerInfoService.getPartnerInfoPage(page, ddBB));
	}

    public enum PARTNER_PARAM_KEY {
        COMPANY,    //检验批ID
        company,    //检验批ID
        PARTNER,
        partner
    }// ;
}
