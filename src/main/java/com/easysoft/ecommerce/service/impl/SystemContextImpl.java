package com.easysoft.ecommerce.service.impl;

import com.easysoft.ecommerce.model.GlobalConfig;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.SiteParam;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.SystemContext;
import com.easysoft.ecommerce.util.CacheData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope("request")
public class SystemContextImpl implements SystemContext {

    private ServiceLocator serviceLocator;

    private HttpServletRequest request;

    private CacheData cacheData;

    @Autowired
    public SystemContextImpl(ServiceLocator serviceLocator,
                             HttpServletRequest request,
                             CacheData cacheData) {
        this.serviceLocator = serviceLocator;
        this.request = request;
        this.cacheData = cacheData;
    }

    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }

    /**
     * multisite: at least one default site must exist;
     * single site: site will be null (site table will be empty)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Site getSite() {
        Site site;
        Map params = request.getParameterMap();
        if (params.containsKey("siteCd")) {
            //first priority, get site by siteCd
            String[] values = (String[]) params.get("siteCode");
            site = serviceLocator.getSiteDao().findUniqueBy("siteCode", values[0]);
        } else if (params.containsKey("siteId")) {
            //second priority, get site by siteId
            String[] values = (String[]) params.get("siteId");
            site = serviceLocator.getSiteDao().findById(Long.parseLong(values[0]));
        } else {
            //the last priority, get site by domain/subdomain
            String serverName = request.getServerName();
            site = (Site) cacheData.getCommonCache(serverName);
            if (site == null) {
                site = serviceLocator.getSiteDao().getSiteByServerNameIncludeExpiredDomain(serverName);
                if (site == null) {
                    //Check and see if the domain have www or not. Try to remove or add www and see if we can find out the domain of the current site.
                    if (serverName.contains("www.")) {
                        serverName = serverName.replace("www.", "");
                        site = (Site) cacheData.getCommonCache(serverName);
                        if (site == null) {
                            site = serviceLocator.getSiteDao().getSiteByServerNameIncludeExpiredDomain(serverName);
                        }
                    } else {
                        serverName = "www."+serverName;
                        site = (Site) cacheData.getCommonCache(serverName);
                        if (site == null) {
                            site = serviceLocator.getSiteDao().getSiteByServerNameIncludeExpiredDomain(serverName);
                        }
                    }
                    if (site == null) {
                        //if cannot find site from domain/subdomain, get default site from cache
                        site = (Site) cacheData.getCommonCache("default_site");
                        if (site == null) {
                            //get default site from database and cache
                            site = serviceLocator.getSiteDao().getDefaultSite();
                            cacheData.addCommonCache("default_site", site);
                        }
                    } else {
                        cacheData.addCommonCache(serverName, site);
                    }
                } else {
                    cacheData.addCommonCache(serverName, site);
                }
            }
        }
        return site;
    }

    @Override
    public String getGlobalConfig(String key) {
        
        Map <String, String> map = (Map <String, String>) cacheData.getCommonCache("GLOBAL_CONFIG");
        if (map != null) {
            return map.get(key);
        } else {
            map = new HashMap<String, String>();
            List<GlobalConfig> list = serviceLocator.getGlobalConfigDao().findAll();
            for (GlobalConfig gc : list) {
                map.put(gc.getKey(), gc.getValue());
            }

            cacheData.addCommonCache("GLOBAL_CONFIG", map);
            return map.get(key);
        }
    }

//    @Override
//    public boolean isSiteAdmin() {
//        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
//        return currentUser != null && currentUser.getAuthorities().contains(new GrantedAuthorityImpl("ROLE_SITE_ADMIN"));
//
//    }
//    @Override
//    public boolean isSystemAdmin() {
//        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
//        return currentUser != null && currentUser.getAuthorities().contains(new GrantedAuthorityImpl("ROLE_SYSTEM_ADMIN"));
//
//    }

}
