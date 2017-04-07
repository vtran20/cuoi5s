package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.SiteProductServiceDao;
import com.easysoft.ecommerce.model.ProductVariant;
import com.easysoft.ecommerce.model.SiteProductService;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SiteProductServiceDaoImpl extends GenericDaoImpl<SiteProductService, Long> implements SiteProductServiceDao {
    @Override
    public ProductVariant getProductVariant(Long productId, Long siteId) {
        List list = getSessionFactory().getCurrentSession().createQuery(
                "select v from SiteProductService o join o.productVariant v where o.product.id = :productId and o.active = 'Y' and o.site.id = :siteId")
                .setParameter("productId", productId)
                .setParameter("siteId", siteId)
                .list();
        if (list != null && list.size() > 0) {
            return (ProductVariant) list.get(0);
        }
        return null;
    }
    @Override
    public SiteProductService getSiteProductService(Long productId, Long siteId) {
        List list = getSessionFactory().getCurrentSession().createQuery(
                "select o from SiteProductService o where o.product.id = :productId and o.active = 'Y' and o.site.id = :siteId")
                .setParameter("productId", productId)
                .setParameter("siteId", siteId)
                .list();
        if (list != null && list.size() > 0) {
            return (SiteProductService) list.get(0);
        }
        return null;
    }
    @Override
    public SiteProductService getSiteProductService(String model, Long siteId) {
        List list =  getSessionFactory().getCurrentSession().createQuery(
                "select o from SiteProductService o join o.product p where p.model = :model and o.active = 'Y' and o.site.id = :siteId")
                .setParameter("model", model)
                .setParameter("siteId", siteId)
                .list();
        if (list != null && list.size() > 0) {
            return (SiteProductService) list.get(0);
        }
        return null;
    }
}
