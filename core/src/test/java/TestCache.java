import com.pc.controller.auth.FuncRolePrivilegesController;
import com.pc.core.Page;
import com.pc.core.ParamsMap;
import com.pc.dao.BaseDao;
import com.pc.dao.cache.OfflineDao;
import com.pc.dao.sys.StatisticsDao;
import com.pc.service.project.impl.ProjectPeriodService;
import com.pc.service.tenant.impl.TenantService;
import com.pc.util.ExcelUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by ThinkPad on 2017/3/24.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:spring-shiro.xml", "classpath:baseServlet.xml"})
//@ContextConfiguration(locations = {"classpath*:cache-redis.xml","classpath*:data.xml","classpath*:common.xml","classpath*:spring-shiro.xml","classpath*:baseServlet.xml","classpath:spring.xml","classpath:spring-front.xml"})
public class TestCache {

    @Resource
    private BaseDao baseDao;
    //    @Resource
//    private TokenService tokenService;
//    @Resource
//    private TCache tCache;
    @Autowired
    private ProjectPeriodService projectPeriodService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private ExcelUtils excelUtils;
    @Autowired
    private FuncRolePrivilegesController funcRolePrivilegesController;
    @Autowired
    private OfflineDao offlineDao;
    @Autowired
    private StatisticsDao statisticsDao;

//    @Test
    public void putCache() throws InterruptedException {
//        tokenService = new TokenService();
//      String token =  tokenService.getSessionToken("14354", "3290fdf", "98fd");
//        System.out.println(token);
//        Thread.currentThread().sleep(30);
//        System.out.println(tokenService.getSessionToken("14354", "3290fdf", "98fd"));
//        baseDao.queryByProsInTab(ParamsMap.newMap("PHONE", "18820276678"), "dems.USER");
//        tCache.testCache("9h4tt");
    }

//    @Test
    public void testOfflineProject() throws IOException {
        System.out.println(System.currentTimeMillis());
//        List<Map<String, Object>> resultList = offlineDao.queryOffLineProject("dems","1", new Date(100000l));
//        List<Map<String, Object>> resultList = offlineDao.queryOffLineProject("dems", "1","f3467010f06943d385481263fde84f77",null);
//        List<Map<String, Object>> resultList = offlineDao.queryChart("dems", "1","f3467010f06943d385481263fde84f77",null);
        List<Map<String, Object>> resultList = offlineDao.queryOfflineProcedure("dems", "1","f3467010f06943d385481263fde84f77", null);
//        List<Map<String, Object>> resultList = offlineDao.queryOfflineProcedure("dems", "1","f3467010f06943d385481263fde84f77", Arrays.asList("4","14"));
        Page page = new Page();
        page.setParams(ParamsMap.newMap("TYPE", 1));
//        List<Map<String, Object>> resultList = statisticsDao.queryAccessByPageTab("dems",page);
        System.out.println(System.currentTimeMillis());
//        List<Map<String, Object>> resultList = offlineDao.queryOfflineProcedure("dems", "f3467010f06943d385481263fde84f77",null);
        resultList.get(0).keySet().forEach(k-> System.out.println(k));
        System.out.println(resultList.get(0));
//        Map<String, Object> procedureTypeMap = ParamsMap.newMap("Device_Imei", "666222000888999").addParams("Device_Register", 1).addParams("Device_Version", 666)
//                .addParams("Device_Active_Time", new Date()).addParams("Device_Pwd_Upd_Time", new Date()).addParams("Install_Code", "9teoiu998u3rj");
//        baseDao.insertByProsInTab("digital_project.YG_DEVICE_INFO", procedureTypeMap);
//        procedureTypeMap.forEach((k, v) -> System.out.println(k + "\t" + v));
//        excelUtils.importData(TableConstants.PROCEDURE_TYPE,"PROCEDURE_TYPE_NAME",TableConstants.PROCEDURE_INFO, Arrays.asList("PROCEDURE_NAME","REMARK"),"PROCEDURE_TYPE_ID",null,null,null);
//        List<Map<String,Object>> dataList = new ArrayList();
//        dataList.add(ParamsMap.newMap("ACCESS_TIME", new Date()).addParams("OPERATE", "abcd"));
//        dataList.add(ParamsMap.newMap("ACCESS_TIME", new Date()).addParams("OPERATE", "fefg"));
//        baseDao.insertBatchByProsInTab("dems.ACCESS_LOG", dataList);
    }
}
