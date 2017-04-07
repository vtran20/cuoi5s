package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.CmsArea;
import com.easysoft.ecommerce.model.Site;

import java.util.List;


public interface CmsAreaDao extends GenericDao<CmsArea, Long> {
    List<CmsArea> getAllCmsAreaBySite(Site site);    
    CmsArea getCmsArea(Site site, String cmsName, String path);
    CmsArea getCmsArea(Site site, String path);
}