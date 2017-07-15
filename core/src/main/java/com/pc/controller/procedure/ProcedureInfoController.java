package com.pc.controller.procedure;

import com.pc.annotation.EncryptProcess;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.Page;
import com.pc.core.TableConstants;
import com.pc.service.procedure.impl.ProcedureInfoService;
import com.pc.util.DateUtil;
import com.pc.util.ExcelUtils;
import com.pc.vo.ParamsVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: wady (2017-03-28 20:19)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-28
 */
@Controller
@RequestMapping("/admin")
public class ProcedureInfoController extends BaseController {
    @Autowired
    private ProcedureInfoService procedureInfoService;
    @Autowired
    private ExcelUtils excelUtils;
    private Logger logger = LogManager.getLogger(this.getClass());

    @RequestMapping("/procedureInfo/getProcedureTree")
    @ResponseBody
    public BaseResult getProcedureTree(@RequestHeader(Constants.TENANT_ID) String tenantId,
                                       @EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
        Map<String, Object> map = new HashMap<>();
        map.put(TableConstants.ProcedureInfo.tenantId.name(), tenantId);
        return new BaseResult(ReturnCode.OK, procedureInfoService.getProcedureTree(map, ddBB));
    }

    @RequestMapping("/procedureInfo/add")
    @ResponseBody
    public BaseResult add(@RequestAttribute(Constants.USER_ID) String userId,
                          @RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
                          @RequestAttribute String ddBB) {

        Map<String, Object> map = new LinkedHashMap<>(params.getParams());
        map.put(TableConstants.TENANT_ID, tenantId);
        map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
        map.put(TableConstants.UPDATE_USER_ID, userId);
        map.put(TableConstants.IS_SEALED, 0);
        procedureInfoService.addProcedureInfo(map, ddBB);
        return new BaseResult(ReturnCode.OK);
    }

    @RequestMapping("/procedureInfo/delete")
    @ResponseBody
    public BaseResult delete(@RequestAttribute(Constants.USER_ID) String userId, @EncryptProcess ParamsVo params,
                             @RequestAttribute String ddBB) {
        Map<String, Object> map = new LinkedHashMap<>(params.getParams());
        map.put(TableConstants.SEALED_TIME, DateUtil.convertDateTimeToString(new Date(), null));
        map.put(TableConstants.SEALED_USER_ID, userId);
        boolean b = procedureInfoService.deleteProcedureInfo(map, ddBB);
        if (b) {
            return new BaseResult(ReturnCode.OK);
        } else {
            return new BaseResult(ReturnCode.FAIL);
        }
    }

    @RequestMapping("/procedureInfo/edit")
    @ResponseBody
    public BaseResult edit(@EncryptProcess ParamsVo params, @RequestAttribute(Constants.USER_ID) String userId,
                           @RequestAttribute String ddBB) {
        Map<String, Object> map = new LinkedHashMap<>(params.getParams());
        map.put(TableConstants.UPDATE_TIME, DateUtil.convertDateTimeToString(new Date(), null));
        map.put(TableConstants.UPDATE_USER_ID, userId);
        boolean b = procedureInfoService.updateProcedureInfo(map, ddBB);
        if (b) {
            return new BaseResult(ReturnCode.OK);
        } else {
            return new BaseResult(ReturnCode.FAIL);
        }

    }

    @RequestMapping("/procedureInfo/get")
    @ResponseBody
    public BaseResult get(@EncryptProcess ParamsVo params, @RequestAttribute String ddBB) {
        Map<String, Object> map = new LinkedHashMap<>(params.getParams());
        return new BaseResult(ReturnCode.OK, procedureInfoService.getProcedureInfo(map, ddBB));
    }

    @RequestMapping("/procedureInfo/getList")
    @ResponseBody
    public BaseResult getList(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess ParamsVo params,
                              @RequestAttribute String ddBB) {
        Map<String, Object> map = new LinkedHashMap<>(params.getParams());
        map.put(TableConstants.TENANT_ID, tenantId);
        map.put(TableConstants.IS_SEALED, 0);
        return new BaseResult(ReturnCode.OK, procedureInfoService.getProcedureInfoList(map, ddBB));
    }

    @RequestMapping("/procedureInfo/getPage")
    @ResponseBody
    public BaseResult getPage(@RequestHeader(Constants.TENANT_ID) String tenantId, @EncryptProcess Page page,
                              @RequestAttribute String ddBB) {
        Map<String, Object> map = new LinkedHashMap<>(page.getParams());
        map.put(TableConstants.TENANT_ID, tenantId);
        map.put(TableConstants.IS_SEALED, 0);
        if(map.containsKey(TableConstants.ProcedureInfo.PROCEDURE_NAME.name())){
			map.put(TableConstants.ProcedureInfo.PROCEDURE_NAME.name(), "%"+map.get(TableConstants.ProcedureInfo.PROCEDURE_NAME.name())+"%");
		}
        return new BaseResult(ReturnCode.OK, procedureInfoService.getProcedureInfoDetailByPage(page, map, ddBB));
    }

    @RequestMapping(value = "/procedure/load", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult importData(@RequestHeader(Constants.TENANT_ID)String tenantId,@RequestAttribute(Constants.USER_ID) String userId,@RequestAttribute String ddBB,MultipartFile file) {
        try {
            if(!file.getOriginalFilename().contains("工序类型")) return new BaseResult(350,"文件名可能不包含\"工序类型\"等字,请选择正确的文件及文件格式,否则可能导致异常");
            excelUtils.importData(ddBB+TableConstants.SEPARATE+TableConstants.PROCEDURE_TYPE, "PROCEDURE_TYPE_NAME", ddBB+TableConstants.SEPARATE+TableConstants.PROCEDURE_INFO, Arrays.<String>asList("PROCEDURE_NAME", "PROCEDURE_CODE"), "PROCEDURE_TYPE_ID", file,tenantId,userId);
        } catch (IOException e) {
            e.printStackTrace();
            return new BaseResult(1, e.getMessage());
        }
        return new BaseResult(0, "OK");
    }

/*    @RequestMapping(value = "/procedure/common/load", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult commonImportData(@RequestHeader(Constants.TENANT_ID)String tenantId,@RequestAttribute(Constants.USER_ID) String userId,@RequestAttribute String ddBB,MultipartFile file) {
        try {
            if(!file.getOriginalFilename().contains("工序类型")) return new BaseResult(350,"文件名可能不包含\"工序类型\"等字,请选择正确的文件及文件格式,否则可能导致异常");
            excelUtils.importData(ddBB+TableConstants.SEPARATE+TableConstants.PROCEDURE_TYPE, "PROCEDURE_TYPE_NAME", ddBB+TableConstants.SEPARATE+TableConstants.PROCEDURE_INFO_COMMON, Arrays.<String>asList("PROCEDURE_NAME", "PROCEDURE_CODE"), "PROCEDURE_TYPE_ID", file,"1",userId);
        } catch (IOException e) {
            e.printStackTrace();
            return new BaseResult(1, e.getMessage());
        }
        return new BaseResult(0, "OK");
    }*/
}
