package com.easysoft.ecommerce.util;

import com.easysoft.ecommerce.web.cache.CacheKeyGenerator;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * This helper class is used for handling cache in the entire web application.
 *
 * CacheTag is used for caching cms content and html page
 * CommonCache is used for caching everything except CacheTag and ImagesServlet
 *
 * User: Vu Tran
 * Date: Sep 7, 2010
 * Time: 11:44:11 AM
 */
public class CacheData {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheData.class);

    @Autowired
    private CacheManager cacheManager;

    /***********Methods are used for Common Cache****************/
    public void addCommonCache (String key, Object value) {
        Element element = new Element(key, value);
        getCommonCache().put(element);
    }

    public Object getCommonCache (String key) {
        if (getCommonCache().get(key) != null) {
            return getCommonCache().get(key).getValue();
        } else {
            return null;
        }
    }

    public void removeCommonCache (String key) {
        if (StringUtils.isNotEmpty(key)) {
            getCommonCache().remove(key);
        }
    }

    public void removeCommonCache (List<String> keys) {
        if (keys != null) {
            for (String key: keys) {
                removeCommonCache(key);
            }
        }
    }

    /***********Methods are used for Cache Tag****************/
    public void addCacheTag (String key, Object value) {
        Element element = new Element(key, value);
        getCacheTag().put(element);
    }

    public Object getCacheTag (String key) {
        if (getCacheTag().get(key) != null) {
            return getCacheTag().get(key).getValue();
        } else {
            return null;
        }
    }
    public void removeCacheTag (String key) {
        if (StringUtils.isNotEmpty(key)) {
            getCacheTag().remove(key);
        }
    }
    public void removeCacheTag (List<String> keys) {
        if (keys != null) {
            for (String key: keys) {
                removeCacheTag(key);
            }
        }
    }

    public Cache getCommonCache () {
        return getCacheManager("CommonCache");
    }
    public Cache getCacheTag () {
        return getCacheManager("CacheTag");
    }

    private Cache getCacheManager(String name) {
        return cacheManager.getCache(name);
    }

    private CacheManager getCacheManager() {
        return cacheManager;
    }
}
