package com.easysoft.ecommerce.service.impl;

import java.util.Properties;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.hibernate.EhCacheRegionFactory;

import org.hibernate.cache.CacheException;
import org.hibernate.cfg.Settings;

/**
 * An implementation of EhCacheRegionFactory which uses CacheManager injected from Spring to provide to Hibernate.
 * Because the available EhCacheManagerFactoryBean class doesn't support setting XA transaction manager (
 * XA cache is a feature of EHCache from 2.0), using this class (instead of EhCacheRegionFactory) also means we
 * will not be able to support the new feature XA caches of EHCache 2. Another disadvantage is
 * HibernateUtil.loadAndCorrectConfiguration() is not called to fix problematic config files. See EhCacheRegionFactory
 * class for more information about these disadvantages.
 */
public class SpringawareEhcacheRegionFactory extends EhCacheRegionFactory {

    private static CacheManager cacheManager;

    // Will be called by spring to inject cacheManager
    public static void setCacheManager(CacheManager cacheManager) {
        SpringawareEhcacheRegionFactory.cacheManager = cacheManager;
    }

    public SpringawareEhcacheRegionFactory(Properties prop) {
        super(prop);
    }

    @Override
    public void start(Settings settings, Properties properties) throws CacheException {
        manager = SpringawareEhcacheRegionFactory.cacheManager;
    }

    @Override
    public void stop() {
        // Spring will cleanup for EHCache
    }

}
