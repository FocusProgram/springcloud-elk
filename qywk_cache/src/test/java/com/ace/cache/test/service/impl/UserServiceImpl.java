package com.qywk.cache.test.service.impl;

import com.qywk.cache.test.cache.MyKeyGenerator;
import com.qywk.cache.test.entity.User;
import com.qywk.cache.test.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.qywk.cache.annotation.Cache;
import com.qywk.cache.annotation.CacheClear;
import com.qywk.cache.parser.ICacheResultParser;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 * Created by dyx on 2020/4
 */
@Service
public class UserServiceImpl implements UserService {


    @Override
    @Cache(key = "user{1}")
    public User get(String account) {
        System.out.println("从方法内读取....");
        User user = new User("Ace", 24, account);
        return user;
    }

    @Override
    @Cache(key = "user:list")
    public List<User> getLlist() {
        System.out.println("从方法内读取....");
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < 20; i++) {
            User user = new User("Ace", i, "ace");
            users.add(user);
        }
        return users;
    }

    @Override
    @Cache(key = "user:set", parser = SetCacheResultParser.class)
    public Set<User> getSet() {
        System.out.println("从方法内读取....");
        Set<User> users = new HashSet<User>();
        for (int i = 0; i < 20; i++) {
            User user = new User("Ace", i, "ace");
            users.add(user);
        }
        return users;
    }

    @Override
    @Cache(key = "user:map", parser = UserMapCacheResultParser.class)
    public Map<String, User> getMap() {
        System.out.println("从方法内读取....");
        Map<String, User> users = new HashMap<String, User>();
        for (int i = 0; i < 20; i++) {
            User user = new User("Ace", i, "ace");
            users.put(user.getAccount() + i, user);
        }
        return users;
    }

    @Override
    @CacheClear(pre = "user")
    public void save(User user) {

    }

    @Override
    @Cache(key = "user", generator = MyKeyGenerator.class)
    public User get(int age) {
        System.out.println("从方法内读取....");
        User user = new User("Ace", age, "Ace");
        return user;
    }

    /**
     * 对map返回结果做处理
     *
     * @Created by dyx
     */
    public static class UserMapCacheResultParser implements ICacheResultParser {

        @Override
        public Object parse(String value, Type returnType, Class<?>... origins) {
            return JSON.parseObject(value, new TypeReference<HashMap<String, User>>() {
            });
        }
    }

    /**
     * 对set返回结果做处理
     *
     * @Created by dyx
     */
    public static class SetCacheResultParser implements ICacheResultParser {

        @Override
        public Object parse(String value, Type returnType, Class<?>... origins) {
//            origins[0]
            return JSON.parseObject(value, new TypeReference<HashSet<User>>() {
            });
        }
    }
}