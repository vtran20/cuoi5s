package com.easysoft.ecommerce.dao;

import java.util.Map;

import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.SiteParam;

public interface SiteParamDao extends GenericDao<SiteParam, Long> {

    public Map<String, String> getSiteParamsBySite (Site site);
}