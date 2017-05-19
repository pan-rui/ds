package com.pc.controller.offline;

import com.alibaba.fastjson.JSON;
import com.pc.annotation.OperationLog;
import com.pc.base.BaseImpl;
import com.pc.base.Constants;
import com.pc.controller.BaseController;
import com.pc.core.TableConstants;
import com.pc.dao.cache.OfflineDao;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.CRC32;

/**
 * @Description: ${Description}
 * @Author: 潘锐 (2017-04-21 19:00)
 * @version: \$Rev: 2351 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-05-16 18:07:26 +0800 (周二, 16 5月 2017) $
 */
@Controller
@RequestMapping("/offline")
public class OfflineController extends BaseController {
    @Autowired
    private BaseImpl baseImpl;
    @Autowired
    private OfflineDao offlineDao;
    @Value("#{config['dateFormat']}")
    private String dFormat;
    private SimpleDateFormat dateFormat;
    private static final Logger logger = LogManager.getLogger(OfflineController.class);
    /**
     * 离线数据下载,支持断点续传
     * @param response
     * @param ddBB
     * @param projectPeriodId       项目期ID
     * @param len      已收到长度
     * @throws IOException
     */
    @RequestMapping(value = "",method = RequestMethod.POST)
    @OperationLog(value = {"离线数据下载","testdd"})
    public void getOfflineDate(HttpServletRequest request,HttpServletResponse response, @RequestAttribute String ddBB, @RequestHeader(Constants.TENANT_ID)String tenantId, @RequestParam String projectPeriodId, String updateTime, @RequestParam(defaultValue = "13") int len) throws IOException {
        Map<String, Object> dataMap = baseImpl.getCacheOfValue("offline", ddBB + "-" + projectPeriodId, Map.class);
            Date date=null;
            try {
                if(!StringUtils.isEmpty(updateTime))
               date =dateFormat.parse(updateTime);
            } catch (ParseException e) {
                logger.warn("日期格式解析错误 ......参数为:"+updateTime);
                e.printStackTrace();
            }
        if (dataMap == null || dataMap.isEmpty()) {
            List<Map<String, Object>> projectList = offlineDao.queryOffLineProject(ddBB, tenantId,projectPeriodId,date);       //TODO:项目期, 层/户 可过滤时间范围
            List<Map<String, Object>> procedureList = offlineDao.queryOfflineProcedure(ddBB,tenantId, projectPeriodId, Arrays.asList("4", "14"));            //验收记录可过滤时间范围  ,部位工序分类
            if (!procedureList.isEmpty()) {
                Map<String, Object> project = projectList.get(0);
/*                List<Map<String,Object>> projectBuildings= (List<Map<String, Object>>) project.get("projectBuilding");
                   Map<String, Object> unitChartPath = new LinkedHashMap<>();
                projectBuildings.forEach(projectBuilding->{
                    List<Map<String, Object>> projectHouseholds = (List<Map<String, Object>>) projectBuilding.get("projectHouseholds");
                    Iterator<Map<String,Object>> it=projectHouseholds.iterator();
                    while (it.hasNext()) {               //户型过滤
                        Map<String, Object> household = it.next();
                       Map<String, Object> unitChart = (Map<String, Object>) household.get("unitChart");
                       if(unitChart!=null)
                       unitChartPath.put((String) unitChart.get("householdTypeId"), unitChart.get("unitChartImagePath"));
                    }
                });
                project.put("unitChartPath", unitChartPath);*/
                List<Map<String, Object>> charts = offlineDao.queryChart(ddBB, tenantId, projectPeriodId, date);
                project.put("charts", charts);
                List<Map<String, Object>> chartAreaType = baseImpl.getSystemValue(ddBB+ TableConstants.SEPARATE_CACHE + TableConstants.HOUSEHOLD_CHART_AREA_TYPE, List.class);
                Map<String, Object> chartAreaMap = new LinkedHashMap<>();
                chartAreaType.forEach(map->{
                    chartAreaMap.put((String)map.get("id"), (String) map.get("householdChartAreaTypeName"));
                });
            //工序部位分类
                List<Map<String, Object>> periodProcedures = new ArrayList<>();
                project.put("periodProcedures", periodProcedures);
                List<Map<String, Object>> buildingProcedures = new ArrayList<>();
                project.put("buildingProcedures", buildingProcedures);
                List<Map<String, Object>> floorProcedures = new ArrayList<>();
                project.put("floorProcedures", floorProcedures);
                List<Map<String, Object>> householdProcedures = new ArrayList<>();
                project.put("householdProcedures", householdProcedures);
                procedureList.forEach(procedure->{
                    switch ((String)procedure.get("regionTypeId")){
                        case "1":
                            periodProcedures.add((Map<String, Object>) procedure.get("procedure"));break;
                        case "2":
                            buildingProcedures.add((Map<String,Object>)procedure.get("procedure"));break;
                        case "3":
                            floorProcedures.add((Map<String,Object>)procedure.get("procedure"));break;
                        case "4":
                            householdProcedures.add((Map<String,Object>)procedure.get("procedure"));break;
                    }
                });
//                project.put("procedureList", procedureList);
                project.put("chartAreaTypes", chartAreaMap);
                baseImpl.setCacheOfValue("offline", ddBB + "-" + projectPeriodId, project);
                dataMap =  project;
            }
        }else {
            if (date != null) {             //按日期过滤
                final Date date1 = date;
               List<Map<String,Object>> projectBuildings= (List<Map<String, Object>>) dataMap.get("projectBuilding");
               projectBuildings.forEach(projectBuilding->{
                   List<Map<String, Object>> projectHouseholds = (List<Map<String, Object>>) projectBuilding.get("projectHouseholds");
                   Iterator<Map<String,Object>> it=projectHouseholds.iterator();
                   while (it.hasNext()) {
                       Map<String, Object> household = it.next();
                       if (((Date) household.get("updateTime")).getTime() <= date1.getTime())
                           it.remove();
                   }
               });
            }
        }
        byte[] bytes = JSON.toJSONString(dataMap).getBytes("utf-8");
        if(len<13) len=13;
        int length=13+bytes.length;
        if(len>length) len=length;
/*        if(len>0){
            byte[] byt = new byte[length - len];
            System.arraycopy(bytes,len-5,byt,0,byt.length);
        }*/
       ByteBuffer byteBuffer=ByteBuffer.allocate(length);
       byteBuffer.put((byte) (0&0xff));
        byteBuffer.putInt(length);
        CRC32 crc32 = new CRC32();
        crc32.update(bytes,len-13,length-len);
        byteBuffer.putLong(crc32.getValue());       //CRC校验
        byteBuffer.put(bytes,len-13,length-len);
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setContentLength(length-len+13);
        OutputStream out=response.getOutputStream();
        out.write(byteBuffer.array(),0,length-len+13);
        out.close();
    }

    @PostConstruct
    public void init() {
        dateFormat =new SimpleDateFormat(dFormat);
    }
}
