package com.pc.base;

import java.io.Serializable;

/**
 * @Description: ${Description}
 * @Author: 潘锐 (2015-09-27 14:21)
 * @version: \$Rev: 2656 $
 * @UpdateAuthor: \$Author: zhangj $
 * @UpdateDateTime: \$Date: 2017-05-31 09:00:46 +0800 (周三, 31 5月 2017) $
 */
public enum ReturnCode implements Serializable {
    OK(0, "操作成功"),
    FAIL(1, "操作失败"),
    TOKEN_VERIFY_ERROR(2, "token无效"),
    REQUEST_PARAMS_VERIFY_ERROR(3, "请求参数错误"),
    HEADER_PARAMS_VERIFY_ERROR(4, "请求头参数错误"),
    SYSTEM_ERROR(5, "服务器错误"),
    REPEAT_SUBMIT_ERROR(6,"重复提交"),
    REQUEST_PARAMS_MISSING_ERROR(7, "请求参数缺失错误"),
    
    LOGIN_PHONE_ERROR(1001, "账号不存在"),
    LOGIN_PWD_ERROR(1002, "密码错误"),
    USER_PHONE_EXIST(1003, "用户号码已存在"),
    USER_NOT_EXISTS(1004, "用户不存在"),
    NO_AUTH(1016, "无此操作权限"),
    OLD_PWD_ERROR(1005, "原密码错误"),

    NO_DATA(2001, "没有相关数据"),

    // 栋添加
    BA_BUILDING_NAME_EXIST_ERROR(3001, "请求栋名称已存在"),
    BA_BUILDING_NAME_MISSING_ERROR(3002, "请求栋名参数缺失"),
    BA_PERIOD_ID_NOT_EXIST_ERROR(3003, "请求期ID不存在"),
    BA_PERIOD_ID_MISSING_ERROR(3004, "请求期ID参数缺失"),

    // 层户添加
    HA_ROOM_NAME_EXIST_ERROR(4001, "请求名称已存在"),
    HA_ROOM_NAME_MISSING_ERROR(4002, "请求栋名参数缺失"),
    HA_FLOOR_MISSING_ERROR(4003, "请求层参数缺失"),
    HA_REGION_MISSING_ERROR(4004, "请求部位参数缺失"),
    HA_ROOM_MISSING_ERROR(4005, "请求部位参数缺失"),
    BA_BUILDING_ID_MISSING_ERROR(4006, "请求栋ID参数缺失"),
    BA_FLOOR_ID_MISSING_ERROR(4007, "请求层ID参数缺失"),

	//其他错误
    ACCEPTANCE_EXIST_ERROR(5005, "验收已存在"),
    ACCEPTANCE_IS_SUCCEED(5004, "验收已合格"),
    ACCEPTANCE_EXIST(5002, "请勿重复报验"),
    CANNOT_SEND_WITH_CHECK_FAIL(5003, "验收失败不能通知监理"),
    
    NO_POST_AUTH(5000, "无此岗位权限"),
	NO_HOUSEHOLD_CHART(5001, "没有户型图");
    
    
    private String msg;
    private int code;

    ReturnCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "{\"code\":" + this.code + ",\"msg\":\"" + getMsg() + "\"}";
    }

}