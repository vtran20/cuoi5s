package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.ProductVariant;
import com.easysoft.ecommerce.model.Site;

import java.util.List;
import java.util.Map;

public interface ProductVariantDao extends GenericDao<ProductVariant, Long> {
    List<ProductVariant> findAll(Long productId);
    List<ProductVariant> findAll(Long productId, String active);
    List<ProductVariant> findProductVariantByColorSize(Long productId, String active);
    ProductVariant getProductVariantDefault(Long productId);
}