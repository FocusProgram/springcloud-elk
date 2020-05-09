package com.qywk.cache.aspect;

import com.qywk.cache.annotation.CacheClear;
import com.qywk.cache.api.CacheAPI;
import com.qywk.cache.constants.CacheScope;
import com.qywk.cache.parser.IKeyGenerator;
import com.qywk.cache.parser.impl.DefaultKeyGenerator;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 清除缓存注解拦截
 */
@Aspect
@Service
public class CacheClearAspect {

    @Autowired
    private IKeyGenerator keyParser;
    @Autowired
    private CacheAPI cacheAPI;
    private ConcurrentHashMap<String, IKeyGenerator> generatorMap = new ConcurrentHashMap<String, IKeyGenerator>();

    @Pointcut("@annotation(com.qywk.cache.annotation.Cache)")
    public void aspect() {
    }

    @Around("aspect()&&@annotation(anno)")
    public Object interceptor(ProceedingJoinPoint invocation, CacheClear anno)
        throws Throwable {
        MethodSignature signature = (MethodSignature) invocation.getSignature();
        Method method = signature.getMethod();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] arguments = invocation.getArgs();
        String key = "";
        if (StringUtils.isNotBlank(anno.key())) {
            key = getKey(anno, anno.key(), CacheScope.application,
                parameterTypes, arguments);
            cacheAPI.remove(key);
        } else if (StringUtils.isNotBlank(anno.pre())) {
            key = getKey(anno, anno.pre(), CacheScope.application,
                parameterTypes, arguments);
            cacheAPI.removeByPre(key);
        } else if (anno.keys().length > 1) {
            for (String tmp : anno.keys()) {
                tmp = getKey(anno, tmp, CacheScope.application, parameterTypes,
                    arguments);
                cacheAPI.removeByPre(tmp);
            }
        }
        return invocation.proceed();
    }

    /**
     * 解析表达式
     *
     * @param anno
     * @param parameterTypes
     * @param arguments
     * @return
     */
    private String getKey(CacheClear anno, String key, CacheScope scope,
        Class<?>[] parameterTypes, Object[] arguments)
        throws InstantiationException, IllegalAccessException {
        String finalKey;
        String generatorClsName = anno.generator().getName();
        IKeyGenerator keyGenerator = null;
        if (anno.generator().equals(DefaultKeyGenerator.class)) {
            keyGenerator = keyParser;
        } else {
            if (generatorMap.containsKey(generatorClsName)) {
                keyGenerator = generatorMap.get(generatorClsName);
            } else {
                keyGenerator = anno.generator().newInstance();
                generatorMap.put(generatorClsName, keyGenerator);
            }
        }
        finalKey = keyGenerator.getKey(key, scope, parameterTypes, arguments);
        return finalKey;
    }
}
