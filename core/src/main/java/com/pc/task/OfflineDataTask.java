package com.pc.task;

import com.pc.base.BaseImpl;
import com.pc.core.TableConstants;
import com.pc.dao.cache.OfflineDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Description: ${Description}
 * @Author: 潘锐 (2017-04-21 18:04)
 * @version: \$Rev: 2282 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-05-15 14:11:40 +0800 (周一, 15 5月 2017) $
 */
@Component
public class OfflineDataTask {
    private final Logger logger = LogManager.getLogger(OfflineDataTask.class);
    @Autowired
    private OfflineDao offlineDao;
    @Autowired
    private BaseImpl baseImpl;
    public void loadOfflineData() {
        logger.debug("in task.........");
        List<Map<String, Object>> tentants = baseImpl.getSystemValue("dems-" + TableConstants.TENANT, List.class);
        tentants.stream().forEach(tentant->{
            String ddBB=(String)tentant.get("dbName");
            List<Map<String,Object>> projectList=offlineDao.queryOffLineProject(ddBB,(String)tentant.get("id"),null,null);       //TODO:项目期, 层/户 可过滤时间范围
            List<Map<String, Object>> chartAreaType = baseImpl.getSystemValue(ddBB+TableConstants.SEPARATE_CACHE + TableConstants.HOUSEHOLD_CHART_AREA_TYPE, List.class);
            Map<String, Object> chartAreaMap = new LinkedHashMap<>();
            chartAreaType.forEach(map->{
                chartAreaMap.put((String)map.get("id"), (String) map.get("householdChartAreaTypeName"));
            });
            projectList.forEach(project->{
                //获取户型图集合
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
                String projectPeriodId=(String)project.get("id");
                List<Map<String, Object>> charts = offlineDao.queryChart(ddBB, (String) tentant.get("id"), projectPeriodId, null);
                project.put("charts", charts);
               List<Map<String,Object>> procedureList= offlineDao.queryOfflineProcedure(ddBB,(String)tentant.get("id"),projectPeriodId , Arrays.asList("4","14"));            //验收记录可过滤时间范围
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
                baseImpl.setCacheOfValue("offline",ddBB+"-"+projectPeriodId,project);
            });
        });
    };
}
