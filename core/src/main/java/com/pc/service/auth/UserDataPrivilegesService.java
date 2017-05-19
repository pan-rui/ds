package com.pc.service.auth;

import com.pc.core.ColumnConstants;
import com.pc.core.TableConstants;
import com.pc.dao.BaseDao;
import com.pc.dao.auth.AuthDao;
import com.pc.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 用户数据权限服务
 * @Author: wady (2017-03-29 16:58)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-29
 */
@Service
public class UserDataPrivilegesService extends BaseService {
    // group key
    public final static String GROUP_KEY = "root";
    private static final Logger logger = LogManager.getLogger(UserDataPrivilegesService.class);
    @Autowired
    private AuthDao authDao;
    @Autowired
    private BaseDao baseDao;

    /**
     * 查询权限操作语句根据权限
     * @param privilegeId 权限编号
     * @return String 数据库语句
     */
    public String getPrivilegesSQLByPrivilege(String privilegeId)
    {
        String sql = "";

        Map<String, Object> privilege = getByID(TableConstants.DATA_PRIVILEGES, privilegeId);
        if(privilege != null)
        {
            if(privilege.get("DATA_DEFINITION") != null)
            {
                sql = (String)privilege.get("DATA_DEFINITION");
            }
        }

        return sql;
    }

    public String getSQLBySceneRule(Map<String, Object> mapRule)
    {
        String sql = "";
        Object val = null;
        Object op = null;
        Object type = "";

        val = mapRule.get(ColumnConstants.SCENE_TABLE_NAME);
        if(val != null)
        {
            sql += String.valueOf(val).toUpperCase() + ".";
        }

        val = mapRule.get(ColumnConstants.SCENE_FIELD_NAME);
        if(val != null)
        {
            sql += val;
        }

        op = mapRule.get(ColumnConstants.SCENE_FIELD_OP);
        if(op != null)
        {
            /*
             > 大于 1>2 False          more
            < 小于 2<1 False          less
            <=小于等于 2<=2 True       less equal
            >= 大于等于 3>=2 True      more equal
            IN 
            NOT IN
            =                          equal
            <>, !=                      not equal
            LIKE
            IS NULL
            IS NOT NULL
             */
            if(ColumnConstants.OP_LESS.compareTo(String.valueOf(op)) == 0)
            {
                 sql += String.format(" %s ", ColumnConstants.OP_LESS_SIGN);
             }
            else if(ColumnConstants.OP_LESS_EQUAL.compareTo(String.valueOf(op)) == 0)
            {
                sql += String.format(" %s ", ColumnConstants.OP_LESS_EQUAL_SIGN);
            }
            else if(ColumnConstants.OP_MORE.compareTo(String.valueOf(op)) == 0)
            {
                sql += String.format(" %s ", ColumnConstants.OP_MORE_SIGN);
            }
            else if(ColumnConstants.OP_MORE_EQUAL.compareTo(String.valueOf(op)) == 0)
            {
                sql += String.format(" %s ", ColumnConstants.OP_MORE_EQUAL_SIGN);
            }
            else if(ColumnConstants.OP_NOT_EQUAL.compareTo(String.valueOf(op)) == 0)
            {
                sql += String.format(" %s ", ColumnConstants.OP_NOT_EQUAL_SIGN);
            }
            else if(ColumnConstants.OP_EQUAL.compareTo(String.valueOf(op)) == 0)
            {
                sql += String.format(" %s ", ColumnConstants.OP_EQUAL_SIGN);
            }
            else if(ColumnConstants.OP_LIKE.compareTo(String.valueOf(op)) == 0
                     || ColumnConstants.OP_IN.compareTo(String.valueOf(op)) == 0
                     || ColumnConstants.OP_NOT_IN.compareTo(String.valueOf(op)) == 0
                     || ColumnConstants.OP_NULL.compareTo(String.valueOf(op)) == 0
                     || ColumnConstants.OP_NOT_NULL.compareTo(String.valueOf(op)) == 0)
             {
                 sql +=  String.format(" %s ", op);
             }
            //sql += op;
        }

        val = mapRule.get(ColumnConstants.SCENE_FIELD_VAL);
        if(val != null)
        {
            type = mapRule.get(ColumnConstants.SCENE_FIELD_TYPE);
            if(type != null)
            {
                if(ColumnConstants.OP_IN.compareTo(String.valueOf(op)) == 0
                        || ColumnConstants.OP_NOT_IN.compareTo(String.valueOf(op)) == 0)
                {
                    sql += String.format("(%s)", val);
                }
                else  if(ColumnConstants.OP_LIKE.compareTo(String.valueOf(op)) == 0)
                {
                    sql += String.format("'%%s%'", val);
                }
                else {
                    if (ColumnConstants.DATA_TYPE_DATE.compareTo(String.valueOf(type)) == 0
                            || ColumnConstants.DATA_TYPE_STRING.compareTo(String.valueOf(type)) == 0) {
                        sql += String.format("'%s'", val);
                    }
                    else
                    {
                        sql += val;
                    }
                }
            }
        }

        logger.info("Function　getSQLBySceneRule　return[" + sql + "]");

        return sql;
    }
    /**
     * 获取SQL语句根据场景规则分组
     * @param listGroup 场景编号
     * @return String 数据库语句
     */
    public String getSQLBySceneRuleGroup(List<Map<String, Object>> listGroup, String groupOp)
    {
        String sql = "";
        String sceneSQL = "";
        String sceneRuleOp = null;
        Map<String, Object> entity = null;

        if(listGroup != null && listGroup.size() > 0) {
            for (int index = 0; index < listGroup.size(); index++) {
                entity = listGroup.get(index);
                if(entity != null) {
                    //sceneRuleOp = (String)entity.get(ColumnConstants.SCENE_RULE_OP);
                    if(groupOp != null) {
                        if(index > 0)
                        {
                            sceneSQL += String.format(" %s ", groupOp);
                        }

                        sceneSQL += getSQLBySceneRule(entity);
                    }
                }
            }

            sql += String.format("%s", sceneSQL);
        }

        logger.info("Function　getSQLBySceneRuleGroup　return[" + sql + "]");

        return sql;
    }

    public Map<String, Object> getHashMapTreeByHash(Map<String, Object> mapRootGroup)
    {
        String groupId = null;
        String parentId = null;
        Map<String, Object> groupEntity = null;

        Map<String, List<Map<String, Object>>>  hashRoot = new HashMap<String, List<Map<String, Object>>>();

        Map<String, Object>  hashTree = new HashMap<String, Object>();
        Map<String, Object>  hashTreeIdx = new HashMap<String, Object>();
        Map<String, Object>  hashTmp = null;
        Map<String, Object>  hashCur = null;

        for (Map.Entry<String, Object> entry : mapRootGroup.entrySet()) {
            //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            groupEntity = (Map<String, Object>)entry.getValue();

            // 获取组的树字串
            if(groupEntity.get(ColumnConstants.ID_TREE) != null) {
                parentId = (String) groupEntity.get(ColumnConstants.PARENT_ID);
                groupId = (String) groupEntity.get(ColumnConstants.SCENE_RULE_GROUP_ID);

                if(parentId != null) {
                    // 判断父节点是否存在
                    if (hashTreeIdx.containsKey(parentId)) {
                        hashTmp = (Map<String, Object>) hashTreeIdx.get(parentId);
                    } else {
                        hashTmp = new HashMap<String, Object>();
                        hashTree.put(parentId, hashTmp);
                        hashTreeIdx.put(parentId, hashTmp);
                    }

                    if (hashTmp != null) {
                        if(!hashTmp.containsKey(groupId))
                        {
                            hashCur = new HashMap<String, Object>();
                            hashTmp.put(groupId, hashCur);

                            hashTreeIdx.put(groupId, hashCur);
                        }
                    }

                    if(hashTree.containsKey(groupId))
                    {
                        hashTree.remove(groupId);
                    }
                }
                else
                {
                    if(!hashTreeIdx.containsKey(groupId)) {
                        hashTmp = new HashMap<String, Object>();
                        hashTree.put(groupId, hashTmp);
                        hashTreeIdx.put(groupId, hashTmp);
                    }
                }
            }
        }

        return hashTree;
    }

    /**
     * 查询操作条件根据场景
     * @param mapScenes 场景基本分组映射
     * @param mapGroups 基本分组哈希集合
     * @param hashTree 基本根分组
     * @param hasRules 父节点下是否有规则
     * @return String 数据库语句
     */
    public String getSQLBySceneRuleGroupTree(Map<String, Object> mapScenes, Map<String, Object> mapGroups, Map<String, Object> hashTree, String sceneGroupOp, boolean hasRules)
    {
        String sql = "";
        String ruleSQL = "";
        String groupSQL = "";
        String groupId = null;
        String parentId = null;
        String ruleOp = "";
        String groupOp = "";
        boolean bRelate = false;
        Map<String, Object> groupTree = null;
        Map<String, Object> groupEntity = null;
        int count = 0;

        List<Map<String, Object>> listGroup = null;
        List<Map<String, Object>> listGroupRules = null;

        if(hashTree != null && hashTree.size() > 0) {
            //sql += " (1=1) ";
            for (Map.Entry<String, Object> entryRoot : hashTree.entrySet()) {
                groupTree = (Map<String, Object>)entryRoot.getValue();

                groupId = entryRoot.getKey();

                if(groupId == null)
                {
                    continue;
                }
                // 如果组ID存在
                groupEntity = (Map<String, Object>)mapGroups.get(groupId);
                if(groupEntity == null)
                {
                    continue;
                }

                groupOp = (String) groupEntity.get(ColumnConstants.SCENE_GROUP_OP);
                if(groupOp == null)
                {
                    groupOp = ColumnConstants.OP_AND;
                }

                listGroupRules = (List<Map<String, Object>>) mapScenes.get(groupId);

                if (listGroupRules != null && listGroupRules.size() > 0) {
                    ruleSQL = getSQLBySceneRuleGroup(listGroupRules, groupOp);
                    bRelate = true;
                }

                if(!groupTree.isEmpty()) {
                    groupSQL = getSQLBySceneRuleGroupTree(mapScenes, mapGroups, groupTree, groupOp, bRelate);
                }

                if(hasRules || count > 0)
                {
                    sql += String.format(" %s ", sceneGroupOp);
                }

                if(hasRules || (!hasRules && hashTree.size() > 1)) {
                    sql += String.format("(%s%s)", ruleSQL, groupSQL);
                }
                else
                {
                    sql += String.format("%s%s", ruleSQL, groupSQL);
                }

                count++;
            }
        }

        logger.info("Function　getSQLBySceneRuleGroupTree　return[" + sql + "]");

        return sql;
    }

    /**
     * 查询操作条件根据场景
     * @param mapScenes 场景基本分组映射
     * @param mapRootGroup 基本根分组
     * @return String 数据库语句
     */
    /*public String getSQLBySceneRuleGroupTree(Map<String, Object> mapScenes,Map<String, Map<String, Object>> mapRootGroup)
    {
        String sql = "";
        String idTree = "";
        String idTempTree = "";
        String idOldTree = "";
        String groupId = null;
        String sceneGroupOp = "";
        String ruleOp = "";
        boolean bRelate = false;
        Map<String, Object> groupEntity = null;

        List<Map<String, Object>> listGroup = null;
        List<Map<String,Object>> listGroupRules = null;

        Map<String, List<Map<String, Object>>>  hashRoot = new HashMap<String, List<Map<String, Object>>>();

        for (Map.Entry<String, Map<String, Object>> entry : mapRootGroup.entrySet()) {
            //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            groupEntity = entry.getValue();
            // 获取组的树字串
            if(groupEntity.get(ColumnConstants.ID_TREE) != null) {
                idTree = (String) groupEntity.get(ColumnConstants.ID_TREE);

                bRelate = false;

                idOldTree = idTree;

                // 判断组是否存在
                for (Map.Entry<String, List<Map<String, Object>>> entryRoot : hashRoot.entrySet())
                {
                    idTempTree = entryRoot.getKey();

                    // 根据长度进行判断谁包含谁
                    if(idTree.length() > idTempTree.length())
                    {
                        // 对象树包含遍历内容节点树
                        if(idTree.contains(idTempTree))
                        {
                            entryRoot.getValue().add(groupEntity);

                            bRelate = true;

                            idOldTree = idTempTree;
                        }
                    }
                    else
                    {
                        // 遍历的内容是否包含对象树
                        if(idTempTree.contains(idTree))
                        {
                            entryRoot.getValue().add(groupEntity);
                            if(idOldTree != null) {
                                if(idOldTree.contains(idTempTree))
                                {
                                    idOldTree = idTempTree;
                                }
                            }
                            else
                            {
                                idOldTree = idTree;
                            }

                            bRelate = true;
                        }
                    }
                }
                // 当前树节点没有在哈希hashRoot中存在
                if(!bRelate)
                {
                    listGroup = new ArrayList<Map<String, Object>>();

                    listGroup.add(groupEntity);

                    hashRoot.put(idOldTree, listGroup);
                }
            }
        }

        if(hashRoot != null && hashRoot.size() > 0) {
            //sql += " (1=1) ";
            for (Map.Entry<String, List<Map<String, Object>>> entryRoot : hashRoot.entrySet()) {
                listGroup = entryRoot.getValue();

                Collections.sort(listGroup, new Comparator() {
                    public int compare(Object a, Object b) {
                        String one = (String) ((Map<String, Object>) a).get(ColumnConstants.ID_TREE);
                        String two = (String) ((Map<String, Object>) b).get(ColumnConstants.ID_TREE);
                        return one.length() - two.length();
                    }
                });

                for (int index = 0; index < listGroup.size(); index++) {
                    groupEntity = listGroup.get(index);

                    if (groupEntity != null && groupEntity.containsKey(ColumnConstants.SCENE_RULE_GROUP_ID)) {

                        ruleOp = (String) groupEntity.get(ColumnConstants.SCENE_GROUP_OP);

                        groupId = (String) groupEntity.get(ColumnConstants.SCENE_RULE_GROUP_ID);

                        if (groupId != null && mapScenes.containsKey(groupId)) {
                            listGroupRules = (List<Map<String, Object>>) mapScenes.get(groupId);

                            if (listGroupRules != null) {
                                if(index > 0) {
                                    sql += " " + sceneGroupOp;
                                }
                                sql += getSQLBySceneRuleGroup(listGroupRules);
                            }
                            // 操作符待增加
                        }
                    }
                }
            }
        }

        logger.info("Function　getSQLBySceneRuleGroupTree　return[" + sql + "]");

        return sql;
    }*/

    /**
     * 查询操作条件根据场景
     * @param sceneId 场景编号
     * @return String 数据库语句
     */
    public String getSQLByScene(String sceneId)
    {
        String sql = "";
        String groupId = null;

        Map<String, Object> entity = null;

        List<Map<String, Object>> listGroup = null;

        List<Map<String, Object>> listRootRules = null;

        Map<String, Object> mapRootGroups = null;

        List<Map<String, Object>> listGroupInfo = null;

        Map<String, Object> mapScenes = new HashMap<String, Object>();

        List<Map<String, Object>> listMap = authDao.queryDataSceneRuleGroupDetailByScene(TableConstants.DATA_SCENE_RULE, TableConstants.DATA_SCENE_RULE_GROUP, sceneId);

        for(int index = 0; index < listMap.size(); index ++)
        {
            entity = listMap.get(index);

            if(entity == null)
            {
                continue;
            }

            if(entity.get(ColumnConstants.SCENE_RULE_GROUP_ID) != null)
            {
                groupId = (String)entity.get(ColumnConstants.SCENE_RULE_GROUP_ID);
            }
            else
            {
                groupId = GROUP_KEY;
            }

            if(mapScenes.containsKey(groupId))
            {
                listGroup = (List<Map<String,Object>>) mapScenes.get(groupId);
            }
            else
            {
                listGroup = new ArrayList<Map<String,Object>>();
            }

/*            if(GROUP_KEY.compareTo(groupId) == 0) {
                if (listRootRules == null) {
                    listRootRules = new ArrayList<Map<String, Object>>();
                }
                listRootRules.add(entity);
            }*/

            if(mapRootGroups == null)
            {
                mapRootGroups = new  HashMap<String, Object>();
            }
            if(!mapRootGroups.containsKey(groupId))
            {
                mapRootGroups.put(groupId, entity);
            }

            listGroup.add(entity);

            mapScenes.put(groupId, listGroup);
        }

        if(mapRootGroups != null && mapRootGroups.size() > 0) {
            Map<String, Object> hashTree = getHashMapTreeByHash(mapRootGroups);

            sql += getSQLBySceneRuleGroupTree(mapScenes, mapRootGroups, hashTree, ColumnConstants.OP_AND, false);
        }
        
        logger.info("Function　getSQLByScene　param[ sceneId---" + sceneId + "] return[" + sql + "]");

        return sql;
    }

    public String getUserSQLByPrivilegeDateType(List<Map<String, Object>> listUserPrivileges)
    {
        String sql = "";
        String sceneSQL = "";
        String privilegeId = null;
        String sceneId = null;
        String dataDefinition = null;
        String dataTable = null;
        Map<String, Object> mapUserPrivilege = null;

        for(int index = 0; index < listUserPrivileges.size();index ++)
        {
            mapUserPrivilege = listUserPrivileges.get(index);

            if(mapUserPrivilege != null)
            {
                privilegeId = (String)mapUserPrivilege.get(ColumnConstants.DATA_PRIVILEGE_ID);

                dataDefinition = (String)mapUserPrivilege.get(ColumnConstants.DATA_DEFINITION);

                dataTable = (String)mapUserPrivilege.get(ColumnConstants.TABLE_NAME);

                if(dataDefinition != null)
                {
                    if(sql != null && sql.length() > 0)
                    {
                        sql += " UNION DISTINCT ";
                    }

                    sql += String.format("SELECT * FROM (%s) %s", dataDefinition, dataTable.toUpperCase());

                    sceneId = (String)mapUserPrivilege.get(ColumnConstants.SCENE_ID);

                    if(sceneId != null) {
                        sceneSQL = getSQLByScene(sceneId);

                        if (sceneSQL != null && sceneSQL.length() > 0) {
                            sql += String.format(" WHERE %s", sceneSQL);
                        }
                    }
                }
            }
        }

        return sql;
    }

    /**
     * 查询权限操作语句根据用户权限
     * @param userId 权限编号
     * @param dataTypeSignID 数据类型ID
     * @return String 数据库语句
     */
    public String getUserPrivilegesSQLByUP(String dataTypeSignID, String userId)
    {
        String sql = "";
        String sceneSQL = "";
        String privilegeId = null;
        String sceneId = null;
        String dataDefinition = null;
        String dataTable = null;
        Map<String, Object> mapUserPrivilege = null;

        List<Map<String, Object>> listUserPrivileges = authDao.queryDataPrivilegesDetailByDataType(userId,
                TableConstants.DATA_PRIVILEGES, TableConstants.USER_DATA_PRIVILEGES_RELATE, TableConstants.DATA_TYPE,dataTypeSignID);

        /*for(int index = 0; index < listUserPrivileges.size();index ++)
        {
            mapUserPrivilege = listUserPrivileges.get(index);

            if(mapUserPrivilege != null)
            {
                privilegeId = (String)mapUserPrivilege.get("DATA_PRIVILEGE_ID");

                dataDefinition = (String)mapUserPrivilege.get(ColumnConstants.DATA_DEFINITION);

                dataTable = (String)mapUserPrivilege.get(ColumnConstants.TABLE_NAME);

                if(dataDefinition != null)
                {
                    sql += String.format("SELECT * FROM (%s) %s", dataDefinition, dataTable.toUpperCase());

                    sceneId = (String)mapUserPrivilege.get(ColumnConstants.SCENE_ID);

                    if(sceneId != null) {
                        sceneSQL = getSQLByScene(sceneId);

                        if (sceneSQL != null && sceneSQL.length() > 0) {
                            sql += String.format(" WHERE %s", sceneSQL);
                        }
                    }
                }
            }
        }*/

        sql = getUserSQLByPrivilegeDateType(listUserPrivileges);

        logger.info("Function　getUserPrivilegesSQLByUP　param[ user---" + userId + ", dataTypeSignID--- " + dataTypeSignID + "] return[" + sql + "]");

        return sql;
    }

    /**
     * 查询权限操作语句根据用户权限
     * @param userId 权限编号
     * @param dataTypeSignID 数据类型ID
     * @return String 数据库语句
     */
    public List<Map<String,Object>> getUserPrivilegesByUP(String dataTypeSignID, String userId)
    {
        String sql = "";

        sql = getUserPrivilegesSQLByUP(dataTypeSignID,userId);

        List<Map<String,Object>>  list = baseDao.queryBySql(sql);

        return list;
    }

    /**
     * 查询权限操作语句根据用户角色权限
     * @param userId 权限编号
     * @param dataTypeSignID 数据类型ID
     * @return String 数据库语句
     */
    public String getUserPrivilegesSQLByURP(String dataTypeSignID, String userId)
    {
        String sql = "";
        String sceneSQL = "";
        String privilegeId = null;
        String sceneId = null;
        String dataDefinition = null;
        String dataTable = null;
        Map<String, Object> mapUserPrivilege = null;

        List<Map<String, Object>> listUserPrivileges = authDao.queryDataPrivilegesDetailByDataTypeRole(userId,
                TableConstants.DATA_ROLE_PRIVILEGES_RELATE, TableConstants.USER_DATA_ROLE_RELATE, TableConstants.DATA_PRIVILEGES, TableConstants.DATA_TYPE, dataTypeSignID);

        sql = getUserSQLByPrivilegeDateType(listUserPrivileges);

        logger.info("Function　getUserPrivilegesSQLByUP　param[ user---" + userId + ", dataTypeSignID--- " + dataTypeSignID + "] return[" + sql + "]");

        return sql;
    }

    /**
     * 查询权限操作语句根据用户权限
     * @param userId 权限编号
     * @param dataTypeSignID 数据类型ID
     * @return String 数据库语句
     */
    public List<Map<String,Object>> getUserPrivilegesByURP(String dataTypeSignID, String userId)
    {
        String sql = "";

        sql = getUserPrivilegesSQLByURP(dataTypeSignID, userId);

        List<Map<String,Object>>  list = baseDao.queryBySql(sql);

        return list;
    }

    /**
     * 查询权限操作语句根据用户角色权限
     * @param userId 权限编号
     * @param dataTypeSignID 数据类型ID
     * @return String 数据库语句
     */
    public String getUserPrivilegesSQL(String dataTypeSignID, String userId)
    {
        String sql = "";
        String RPSQL = "";
        String privilegeId = null;
        String sceneId = null;
        String dataDefinition = null;
        String dataTable = null;
        Map<String, Object> mapUserPrivilege = null;
        String URPSQL = "";

        RPSQL = getUserPrivilegesSQLByUP(dataTypeSignID,userId);

        URPSQL = getUserPrivilegesSQLByURP(dataTypeSignID, userId);

        if(RPSQL != null && RPSQL.trim().length() > 0)
        {
            sql += String.format("%s", RPSQL);
        }

        if(URPSQL != null && URPSQL.trim().length() > 0)
        {
            sql += String.format(" %s %s ", "UNION DISTINCT", URPSQL);
        }

        logger.info("Function　getUserPrivilegesSQLByUP　param[ user---" + userId + ", dataTypeSignID--- " + dataTypeSignID + "] return[" + sql + "]");

        return sql;
    }

    /**
     * 查询权限操作语句根据用户权限
     * @param userId 权限编号
     * @param dataTypeSignID 数据类型ID
     * @return String 数据库语句
     */
    public List<Map<String,Object>> getUserPrivileges(String dataTypeSignID, String userId)
    {
        String sql = "";
        List<Map<String, Object>> list = null;

        sql = getUserPrivilegesSQL(dataTypeSignID, userId);

        if(sql != null && sql.trim().length() > 0) {
            list = baseDao.queryBySql(sql);
        }

        return list;
    }

    /**
     * 查询权限操作语句根据用户角色权限
     * @param userId 权限编号
     * @param dataTypeSignID 数据类型ID
     * @param dataPrivilegeID 权限编号ID
     * @param dataRoleID 数据角色ID
     * @return String 数据库语句
     */
   /* public String getUserPrivilegesSQLByURP(String dataTypeSignID, String dataPrivilegeID, String dataRoleID, String userId)
    {
        String sql = "";
        String sceneSQL = "";
        String privilegeId = null;
        String sceneId = null;
        String dataDefinition = null;
        String dataTable = null;
        Map<String, Object> mapUserPrivilege = null;

        List<Map<String, Object>> listUserPrivileges = authDao.queryDataPrivilegesDetailByDataTypeRole(userId,
               TableConstants.DATA_ROLE_PRIVILEGES_RELATE, TableConstants.USER_DATA_ROLE_RELATE, TableConstants.DATA_PRIVILEGES, TableConstants.DATA_TYPE, dataTypeSignID);

        sql = getUserSQLByPrivilegeDateType(listUserPrivileges);

        logger.info("Function　getUserPrivilegesSQLByUP　param[ user---" + userId + ", dataTypeSignID--- " + dataTypeSignID + "] return[" + sql + "]");

        return sql;
    }*/

    /*public List<String> getDataPrivilegesSQLList(String dataTypeSignID) {
        String sql = "";

        List<String> listSQL = new ArrayList<String>();

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("", dataTypeSignID);

        List<Map<String, Object>> listPrivileges = authDao.(map);

        for(int index = 0; index < listPrivileges.size(); index ++)
        {
            //TODO
        }

        return listSQL;
    }*/

}
