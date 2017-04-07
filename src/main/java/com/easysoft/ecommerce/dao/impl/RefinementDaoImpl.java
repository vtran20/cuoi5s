package com.easysoft.ecommerce.dao.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.easysoft.ecommerce.model.RefinementValue;
import org.springframework.stereotype.Repository;

import com.easysoft.ecommerce.dao.RefinementDao;
import com.easysoft.ecommerce.model.Refinement;
import com.easysoft.ecommerce.model.Site;

@Repository
public class RefinementDaoImpl extends GenericDaoImpl<Refinement, Long> implements RefinementDao {


    @SuppressWarnings("unchecked")
    @Override
    public Map getAllRefinements(Site site, Long categoryId) {
        Map results = new LinkedHashMap ();

        results.putAll(getRefinements (site));
        results.putAll(getRefinements (categoryId));

        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map getRefinements(Site site) {
        Map results = new LinkedHashMap ();

        List <Refinement> list = this.getSessionFactory().getCurrentSession().createQuery("select a from Refinement a where a.active = 'Y' and a.refinementScope = 'SITE' order by a.sequence")
                .setCacheable(true)
                .list();
        for (Refinement refinement: list) {
            List <RefinementValue> refinementValues = getRefinementValues(refinement.getId(), site, true);
            if (refinementValues != null && !refinementValues.isEmpty()) {
                results.put(refinement, refinementValues);
            }
        }

        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map getRefinements (Long categoryId) {
        Map results = new LinkedHashMap ();

        List <Refinement> list = this.getSessionFactory().getCurrentSession().createQuery("select a from Refinement a where a.active = 'Y' and a.refinementScope = 'CATEGORY' order by a.sequence")
                .setCacheable(true)
                .list();

        for (Refinement refinement: list) {
            List <RefinementValue> refinementValues = getRefinementValues(refinement.getId(), categoryId, true);
            if (refinementValues != null && !refinementValues.isEmpty()) {
                results.put(refinement, refinementValues);
            }
        }

        return results;
        
    }

    /**
     * Get refinement values for Category.
     *
     * @param refinementId
     * @param site
     * @param isActive
     * @return
     */
    @SuppressWarnings("unchecked")
    public List <RefinementValue> getRefinementValues (Long refinementId, Site site, Boolean isActive) {
        List <RefinementValue> results;
        if (isActive) {
            results = this.getSessionFactory().getCurrentSession().createQuery("select a from RefinementValue a where a.site.id = :siteId and a.refinement.id = :refinementId and a.active = :active order by a.sequence").
                    setParameter("siteId",site.getId()).
                    setParameter("refinementId",refinementId).
                    setParameter("active","Y")
                    .setCacheable(true)
                    .list();
        } else {
            results = this.getSessionFactory().getCurrentSession().createQuery("select a from RefinementValue a where a.site.id = :siteId and a.refinement.id = :refinementId order by a.sequence").
                    setParameter("siteId",site.getId()).
                    setParameter("refinementId",refinementId)
                    .setCacheable(true)
                    .list();
        }
        return results;
    }

    /**
     * Get refinement values for Category.
     *
     * @param refinementId
     * @param categoryId
     * @param isActive
     * @return
     */
    @SuppressWarnings("unchecked")
    public List <RefinementValue> getRefinementValues (Long refinementId, Long categoryId, Boolean isActive) {
        List <RefinementValue> results;
        if (isActive) {
            results = this.getSessionFactory().getCurrentSession().createQuery("select a from RefinementValue a where a.category.id = :categoryId and a.refinement.id = :refinementId and a.active = :active order by a.sequence").
                    setParameter("categoryId",categoryId).
                    setParameter("refinementId",refinementId).
                    setParameter("active","Y")
                    .setCacheable(true)
                    .list();
        } else {
            results = this.getSessionFactory().getCurrentSession().createQuery("select a from RefinementValue a where a.category.id = :categoryId and a.refinement.id = :refinementId order by a.sequence").
                    setParameter("categoryId",categoryId).
                    setParameter("refinementId",refinementId)
                    .setCacheable(true)
                    .list();
        }
        return results;
    }


}