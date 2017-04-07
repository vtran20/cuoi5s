package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.CmsAreaDao;
import com.easysoft.ecommerce.model.CmsArea;
import com.easysoft.ecommerce.model.Site;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsAreaDaoImpl extends GenericDaoImpl<CmsArea, Long> implements CmsAreaDao {

    @Override
    public List<CmsArea> getAllCmsAreaBySite(Site site) {
        List<CmsArea> list = this.getSessionFactory().getCurrentSession().createQuery("select a from CmsArea a where a.site.id = :siteId").
                setParameter("siteId",site.getId())
                .setCacheable(true)
                .list();
        return list;
    }
    @Override
    public CmsArea getCmsArea(Site site, String cmsName, String path) {
        List list = this.getSessionFactory().getCurrentSession().createQuery("select a from CmsArea a where a.site.id = :siteId and a.cmsName = :cmsName and a.path = :path").
                setParameter("siteId",site.getId()).
                setParameter("cmsName", cmsName).
                setParameter("path",path)
                .setCacheable(true)
                .list();

        return (list != null && list.size() >= 1)? (CmsArea) list.get(0) : null;

    }
    @Override
    public CmsArea getCmsArea(Site site, String path) {
        List list = this.getSessionFactory().getCurrentSession().createQuery("select a from CmsArea a where a.site.id = :siteId and a.path = :path").
                setParameter("siteId",site.getId()).
                setParameter("path",path)
                .setCacheable(true)
                .list();

        return (list != null && list.size() >= 1)? (CmsArea) list.get(0) : null;

    }

}