package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.Template;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SiteDao extends GenericDao<Site, Long> {

    Site getSiteByServerName(String serverName);

    Site getSiteByServerNameIncludeExpiredDomain(String serverName);

    Site getSite(String siteCd);

    Site getDefaultSite();
    List<Site> getSitesByUser(Long userId);
    List<Site> getPartnerSitesByUser(Long userId);

    Site getSiteSample(Template selectedTemplate);
    Site getSiteSample(Template selectedTemplate, String active);
    Template getTemplateFromSite(Site site);

    List <Map> searchClientSite(Map input, Site site);

    List <Map> searchTemplateSite(Map input, Site site);
    void updateExpiredDateForDemoSite (Date endDate);
}