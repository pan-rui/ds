package com.pc.wx.action;

import com.pc.base.BaseImpl;
import com.pc.base.BaseResult;
import com.pc.core.Base64;
import com.pc.core.Constants;
import com.pc.core.ParamsMap;
import com.pc.core.TableConstants;
import com.pc.dao.BaseDao;
import com.pc.service.BaseService;
import com.pc.wx.co.CacheKey;
import com.pc.wx.dao.LabourDao;
import com.pc.wx.util.ImgUtil;
import com.pc.wx.vo.VO;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.smartcardio.CardChannel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @Description: ${Description}
 * @Author: 潘锐 (2017-07-24 09:57)
 * @version: \$Rev: 3919 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-08-03 11:20:11 +0800 (周四, 03 8月 2017) $
 */
@Controller
//@RequestMapping("wx")
public class UserAction extends BaseAction{

    @Autowired
    private BaseDao baseDao;
    @Autowired
    private BaseImpl baseImpl;
    @Autowired
    private LabourDao labourDao;
    @Value("#{config['serverPath']}")
    private String serverPath;

    @RequestMapping(value = "userInfo",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult userInfo(HttpServletRequest request, @RequestParam(required = true) String openId,String tenantId,String ddBB,@RequestParam(defaultValue = "WeChat") String platform) {
        BaseResult failResult=new BaseResult(33, "未绑定用户");
        List<Map<String,Object>> socList= baseDao.queryByProsInTab("dems." + TableConstants.SOCIAL_LOGIN, ParamsMap.newMap("SOCIAL_UID", openId));
        if(CollectionUtils.isEmpty(socList)){
            return failResult;
        }else {
            Jedis jedis=Constants.jedisPool.getResource();
            if(StringUtils.isEmpty(tenantId))
            tenantId = jedis.hget(CacheKey.TENANTID, openId);
            String projectId=jedis.hget(CacheKey.PROJECTID, openId);
            if (StringUtils.isEmpty(projectId)||StringUtils.isEmpty(tenantId)) {
                jedis.close();
                return failResult;
            }
//            String userId = jedis.hget(CacheKey.USERID, openId);
            if(StringUtils.isEmpty(ddBB)) {
                List<Map<String, Object>> tentants = baseImpl.getSystemValue("dems-" + TableConstants.TENANT, List.class);
                final String tId=tenantId;
                Optional optional=tentants.stream().filter(map-> tId.equals(map.get("ID"))).findFirst();
                if(optional.isPresent()) {
                    ddBB = (String) ((Map<String, Object>) optional.get()).get("dbName");
                    jedis.hsetnx(CacheKey.DDBB, openId, ddBB);
                }
            }
            jedis.close();
            Map<String,Object> userInfo=labourDao.queryUserInfoMul(ddBB, (String) socList.get(0).get("userId"),tenantId, projectId);
            return new BaseResult(0,userInfo);
        }
    }

    @RequestMapping(value = "extInfos", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> extInfo(HttpServletRequest request,String openId) {
        Map<String, Object> listData = new HashMap<>();
        Jedis jedis=Constants.jedisPool.getResource();
        String projectCode=jedis.hget(CacheKey.PROJECTCODE, openId);
        String ddBB=jedis.hget(CacheKey.DDBB, openId);
        jedis.close();
        List<Map<String, Object>> pMap = baseDao.queryByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.PROJECT_PERIOD, ParamsMap.newMap("PROJECT_CODE", projectCode));
        listData.put("project", pMap);
        listData.put("companyInfo", baseDao.queryAllInTab(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_CONTRACTOR_COMPANY_INFO));//
        listData.put("workTypeInfo", baseDao.queryAllInTab(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_WORK_TYPENAME_INFO));//班组
        listData.put("jobTypeInfo", baseDao.queryAllInTab(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_JOB_TYPENAME_INFO));//类别
        listData.put("empCategoryInfo", baseDao.queryAllInTab(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_EMP_CATEGORY_INFO));//类型
        listData.put("jobInfo", baseDao.queryAllInTab(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_JOB_NAME_INFO));//工种
        return listData;
    }

    @RequestMapping(value = "validAndUser",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult validAndUser(@RequestBody VO vo) {
//    public BaseResult validAndUser(String projectCode,String phone,String openId) {
        String projectCode = (String) vo.getParams().get("projectCode");
        String phone = (String) vo.getParams().get("phone");
        String openId = (String) vo.getParams().get("openId");
        List<Map<String, Object>> projectMap =  baseDao.queryByProsInTab("dems."+TableConstants.PROJECT_PERIOD,ParamsMap.newMap("PROJECT_CODE", projectCode));
        if (CollectionUtils.isEmpty(projectMap)) {
            return new BaseResult(104, "项目编号不存在");
        }
        Map<String,Object> projectM=projectMap.get(0);
            final String tId=  projectM.get("tenantId").toString();
        Jedis jedis=Constants.jedisPool.getResource();
        String ddBB= jedis.hget(CacheKey.DDBB, openId);
        jedis.hset(CacheKey.TENANTID, openId, tId);
        jedis.hset(CacheKey.PROJECTCODE, openId, projectCode);
        jedis.hset(CacheKey.PROJECTID, openId, projectM.get("id").toString());
        jedis.hset(CacheKey.PHONE, openId, phone);
//        jedis.hset(CacheKey.USER, openId, userId);
        if (StringUtils.isEmpty(ddBB)) {
            List<Map<String, Object>> tentants = baseImpl.getSystemValue("dems-" + TableConstants.TENANT, List.class);
            Optional optional=tentants.stream().filter(map-> map.get("id").equals(tId)).findFirst();
            if(optional.isPresent()) {
                ddBB = (String) ((Map<String, Object>) optional.get()).get("dbName");
                jedis.hset(CacheKey.DDBB, openId, ddBB);
            }
        }
            String userId=UUID.randomUUID().toString().replace("-", "");
        Map<String, Object> userMap = ParamsMap.newMap("ID", userId).addParams("USER_NAME", phone)
                .addParams("PHONE", phone).addParams("PWD", Base64.encode(DigestUtils.md5Hex(phone.substring(5)).getBytes())).addParams("POST_ID", "20").addParams("TENANT_ID", tId);       //TODO:劳务工岗位
        List<Map> users=baseDao.queryByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.USER, ParamsMap.newMap("PHONE", phone));
        jedis.close();
        int i=users.size();
        if(CollectionUtils.isEmpty(users)){
            i=baseDao.insertIgnoreByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.USER, userMap);
        }
        if(i>0){
            Map<String,Object> wxUser=(Map<String, Object>) vo.getParams().get("wxUser");
            baseDao.insertUpdateByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.SOCIAL_LOGIN,ParamsMap.newMap("ID",UUID.randomUUID().toString().replace("-", ""))
            .addParams("FROM_PLATFORM","WeChat").addParams("SOCIAL_UID",wxUser.get("openid")).addParams("USER_ID",CollectionUtils.isEmpty(users)?userId:users.get(0).get("id")).addParams("CITY",wxUser.get("city"))
            .addParams("SEX",wxUser.get("sex")).addParams("USER_IMAGE",wxUser.get("headimgurl")).addParams("SOCIAL_USER_CREATE_TIME",new Date()).addParams("PROVINCE",wxUser.get("province"))
                    .addParams("MOBILE",phone).addParams("MEDIA_UID",wxUser.get("unionid")).addParams("IS_SEALED","0").addParams("UPDATE_TIME",new Date()).addParams("TENANT_ID",tId));
            String usId=UUID.randomUUID().toString().replace("-", "");
//            List<Map<String,Object>> userMaps=baseDao.queryByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.USER, ParamsMap.newMap("PHONE", phone));
//            if(!CollectionUtils.isEmpty(userMaps))
//                usId= (String) userMaps.get(0).get("id");
            i=baseDao.insertUpdateByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PERSON_INFO, ParamsMap.newMap("ID", usId).addParams("USER_ID", CollectionUtils.isEmpty(users)?userId:users.get(0).get("id")).addParams("EMP_PHONE", phone));
        }
        return i > 0 ? new BaseResult(0, "OK") : new BaseResult(100, "存在该用户");
    }

    @RequestMapping(value = "addUser",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult addUser(@RequestBody VO vo) {
//    public BaseResult addUser(String openId,String tenantId,String ddBB,String projectCode,Map<String,Object> userInfo) {
        System.out.println("vo======>"+vo.getParams());
        String openId = (String) vo.getParams().get("openId");
        String tenantId = (String) vo.getParams().get("tenantId");
        String ddBB = (String) vo.getParams().get("ddBB");
        String projectCode = (String) vo.getParams().get("projectCode");
        Map userInfo = (Map) vo.getParams().get("userInfo");
        Jedis jedis=Constants.jedisPool.getResource();
        if(StringUtils.isEmpty(tenantId))
            tenantId = jedis.hget(CacheKey.TENANTID, openId);
        if(StringUtils.isEmpty(ddBB))
            ddBB = jedis.hget(CacheKey.DDBB, openId);
        if(StringUtils.isEmpty(projectCode))
            projectCode = jedis.hget(CacheKey.PROJECTCODE, openId);
        String persId=UUID.randomUUID().toString().replace("-", "");
        ParamsMap<String, Object> pseronInfo = ParamsMap.newMap("ID", persId).addParams("EMP_PHONE", userInfo.get("lpi_empPhone")).addParams("EMP_NAME", userInfo.get("lpi_empName"))
                .addParams("EMP_NATION", userInfo.get("lpi_empNation")).addParams("EMP_NATIVE_PROVINCE",userInfo.get("lpi_empNativeProvince")).addParams("EMP_NATIVEPLACE", userInfo.get("lpi_empNativeplace"))
                .addParams("ID_CODE", userInfo.get("lpi_idCode")).addParams("ID_AGENCY", userInfo.get("lpi_idAgency")).addParams("ID_VALIDDATE", userInfo.get("lpi_idValiddate")).addParams("TENANT_ID", tenantId)
                .addParams("SEX",userInfo.get("lpi_sex")).addParams("UPDATE_TIME", new Date()).addParams("IS_SEALED", "0");//TODO: ID_CARD
        List<Map<String,Object>> userMaps=baseDao.queryByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.USER, ParamsMap.newMap("PHONE", userInfo.get("lpi_empPhone")));
        if(!CollectionUtils.isEmpty(userMaps))
            pseronInfo.addParams("USER_ID", userMaps.get(0).get("id"));
        try {
            int size1=baseDao.insertUpdateByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PERSON_INFO, pseronInfo),size2=0;
            if(size1>0) {
                if(size1>1){
                    List<Map<String,Object>> users=baseDao.queryByProsInTab(ddBB+TableConstants.SEPARATE+TableConstants.LABOR_PERSON_INFO,ParamsMap.newMap("ID_CODE",userInfo.get("lpi_idCode")));
                    persId = (String) users.get(0).get("id");
                }
                String ppId = UUID.randomUUID().toString().replace("-", "");
                String projectId= (String) userInfo.get("pp_id");
                Map<String, Object> projectPersonMap = ParamsMap.newMap("ID", ppId).addParams("EMP_CARDNUM", userInfo.get("lppi_empCardnum")).addParams("EMP_BANKNAME", userInfo.get("lppi_empBandname"))
                        .addParams("PERSON_ID", persId).addParams("PROJECT_ID",StringUtils.isEmpty(projectId)?jedis.hget(CacheKey.PROJECTID,openId):projectId).addParams("EMP_COMPANT_ID", userInfo.get("lcci_id")).addParams("EMP_CATEGORY_ID", userInfo.get("leci_id"))
                        .addParams("WORK_TYPENAME_ID", userInfo.get("lwti_id")).addParams("JOB_TYPENAME_ID", userInfo.get("ljti_id")).addParams("JOB_NAME_ID", userInfo.get("ljni_id")).addParams("IS_SYNCHRO", "1")
                        .addParams("EMP_STATUS","0").addParams("TENANT_ID", tenantId).addParams("UPDATE_TIME", new Date()).addParams("IS_SEALED", "0");
                size2 = baseDao.insertUpdateByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PROJECT_PERSON_INFO, projectPersonMap);
            }
            return size2 > 0 ? new BaseResult(0, "OK") : new BaseResult(101, "FAIL");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        return new BaseResult(221, "FAIL");
    }

    @RequestMapping(value = "getCheckWork",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult getCheckWork(HttpServletRequest request, @RequestParam(required = true) String openId, String tenantId, String projectCode, String ddBB,String month) {
        List<Map<String,Object>> resultList=labourDao.queryCheckWork(ddBB, openId, tenantId, projectCode, month);
        return CollectionUtils.isEmpty(resultList) ? new BaseResult(101, "没有数据!") : new BaseResult(0, resultList);
    }
    @RequestMapping(value = "getSalary",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult getSalary(HttpServletRequest request, @RequestParam(required = true) String openId, String tenantId, String projectCode, String ddBB) {
        List<Map<String,Object>> resultList=labourDao.querySalary(ddBB, openId, tenantId, projectCode);
        return CollectionUtils.isEmpty(resultList) ? new BaseResult(101, "没有数据!") : new BaseResult(0, resultList);
    }

    @RequestMapping(value = "downImg",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult downImg(HttpServletRequest request,String openId,String tenantId,String ddBB,String projectCode,String serverId,String accToken,int isFront){
        Map<String, Object> userMul = (Map<String, Object>) userInfo(request, openId, tenantId, ddBB, "WeChat").getData();
        if(!CollectionUtils.isEmpty(userMul)){
            String pathSuffix= userMul.get("pp_id") + "/1/"+userMul.get("lpi_idCode")+"_"+isFront+".jpg";
            int size=0;
//            File file = new File(ImgUtil.BASE_PATH+pathSuffix);
//            if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
            if(downUrlTxt("http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="+accToken+"&media_id="+serverId,ImgUtil.BASE_PATH+pathSuffix)){
               size=baseDao.updateByProsInTab(ddBB + TableConstants.SEPARATE + TableConstants.LABOR_PERSON_INFO, ParamsMap.newMap(isFront > 1 ? "IDPHOTO_SCAN2" : "IDPHOTO_SCAN", pathSuffix)
                       .addParams("UPDATE_TIME", new Date()).addParams("ID", userMul.get("lpi_id")));
            };
            return size>0?new BaseResult(0, "OK",ParamsMap.newMap("url",serverPath+pathSuffix)):new BaseResult(301,"保存图片失败!");
        }
        return new BaseResult(303,"下载图片失败!");
    }

    public boolean downUrlTxt(String fileUrl,String filePath){
            File file = new File(filePath);//创建新文件
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            if(file!=null && !file.exists()){
                file.createNewFile();
            }
            OutputStream oputstream = new FileOutputStream(file);
            URL url = new URL(fileUrl);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.setConnectTimeout(3*1000);
            uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            uc.setDoInput(true);//设置是否要从 URL 连接读取数据,默认为true
            uc.connect();
            InputStream iputstream = uc.getInputStream();
            System.out.println("file size is:"+uc.getContentLength());//打印文件长度
            byte[] buffer = new byte[4*1024];
            int byteRead = -1;
            while((byteRead=(iputstream.read(buffer)))!= -1){
                oputstream.write(buffer, 0, byteRead);
            }
            oputstream.flush();
            iputstream.close();
            oputstream.close();
            return true;
        } catch (Exception e) {
            System.out.println("读取失败！");
            e.printStackTrace();
        }
        return false;
    }

}
