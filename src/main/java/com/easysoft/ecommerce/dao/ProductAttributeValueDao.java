package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.ProductAttributeValue;

import java.util.List;

public interface ProductAttributeValueDao extends GenericDao<ProductAttributeValue, Long> {
    List<ProductAttributeValue> getProductAttributeValues(Long productId);
}