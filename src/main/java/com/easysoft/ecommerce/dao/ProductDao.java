package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.ProductToProduct;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.util.Messages;
import org.hibernate.search.FullTextSession;

import java.util.List;
import java.util.Map;

public interface ProductDao extends GenericDao<Product, Long> {

    public static int PRODUCT_RELATED_PRODUCT = 1;
    public static int ACCESSORY_RELATED_PRODUCT = 2;

    List<Product> findAll(Site site);

    Product getModule(String model);
    List<Product> getModules(Long siteId);
    List<Product> getRequiredModules();
    List<Product> getOptionModules();
    List<Product> getAddedModules(Long siteId, Long clientSiteId);
    List<Product> getNotAddedModules(Long siteId, Long clientSiteId);

    Product createProduct (Product product);

    List getProductBySubCategory(Long subCatId, int startPos, int maxResult, String isActive, String sortField, boolean reverse);

    List <Product>getProductBySubCategory(Long subCatId, int startPos, int maxResult, String sortField, boolean reverse);

    Long countProductBySubCategory(Long subCatId, String isActive);

    Long countProductBySubCategory(Long subCatId);

    Product getProduct(Long productId) throws Exception;

    List<Product> getProductsByIds(String productIds);

    List <Product> getProductsRelatedProduct (Long productId);

    List <Product> getAccessoriesRelatedProduct (Long productId);

    List<Product> getRelatedProduct (Long productId, int relationType, String active);
    Product getParentProduct (Long relatedProductId, int relationType, String active);

    List<ProductToProduct> getRelateds(Long productId, int relationType);

    boolean isProductExistedInCategory(Long categoryId, Long productId);

    ProductToProduct getRelated(Long productId, Long relatedProductId);

    Messages createOrUpdate(Product entity) throws Exception;

    FullTextSession getFullTextSearchSession();

    List<String> runPromoPriceJob ();

    void rebuildIndex(Class... entityTypes);

    List<String> updateProductPrice ();

    List<String> updateProductPrice(Long productId);

    List<Product> getPromotionProducts(int numberOfProducts);

    List<Product> getNewProducts(int numberOfProducts);

    List<Product> searchProduct(Map input, Site site);
}