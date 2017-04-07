package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.CatalogDao;
import com.easysoft.ecommerce.model.Catalog;
import com.easysoft.ecommerce.model.Site;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CatalogDaoImpl extends GenericDaoImpl<Catalog, Long> implements CatalogDao {


    @SuppressWarnings("unchecked")
    @Override
    public List<Catalog> getAllCatalogsBySite(Site site, boolean active) {
        if (active) {
            return getSessionFactory().getCurrentSession().createQuery(
                    "select o from " + getPersistentClass().getName() + " o join o.sites s where s.id = :siteId and o.active = :active")
                    .setParameter("siteId", site.getId())
                    .setParameter("active", active)
                    .setCacheable(true)
                    .list();
        } else {
            return getSessionFactory().getCurrentSession().createQuery(
                    "select o from " + getPersistentClass().getName() + " o join o.sites s where s.id = :siteId")
                    .setParameter("siteId", site.getId())
                    .setCacheable(true)
                    .list();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Catalog getFirstCatalogsBySite(Site site, boolean active) {
        if (active) {
            return (Catalog) getSessionFactory().getCurrentSession().createQuery(
                    "select o from " + getPersistentClass().getName() + " o join o.sites s where s.id = :siteId and o.active = :active")
                    .setParameter("siteId", site.getId())
                    .setParameter("active", active)
                    .setCacheable(true)
                    .uniqueResult();
        } else {
            return (Catalog) getSessionFactory().getCurrentSession().createQuery(
                    "select o from " + getPersistentClass().getName() + " o join o.sites s where s.id = :siteId")
                    .setParameter("siteId", site.getId())
                    .setCacheable(true)
                    .uniqueResult();
        }
    }

    @Override
    public List<Catalog> getAllCatalogsBySite(Site site) {
        return getAllCatalogsBySite(site, true);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Catalog> getCatalogFromCategory(Long categoryId, boolean active) {
        if (active) {
            return getSessionFactory().getCurrentSession().createQuery(
                    "select o from " + getPersistentClass().getName() + " o join o.categories a where a.id = :categoryId and o.active = :active")
                    .setParameter("categoryId", categoryId)
                    .setParameter("active", active)
                    .setCacheable(true)
                    .list();
        } else {
            return  getSessionFactory().getCurrentSession().createQuery(
                    "select o from " + getPersistentClass().getName() + " o join o.categories a where a.id = :categoryId")
                    .setParameter("categoryId", categoryId)
                    .setCacheable(true)
                    .list();
        }
    }


}
