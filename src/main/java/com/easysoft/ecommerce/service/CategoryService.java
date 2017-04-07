package com.easysoft.ecommerce.service;

import com.easysoft.ecommerce.model.Category;
import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.ProductVariant;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.util.Messages;

import java.util.List;


public interface CategoryService {

    Category getCategory (Long categoryId) throws Exception;

    void addSubCategories (Long catId, Long subCatId) throws Exception;

//    List<Category> getSubCategories(Long catId) throws Exception;

    Messages addProductToCategory(Long catId, Product product) throws Exception;

    Messages addProductToCategory(Long[] catIds, Product product) throws Exception;

    Messages addProductToCategory(Long catId, Long prodId) throws Exception;

    List<Category> findAll() throws Exception;

    Messages createOrUpdate(Category entity, Long parentCatId) throws Exception;

    Messages createOrUpdate(Product entity, ProductVariant productVariant, Long categoryIds[]) throws Exception;

    Messages deleteProduct(Long productId) throws Exception;

    void remove(Category entity, Site site) throws Exception;

    Messages addVariant (ProductVariant productVariant, Long productId);
}

