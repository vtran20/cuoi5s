package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.Catalog;
import com.easysoft.ecommerce.model.Category;
import com.easysoft.ecommerce.model.Site;

import java.util.List;

public interface CatalogDao extends GenericDao<Catalog, Long> {
    List<Catalog> getAllCatalogsBySite (Site site, boolean active);
    Catalog getFirstCatalogsBySite(Site site, boolean active);
    List<Catalog> getAllCatalogsBySite (Site site);
    List<Catalog> getCatalogFromCategory(Long categoryId, boolean active);
}
