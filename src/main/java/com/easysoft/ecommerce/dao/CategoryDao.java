package com.easysoft.ecommerce.dao;

import java.util.List;

import com.easysoft.ecommerce.model.Category;
import com.easysoft.ecommerce.model.Site;

public interface CategoryDao extends GenericDao<Category, Long> {

    List<Category> getRootCategories(Site site, Long catalogId, boolean active);
    List<Category> getRootCategories(Site site, boolean active);
    Category getSubCategory(Long prodId);

    List <Category> getSubCategories(Long prodId);
    List <Category> getSubCategories(Long prodId, String active);
    List <Category> getSubCategoriesBySite(Site site, boolean active);

    Category getParentCategory(Long catId);
    
    List<Category> getCategoriesByProdIds(String prodIds);

    void addProductToCategory (Long catId, Long prodId);

    void addSubCategory (Long catId, Long subCatId);

    List <Category> getAllCategoriesExclude (List <Category>categories, Long catalogId);

    List <Category> getRootCategoriesByCatalog (Long catalogId);

    List <Category> getAllCategoriesFromCatalog(Long catalogId, boolean active);

    void removeProductCategory (Long categoryId, Long productId);
    void removeProductCategory (Long productId, Site site);
    void removeCategory (Long categoryId, Site site);
}
