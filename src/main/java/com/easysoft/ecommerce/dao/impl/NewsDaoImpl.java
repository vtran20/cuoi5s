package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.NewsDao;
import com.easysoft.ecommerce.model.News;
import com.easysoft.ecommerce.model.NewsCategory;
import com.easysoft.ecommerce.model.Site;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NewsDaoImpl extends GenericDaoImpl<News, Long> implements NewsDao {


    @Override
    public void removeNewsNewsCategory(Long newsId) {
        News cat = this.findById(newsId);
        List<NewsCategory> newsCategories = findNewscategoriesByNewsId(newsId);
        cat.removeNewsNewsCategory(newsCategories);

    }

    @Override
    public List<NewsCategory> findNewscategoriesByNewsId (Long newsId) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select c from NewsCategory c join c.newses o where o.id =:newsId")
                .setLong("newsId", newsId);
        return query.list();

    }

    public List<News> findNewsByNewsCategory(String categoryUri, Long categoryId, Integer startPosition, Integer maxResult, String orderByAttr, Long siteId) {
        List result = null;
        String hql = null;
        Query query = null;
        if (StringUtils.isNotBlank(categoryUri)) {
            hql = "select n from News n join n.newsCategories c where c.uri =:categoryUri and c.site.id =:siteId and n.active = 'Y' ";
            if (StringUtils.isNotBlank(orderByAttr)) hql += " order by n." + orderByAttr;
             query = getSessionFactory().getCurrentSession().createQuery(hql)
                    .setString("categoryUri", categoryUri)
                    .setLong("siteId", siteId);
            if (startPosition != null) query.setFirstResult(startPosition);
            if (maxResult != null) query.setMaxResults(maxResult);

            result = query.list();
        }

        //If result = null, find out base on categoryId
        if (result != null && !result.isEmpty()) {
            if (categoryId != null && categoryId > 0) {
                hql = "select n from News n join n.newsCategories c where c.id =:categoryId and c.site.id =:siteId and n.active = 'Y' ";
                if (StringUtils.isNotBlank(orderByAttr)) hql += " order by n." + orderByAttr;
                query = getSessionFactory().getCurrentSession().createQuery(hql)
                        .setLong("categoryId", categoryId)
                        .setLong("siteId", siteId);
                if (startPosition != null) query.setFirstResult(startPosition);
                if (maxResult != null) query.setMaxResults(maxResult);

                result = query.list();
            }
        }
        return result;
    }

    public Long countActiveNewsByNewsCategory(String categoryUri, Long categoryId, Long siteId) {
        Long result = null;
        String hql = null;
        Query query = null;
        if (StringUtils.isNotBlank(categoryUri)) {
            hql = "select count(n) from News n join n.newsCategories c where c.uri =:categoryUri and c.site.id =:siteId and n.active = 'Y'";
            query = getSessionFactory().getCurrentSession().createQuery(hql)
                    .setString("categoryUri", categoryUri)
                    .setLong("siteId", siteId);

            result = (Long) query.uniqueResult();
        }

        //If result = null, find out base on categoryId
        if (result != null && result == 0) {
            if (categoryId != null && categoryId > 0) {
                hql = "select count(n) from News n join n.newsCategories c where c.id =:categoryId and c.site.id =:siteId  and n.active = 'Y'";
                query = getSessionFactory().getCurrentSession().createQuery(hql)
                        .setLong("categoryId", categoryId)
                        .setLong("siteId", siteId);

                result = (Long) query.uniqueResult();
            }
        }
        return result;
    }

    @Override
    public Long countNewsInNewsCategory(Long newsCategoryId, String active) {
        if ("Y".equals(active)) {
            return (Long) this.getSessionFactory().getCurrentSession().createQuery("select count(a.id) from News a join a.newsCategories b where a.active = :active and b.active = :active and b.id = :catId").
                    setParameter("active", active).
                    setParameter("catId", newsCategoryId).uniqueResult();
        } else {
            return (Long) this.getSessionFactory().getCurrentSession().createQuery("select count(a.id) from News a join a.newsCategories b where b.id = :catId").
                    setParameter("catId", newsCategoryId).uniqueResult();
        }
    }

    public List<NewsCategory> getNewsCategories(Site site, Long newsId, String active) {
        List<NewsCategory> menu;
        if ("Y".equals(active)) {
            menu = (List<NewsCategory>) this.getSessionFactory().getCurrentSession().createQuery("select a from NewsCategory a join a.newses r where a.site.id = :siteId and r.id = :newsId and a.active='Y'").
                    setParameter("siteId", site.getId()).
                    setParameter("newsId", newsId).list();
        } else {
            menu = (List<NewsCategory>) this.getSessionFactory().getCurrentSession().createQuery("select a from NewsCategory a join a.newses r where a.site.id = :siteId and r.id = :newsId").
                    setParameter("siteId", site.getId()).
                    setParameter("newsId", newsId).list();
        }

        return menu;
    }

}