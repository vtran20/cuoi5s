package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.CmsAreaContent;
import com.easysoft.ecommerce.model.Site;

public interface CmsAreaContentDao extends GenericDao<CmsAreaContent, Long> {
    CmsAreaContent getCmsAreaContent (Site site, String cmsName);
    CmsAreaContent getCmsAreaDynamicContent (Site site, String cmsName, String path);
    CmsAreaContent getPageContent (Site site, String path);
    CmsAreaContent getCmsAreaContents (Site site, String cmsName);
    CmsAreaContent getCmsAreaContents (Site site, String cmsName, boolean active);
}