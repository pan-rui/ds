package com.pc.base;

import com.pc.core.ColumnProcess;
import com.pc.core.Constants;
import com.pc.core.TableConstants;
import com.pc.dao.BaseDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.interceptor.CacheOperation;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @Description: ${Description}
 * @Author: 潘锐 (2014-12-27 14:21)
 * @version: \$Rev: 2895 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-06-07 17:42:18 +0800 (周三, 07 6月 2017) $
 */
@Component
public class BaseImpl implements IBase, ApplicationContextAware, InitializingBean, CacheResolver {
    public static final Map<String, List<String>> initDataMap = new HashMap<>();
    @Autowired
    public JdkSerializationRedisSerializer jdkRedisSerializer;
    @Autowired
    public BaseDao baseDao;
    //    protected javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    protected javax.validation.Validator validator = Validation.byProvider(HibernateValidator.class).configure().buildValidatorFactory().getValidator();
    //    public SpelExpressionParser expressionParser = new SpelExpressionParser();
    protected ApplicationContext applicationContext;
    @Autowired
    protected RedisCacheManager cacheManager;
    @Autowired
    protected RedisCacheManager cacheManagerSlave;
    @Autowired
    protected JedisPool jedisPool;
    private Logger logger = LogManager.getLogger(BaseImpl.class);
    @Value("#{initData['systemData']}")
    private String systemData;
    @Value("#{initData['frontData']}")
    private String frontData;
    @Value("#{initData['backstageData']}")
    private String backstageData;

    public BaseImpl() {
//        super(sqlSessionFactory);
//        Class clazz = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()
/*        Type type = getClass().getGenericSuperclass();
        Class clazz = null;
        if (type instanceof ParameterizedType) {
            clazz = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            className = clazz.getSimpleName();
        } else {
            String namee = ((Class) type).getSimpleName();
            className = namee.substring(0, namee.indexOf("Service")==-1?namee.length():namee.indexOf("Service"));
        }*/
//           System.out.println("***********\t"+className);
    }

    public <E> BaseResult validAndReturn(E t) {
        Set<ConstraintViolation<E>> errs = validator.validate(t);
        if (errs.size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (ConstraintViolation<E> cons : errs)
                sb.append(cons.getPropertyPath() + ":" + cons.getInvalidValue() + "===>" + cons.getMessage() + "\r\n");
            return new BaseResult(1, sb.toString());
        } else return new BaseResult(ReturnCode.OK);
    }

    public Jedis getJedis() {
        return this.jedisPool.getResource();
    }

/*    @Override
    public void afterPropertiesSet() throws Exception {
        Constants.cacheManager = this.cacheManager;
        Constants.jedisPool = this.jedisPool;
        Constants.applicationContext = this.applicationContext;
        Constants.publicKey = Constants.getSystemStringValue("PUBLIC_KEY");
//        if(jdkRedisSerializer==null) jdkRedisSerializer = new JdkSerializationRedisSerializer();
        //TODO:将系统数据初始化到缓存
        //t_system_paramet
        //t_system_front
        initSystemData();
    }*/

    public Object getCacheOfValue(String cacheName, Object key) {
        return cacheManager.getCache(cacheName).get(key).get();
    }

    public <T> T getCacheOfValue(String cacheName, Object key, Class<T> clazz) {
        return cacheManager.getCache(cacheName).get(key, clazz);
    }

    public void setCacheOfValue(String cacheName, Object key, Object value) {
        cacheManager.getCache(cacheName).put(key, value);
    }

    public <T> T getSystemValue(String key, Class<T> clazz) {
        T obj = Constants.getCacheValue("system", key, clazz);
        if (Objects.isNull(obj)) {
//            initSystemData();
//            BaseDao dao = (BaseDao) applicationContext.getBean(StringUtils.uncapitalize(key) + "Dao");
//          obj= (T) dao.queryAllForCache();
//            obj = Constants.getCacheValue("system", key, clazz);
            obj = (T) baseDao.queryAllInTab(key.replace(TableConstants.SEPARATE_CACHE, TableConstants.SEPARATE));
        }
        return obj;
    }

    @CacheEvict(value = {"qCache", "system", "tmp", "auth","columns"},allEntries = true,cacheManager = "cacheManager")
    public void clearAllCache() {
        logger.info("清除所有缓存....");
    }

    @CacheEvict(value = "columns",allEntries = true,cacheManager = "cacheManager")
    public void clearColumns() {
        logger.debug("清除Columns缓存");
    }

    public void initApplication() {
        initColumns();
        initSystemData();
    }

    public void initColumns() {
        List<Map<String, Object>> dataMeta = baseDao.queryByS("SELECT TABLE_NAME,COLUMN_NAME FROM information_schema.Columns  WHERE table_schema='dems'");
        String prevTable = "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < dataMeta.size(); i++) {
            Map<String, Object> map = dataMeta.get(i);
            String tableName = (String) map.get("TABLE_NAME");
            if (tableName.equals(prevTable)) {
                sb.append(TableConstants.SEPARATE_SPLIT).append(map.get("COLUMN_NAME")).append(TableConstants.SPACE).append(ColumnProcess.encryptVal((String) map.get("COLUMN_NAME")));
            } else {
                if (i != 0)
                    cacheManager.getCache("columns").putIfAbsent(prevTable, sb.toString().substring(1));
                prevTable = tableName;
                sb = new StringBuffer();
                sb.append(TableConstants.SEPARATE_SPLIT).append(map.get("COLUMN_NAME")).append(TableConstants.SPACE).append(ColumnProcess.encryptVal((String) map.get("COLUMN_NAME")));
            }
        }
        cacheManager.getCache("columns").put(prevTable, sb.toString().substring(1));
    }

    public void initSystemData() {
/*        initDataMap.put("system", Collections.<String>emptyList());
        initDataMap.put("common", Collections.<String>emptyList());
        initDataMap.put("backstage", Collections.<String>emptyList());
        initDataMap.put("front", Collections.<String>emptyList());*/
        if (!StringUtils.isEmpty(systemData)) {
            String[] initDatas = systemData.split(",");
            List<String> inits = Arrays.asList(initDatas);
            initDataMap.put("system", inits);
/*            Consumer<String> in = poName -> {
                BaseDao dao = (BaseDao) applicationContext.getBean(StringUtils.uncapitalize(poName) + "Dao");
                List list = dao.queryAll();//TODO:基本数据初始化
*//*                if ("SystemDict".equals(poName)) {
                    ((List<SystemDict>) list).forEach((sd) -> setCacheOfValue("system", sd.getCode(), sd.getValue()));
                } else if ("Para".equals(poName)) {
//                    ((List<Para>) list).forEach((sd) -> setCacheOfValue("system", sd.getParaKey(), sd.getParaValue()));
                }*//*
            };
            inits.forEach(in);*/
            inits.forEach((tableName) -> {
               List<Map<String,Object>> results= baseDao.queryAllInTab(tableName);
                cacheManager.getCache("system").put(tableName.replace(".","-"),results);
            });
        }
/*        if (!StringUtils.isEmpty(frontData))
            initDataMap.put("front", Arrays.asList(frontData.split(",")));
        if (!StringUtils.isEmpty(backstageData))
            initDataMap.put("backstage", Arrays.asList(backstageData.split(",")));*/
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> cacheOperationInvocationContext) {
        logger.debug("in......cacheResolve\t"+cacheOperationInvocationContext.getArgs());
        for (String cacheName : initDataMap.keySet()) {
            if (initDataMap.get(cacheName).contains(cacheOperationInvocationContext.getArgs()[0])) {
                CacheOperation operation = (CacheOperation) cacheOperationInvocationContext.getOperation();
//                Constants.ReflectUtil.setFieldValue(operation, "cacheManager", "cacheManager");
                return Arrays.asList(cacheManager.getCache(cacheName));
            }
        }
//        cacheManager.getCache("system").evict();
        Set<String> cacheNames = cacheOperationInvocationContext.getOperation().getCacheNames();
        return Arrays.asList(cacheManager.getCache(cacheNames.stream().findFirst().get()));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Constants.cacheManager = this.cacheManager;
        Constants.jedisPool = this.jedisPool;
        Constants.applicationContext = this.applicationContext;
        Constants.publicKey = Constants.getSystemStringValue("PUBLIC_KEY");
        initApplication();
    }

    public RedisCacheManager getCacheManager() {
        return cacheManager;
    }
}
