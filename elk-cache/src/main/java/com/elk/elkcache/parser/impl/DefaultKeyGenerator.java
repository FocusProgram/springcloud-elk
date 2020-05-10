package com.elk.elkcache.parser.impl;


import com.elk.elkcache.constants.CacheScope;
import com.elk.elkcache.parser.IKeyGenerator;
import com.elk.elkcache.parser.IUserKeyGenerator;
import com.elk.elkcache.utils.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DefaultKeyGenerator extends IKeyGenerator {

    @Autowired(required = false)
    private IUserKeyGenerator userKeyGenerator;

    @Override
    public String buildKey(String key, CacheScope scope, Class<?>[] parameterTypes, Object[] arguments) {
        boolean isFirst = true;
        if (key.indexOf("{") > 0) {
            key = key.replace("{", ":{");
            Pattern pattern = Pattern.compile("\\d+\\.?[\\w]*");
            Matcher matcher = pattern.matcher(key);
            while (matcher.find()) {
                String tmp = matcher.group();
                String express[] = matcher.group().split("\\.");
                String i = express[0];
                int index = Integer.parseInt(i) - 1;
                Object value = arguments[index];
                if (parameterTypes[index].isAssignableFrom(List.class)) {
                    List result = (List) arguments[index];
                    value = result.get(0);
                }
                if (value == null || value.equals("null")) {
                    value = "";
                }
                if (express.length > 1) {
                    String field = express[1];
                    value = ReflectionUtils.getFieldValue(value, field);
                }
                if (isFirst) {
                    key = key.replace("{" + tmp + "}", value.toString());
                } else {
                    key = key.replace("{" + tmp + "}", LINK + value.toString());
                }
            }
        }
        return key;
    }

    @Override
    public IUserKeyGenerator getUserKeyGenerator() {
        return userKeyGenerator;
    }

}
