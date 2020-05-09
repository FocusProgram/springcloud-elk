package com.qywk.cache.annotation;

import com.qywk.cache.constants.CacheScope;
import com.qywk.cache.parser.ICacheResultParser;
import com.qywk.cache.parser.IKeyGenerator;
import com.qywk.cache.parser.impl.DefaultKeyGenerator;
import com.qywk.cache.parser.impl.DefaultResultParser;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启缓存
 */
@Retention(RetentionPolicy.RUNTIME)
// 在运行时可以获取
@Target(value = {ElementType.METHOD, ElementType.TYPE})
// 作用到类，方法，接口上等
public @interface Cache {

    /**
     * 缓存key menu_{0.id}{1}_type
     *
     * @return
     * @author dyx
     * @date 2017年5月3日
     */
    public String key() default "";

    /**
     * 作用域
     *
     * @return
     * @author dyx
     * @date 2017年5月3日
     */
    public CacheScope scope() default CacheScope.application;

    /**
     * 过期时间
     *
     * @return
     * @author dyx
     * @date 2017年5月3日
     */
    public int expire() default 720;

    /**
     * 描述
     *
     * @return
     * @author dyx
     * @date 2017年5月3日
     */
    public String desc() default "";

    /**
     * 返回类型
     *
     * @return
     * @author dyx
     * @date 2017年5月4日
     */
    public Class[] result() default Object.class;

    /**
     * 返回结果解析类
     *
     * @return
     */
    public Class<? extends ICacheResultParser> parser() default DefaultResultParser.class;

    /**
     * 键值解析类
     *
     * @return
     */
    public Class<? extends IKeyGenerator> generator() default DefaultKeyGenerator.class;
}
