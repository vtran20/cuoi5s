package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.NewsCategoryDao;
import com.easysoft.ecommerce.model.Menu;
import com.easysoft.ecommerce.model.NewsCategory;
import com.easysoft.ecommerce.model.Site;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NewsCategoryDaoImpl extends GenericDaoImpl<NewsCategory, Long> implements NewsCategoryDao {


    @Override
    public List<NewsCategory> getRootNewsCategories(Site site) {
        return getRootNewsCategories(site, "Y");
    }

    @Override
    public List<NewsCategory> getRootNewsCategories(Site site, String active) {
        if ("Y".equals(active)) {
            return this.getSessionFactory().getCurrentSession().createQuery("select a from NewsCategory a where a.site.id = :siteId and a.parentNewsCategory is null and a.active='Y' order by sequence").
                    setParameter("siteId", site.getId()).list();
        } else {
            return this.getSessionFactory().getCurrentSession().createQuery("select a from NewsCategory a where a.site.id = :siteId and a.parentNewsCategory is null order by sequence").
                    setParameter("siteId", site.getId()).list();
        }
    }

    @Override
    public List<NewsCategory> getSubNewsCategories(Site site, NewsCategory newsCategory) {
        return getSubNewsCategories(site, newsCategory, "Y");
    }
    @Override
    public List<NewsCategory> getSubNewsCategories(Site site, NewsCategory newsCategory, String active) {
        if ("Y".equals(active)) {
            return this.getSessionFactory().getCurrentSession().createQuery("select a from NewsCategory a where a.site.id = :siteId and a.parentNewsCategory.id = :newsCategoryId and a.active='Y' order by sequence").
                    setParameter("siteId", site.getId()).
                    setParameter("newsCategoryId", newsCategory.getId()).list();
        } else {
            return this.getSessionFactory().getCurrentSession().createQuery("select a from NewsCategory a where a.site.id = :siteId and a.parentNewsCategory.id = :newsCategoryId order by sequence").
                    setParameter("siteId", site.getId()).
                    setParameter("newsCategoryId", newsCategory.getId()).list();
        }
    }

    @Override
    public NewsCategory getParentNewsCategory(Site site, Long newsCategoryId) {
        return getParentNewsCategory(site, newsCategoryId, "Y");
    }

    @Override
    public NewsCategory getParentNewsCategory(Site site, Long newsCategoryId, String active) {
        if ("Y".equals(active)) {
            return (NewsCategory) this.getSessionFactory().getCurrentSession().createQuery("select a.parentNewsCategory from NewsCategory a  where a.site.id = :siteId and a.id = :newsCategoryId and a.parentNewsCategory.active = 'Y'").
                    setParameter("siteId", site.getId()).setLong("newsCategoryId", newsCategoryId).uniqueResult();
        } else {
            return (NewsCategory) this.getSessionFactory().getCurrentSession().createQuery("select a.parentNewsCategory from NewsCategory a  where a.site.id = :siteId and a.id = :newsCategoryId").
                    setParameter("siteId", site.getId()).setLong("newsCategoryId", newsCategoryId).uniqueResult();
        }
    }

    @Override
    public NewsCategory getNewsCategory(Site site, Long newsCategoryId) {
        return getNewsCategory(site, newsCategoryId, "Y");
    }

    @Override
    public NewsCategory getNewsCategory(Site site, Long newsCategoryId, String active) {
        if ("Y".equals(active)) {
            return (NewsCategory) this.getSessionFactory().getCurrentSession().createQuery("select a from NewsCategory a where a.site.id = :siteId and a.id = :newsCategoryId and a.active='Y'").
                    setParameter("siteId", site.getId()).
                    setParameter("newsCategoryId", newsCategoryId).uniqueResult();
        } else {
            return (NewsCategory) this.getSessionFactory().getCurrentSession().createQuery("select a from NewsCategory a where a.site.id = :siteId and a.id = :newsCategoryId").
                    setParameter("siteId", site.getId()).
                    setParameter("newsCategoryId", newsCategoryId).uniqueResult();
        }
    }

    @Override
    public NewsCategory getNewsCategory(Site site, String uri) {
        return getNewsCategory(site, uri, "Y");
    }

    @Override
    public NewsCategory getNewsCategory(Site site, String uri, String active) {
        if ("Y".equals(active)) {
            return (NewsCategory) this.getSessionFactory().getCurrentSession().createQuery("select a from NewsCategory a where a.site.id = :siteId and a.uri = :uri and a.active='Y'").
                    setParameter("siteId", site.getId()).
                    setParameter("uri", uri).uniqueResult();
        } else {
            return (NewsCategory) this.getSessionFactory().getCurrentSession().createQuery("select a from NewsCategory a where a.site.id = :siteId and a.uri = :uri").
                    setParameter("siteId", site.getId()).
                    setParameter("uri", uri).uniqueResult();
        }
    }

    @Override
    public List<NewsCategory> findNewscategoriesHavingNews (Long siteId) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select distinct c from NewsCategory c join c.newses o where o.site.id =:siteId order by c.sequence")
                .setLong("siteId", siteId);
        return query.list();

    }
    @Override
    public NewsCategory findNewsCategory (Long newsId, Long newsCategoryId, Long siteId) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select c from NewsCategory c join c.newses o where o.id =:newsId and c.id = :newsCategoryId and c.site.id = :siteId")
                .setLong("newsId", newsId)
                .setLong("newsCategoryId", newsCategoryId)
                .setLong("siteId", siteId);
        return (NewsCategory) query.uniqueResult();

    }

}
