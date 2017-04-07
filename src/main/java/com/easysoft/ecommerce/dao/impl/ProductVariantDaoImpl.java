package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.ProductVariantDao;
import com.easysoft.ecommerce.model.ProductVariant;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductVariantDaoImpl extends GenericDaoImpl<ProductVariant, Long> implements ProductVariantDao {

    @SuppressWarnings("unchecked")
    @Override
    public List<ProductVariant> findAll(Long productId) {
        return findAll(productId, "Y");
    }

    @SuppressWarnings("unchecked")
    public List<ProductVariant> findAll(Long productId, String active) {
        if ("Y".equals(active)) {
            return getSessionFactory().getCurrentSession().createQuery(
                    "select o from ProductVariant o where o.product.id = :productId and o.active = :active order by o.sequence")
                    .setParameter("productId", productId)
                    .setParameter("active", active)
                    .list();
        } else {
            return getSessionFactory().getCurrentSession().createQuery(
                    "select o from ProductVariant o where o.product.id = :productId order by o.sequence")
                    .setParameter("productId", productId)
                    .list();
        }
    }

    @SuppressWarnings("unchecked")
    public List<ProductVariant> findProductVariantByColorSize(Long productId, String active) {
        return getSessionFactory().getCurrentSession().createQuery(
                "select o from ProductVariant o where o.product.id = :productId and o.active = :active order by o.colorCode, o.sizeCode")
                .setParameter("productId", productId)
                .setParameter("active", active)
                .list();
    }

    public ProductVariant getProductVariantDefault(Long productId) {
        List<ProductVariant> variants =  getSessionFactory().getCurrentSession().createQuery(
                "select o from ProductVariant o where o.product.id = :productId and o.active = 'Y' and o.default = 'Y'")
                .setParameter("productId", productId).list();
        if (variants != null && variants.size() > 0) {
            return variants.get(0);
        } else {
            return null;
        }
    }
}
