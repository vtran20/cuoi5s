package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.CategoryDao;
import com.easysoft.ecommerce.model.Category;
import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.Site;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class CategoryDaoImpl extends GenericDaoImpl<Category, Long> implements CategoryDao {

    @SuppressWarnings("unchecked")
    @Override
    public List<Category> getRootCategories(Site site, boolean active) {
        Query query;
        String sqlActive = "SELECT c FROM Category c where c.site.id = :siteId and c.parentCategory = null ";
        if (site != null) {
            if (active) {
                sqlActive += " and c.active = :active ";
            }
            sqlActive += " ORDER BY c.sequence ";
            query = getSessionFactory().getCurrentSession().createQuery(sqlActive)
                    .setParameter("siteId", site.getId());
            if (active) {
                query = query.setParameter("active", "Y");
            }
            return query.setCacheable(true).list();
        } else {
            return null;
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public Category getParentCategory(Long catId) {
        return (Category) getSessionFactory().getCurrentSession().createQuery("select c.parentCategory from Category c where c.id = ?")
                .setParameter(0, catId).uniqueResult();
    }

    @Override
    public List getCategoriesByProdIds(String prodIds) {
        return getSessionFactory().getCurrentSession().createQuery("select distinct a, count(b.id) from Category a join a.products b where a.active = :active and b.active = :active and b.id in (" + prodIds + ") group by a order by a.sequence")
                .setParameter("active", "Y").list();
    }

    @Override
    public Category getSubCategory(Long prodId) {
        List result = getSubCategories(prodId);
        if (result != null && result.size() > 0) {
            return (Category) result.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Category> getSubCategories(Long prodId) {
        return getSubCategories(prodId, "Y");
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<Category> getSubCategories(Long prodId, String active) {
        if ("Y".equals(active)) {
            return getSessionFactory().getCurrentSession().createQuery("select a from Category a join a.products b where a.active = :active and b.active = :active and b.id = :prodId order by a.sequence")
                    .setParameter("active", "Y")
                    .setParameter("prodId", prodId).list();
        } else {
            return getSessionFactory().getCurrentSession().createQuery("select a from Category a join a.products b where b.id = :prodId order by a.sequence")
                    .setParameter("prodId", prodId).list();
        }
    }

    @Override
    public List<Category> getSubCategoriesBySite(Site site, boolean active) {
        List<Category> categories = null;

        if (active) {
            //Get all subcategories level 2
            categories = getSessionFactory().getCurrentSession().createQuery("select distinct a from Category a where a.parentCategory in (select c from Site s join s.catalogs o join o.categories c where s.id=:siteId) and a.active=:active")
                    .setParameter("siteId", site.getId())
                    .setParameter("active", "Y")
                    .setCacheable(true).list();
            //Get all subcategories level 1
            List<Category> categories1 = getSessionFactory().getCurrentSession().createQuery("select distinct c from Site s join s.catalogs o join o.categories c join c.products p where c.active = :active and s.id=:siteId")
                    .setParameter("siteId", site.getId())
                    .setParameter("active", "Y")
                    .setCacheable(true).list();
            if (categories != null && categories.size() > 0) {
                categories.addAll(categories1);
            } else {
                categories = categories1;
            }
        } else {
            //Get all subcategories level 2
            categories = getSessionFactory().getCurrentSession().createQuery("select distinct c from Category where c.parentCategory in (select c from Site s join s.catalogs o join o.categories c where s.id=:siteId)")
                    .setParameter("siteId", site.getId())
                    .setCacheable(true).list();
            //Get all subcategories level 1
            List<Category> categories1 = getSessionFactory().getCurrentSession().createQuery("select distinct c from Site s join s.catalogs o join o.categories c join c.products p where s.id=:siteId")
                    .setParameter("siteId", site.getId())
                    .setCacheable(true).list();
            if (categories != null && categories.size() > 0) {
                categories.addAll(categories1);
            } else {
                categories = categories1;
            }
        }
        return categories;
    }

    @Override
    public void addProductToCategory(Long catId, Long prodId) {
        Category cat = this.findById(catId);
        Product prod = (Product) getSessionFactory().getCurrentSession().get(Product.class, prodId);
        cat.addProduct(prod);
    }

    @Override
    public void removeProductCategory(Long categoryId, Long productId) {
        Category cat = this.findById(categoryId);
        Product prod = (Product) getSessionFactory().getCurrentSession().get(Product.class, productId);
        cat.removeProduct(prod);
    }

    /**
     * Remove relationship between category and product
     * @param productId
     * @param site
     */
    @Override
    public void removeProductCategory(Long productId, Site site) {
        List<Category> categories = getSessionFactory().getCurrentSession().createQuery("select c from Category c join c.products p where p.id = :productId and p.site.id = :siteId")
                .setParameter("productId", productId).setParameter("siteId", site.getId()).list();
        Product product = (Product) getSessionFactory().getCurrentSession().get(Product.class, productId);
        for (Category cat: categories) {
            cat.removeProduct(product);
        }
    }

    @Override
    public void removeCategory(Long categoryId, Site site) {
        
    }

    @Override
    public void addSubCategory(Long catId, Long subCatId) {
        Category cat = this.findById(catId);
        Category subCat = this.findById(subCatId);
        cat.addSubCategory(subCat);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Category> getAllCategoriesExclude(List<Category> categories, Long catalogId) {
        if (categories != null && !categories.isEmpty()) {
            Collection catIds = new ArrayList();
            for (Category cat : categories) {
                catIds.add(cat.getId());
            }
            Query query = getSessionFactory().getCurrentSession().createQuery("select c from Category c join c.catalogs o where c.id not in (:catIds) and o.id = :catalogId order by c.sequence")
                    .setParameterList("catIds", catIds)
                    .setParameter("catalogId", catalogId);
            return query.list();
        } else {
            return this.getAllCategoriesFromCatalog(catalogId, false);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Category> getRootCategoriesByCatalog(Long catalogId) {
        Query query = getSessionFactory().getCurrentSession()
                .createQuery("SELECT c FROM Category c join c.catalogs o where o.id = ? ORDER BY c.sequence")
                .setParameter(0, catalogId);
        return query.setCacheable(true).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List <Category> getAllCategoriesFromCatalog(Long catalogId, boolean active) {
        if (active) {
            return getSessionFactory().getCurrentSession()
                    .createQuery("SELECT c FROM Category c join c.catalogs o where o.id = :catalogId and c.active = :active ORDER BY c.sequence")
                    .setParameter("catalogId", catalogId)
                    .setParameter("active", "Y").setCacheable(true).list();
        } else {
            return getSessionFactory().getCurrentSession()
                    .createQuery("SELECT c FROM Category c join c.catalogs o where o.id = :catalogId ORDER BY c.sequence")
                    .setParameter("catalogId", catalogId)
                    .setCacheable(true).list();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Category> getRootCategories(Site site, Long catalogId, boolean active) {
        Query query;
        String sqlCatalog = "";
        String sqlActive = "";
        if (catalogId != null && catalogId > 0) {
            sqlCatalog = " and d.id = :catalogId ";
        }
        if (active) {
            sqlActive = " and c.active = :active ";
        }
        if (site != null) {
            query = getSessionFactory().getCurrentSession()
                    .createQuery("SELECT c FROM Category c join c.catalogs d join d.sites s where s.id = :siteId and c.parentCategory = null "+sqlCatalog + sqlActive+"  ORDER BY c.sequence")
                    .setParameter("siteId", site.getId());
        } else {
            query = getSessionFactory().getCurrentSession()
                    .createQuery("SELECT c FROM Category c join c.catalogs d join d.sites s where c.parentCategory = null "+sqlCatalog + sqlActive+" ORDER BY sequence");
        }
        if (catalogId != null && catalogId > 0) {
            query = query.setParameter("catalogId", catalogId);
        }
        if (active) {
            query = query.setParameter("active", "Y");
        }
        return query.setCacheable(true).list();
    }

}
