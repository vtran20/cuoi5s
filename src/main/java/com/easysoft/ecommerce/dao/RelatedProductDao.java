package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.ProductToProduct;
import com.easysoft.ecommerce.model.ProductVariant;

import java.util.List;

public interface RelatedProductDao extends GenericDao<ProductToProduct, Long> {
    int removeAll();
    int removeByProductId(Long productId);
}