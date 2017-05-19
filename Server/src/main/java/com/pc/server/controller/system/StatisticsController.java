package com.pc.server.controller.system;

import com.pc.base.BaseImpl;
import com.pc.base.BaseResult;
import com.pc.controller.BaseController;
import com.pc.dao.sys.StatisticsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Description: ${Description}
 * @Author: 潘锐 (2017-05-17 17:48)
 * @version: \$Rev: 2395 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-05-18 10:36:38 +0800 (周四, 18 5月 2017) $
 */
@Controller
@RequestMapping("/system")
public class StatisticsController extends BaseController {
    @Autowired
    private BaseImpl baseImpl;
    @Autowired
    private StatisticsDao statisticsDao;

    public BaseResult accessStati(HttpServletRequest request, @RequestParam(required = false) Date sDate, @RequestParam(required = false) Date eDate, int type) {
        return null;
    }

}
