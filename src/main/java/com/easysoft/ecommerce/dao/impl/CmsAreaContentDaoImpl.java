package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.CmsAreaContentDao;
import com.easysoft.ecommerce.model.CmsAreaContent;
import com.easysoft.ecommerce.model.Site;
import org.hibernate.type.TimestampType;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class CmsAreaContentDaoImpl extends GenericDaoImpl<CmsAreaContent, Long> implements CmsAreaContentDao {

    /**
     * This method is used for cmscontent.tag
     * @param site
     * @param cmsName
     * @return
     */
    @Override
    public CmsAreaContent getCmsAreaContent(Site site, String cmsName) {
        List list = this.getSessionFactory().getCurrentSession()
        		.createQuery("select b from CmsArea a join a.contents b where a.site.id = :siteId and a.cmsName = :cmsName and ((b.startDate <= :currentDate and b.endDate >= :currentDate) or b.endDate is null) and b.active = 'Y' order by b.startDate desc")
                .setParameter("siteId",site.getId())
                .setParameter("cmsName",cmsName)
                .setParameter("currentDate", new Date(), new TimestampType()).list();

        return (list != null && list.size() >= 1)? (CmsAreaContent) list.get(0) : null;

    }

    /**
     * This method is used for cmsdynamiccontent.tag
     *
     * @param site
     * @param cmsName
     * @param path
     * @return
     */
    @Override
    public CmsAreaContent getCmsAreaDynamicContent(Site site, String cmsName, String path) {
        List list = this.getSessionFactory().getCurrentSession().createQuery("select b from CmsArea a join a.contents b where a.site.id = :siteId and a.cmsName = :cmsName and a.path = :path and ((b.startDate <= :currentDate and b.endDate >= :currentDate) or b.endDate is null) and b.active = 'Y' order by b.startDate desc").
                setParameter("siteId",site.getId()).
                setParameter("cmsName", cmsName).
                setParameter("path",path).
                setParameter("currentDate", new Date(), new TimestampType()).list();

        return (list != null && list.size() >= 1)? (CmsAreaContent) list.get(0) : null;

    }

    /**
     * This method is used to get data for content page.
     * @param site
     * @param path
     * @return
     */
    @Override
    public CmsAreaContent getPageContent(Site site, String path) {
        List list = this.getSessionFactory().getCurrentSession().createQuery("select b from CmsArea a join a.contents b where a.site.id = :siteId and a.path = :path and ((b.startDate <= :currentDate and b.endDate >= :currentDate) or b.endDate is null) and b.active = 'Y' order by b.startDate desc").
                setParameter("siteId",site.getId()).
                setParameter("path",path).
                setParameter("currentDate", new Date(), new TimestampType()).list();

        return (list != null && list.size() >= 1)? (CmsAreaContent) list.get(0) : null;

    }

    @Override
    public CmsAreaContent getCmsAreaContents(Site site, String cmsName) {
        return getCmsAreaContents(site, cmsName, true);
    }

    @Override
    public CmsAreaContent getCmsAreaContents(Site site, String cmsName, boolean active) {

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}