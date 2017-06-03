package com.pc.server.controller.system;

import com.pc.annotation.EncryptProcess;
import com.pc.annotation.OperationLog;
import com.pc.base.BaseImpl;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import com.pc.controller.BaseController;
import com.pc.core.Page;
import com.pc.dao.sys.StatisticsDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description: ${Description}
 * @Author: 潘锐 (2017-05-17 17:48)
 * @version: \$Rev: 2691 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-05-31 21:03:22 +0800 (周三, 31 5月 2017) $
 */
@Controller
@RequestMapping("/")
public class StatisticsController extends BaseController {
    private static final Logger logger = LogManager.getLogger(StatisticsController.class);
    @Autowired
    private BaseImpl baseImpl;
    @Autowired
    private StatisticsDao statisticsDao;

    @RequestMapping(value = "system/access")
    @ResponseBody
    @OperationLog(value = "查询访问统计",table = {"POST_INFO","ACCESS_STITASTICS"})
    public BaseResult accessStati(HttpServletRequest request,@RequestAttribute(Constants.DDBB)String ddBB, @EncryptProcess Page page) {
        List<Map<String,Object>> resultList=null;
        try {
            resultList = statisticsDao.queryAccessByPageTab(ddBB,page);
            page.setResults(resultList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询访问统计异常:"+e.getMessage());
            return new BaseResult(ReturnCode.FAIL);
        }
        return new BaseResult(0,page);
    }

    @RequestMapping(value = "manage/access",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult accessStatiT(HttpServletRequest request,@EncryptProcess Page page) {
        List<Map<String,Object>> resultList=null;
        try {
            resultList = statisticsDao.queryAccessTByPageTab("dems",page);
            page.setResults(resultList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询访问统计异常:"+e.getMessage());
            return new BaseResult(ReturnCode.FAIL);
        }
        return new BaseResult(0,page);
    }


}
