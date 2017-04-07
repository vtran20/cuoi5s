package com.easysoft.ecommerce.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.easysoft.ecommerce.dao.SiteParamDao;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.SiteParam;

@Repository
public class SiteParamDaoImpl extends GenericDaoImpl<SiteParam, Long> implements SiteParamDao {

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> getSiteParamsBySite(Site site) {
        Map<String, String> result = new HashMap<String, String>();

        Query query;
        if (site != null) {
                query = this.getSessionFactory().getCurrentSession()
                .createQuery("SELECT o FROM SiteParam o where o.siteId = ?")
                .setParameter(0, site.getId());
        } else {
                query = this.getSessionFactory().getCurrentSession()
                .createQuery("SELECT o FROM SiteParam o");
        }
        List<SiteParam> results = query.setCacheable(true).list();
        for (SiteParam p : results) {
            result.put(p.getKey(), p.getValue());
        }

        return result;
    }
}