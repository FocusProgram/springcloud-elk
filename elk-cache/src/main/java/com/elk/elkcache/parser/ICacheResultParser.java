package com.elk.elkcache.parser;

import java.lang.reflect.Type;

/**
 * cache结果解析
 * <p/>
 * 解决问题：
 */
public interface ICacheResultParser {

    /**
     * 解析结果
     *
     * @param value
     * @param returnType
     * @param origins
     * @return
     */
    public Object parse(String value, Type returnType, Class<?>... origins);
}
