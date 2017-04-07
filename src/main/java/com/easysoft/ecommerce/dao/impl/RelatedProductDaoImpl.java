package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.ProductVariantDao;
import com.easysoft.ecommerce.dao.RelatedProductDao;
import com.easysoft.ecommerce.model.ProductToProduct;
import com.easysoft.ecommerce.model.ProductVariant;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RelatedProductDaoImpl extends GenericDaoImpl<ProductToProduct, Long> implements RelatedProductDao {

    @Override
    public int removeAll() {
        return this.getSessionFactory().getCurrentSession().createQuery("delete from ProductToProduct").executeUpdate();
    }

    @Override
    public int removeByProductId(Long productId) {
        return this.getSessionFactory().getCurrentSession().createQuery("delete from ProductToProduct p where p.product.id = :productId").setParameter("productId", productId).executeUpdate();
    }
}
