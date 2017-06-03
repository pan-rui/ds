import com.pc.core.Base64;
import com.pc.core.ColumnProcess;
import com.pc.core.ParamsMap;
import com.pc.dao.BaseDao;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ThinkPad on 2017/3/25.
 */
@Service
public class TestMain {

    //    @Autowired
    public static BaseDao baseDao;

    public static void main2(String[] args) {
        System.out.println(Long.MAX_VALUE);
        System.out.println(Base64.encode(DigestUtils.md5("12345678".getBytes())));
        HashedCredentialsMatcher matcher3=new HashedCredentialsMatcher("md5");
        SimpleCredentialsMatcher matcher4=new SimpleCredentialsMatcher();
        matcher3.setHashIterations(1);
        matcher3.setStoredCredentialsHexEncoded(false);
        System.out.println(matcher3.doCredentialsMatch(new UsernamePasswordToken("18820276678", "12345678", true), new SimpleAuthenticationInfo("18820276678", "JdVa0oOqQAr0ZMdtcTwHrQ==","")));
        System.out.println(matcher4.doCredentialsMatch(new UsernamePasswordToken("18820276678", "ZTEwYWRjMzk0OWJhNTlhYmJlNTZlMDU3ZjIwZjg4M2U=", true), new SimpleAuthenticationInfo("18820276678", "ZTEwYWRjMzk0OWJhNTlhYmJlNTZlMDU3ZjIwZjg4M2U=","")));
        System.out.println(org.apache.commons.codec.binary.Base64.encodeBase64String(DigestUtils.md5("12345678".getBytes())));
        Pattern tPattern = Pattern.compile("(\\w+\\.)?\\*", Pattern.CASE_INSENSITIVE);
        Matcher matcher2=tPattern.matcher("SELECT * FROM ${opTableName} a RIGHT JOIN ${orprTableName} b ON a.ID=b.OPERATE_PRIVILEGE_ID RIGHT JOIN ${orTableName} c ON b.OPERATE_ROLE_ID=c.ID WHERE a.TENANT_ID=1 AND a.STATE_SIGN=0 AND c.ID=#{operatRoleId}");
        if(matcher2.find(6))
        System.out.println(matcher2.group(1)==null);
        System.out.println("a.* FROM ${opTableName} a RIGHT JOIN ${orprTableName} b ON a.ID=b.OPERATE_PRIVILEGE_ID RIGHT JOIN ${orTableName} c ON b.OPERATE_ROLE_ID=c.ID WHERE a.TENANT_ID=1 AND a.STATE_SIGN=0 AND c.ID=#{operatRoleId}".replaceFirst("\\w+\\.\\*|\\*", "AA aa,BdgeB bdgeB"));
        System.out.println("queryPageInTab".matches(".+Page\\w*"));
        Pattern pattern = Pattern.compile("(?<=\\.)(\\w+)", Pattern.CASE_INSENSITIVE);
//      Matcher matcher= pattern.matcher("SELECT TABLE_NAME,COLUMN_NAME FROM information_schema.Columns  WHERE table_schema='dems'");
//      Matcher matcher= pattern.matcher("SELECT * FROM information_schema.Columns a  join dems.USER b on a.dd=b.33 WHERE table_schema='dems'");
      Matcher matcher= pattern.matcher("SELECT a.* FROM ${opTableName} a RIGHT JOIN ${orprTableName} b ON a.ID=b.OPERATE_PRIVILEGE_ID RIGHT JOIN ${orTableName} c ON b.OPERATE_ROLE_ID=c.ID WHERE a.TENANT_ID=1 AND a.STATE_SIGN=0 AND c.ID=#{operatRoleId}");
//      Matcher matcher= pattern.matcher("select * from dems.USER");
//        System.out.println(matcher.find(1));
        while (matcher.find()) {
            System.out.println(matcher.group(1));
            System.out.println(matcher.groupCount());
        }

        //query.............
/*        Map<String, Object> params = new LinkedHashMap<>();
        params.put("ID", "93fjigrylj93t4t");
        List<Map<String, Object>> resultList = baseDao.queryAllInTab("dems.USER",params);
        System.out.println(new Date().getTime());
        System.out.println(new java.sql.Date(new Date().getTime()));*/
    }

    public static void main3(String[] args) {
        String testStr = "{\"params\":{\"id\":\"9187cc14b66444b3b754d1da3b1f6539\",\"dominantItemCheckResult\":\"1\",\"generalItemCheckResult\":\"1\",\"generalItemCheckScore\":\"83\",\"totalCheckResult\":\"1\",\"totalCheckScore\":\"85\",\"eligibleRate\":\"0.81\",\"acceptanceAttach\":[{\"attachPath\":\"temp/img/d6607154-5447-4bef-badc-3cd991ca30ea.jpg\"}],\"acceptanceDominantItem\":[{\"dominantItemId\":\"2bbccfb9b3e94c2f92d2a89a250e0cbe\",\"eligible\":\"1\"},{\"dominantItemId\":\"32f235e34d8e40659dab1faa996eeb3c\",\"eligible\":\"1\"}],\"acceptanceGeneralItem\":[{\"generalItemId\":\"6ad4a7dae853440a83d425f5057aa834\",\"eligible\":\"1\",\"score\":\"88\",\"minPassRatio\":\"0.8\",\"checkPointSize\":\"5\",\"checkPointMaxDiff\":\"2.5\",\"eligibleRate\":\"0.8\",\"coincidentRate\":\"0.8\",\"unit\":\"mm\",\"acceptancePoint\":[{\"orderNo\":\"1\",\"realVal\":\"7.5\"},{\"orderNo\":\"2\",\"realVal\":\"4.5\"},{\"orderNo\":\"3\",\"realVal\":\"5.5\"},{\"orderNo\":\"4\",\"realVal\":\"5\"},{\"orderNo\":\"5\",\"realVal\":\"4.5\"}]}]}}";
        Pattern pattern=Pattern.compile("\\\"(\\w+)\\\":");
        Matcher matcher=pattern.matcher(testStr);
        StringBuffer sb = new StringBuffer();
        int last=8;
        while (matcher.find()) {
            String replaceStr = ColumnProcess.decryptVal(matcher.group(1));
//            String replaceStr = ColumnProcess.decryptVal(matcher.group(1));
            last = matcher.end(1);
//            sb.append(testStr.substring(0,matcher.start(1))).append(replaceStr);
            matcher.appendReplacement(sb, "\""+replaceStr+"\":");
        }
            matcher.appendTail(sb);
        System.out.println(sb.toString());
        System.out.println(matcher);
        System.out.println("3ojhfrt".indexOf("f",-1));
    }

    public static void main4(String[] args) {
        Pattern pattern = Pattern.compile("(?<=\\.)(\\w+)(\\s+(\\w+)(?=,)?)?");
//        String sql = "select a.* from dems.USER adf,dems.IOFD af,dems.DFS dfs";
//        String sql = "select a.* from dems.USER adf join dems.IOFD af join dems.DFS dfs";
        String sql = "select a.* from dems.USER a";
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
            System.out.println(matcher.group(3));
        }
        String str = "ID id,PARENT_COMPANY_ID parentCompanyId,SUPERVISOR_COMPANY supervisorCompany,SUPERVISOR_COMPANY_ID supervisorCompanyId,CONTRACTING_PRO contractingPro,CONTRACTING_PRO_ID contractingProId,CONSTRUCTION_TEAM constructionTeam,CONSTRUCTION_TEAM_ID constructionTeamId,CONSTRUCTION_MANAGER_ID constructionManagerId,PROJECT_PERIOD_ID projectPeriodId,PROJECT_NAME projectName,REGION_ID regionId,REGION_MIN_NAME regionMinName,REGION_NAME regionName,REGION_TYPE regionType,REGION_ID_TREE regionIdTree,REGION_NAME_TREE regionNameTree,PROCEDURE_ID procedureId,PROCEDURE_NAME procedureName,STATEMENT_ID statementId,BATCH_TIMES batchTimes,CHECK_TIMES checkTimes,COINCIDENCE_RATE coincidenceRate,WORK_RATIO workRatio,SUPERVISOR_ID supervisorId,SUPERVISOR_NAME supervisorName,SUPERVISOR_CHECK_TIME supervisorCheckTime,SUPERVISOR_CHECKED supervisorChecked,SUPERVISOR_CHECK_RESULT supervisorCheckResult,PROJECT_OWNER_RANDOM_TIME projectOwnerRandomTime,PROJECT_OWNER_ID projectOwnerId,PROJECT_OWNER_CHECKED projectOwnerChecked,PROJECT_OWNER_CHECK_RESULT projectOwnerCheckResult,CONSTRUCTION_INSPECTOR constructionInspector,CONSTRUCTION_INSPECTOR_ID constructionInspectorId,CONSTRUCTION_INSPECTOR_CHECKED constructionInspectorChecked,CONSTRUCTION_INSPECTOR_CHECK_RESULT constructionInspectorCheckResult,CONSTRUCTION_INSPECTOR_CHECK_DATE constructionInspectorCheckDate,TEAM_INSPECTOR teamInspector,TEAM_INSPECTOR_ID teamInspectorId,TEAM_INSPECTOR_CHECKED teamInspectorChecked,TEAM_INSPECTOR_CHECK_RESULT teamInspectorCheckResult,TEAM_INSPECTOR_CHECK_TIME teamInspectorCheckTime,REMARK remark,UPDATE_USER_ID updateUserId,UPDATE_TIME updateTime,IS_SEALED isSealed,SEALED_USER_ID sealedUserId,SEALED_TIME sealedTime,TENANT_ID tenantId";
        String prefix="an.";
        System.out.println(str.replaceAll("([A-Z_]+)\\s(\\w+)", prefix+"$1 " + prefix.replace(".","_")+"$2"));
    }

    public static void main(String[] args) {
        System.out.println(Long.MAX_VALUE);
        ParamsMap paramsMap = ParamsMap.newMap("sdddddddddfd", "39ufr");
        paramsMap.addParams("tList", Arrays.asList("fdf", "0j", "3fdf"));
        System.out.println(paramsMap);
    }

}
