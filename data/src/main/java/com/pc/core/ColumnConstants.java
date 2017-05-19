package com.pc.core;

/**
 * @Description: 字段常量
 * @Author: wady (2017-03-29 17:41)
 * @version: 1.0
 * @UpdateAuthor: wady
 * @UpdateDateTime: 2017-03-29
 */
public class ColumnConstants {
    // 等于
    public static final String OP_EQUAL =  "EQUAL";
    // 大于等于
    public static final String OP_MORE_EQUAL =  "MORE EQUAL";
    // 小于等于
    public static final String OP_LESS_EQUAL =  "LESS EQUAL";
    // 大于
    public static final String OP_MORE =  "MORE";
    // 小于
    public static final String OP_LESS =  "LESS";
    // 不等于
    public static final String OP_NOT_EQUAL =  "NOT EQUAL";
    // 包含
    public static final String OP_IN =  "IN";
    // 不包含
    public static final String OP_NOT_IN =  "NOT IN";
    // 相似
    public static final String OP_LIKE =  "LIKE";

    // 大于等于符号
    public static final String OP_EQUAL_SIGN =  "=";
    // 大于等于符号
    public static final String OP_MORE_EQUAL_SIGN =  ">=";
    // 小于等于符号
    public static final String OP_LESS_EQUAL_SIGN =  "<=";
    // 大于符号
    public static final String OP_MORE_SIGN  =  ">";
    // 小于符号
    public static final String OP_LESS_SIGN  =  "<";
    // 不等于符号
    public static final String OP_NOT_EQUAL_SIGN  =  "<>";
    // NULL
    public static final String OP_NULL =  "IS NULL";
    // NOT NULL
    public static final String OP_NOT_NULL =  "IS NOT NULL";
    // AND
    public static final String OP_AND =  "AND";
    // OR
    public static final String OP_OR =  "OR";
    //日期和时间数据类型
    public static final String DATA_TYPE_DATE =  "DATE";
    //整型
    public static final String DATA_TYPE_INT =  "INT";
    //浮点型
    public static final String DATA_TYPE_FLOAT =  "FLOAT";
    //字符串数据类型
    public static final String DATA_TYPE_STRING =  "STRING";

    /*************************************  Table DATA_SCENE_RULE Begin  *********************************************/
    // 场景编号ID
    public static final String SCENE_ID = "SCENE_ID";
    //场景规则名
    public static final String SCENE_RULE_NAME="SCENE_RULE_NAME";
    //场景规则对应表名
    public static final String SCENE_TABLE_NAME="SCENE_TABLE_NAME";
    //场景规则对应字段名
    public static final String SCENE_FIELD_NAME="SCENE_FIELD_NAME";
    //场景规则对应操作符
    public static final String SCENE_FIELD_OP="SCENE_FIELD_OP";
    //场景规则字段类型
    public static final String SCENE_FIELD_TYPE="SCENE_FIELD_TYPE";
    //场景规则字段值
    public static final String SCENE_FIELD_VAL="SCENE_FIELD_VAL";
    //场景规则组编号
    public static final String SCENE_RULE_GROUP_ID="SCENE_RULE_GROUP_ID";
    //场景规则组内操作符
    public static final String SCENE_RULE_OP="SCENE_RULE_OP";

    /*************************************  Table DATA_SCENE_RULE End  *********************************************/
    /*************************************  Table DATA_SCENE_RULE_GROUP Begin  *********************************************/
    //场景规则组编号
    public static final String ID="ID";
    //场景规则组名称
    public static final String NAME="NAME";
    //场景规则组名称
    public static final String SCENE_GROUP_OP="SCENE_GROUP_OP";
    //场景规则组父编号
    public static final String PARENT_ID="PARENT_ID";
    //场景规则组ID树
    public static final String ID_TREE="ID_TREE";
    //场景规则组NAME树
    public static final String NAME_TREE="NAME_TREE";

    /*************************************  Table DATA_SCENE_RULE_GROUP End  *********************************************/

    /*************************************  Table DATA_PRIVILEGES Begin  *********************************************/
    //数据定义
    public static final String DATA_DEFINITION="DATA_DEFINITION";
    //数据权限名称
    public static final String DATA_PRIVILEGES_NAME="DATA_PRIVILEGES_NAME";
    //数据类型编号ID
    public static final String DATA_TYPE_ID="DATA_TYPE_ID";

    /*************************************  Table USER_DATA_PRIVILEGES End  *********************************************/

    /*************************************  Table DATA_PRIVILEGES_RELATE Begin  *********************************************/
    //数据定义
    public static final String DATA_PRIVILEGE_ID="DATA_PRIVILEGE_ID";

    /*************************************  Table DATA_PRIVILEGES_RELATE End  *********************************************/

    /*************************************  Table DATA_TYPE Begin  *********************************************/
    //数据定义
    public static final String TABLE_NAME="TABLE_NAME";

    //数据定义
    public static final String DATA_TYPE_NAME="DATA_TYPE_NAME";

    /*************************************  Table DATA_TYPE End  *********************************************/

}
