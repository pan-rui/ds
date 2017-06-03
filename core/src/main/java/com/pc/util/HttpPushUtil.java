package com.pc.util;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class HttpPushUtil {
	String url = "http://61.144.226.194:7040";
	String apiVersion="1.1";
	//通用配置，提出来建表
	String apiKey="3D92297B82384EA394FEDEFBFB524382";
	String apiSecret="AFF160EFA5744FFB966F00195D0D6C93";
	String clientSerial="PL6B9CFD1F09B14464BA3F142B5F08CE08";
	String projectId="44030120170531001";
	//查询接口
	String workTypeName="/CWRService/DictListWorkTypeName";
	String empCategory="/CWRService/DictListEmpCategory";
	String jobTypeName="/CWRService/DictListJobTypeName";
	String jobName="/CWRService/DictListJobName";
	String companyType="/CWRService/GetCompanyType";
	//上传接口
	String registerEmployee="/CWRService/RegisterEmployee";
	String uploadPassedLog="/CWRService/UploadPassedLog";
	String uploadSafetyEducation="/CWRService/UploadSafetyEducation";
	String uploadPayroll="/CWRService/UploadPayroll";
	String uploadContract="/CWRService/UploadContract";
	String userLeaveProjec="/CWRService/userLeaveProjec";
	String addCompany="/CWRService/AddCompany";
	String projectRemoveCompany=" /CWRService/ProjectRemoveCompany";
	
	private String getSignature(String timestamp,String jsonParam){
		StringBuffer signature=new StringBuffer(apiSecret);
		signature.append("api_key"+apiKey);
		signature.append("api_version"+apiVersion);
		signature.append("body"+(StringUtils.isBlank(jsonParam)?"{}":jsonParam));
		signature.append("client_serial"+clientSerial);
		signature.append("timestamp"+timestamp);
		signature.append(apiSecret);
		return MD5Util.getMD5(signature.toString());
	}
	
	private String getUrl(String apiName,String timestamp,String signature){
		return url+apiName+"?api_key="+apiKey+"&api_version="+apiVersion+"&client_serial="+clientSerial+"&timestamp="+URLEncoder.encode(timestamp)+"&signature="+signature;
	}
	
	public JSONObject httpPost(String apiName,String jsonParam){
		String timestamp=DateUtil.convertDateTimeToString(new Date(), null);
		String signature=getSignature(timestamp,jsonParam);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonResult = null;
        
        String purl=getUrl(apiName, timestamp,signature);
        
        HttpPost method = new HttpPost(purl);
        try {
            if (StringUtils.isNotBlank(jsonParam)) {
                StringEntity entity = new StringEntity(jsonParam, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                method.setEntity(entity);
            }
            HttpResponse result = httpClient.execute(method);
            
            if (result.getStatusLine().getStatusCode() == 200) {
                String str = "";
                try {
                    str = EntityUtils.toString(result.getEntity());
                    jsonResult = JSONObject.parseObject(str);
                } catch (Exception e) {
                    System.out.println(purl+"-请求提交失败:" + e);
                }
            }else{
            	return null;
            }
        } catch (IOException e) {
        	System.out.println(purl+"-请求提交失败:" + e);
        }
        return jsonResult;
    }
	
	public static void main(String[] args) {
		HttpPushUtil httpPushUtil=new HttpPushUtil();
//		JSONObject company=new JSONObject();
//		company.put("Project_ID", httpPushUtil.projectId);
//		company.put("Company_Name", "鹏建互联");
//		company.put("Capital", 500);
//		company.put("Legal_Person", "谢总");
//		company.put("SUID", "000001");
//		company.put("Bank_Open", "中国银行");
//		company.put("Bank_Num", "88888888");
//		company.put("Address", "深圳市福田区");
//		company.put("Contacts", "谭总");
//		company.put("Mobile_Phone", "18888888888");
//		company.put("Email", "666@qq.com");
//		company.put("Postcode", "515200");
//		company.put("Type", "13");
//		JSONObject obj=httpPushUtil.httpPost(httpPushUtil.addCompany, company.toString());
//		System.out.println(obj);
		
		JSONObject user=new JSONObject();
		user.put("Project_ID", httpPushUtil.projectId);
		user.put("id_code", "430623199101034217");
		user.put("id_photo", ImgUtil.encodeBase64Img(ImgUtil.BASE_PATH+ImgUtil.TEMP_PATH+"0b91b9a4-9020-40ff-abda-0b1c90a8b4db.jpg"));
		user.put("emp_name", "张三");
		user.put("emp_phone", "188888888888");
		user.put("emp_nativeplace", "湖南岳阳");
		user.put("emp_nation", "汉");
		user.put("pass_period", "2017-06-01:");
		user.put("match_flag", "Y");
		user.put("facephoto", ImgUtil.encodeBase64Img(ImgUtil.BASE_PATH+ImgUtil.TEMP_PATH+"77e992ac-1da5-4bd9-9dfa-6fcb3b20dafb.jpg"));
		user.put("emp_company", "鹏建互联");
		user.put("work_typename", "保安保洁");
		user.put("emp_category", "00");
		user.put("cwr_iskeypsn", "0");
		user.put("emp_dept", "搬砖部");
		user.put("job_typename", "1");
		user.put("job_name", "EEF215A8FEE411E68E5A089E016627F6");
		user.put("contract_status", "1");
		user.put("id_agency", "湖南省公安厅");
		user.put("id_validdate", "2008.08.07-2028.08.07");
		user.put("emp_bankname", "建行西丽分行");
		user.put("emp_cardnum", "88888888");
		user.put("job_dept", "搬砖部");
		user.put("idphoto_scan", ImgUtil.encodeBase64Img(ImgUtil.BASE_PATH+ImgUtil.TEMP_PATH+"2bfbcb8c-78fe-4d48-b303-1e2cdb2d048f.jpg"));
		user.put("idphoto_scan2", ImgUtil.encodeBase64Img(ImgUtil.BASE_PATH+ImgUtil.TEMP_PATH+"3d53972c-28d0-43bd-ba95-78a3a17533f0.jpg"));
		System.out.println(user.toString());
		JSONObject obj=httpPushUtil.httpPost(httpPushUtil.registerEmployee, user.toString());
		System.out.println(obj);
	
	}
}
