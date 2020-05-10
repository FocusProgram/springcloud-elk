package com.elk.elkcache.service;


import com.elk.elkcache.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by dyx on 2020/4
 */
public interface UserService {
    public User get(String account);

    public List<User> getLlist();

    public Set<User> getSet();

    public Map<String, User> getMap();

    public void save(User user);

    public User get(int age);
}
