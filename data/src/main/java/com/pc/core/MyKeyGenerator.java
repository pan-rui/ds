package com.pc.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Description: ${Description}
 * @Create: 潘锐 (2016-11-28 16:50)
 * @version: \$Rev: 1158 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-04-18 15:53:47 +0800 (周二, 18 4月 2017) $
 */
@Component
public class MyKeyGenerator implements KeyGenerator {
    @Autowired
    public RedisCacheManager cacheManager;
    @Autowired
    public JedisPool jedisPool;
    @Override
    public Object generate(Object o, Method method, Object... objects) {
        if(objects[1] instanceof Map && method.getName().endsWith("InTab")){
            Map<String, Object> dataMap = (Map<String, Object>) objects[1];
//            Collection values=dataMap.values();
//            for (Object obj: dataMap.values())
            if(dataMap.size()>0)
                return objects[0] + "~" + dataMap.values().toArray()[dataMap.size()-1];
        }
        return null;
    }

    @PostConstruct
    public void setProperties() {
        Constants.cacheManager=cacheManager;
        Constants.jedisPool=jedisPool;
    }

/*    @Override
    public void afterPropertiesSet() throws Exception {
        Constants.cacheManager=cacheManager;
        Constants.jedisPool=jedisPool;
    }*/
}
