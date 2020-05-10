package com.elk.elkcache;

import com.elk.elkcache.constants.CacheScope;
import com.elk.elkcache.parser.IKeyGenerator;
import com.elk.elkcache.parser.IUserKeyGenerator;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-05-22 14:05
 */
public class MyKeyGenerator extends IKeyGenerator {

    @Override
    public IUserKeyGenerator getUserKeyGenerator() {
        return null;
    }

    @Override
    public String buildKey(String key, CacheScope scope, Class<?>[] parameterTypes, Object[] arguments) {
        return "myKey_" + arguments[0];
    }
}
