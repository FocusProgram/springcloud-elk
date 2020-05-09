/**
 *
 */
package com.qywk.cache.service;

import com.qywk.cache.entity.CacheBean;
import com.qywk.cache.vo.CacheTree;
import java.util.List;


public interface ICacheManager {

    public void removeAll();

    public void remove(String key);

    public void remove(List<CacheBean> caches);

    public void removeByPre(String pre);

    public List<CacheTree> getAll();

    public List<CacheTree> getByPre(String pre);

    public void update(String key, int hour);

    public void update(List<CacheBean> caches, int hour);

    public String get(String key);
}
