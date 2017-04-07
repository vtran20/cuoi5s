package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.ProductAttributeGroupDao;
import com.easysoft.ecommerce.model.ProductAttributeGroup;
import org.springframework.stereotype.Repository;

@Repository
public class ProductAttributeGroupDaoImpl extends GenericDaoImpl<ProductAttributeGroup, Long> implements ProductAttributeGroupDao {

//    @SuppressWarnings("unchecked")
//    @Override
//    public Map<String, String> getSiteParamsBySite(Site site) {
//        Map<String, String> result = new HashMap<String, String>();
//
//        Query query;
//        if (site != null) {
//                query = this.getSessionFactory().getCurrentSession()
//                .createQuery("SELECT o FROM SiteParam o where o.siteId = ?")
//                .setParameter(0, site.getId());
//        } else {
//                query = this.getSessionFactory().getCurrentSession()
//                .createQuery("SELECT o FROM SiteParam o");
//        }
//        List<SiteParam> results = query.setCacheable(true).list();
//        for (SiteParam p : results) {
//            result.put(p.getKey(), p.getValue());
//        }
//
//        return result;
//    }
}