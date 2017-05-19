package com.pc.service.organization.impl;

import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.dao.organization.CompanyDao;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CompanyService extends BaseService {
    private Logger logger = LogManager.getLogger(this.getClass());
    
    @Autowired
    private CompanyDao companyDao;

     
    public void addCompany(Map<String, Object> params, String ddBB) {
		add(params, ddBB + TableConstants.SEPARATE + TableConstants.COMPANY);
	}

	public Page getCompanyPage(Page<Map<String, Object>> page, String ddBB) {
		 
		return queryPage(page, ddBB + TableConstants.SEPARATE + TableConstants.COMPANY);

	}

	public boolean updateCompany(Map<String, Object> params, String ddBB) {
		return update(params, ddBB + TableConstants.SEPARATE + TableConstants.COMPANY) > 0;
	}

	public Map<String, Object> getCompany(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.COMPANY);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, Object> getByID(String id, String ddBB) {
		return super.getByID(ddBB + TableConstants.SEPARATE + TableConstants.COMPANY,id);
	}
	
    public List<Map<String, Object>> getCompanyList(Map<String, Object> params, String ddBB) {
		List<Map<String, Object>> list = queryList(params, null,
				ddBB + TableConstants.SEPARATE + TableConstants.COMPANY);
		return list;
	}
    
    public List<Map<String, Object>> getPartnerCompanyList(String projectPeriodId, Map<String, Object> map, String ddBB) {
    	map.put("projectPeriodId", projectPeriodId);
    	map.put("cTableName", ddBB + TableConstants.SEPARATE + TableConstants.COMPANY);
    	map.put("pTableName", ddBB + TableConstants.SEPARATE + TableConstants.PARTNER_INFO);
    	map.put("pprTableName", ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PARTNER_RELATE);
    	map.put("tTableName", ddBB + TableConstants.SEPARATE + TableConstants.TEAM_INFO);
		return companyDao.queryPartnerCompanyInTab(map);
	}

	public boolean deleteCompany(Map<String, Object> params, String ddBB) {
		return deleteByState(params, ddBB + TableConstants.SEPARATE + TableConstants.COMPANY) > 0;
	}

	public String companyAdd(String userId,
									String tenantId, Map<String, Object> commap,
									String ddBB, String datestr)
	{
		String id = null;

		if(commap != null && !commap.isEmpty())
		{
			id = UUID.randomUUID().toString().replace("-", "");

			// 新增公司
			if(commap.get(TableConstants.Company.ID.name()) == null) {
				commap.put(TableConstants.TENANT_ID, tenantId);
				commap.put(TableConstants.UPDATE_TIME, datestr);
				commap.put(TableConstants.UPDATE_USER_ID, userId);
				commap.put(TableConstants.IS_SEALED, 0);
				commap.put(TableConstants.Company.ID.name(), id);
				addCompany(commap, ddBB);
			}
			else
			{
				// 更新公司信息
				Map<String, Object> param = new LinkedHashMap<>();
				param.put(TableConstants.Company.ID.name(), commap.get(TableConstants.Company.ID.name()));

				Map<String, Object> company = getCompany(param, ddBB);
				if(company != null) {
					id = (String)commap.get(TableConstants.Company.ID.name());

					if (!((company.get(TableConstants.Company.corporateType.name()) != null
							&& commap.get(TableConstants.Company.CORPORATE_TYPE.name()) != null
							&& String.valueOf(company.get(TableConstants.Company.corporateType.name())).compareTo(String.valueOf(commap.get(TableConstants.Company.CORPORATE_TYPE.name()))) == 0)
							|| (company.get(TableConstants.Company.corporateType.name()) == null && commap.get(TableConstants.Company.CORPORATE_TYPE.name()) == null))
							|| !((company.get(TableConstants.Company.corporatePhone.name()) != null
							&& commap.get(TableConstants.Company.CORPORATE_PHONE.name()) != null
							&& String.valueOf(company.get(TableConstants.Company.corporatePhone.name())).compareTo(String.valueOf(commap.get(TableConstants.Company.CORPORATE_PHONE.name()))) == 0)
							|| (company.get(TableConstants.Company.corporatePhone.name()) == null && commap.get(TableConstants.Company.CORPORATE_PHONE.name()) == null))
							|| !((company.get(TableConstants.Company.corporateName.name()) != null
							&& commap.get(TableConstants.Company.CORPORATE_NAME.name()) != null
							&& String.valueOf(company.get(TableConstants.Company.corporateName.name())).compareTo(String.valueOf(commap.get(TableConstants.Company.CORPORATE_NAME.name()))) == 0)
							|| (company.get(TableConstants.Company.corporateName.name()) == null && commap.get(TableConstants.Company.CORPORATE_NAME.name()) == null))
							|| !((company.get(TableConstants.Company.corporateLinker.name()) != null
							&& commap.get(TableConstants.Company.CORPORATE_LINKER.name()) != null
							&& String.valueOf(company.get(TableConstants.Company.corporateLinker.name())).compareTo(String.valueOf(commap.get(TableConstants.Company.CORPORATE_LINKER.name()))) == 0)
							|| (company.get(TableConstants.Company.corporateLinker.name()) == null && commap.get(TableConstants.Company.CORPORATE_LINKER.name()) == null))
							|| !((company.get(TableConstants.Company.corporateLegalPerson.name()) != null
							&& commap.get(TableConstants.Company.CORPORATE_LEGAL_PERSON.name()) != null
							&& String.valueOf(company.get(TableConstants.Company.corporateLegalPerson.name())).compareTo(String.valueOf(commap.get(TableConstants.Company.CORPORATE_LEGAL_PERSON.name()))) == 0)
							|| (company.get(TableConstants.Company.corporateLegalPerson.name()) == null && commap.get(TableConstants.Company.CORPORATE_LEGAL_PERSON.name()) == null))
							|| !((company.get(TableConstants.Company.corporateComment.name()) != null
							&& commap.get(TableConstants.Company.CORPORATE_COMMENT.name()) != null
							&& String.valueOf(company.get(TableConstants.Company.corporateComment.name())).compareTo(String.valueOf(commap.get(TableConstants.Company.CORPORATE_COMMENT.name()))) == 0)
							|| (company.get(TableConstants.Company.corporateComment.name()) == null && commap.get(TableConstants.Company.CORPORATE_COMMENT.name()) == null))
							|| !((company.get(TableConstants.Company.corporateCode.name()) != null
							&& commap.get(TableConstants.Company.CORPORATE_CODE.name()) != null
							&& String.valueOf(company.get(TableConstants.Company.corporateCode.name())).compareTo(String.valueOf(commap.get(TableConstants.Company.CORPORATE_CODE.name()))) == 0)
							|| (company.get(TableConstants.Company.corporateCode.name()) == null && commap.get(TableConstants.Company.CORPORATE_CODE.name()) == null))
							|| !((company.get(TableConstants.Company.corporateAddr.name()) != null
							&& commap.get(TableConstants.Company.CORPORATE_ADDR.name()) != null
							&& String.valueOf(company.get(TableConstants.Company.corporateAddr.name())).compareTo(String.valueOf(commap.get(TableConstants.Company.CORPORATE_ADDR.name()))) == 0)
							|| (company.get(TableConstants.Company.corporateAddr.name()) == null && commap.get(TableConstants.Company.CORPORATE_ADDR.name()) == null))
							|| !((company.get(TableConstants.Company.issuedCapitalStock.name()) != null
							&& commap.get(TableConstants.Company.ISSUED_CAPITAL_STOCK.name()) != null
							&& Float.valueOf(String.valueOf(company.get(TableConstants.Company.issuedCapitalStock.name()))).floatValue() == Float.valueOf(String.valueOf(commap.get(TableConstants.Company.ISSUED_CAPITAL_STOCK.name()))).floatValue() )
							|| (company.get(TableConstants.Company.issuedCapitalStock.name()) == null && commap.get(TableConstants.Company.ISSUED_CAPITAL_STOCK.name()) == null))
							|| !((company.get(TableConstants.Company.corporateLeader.name()) != null
							&& commap.get(TableConstants.Company.CORPORATE_LEADER.name()) != null
							&& String.valueOf(company.get(TableConstants.Company.corporateLeader.name())).compareTo(String.valueOf(commap.get(TableConstants.Company.CORPORATE_LEADER.name()))) == 0)
							|| (company.get(TableConstants.Company.corporateLeader.name()) == null && commap.get(TableConstants.Company.CORPORATE_LEADER.name()) == null))
							) {
						updateCompany(commap, ddBB);
					}
				}
				else
				{
					commap.put(TableConstants.TENANT_ID, tenantId);
					commap.put(TableConstants.UPDATE_TIME, datestr);
					commap.put(TableConstants.UPDATE_USER_ID, userId);
					commap.put(TableConstants.IS_SEALED, 0);

					commap.put(TableConstants.Company.ID.name(), id);
					addCompany(commap, ddBB);
				}
			}
		}

		return id;
	}
}