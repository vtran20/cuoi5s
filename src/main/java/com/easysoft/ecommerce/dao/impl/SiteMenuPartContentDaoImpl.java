package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.SiteMenuPartContentDao;
import com.easysoft.ecommerce.model.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SiteMenuPartContentDaoImpl extends GenericDaoImpl<SiteMenuPartContent, Long> implements SiteMenuPartContentDao {

    @Override
    public List<Row> getMenuRows(Long menuId) {
        return getMenuRows(menuId, "Y");
    }

    @Override
    public List<Row> getMenuRows(Long menuId, String active) {
        if ("Y".equals(active)) {
            return this.getSessionFactory().getCurrentSession().createQuery("select a from Row a where a.menu.id = :menuId and a.active='Y' order by a.sequence").
                    setParameter("menuId", menuId).list();
        } else {
            return this.getSessionFactory().getCurrentSession().createQuery("select a from Row a where a.menu.id = :menuId order by a.sequence").
                    setParameter("menuId", menuId).list();
        }
    }

    @Override
    public List<Row> getMenuRows(String uri, Site site) {
        return getMenuRows(uri, site, "Y");
    }

    @Override
    public List<Row> getMenuRows(String uri, Site site, String active) {
        if ("Y".equals(active)) {
            return this.getSessionFactory().getCurrentSession().createQuery("select a from Row a join a.menu m where m.uri = :uri and m.site.id = :siteId and a.active='Y' order by a.sequence").
                    setParameter("uri", uri).
                    setParameter("siteId", site.getId()).list();
        } else {
            return this.getSessionFactory().getCurrentSession().createQuery("select a from Row a join a.menu m where m.uri = :uri and m.site.id = :siteId order by a.sequence").
                    setParameter("uri", uri).
                    setParameter("siteId", site.getId()).list();
        }
    }

    @Override
    public List<SiteMenuPartContent> getContentParts(Long rowId) {
        return getContentParts(rowId, "Y");
    }

    @Override
    public List<SiteMenuPartContent> getContentParts(Long rowId, String active) {
        if ("Y".equals(active)) {
            return this.getSessionFactory().getCurrentSession().createQuery("select a from SiteMenuPartContent a join a.row where a.row.id = :rowId and a.active='Y' order by a.sequence").
                    setParameter("rowId", rowId).list();
        } else {
            return this.getSessionFactory().getCurrentSession().createQuery("select a from SiteMenuPartContent a join a.row where a.row.id = :rowId order by a.sequence").
                    setParameter("rowId", rowId).list();
        }
    }
    @Override
    public List<Product> getProductContentParts(Long rowId) {
        return getProductContentParts(rowId, "Y");
    }

    @Override
    public List<Product> getProductContentParts(Long rowId, String active) {
        if ("Y".equals(active)) {
            return this.getSessionFactory().getCurrentSession().createQuery("select a from Product a where a.active = 'Y' and a.id in (select c.header from SiteMenuPartContent c where c.row.id = :rowId and c.active = 'Y' order by c.sequence)").
                    setParameter("rowId", rowId).list();
        } else {
            return this.getSessionFactory().getCurrentSession().createQuery("select a from Product a where a.id in (select c.header from SiteMenuPartContent c where c.row.id = :rowId order by c.sequence)").
                    setParameter("rowId", rowId).list();
        }
    }
    @Override
    public List<News> getNewsContentParts(Long rowId) {
        return getNewsContentParts(rowId, "Y");
    }

    @Override
    public List<News> getNewsContentParts(Long rowId, String active) {
        if ("Y".equals(active)) {
            return this.getSessionFactory().getCurrentSession().createQuery("select a from News a where a.active = 'Y' and a.id in (select c.header from SiteMenuPartContent c where c.row.id = :rowId and c.active = 'Y' order by c.sequence)").
                    setParameter("rowId", rowId).list();
        } else {
            return this.getSessionFactory().getCurrentSession().createQuery("select a from News a where a.id in (select c.header from SiteMenuPartContent c where c.row.id = :rowId order by c.sequence)").
                    setParameter("rowId", rowId).list();
        }
    }

    @Override
    public WidgetTemplate getWidgetTemplate(Long rowId) {
        return (WidgetTemplate) this.getSessionFactory().getCurrentSession().createQuery("select m from Row a join a.widgetTemplate m where a.id = :rowId").
                setParameter("rowId", rowId).uniqueResult();
    }
    @Override
    public WidgetTemplate getWidgetTemplateByPartContent(Long partContentId) {
        return (WidgetTemplate) this.getSessionFactory().getCurrentSession().createQuery("select w from SiteMenuPartContent p join p.row r join r.widgetTemplate w where w.id = :partContentId").
                setParameter("partContentId", partContentId).uniqueResult();
    }
}
