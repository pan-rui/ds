package com.pc.base;

public final class Constants {

    public static final String USER_TOKEN = "application1";
    public static final String APP_VERSION = "application3";
    public static final String TENANT_ID = "application2";
    public static final String APPLICATION_JSON = "application/json;charset=utf-8";
    public static final String CURRENT_USER = "user";
    //redis使用的key
    public static final String REDIS_TOKEN_KEY = "'quality-token-'+#tenantId+#phone";
    public static final String REDIS_TOKEN_USER_KEY = "'quality-user-'+#token";
    public static final String USER_ID = "USER_ID";
    public static final String USER_PHONE = "USER_PHONE";
    public static final String TENANTID = "TENANT_ID";
    public static final String DDBB = "ddBB";
    //缓存Key常量
    public static final String CACHE_AUTHENTICATION_PREFIX = "AUTHENTICATION_";
    public static final String CACHE_USER_ROLES_PREFIX = "USER_ROLES_";
    public static final String CACHE_PERMISSIONS_PREFIX = "PERMISSIONS_";
    public static final String CACHE_USER_PERMISSIONS_PREFIX = "USER_PERMISSIONS_";

    public enum AppType {
        IOS, ANDROID, PC
    }
}
