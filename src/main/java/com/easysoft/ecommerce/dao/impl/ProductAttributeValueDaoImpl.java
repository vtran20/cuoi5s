package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.ProductAttributeValueDao;
import com.easysoft.ecommerce.model.ProductAttributeValue;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductAttributeValueDaoImpl extends GenericDaoImpl<ProductAttributeValue, Long> implements ProductAttributeValueDao {

    @SuppressWarnings("unchecked")
    @Override
    public List<ProductAttributeValue> getProductAttributeValues(Long productId) {
        return  this.getSessionFactory().getCurrentSession()
                .createQuery("SELECT o FROM ProductAttributeValue o where o.product.id = :productId")
                .setParameter("productId", productId).list();
    }
}