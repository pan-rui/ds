package com.pc.util;

import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.dao.BaseDao;
import javafx.scene.control.Tab;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Description: ${Description}
 * @Author: 潘锐 (2017-04-26 16:44)
 * @version: \$Rev: 2913 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-06-08 19:09:46 +0800 (周四, 08 6月 2017) $
 */
@Service
public class ExcelUtils {
    private static Logger logger = LogManager.getLogger(ExcelUtils.class);
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";
    @Autowired
    private BaseDao baseDao;

    /**
     * 读入excel文件，解析后返回
     *
     * @param file
     * @throws IOException
     */
    public static List<String[]> readExcel(MultipartFile file) throws IOException {
        //检查文件
        checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<String[]> list = new ArrayList<String[]>();
        if (workbook != null) {
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                //获得当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if (sheet == null) {
                    continue;
                }
                //获得当前sheet的开始行
                int firstRowNum = sheet.getFirstRowNum();
                //获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                //循环除了第一行的所有行
                for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                    //获得当前行
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }
                    //获得当前行的开始列
                    int firstCellNum = row.getFirstCellNum();
                    //获得当前行的列数
                    int lastCellNum = row.getLastCellNum();
                    String[] cells = new String[lastCellNum - firstCellNum + 1];
                    //循环当前行
                    for (int cellNum = firstCellNum; cellNum <= lastCellNum; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        cells[cellNum] = getCellValue(cell);
                    }
                    list.add(cells);
                }
            }
            workbook.close();
        }
        return list;
    }

    public static void checkFile(MultipartFile file) throws IOException {
        //判断文件是否存在
        if (null == file) {
            logger.error("文件不存在！");
            throw new FileNotFoundException("文件不存在！");
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if (!fileName.endsWith(xls) && !fileName.endsWith(xlsx)) {
            logger.error(fileName + "不是excel文件");
            throw new IOException(fileName + "不是excel文件");
        }
    }

    public static Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith(xls)) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith(xlsx)) {
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return workbook;
    }

    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        //把数字当成String来读，避免出现1读成1.0的情况
/*        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }*/
        //判断数据的类型
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: //数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                if (cellValue.endsWith(".0"))
                    cellValue = cellValue.substring(0, cellValue.length() - 2);
                break;
            case Cell.CELL_TYPE_STRING: //字符串
                cellValue = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            default: //空值
//            case Cell.CELL_TYPE_BLANK: //空值
                cellValue = "";
                break;
/*            case Cell.CELL_TYPE_ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;*/
        }
        return cellValue;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    public void importData(String treeTableName, String treeFieldName, String tableName, List<String> fields, String relativeField, MultipartFile file, String tenantId, String userId) throws IOException {//PROCEDURE_TYPE_NAME
        //检查文件
        checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        if (workbook != null) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) return;
            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum();
            int lastRowNum = sheet.getLastRowNum();
            Map<Integer, String> names = new LinkedHashMap<>();
            Map<Integer, String> ids = new LinkedHashMap<>();
            ParamsMap<String, String> fieldMap = new ParamsMap<>();
            //循环除了第一行的所有行
            for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if (row == null)
                    continue;
                //获得当前行的开始列
                int firstCellNum = row.getFirstCellNum();
                int lastCellNum = row.getLastCellNum();
                //循环当前行
                for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                    Cell cell = row.getCell(cellNum);
                    String cellVal = getCellValue(cell);
                    if (StringUtils.isNotEmpty(cellVal)) {
                        names.put(cellNum, cellVal);
                        String id = UUID.randomUUID().toString().replace("-", "");
                        ids.put(cellNum, id);
                        if (cellNum < lastCellNum - fields.size()) {
                            baseDao.insertIgnoreByProsInTab(treeTableName, ParamsMap.newMap("ID", id).addParams(treeFieldName, cellVal).addParams("ID_TREE", getTreeName(ids, cellNum)).addParams("NAME_TREE", getTreeName(names, cellNum))
                                    .addParams("LEVEL", cellNum - firstCellNum + 1).addParams("IS_LEAF", cellNum == lastCellNum - fields.size() - 1 ? "1" : "0").addParams("UPDATE_TIME", new Date()).addParams("PARENT_ID", ids.get(cellNum - 1)).addParams("TENANT_ID", tenantId).addParams("UPDATE_USER_ID", userId));
                        } else if (cellNum >= lastCellNum - fields.size() && cellNum < lastCellNum - 1) {
                            fieldMap.put(fields.get(cellNum - lastCellNum + fields.size()), cellVal);
                        } else {
                            fieldMap.put(fields.get(cellNum - lastCellNum + fields.size()), cellVal);
                            baseDao.insertIgnoreByProsInTab(tableName, fieldMap.addParams("ID", id).addParams(relativeField, ids.get(lastCellNum - fields.size() - 1)).addParams("UPDATE_TIME", new Date()).addParams("TENANT_ID", tenantId).addParams("UPDATE_USER_ID", userId));
                        }
                    }
                }
            }
        }
    }

    public String getTreeName(Map<Integer, String> treeMap, int cellNum) {
        StringBuffer sb = new StringBuffer();
        treeMap.forEach((k, v) -> {
            if (k <= cellNum)
                sb.append(TableConstants.SEPARATE_TREE).append(v);
        });
        return sb.toString().substring(1);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    public void importItem(Map<String, String> tableMap, String relateTable, String relateField, List<String> fields, MultipartFile file, String tenantId, String userId) throws IOException {
        //检查文件
        checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        if (workbook != null) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) return;
            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum();
            int lastRowNum = sheet.getLastRowNum();
            List<String> procedureIdList = null;
            String tableName = null;
            //获得行的开始列
            int firstCellNum = sheet.getRow(firstRowNum).getFirstCellNum();
            int lastCellNum = sheet.getRow(firstRowNum).getLastCellNum();
            //循环除了第一行的所有行
            a:
            for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                ParamsMap<String, Object> fieldMap = new ParamsMap<>();
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if (row == null)
                    continue;
                //循环当前行
                b:
                for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                    Cell cell = row.getCell(cellNum);
                    String cellVal = getCellValue(cell);
                    if (cellNum > firstCellNum) {
                        if (cellNum == lastCellNum - fields.size() - 2) {
                            if (StringUtils.isNotEmpty(cellVal)) {
                                List<Map<String, Object>> procedure = baseDao.queryByProsInTab(relateTable, ParamsMap.newMap("PROCEDURE_CODE", cellVal));
                                procedureIdList = new LinkedList<>();
                                if (!procedure.isEmpty()) {
                                    for (Map<String, Object> map : procedure)
                                        procedureIdList.add((String) map.get("id"));
                                }
                            }
                        } else if (cellNum == lastCellNum - fields.size() - 1) {
                            if (StringUtils.isNotEmpty(cellVal))
                                tableName = tableMap.get(cellVal.trim().substring(0, 2));
                        } else if (cellNum == lastCellNum - 1) {
                            fieldMap.put(fields.get(cellNum - lastCellNum + fields.size()), cellVal);
                            if (StringUtils.isEmpty((String) fieldMap.get("GRADING_STANDARD"))) break a;
                            fieldMap.addParams("UPDATE_TIME", new Date()).addParams("ID", UUID.randomUUID().toString().replace("-", "")).addParams("TENANT_ID", tenantId).addParams("UPDATE_USER_ID", userId);
                            if (StringUtils.isNotEmpty(tableName) && !procedureIdList.isEmpty()) {
                                String tName = tableName;
                                //一般项目在主控
                                if (!StringUtils.isEmpty((String) fieldMap.get("PASS_TEXT"))) {
                                    if (tName.equals(tableMap.get("一般")))
                                        fieldMap.put("IS_GENERAL", "1");
                                    tName = TableConstants.DOMINANT_ITEM;
                                }
                                for (String procedureId : procedureIdList) {
                                    fieldMap.addParams(relateField, procedureId);
                                    baseDao.insertByProsInTab(tName, fieldMap);
                                }
                            }
                        } else {
                            fieldMap.put(fields.get(cellNum - lastCellNum + fields.size()), cellVal);
                        }
                    }
                }
            }
        }
    }
//实名制企业管理表
@Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
public void importCompany(MultipartFile file, String tableName, List<String> fields, String tenantId, String userId,String projectId) throws IOException {
        //检查文件
        checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        if (workbook != null) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) return;
            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum();
            int lastRowNum = sheet.getLastRowNum();
            List<Map<String, Object>> listData = new LinkedList<>();
            String ddBB=tableName.split("\\.")[0];
            //循环除了第一行的所有行
            for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                ParamsMap<String, Object> fieldMap = new ParamsMap<>();
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if (row == null)
                    continue;
                //获得当前行的开始列
                int firstCellNum = row.getFirstCellNum();
                int lastCellNum = row.getLastCellNum();
                //循环当前行
                for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                    Cell cell = row.getCell(cellNum);
                    String cellVal = getCellValue(cell);
                    if (StringUtils.isNotEmpty(cellVal)) {
                        switch (cellNum) {
                            default:
                                String key = fields.get(cellNum);
                                if (!StringUtils.isEmpty(key))
                                    fieldMap.put(key, cellVal);
                                break;
                        }
                    }
                    if (cellNum == lastCellNum - 1) {
                        List<Map<String,Object>> rList=baseDao.queryByProsInTab(ddBB+TableConstants.SEPARATE+TableConstants.LABOR_COMPANY_TYPE_INFO,ParamsMap.newMap("NAME",cellVal));
                        if(!rList.isEmpty()) fieldMap.put(fields.get(cellNum), rList.get(0).get("id"));
                        fieldMap.addParams("ID", UUID.randomUUID().toString().replace("-", "")).addParams("TENANT_ID", tenantId).addParams("UPDATE_USER_ID", userId).addParams("PROJECT_ID", projectId).addParams("IS_SEALED", 0);
                        listData.add(fieldMap);
                    }
                }
            }
            int size = baseDao.insertUpdateBatchByProsInTab(tableName, listData);
        }
    }

    //实名制人员管理表
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    public void importPerson(MultipartFile file, String tableName, List<String> fields, String tenantId, String userId,String projectId) throws IOException {
        //检查文件
        checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        if (workbook != null) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) return;
            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum();
            int lastRowNum = sheet.getLastRowNum();
            List<Map<String, Object>> listData = new LinkedList<>();
            List<Map<String, Object>> listData2 = new LinkedList<>();
            String ddBB=tableName.split("\\.")[0];
            //循环除了第一行的所有行
            for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                ParamsMap<String, Object> fieldMap = new ParamsMap<>();
                ParamsMap<String, Object> fieldMap2 = new ParamsMap<>();
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if (row == null)
                    continue;
                //获得当前行的开始列
                int firstCellNum = row.getFirstCellNum();
                int lastCellNum = row.getLastCellNum();
                //循环当前行
                for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                    Cell cell = row.getCell(cellNum);
                    String cellVal = getCellValue(cell);
                    if (StringUtils.isNotEmpty(cellVal)) {
                        switch (cellNum) {
                            case 11:
                                List<Map<String,Object>> list11=baseDao.queryByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_CONTRACTOR_COMPANY_INFO, ParamsMap.newMap("COMPANY_NAME", cellVal));
                                if(!list11.isEmpty()) fieldMap2.put("EMP_COMPANT_ID", list11.get(0).get("id"));
                                break;
                            case 13:
                                List<Map<String, Object>> list13 = baseDao.queryByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_WORK_TYPENAME_INFO, ParamsMap.newMap("NAME", cellVal));
                                if(!list13.isEmpty()){
                                    fieldMap2.put("WORK_TYPENAME_ID", list13.get(0).get("id"));
                                }else{
                                    baseDao.insertByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_WORK_TYPENAME_INFO, ParamsMap.newMap("ID", UUID.randomUUID().toString().replace("-", "")).addParams("NAME", cellVal).addParams("PROJECT_ID", projectId));
                                }
                                break;
                            case 24:
                                fieldMap2.put("SAFE_EDUCATION_SCORE", cellVal);
                                break;
                            case 25:
                                List<Map<String, Object>> list25 = baseDao.queryByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_ORG_INFO, ParamsMap.newMap("ORGANIZATION_NAME", cellVal));
                                if(!list25.isEmpty()) fieldMap2.put("EMP_DEPT", list25.get(0).get("id"));
                                break;
                            case 26:
                                List<Map<String, Object>> list26 = baseDao.queryByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_ORG_INFO, ParamsMap.newMap("ORGANIZATION_NAME", cellVal));
                                if(!list26.isEmpty()) fieldMap2.put("JOB_DEPT", "");
                                break;
                            case 27:
                                fieldMap2.put("CONTRACT_STATUS", cellVal);
                                break;
                            case 28:
                                fieldMap2.put("CONTRACT_SIGN_TIME", cellVal);
                                break;
                            case 31:
                                fieldMap2.put("HAS_INSURANCE", cellVal);
                                break;
                            case 32:
                                fieldMap2.put("HAS_SAFE_EDUCATION_CARD", cellVal);
                                break;
                            case 33:
                                fieldMap2.put("HAS_SAFE_EDUCATION_CARD_TIME", cellVal);
                                break;
                            case 38:        //人员类型
                                List<Map<String, Object>> list38 = baseDao.queryByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_JOB_TYPENAME_INFO, ParamsMap.newMap("NAME", cellVal));
                                if(!list38.isEmpty()) fieldMap2.put("JOB_TYPENAME_ID", list38.get(0).get("id"));
                                break;
                            case 39:
                                List<Map<String, Object>> list39 = baseDao.queryByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_JOB_NAME_INFO, ParamsMap.newMap("NAME", cellVal));
                                if(!list39.isEmpty()) fieldMap2.put("JOB_NAME_ID", list39.get(0).get("id"));
                                break;
                            default:
                                if(cellNum<fields.size()) {
                                    String key = fields.get(cellNum);
                                    if (!StringUtils.isEmpty(key))
                                        fieldMap.put(key, cellVal);
                                }
                                break;
                        }
                    }
                    if (cellNum == lastCellNum - 1) {
                        List<Map<String,Object>> rList=baseDao.queryByProsInTab(ddBB+TableConstants.SEPARATE+TableConstants.LABOR_COMPANY_TYPE_INFO,ParamsMap.newMap("NAME",cellVal));
                        if(!rList.isEmpty()) fieldMap.put(fields.get(cellNum), rList.get(0).get("id"));
                        String id1=UUID.randomUUID().toString().replace("-", "");
                        fieldMap.addParams("ID", id1).addParams("TENANT_ID", tenantId).addParams("UPDATE_USER_ID", userId).addParams("IS_SEALED", "0");
//                        listData.add(fieldMap);
                        int size=baseDao.insertUpdateByProsInTab(tableName, fieldMap);
                        if(size>0) {
                            List<Map<String, Object>> list38 = baseDao.queryByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_EMP_CATEGORY_INFO, ParamsMap.newMap("NAME", "建筑工人"));
                            if (!list38.isEmpty())
                                fieldMap2.put("EMP_CATEGORY_ID", list38.get(0).get("id"));        //人员类别
                            fieldMap2.addParams("ID", UUID.randomUUID().toString().replace("-", "")).addParams("PERSON_ID", id1).addParams("IS_SEALED", "0").addParams("PROJECT_ID", projectId).addParams("TENANT_ID", tenantId);
//                        listData2.add(fieldMap2);
                            baseDao.insertUpdateByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PROJECT_PERSON_INFO, fieldMap2);
                        }
                    }
                }
            }
/*            int size = baseDao.insertBatchByProsInTab(tableName, listData);
            if(size>0)
                baseDao.insertBatchByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PROJECT_PERSON_INFO, listData2);*/
        }
    }
}
