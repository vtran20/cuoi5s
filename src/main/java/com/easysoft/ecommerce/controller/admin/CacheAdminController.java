package com.easysoft.ecommerce.controller.admin;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.easysoft.ecommerce.service.ServiceLocatorHolder;

@Controller
@RequestMapping("/admin/caches")
public class CacheAdminController {

    @RequestMapping (value="index.html", method=RequestMethod.GET)
    public ModelAndView index() throws Exception {
        return new ModelAndView("admin/caches/index");
    }

    @RequestMapping (value="action.html", method=RequestMethod.POST)
    public ModelAndView index(@RequestParam("action") String action) throws Exception {
        CacheManager cacheManager = ServiceLocatorHolder.getServiceLocator().getCacheManager();
        for (String name : cacheManager.getCacheNames()) {
            Ehcache cache = cacheManager.getEhcache(name);
            if (cache != null) {
                if ("cleanCache".equals(action)) {
                    cache.removeAll();
                }
                if ("cleanStatistics".equals(action)) {
                    cache.clearStatistics();
                }
                if ("enableStatistics".equals(action)) {
                    cache.setStatisticsEnabled(true);
                }
                if ("disableStatistics".equals(action)) {
                    cache.setStatisticsEnabled(false);
                }
            }
        }
        return new ModelAndView("admin/caches/index");
    }

    @RequestMapping (value="clean.html", method=RequestMethod.POST)
    public ModelAndView clean(@RequestParam("name") String name) throws Exception {
        CacheManager cacheManager = ServiceLocatorHolder.getServiceLocator().getCacheManager();
        Ehcache cache = cacheManager.getEhcache(name);
        if (cache != null) {
            cache.removeAll();
        }
        return new ModelAndView("admin/caches/index");
    }
    @RequestMapping (value="view.html")
    public ModelAndView view(@RequestParam("name") String name) throws Exception {
        CacheManager cacheManager = ServiceLocatorHolder.getServiceLocator().getCacheManager();
        Ehcache cache = cacheManager.getEhcache(name);
        return new ModelAndView("admin/caches/view", "cache", cache);
    }
    @RequestMapping (value="statistics.html", method=RequestMethod.POST)
    public ModelAndView statisticsToggle(@RequestParam("name") String name) throws Exception {
        CacheManager cacheManager = ServiceLocatorHolder.getServiceLocator().getCacheManager();
        Ehcache cache = cacheManager.getEhcache(name);
        cache.setStatisticsEnabled(!cache.isStatisticsEnabled());
        return new ModelAndView("admin/caches/index");
    }
    @RequestMapping (value="statistics.html", method=RequestMethod.DELETE)
    public ModelAndView statisticsClear(@RequestParam("name") String name) throws Exception {
        CacheManager cacheManager = ServiceLocatorHolder.getServiceLocator().getCacheManager();
        Ehcache cache = cacheManager.getEhcache(name);
        cache.clearStatistics();
        return new ModelAndView("admin/caches/index");
    }

}
