package com.pc.service.user;

import com.pc.base.Constants;
import com.pc.core.ParamsMap;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class TokenService {

	/**
	 * 获取token，先读缓存不存在就生成新的
	 * 
	 * @param phone
	 * @param tenantId
	 * @return
	 */
	@Cacheable(value = "token", key = Constants.REDIS_TOKEN_KEY, cacheManager = "cacheManager")
	public String getToken(String phone, String tenantId) {
		String token = UUID.randomUUID().toString().replace("-", "");
		return token;
	}

	/**
	 * 获取token没有返回null
	 * 
	 * @param phone
	 * @param tenantId
	 * @return
	 */
	@Cacheable(value = "token", key = Constants.REDIS_TOKEN_KEY, cacheManager = "cacheManager", unless = "true")
	public String getTokenToCheck(String phone, String tenantId) {
		return null;
	}

	/**
	 * 清除token
	 * 
	 * @param phone
	 * @param tenantId
	 * @return
	 */
	@CacheEvict(value = "token", key = Constants.REDIS_TOKEN_KEY, cacheManager = "cacheManager")
	public String delToken(String phone, String tenantId) {
		return null;
	}

	@Cacheable(value = "token", key = Constants.REDIS_TOKEN_USER_KEY, cacheManager = "cacheManager")






    public Map<String, String> cacheUserByToken(String token, String userId, String phone, String tenantId, String ddBB) {
        return ParamsMap.newMap(Constants.USER_ID, userId).addParams(Constants.USER_PHONE, phone).addParams(Constants.TENANTID, tenantId).addParams(Constants.DDBB, ddBB);
    }

	@Cacheable(value = "token", key = Constants.REDIS_TOKEN_USER_KEY, cacheManager = "cacheManager", unless = "true")
	public Map<String, String> getUser(String token) {
		return null;
	}

	@CacheEvict(value = "token", key = Constants.REDIS_TOKEN_USER_KEY, cacheManager = "cacheManager")
	public String delUserByToken(String token) {
		return null;
	}

	@CacheEvict(value = {"qCache", "system", "tmp", "auth", "columns"}, allEntries = true, cacheManager = "cacheManager")
	public void clearAllCache() {
	}

	@CacheEvict(value ="offline",allEntries = true,cacheManager = "cacheManager")
	public void clearOfflineCache() {
		System.out.println("清除Offline缓存....");
	}
	
}
