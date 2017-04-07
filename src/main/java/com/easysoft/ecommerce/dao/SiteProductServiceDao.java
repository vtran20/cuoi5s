package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.ProductVariant;
import com.easysoft.ecommerce.model.SiteProductService;

public interface SiteProductServiceDao extends GenericDao<SiteProductService, Long> {
    ProductVariant getProductVariant (Long productId, Long siteId);
    SiteProductService getSiteProductService(Long productId, Long siteId);
    SiteProductService getSiteProductService(String model, Long siteId);
}