package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.ProductDao;
import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.ProductToProduct;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.util.Messages;
import com.easysoft.ecommerce.util.WebUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.*;
import org.hibernate.search.FullTextSession;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * http://docs.jboss.org/hibernate/search/3.2/reference/en/html/search-query.html
 * http://www.javaworld.com/javaworld/jw-07-2008/jw-07-hibernate-search.html?page=6
 * http://www.ibm.com/developerworks/web/library/wa-lucene2/
 * http://sujitpal.blogspot.com/2007/01/faceted-searching-with-lucene.html
 */

@Repository
public class ProductDaoImpl extends GenericDaoImpl<Product, Long> implements ProductDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDaoImpl.class);

    @SuppressWarnings("unchecked")
    @Override
    public List<Product> findAll(Site site) {
        return getSessionFactory().getCurrentSession().createQuery(
                "select p from Product p where p.site.id = :siteId order by p.sequence")
                .setParameter("siteId", site.getId())
                .list();
    }
    @SuppressWarnings("unchecked")
    @Override
    public Product getModule(String model) {
        return (Product) getSessionFactory().getCurrentSession().createQuery(
                "select p from Product p where p.model = :model and p.active = 'Y' and p.module = 'Y'")
                .setParameter("model", model).uniqueResult();
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<Product> getModules(Long siteId) {
        return getSessionFactory().getCurrentSession().createQuery(
                "select p from Product p where p.active = 'Y' and p.module = 'Y' and p.site.id = :siteId")
                .setParameter("siteId", siteId).list();
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<Product> getRequiredModules() {
        return getSessionFactory().getCurrentSession().createQuery(
                "select p from Product p where p.module = 'Y' and p.required = 'Y' and p.active = 'Y'").list();
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<Product> getOptionModules() {
        return getSessionFactory().getCurrentSession().createQuery(
                "select p from Product p where p.module = 'Y' and p.required = 'N' and p.active = 'Y'").list();
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<Product> getAddedModules(Long siteId, Long clientSiteId) {
        return getSessionFactory().getCurrentSession().createQuery(
                "select p from SiteProductService s join s.product p where p.module = 'Y' and p.active = 'Y' and p.site.id = :siteId and s.site.id = :clientSiteId")
                .setParameter("siteId", siteId)
                .setParameter("clientSiteId", clientSiteId).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Product> getNotAddedModules(Long siteId, Long clientSiteId) {
        return getSessionFactory().getCurrentSession().createQuery(
                "select p from Product p where p.active = 'Y' and p.module = 'Y' and p.site.id = :siteId and p.id not in (select s.product.id from SiteProductService s where s.site.id = :clientSiteId)")
                .setParameter("siteId", siteId)
                .setParameter("clientSiteId", clientSiteId).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Product> searchProduct(Map input, Site site){
        return getSessionFactory().getCurrentSession().createQuery(
                "select p from Product p where p.site.id = :siteId and p.name like :name order by p.sequence")
                .setParameter("siteId", site.getId())
                .setParameter("name", "%"+input.get("keyword")+"%")
                .list();
    }

    @Override
    public Product createProduct(Product product) {
        return (Product) this.getSessionFactory().getCurrentSession().merge(product);
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Product> getProductBySubCategory(Long subCatId, int startPos, int maxResult, String isActive, String sortField, boolean reverse) {
        String query;
        if ("Y".equals(isActive)) {
            query = "select a from Product a join a.categories b where a.active = :active and b.active = :active and b.id = :catId ORDER BY  :orderField";
        } else {
            query = "select a from Product a join a.categories b where b.id = :catId ORDER BY  :orderField";
        }
        query = query.replaceFirst(":orderField", buildSortString(sortField, reverse));
        if ("Y".equals(isActive)) {
            return this.getSessionFactory().getCurrentSession().createQuery(query).
                    setParameter("active", isActive).
                    setParameter("catId", subCatId).
                    setFirstResult(startPos).setMaxResults(maxResult).list();
        } else {
            return this.getSessionFactory().getCurrentSession().createQuery(query).
                    setParameter("catId", subCatId).
                    setFirstResult(startPos).setMaxResults(maxResult).list();
        }
    }

    @Override
    public List<Product> getProductBySubCategory(Long subCatId, int startPos, int maxResult, String sortField, boolean reverse) {
        return getProductBySubCategory(subCatId, startPos, maxResult, "Y", sortField, reverse);
    }


    @Override
    public Long countProductBySubCategory(Long subCatId, String isActive) {
        if ("Y".equals(isActive)) {
            return (Long) this.getSessionFactory().getCurrentSession().createQuery("select count(a.id) from Product a join a.categories b where a.active = :active and b.active = :active and b.id = :catId").
                    setParameter("active", isActive).
                    setParameter("catId", subCatId).uniqueResult();
        } else {
            return (Long) this.getSessionFactory().getCurrentSession().createQuery("select count(a.id) from Product a join a.categories b where b.id = :catId").
                    setParameter("catId", subCatId).uniqueResult();
        }
    }

    @Override
    public Long countProductBySubCategory(Long subCatId) {
        return (Long) this.getSessionFactory().getCurrentSession().createQuery("select count(a.id) from Product a join a.categories b where a.active = :active and b.active = :active and b.id = :catId").
                setParameter("active", "Y").
                setParameter("catId", subCatId).uniqueResult();
    }

    private String buildSortString(String sortField, boolean reverse) {
        StringBuilder sort = new StringBuilder();
        if (StringUtils.isEmpty(sortField)) {
            sort.append("a.sequence asc");
        } else {
            if (reverse) {
                sort.append("a.").append(sortField).append(" desc");
            } else {
                sort.append("a.").append(sortField).append(" asc");
            }
        }
        return sort.toString();
    }

    @Override
    public Product getProduct(Long productId) throws Exception {
        return this.findById(productId);
    }

    /**
     * Using for recently view product
     *
     * @param productIds
     * @return Get list of products from list of ids.
     */
    @Override
    public List<Product> getProductsByIds(String productIds) {
        if (StringUtils.isNotBlank(productIds)) {
            String[] array = productIds.split(",");
            List<Long> ids = new ArrayList<Long>();
            for (String a : array) {
                ids.add(Long.valueOf(a));
            }
            //TODO: pass siteId
            return this.findObjectInBy(null, "id", ids, null, null, null, null);
        } else {
            return null;
        }
    }

    /**
     * Get 'numberOfProducts' promotion products
     *
     * @param numberOfProducts
     * @return
     */
    @Override
    public List<Product> getPromotionProducts(int numberOfProducts) {
        return this.getSessionFactory().getCurrentSession().createQuery("select a from Product a where a.displayPricePromo is not null and a.displayPricePromo != '' and a.active = 'Y'").setMaxResults(numberOfProducts).list();
    }


    /**
     * Get 'numberOfProducts' new products
     *
     * @param numberOfProducts
     * @return
     */
    @Override
    public List<Product> getNewProducts(int numberOfProducts) {
        return this.getSessionFactory().getCurrentSession().createQuery("select a from Product a where a.active = 'Y' order by a.createdDate desc").setMaxResults(numberOfProducts).list();
    }

    @Override
    public List<Product> getProductsRelatedProduct(Long productId) {
        return getRelatedProduct(productId, 1, "Y");
    }

    @Override
    public List<Product> getAccessoriesRelatedProduct(Long productId) {
        return getRelatedProduct(productId, 2, "Y");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Product> getRelatedProduct(Long productId, int relationType, String active) {
        if ("Y".equals(active)) {
            if (relationType > 0) {
                return this.getSessionFactory().getCurrentSession().createQuery("select b.relateProduct from Product a join a.relatedProducts b where b.relationType = :relationId and b.product.id = :productId and a.active = :active").
                        setParameter("active", active).
                        setParameter("relationId", relationType).
                        setParameter("productId", productId).list();
            } else {
                return this.getSessionFactory().getCurrentSession().createQuery("select b.relateProduct from Product a join a.relatedProducts b where b.product.id = :productId and a.active = :active").
                        setParameter("active", active).
                        setParameter("productId", productId).list();
            }
        } else {
            if (relationType > 0) {
                return this.getSessionFactory().getCurrentSession().createQuery("select b.relateProduct from Product a join a.relatedProducts b where b.relationType = :relationId and b.product.id = :productId").
                        setParameter("relationId", relationType).
                        setParameter("productId", productId).list();
            } else {
                return this.getSessionFactory().getCurrentSession().createQuery("select b.relateProduct from Product a join a.relatedProducts b where  b.product.id = :productId").
                        setParameter("productId", productId).list();
            }
        }
    }

    @Override
    public Product getParentProduct(Long relatedProductId, int relationType, String active) {
        if ("Y".equals(active)) {
                return (Product) this.getSessionFactory().getCurrentSession().createQuery("select a from Product a join a.relatedProducts b where b.relationType = :relationId and b.relateProduct.id = :relatedProductId and a.active = :active").
                        setParameter("active", active).
                        setParameter("relationId", relationType).
                        setParameter("relatedProductId", relatedProductId).uniqueResult();
        } else {
                return (Product) this.getSessionFactory().getCurrentSession().createQuery("select a from Product a join a.relatedProducts b where b.relationType = :relationId and b.product.id = :relatedProductId").
                        setParameter("relationId", relationType).
                        setParameter("relatedProductId", relatedProductId).uniqueResult();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProductToProduct> getRelateds(Long productId, int relationType) {
        if (relationType > 0) {
            return this.getSessionFactory().getCurrentSession().createQuery("select b from ProductToProduct b where b.relationType = :relationId and b.product.id = :productId").
                    setParameter("relationId", relationType).
                    setParameter("productId", productId).list();
        } else {
            return this.getSessionFactory().getCurrentSession().createQuery("select b from ProductToProduct b where b.product.id = :productId").
                    setParameter("productId", productId).list();
        }
    }

    @Override
    public boolean isProductExistedInCategory(Long categoryId, Long productId) {
        List result = this.getSessionFactory().getCurrentSession().createQuery("select a.id from Product a join a.categories b where a.id = :productId and b.id = :categoryId").
                setParameter("productId", productId).
                setParameter("categoryId", categoryId).list();
        return (result != null && result.size() > 0); 
    }

    @Override
    public ProductToProduct getRelated(Long productId, Long relatedProductId) {
        return (ProductToProduct) this.getSessionFactory().getCurrentSession().createQuery("select b from ProductToProduct b where b.relateProduct.id = :relatedProductId and b.product.id = :productId").
                setParameter("relatedProductId", relatedProductId).
                setParameter("productId", productId).uniqueResult();
    }

    @Override
    public Messages createOrUpdate(Product entity) throws Exception {
        Messages messages = new Messages();

        Date date = new Date();
        entity.setUpdatedDate(date);

        if (entity.getId() == null) {
            entity.setCreatedDate(date);
            this.persist(entity);
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
        } else {
            Product original = this.findById(entity.getId());
            boolean isChange = false;
            if (!entity.getName().equals(original.getName())) {
                original.setName(entity.getName());
                isChange = true;
            }
            if (!entity.getUri().equals(original.getUri())) {
                original.setUri(entity.getUri());
                isChange = true;
            }
            if (entity.getActive().equals(original.getActive())) {
                if ("Y".equals(entity.getActive())) {
                    original.setActive(entity.getActive());
                } else {
                    original.setActive(entity.getActive());
                }
                isChange = true;
            }

            if (isChange) {
                original.setUpdatedDate(entity.getUpdatedDate());
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            } else {
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
            }
        }
        return messages;
    }

    @Override
    //http://seamframework.org/56511.lace
    public void rebuildIndex(Class... entityTypes) {
        FullTextSession fullTextSession = getFullTextSession();

        int BATCH_SIZE = 500;
        fullTextSession.setFlushMode(FlushMode.MANUAL);
        fullTextSession.setCacheMode(CacheMode.IGNORE);
        for (Class entityType : entityTypes) {
            fullTextSession.purgeAll(entityType);//remove old entities from index.
            //Scrollable results will avoid loading too many objects in memory
            ScrollableResults results = fullTextSession.createCriteria(entityType).setFetchSize(BATCH_SIZE).scroll(ScrollMode.FORWARD_ONLY);
            int index = 0;
            while (results.next()) {
                index++;
                fullTextSession.index(results.get(0)); //index each element
                if (index % BATCH_SIZE == 0) {
                    fullTextSession.flushToIndexes(); //apply changes to indexes
                    fullTextSession.clear(); //free memory since the queue is processed
                }
            }

            try {
                results.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
        }
    }

    /**
     * This method will build priceMin (using for sort/filter) and displayPrice using for display price on the website. This method
     * will be called period of time to make sure the products will be synchronized when product data change. Here is data that can be affected.
     * 1. priceMin
     * 2. displayPrice
     * 3. displayPricePromo
     * 4. Rebuild index
     */
    @Override
    public List<String> updateProductPrice() {
        List<String> messages = new ArrayList<String>();
        String sql = "select product_id, min(price) minPrice, max(price) maxPrice, min(pricePromo) minPricePromo, max(pricePromo) maxPricePromo from product_variant where active='Y' and inventory >= 0 group by product_id";
        SQLQuery query = this.getSessionFactory().getCurrentSession().createSQLQuery(sql)
                .addScalar("product_id", StandardBasicTypes.LONG)
                .addScalar("minPrice", StandardBasicTypes.LONG)
                .addScalar("maxPrice", StandardBasicTypes.LONG)
                .addScalar("minPricePromo", StandardBasicTypes.LONG)
                .addScalar("maxPricePromo", StandardBasicTypes.LONG);
        List<Object[]> rs = (List<Object[]>) query.list();
        Session session = this.getSessionFactory().getCurrentSession();

        String variantSql = "select price , pricePromo from product_variant where active='Y' and inventory >= 0 and product_id = :productId";
        SQLQuery variantQuery = this.getSessionFactory().getCurrentSession().createSQLQuery(variantSql).addScalar("price", StandardBasicTypes.LONG).addScalar("pricePromo", StandardBasicTypes.LONG);

        for (int i = 0; i < rs.size(); i++) {
            Object[] obj = rs.get(i);
            Long prodId = (Long) obj[0];
            Long minPrice = (Long) obj[1];
            Long maxPrice = (Long) obj[2];
            Product prod = this.findById(prodId);
            List<Object[]> var = (List<Object[]>) variantQuery.setParameter("productId", prodId).list();
            //Handle the case minPrice = 0. We need log this issue and job will be fail. This is invalid, product cannot have price = 0
//            if (minPrice == 0) {
//                prod.setActive("N");
//                LOGGER.error("ProductId="+prodId+" - Handle the case minPrice = 0. We need log this issue and job will be fail. This is invalid, product cannot have price = 0");
//                messages.add("ProductId="+prodId+" - Handle the case minPrice = 0. We need log this issue and job will be fail. This is invalid, product cannot have price = 0");
//            }
            String displayPrice = WebUtil.generateDisplayPrice(minPrice, maxPrice);
            String displayPricePromo = WebUtil.generateDisplayPromoPrice(var);
            prod.setDisplayPrice(displayPrice);
            prod.setDisplayPricePromo(displayPricePromo);
            prod.setPriceMin(WebUtil.generatePriceMin(displayPrice, displayPricePromo));

            //this.getSessionFactory().getCurrentSession().merge(prod);
            //Batch seem instatble
            session.save(prod);
            if (i % 100 == 0) { //100, same as the JDBC batch size
                //flush a batch of inserts and release memory:
                session.flush();
                session.clear();
            }
        }
        //flush a batch of inserts and release memory of the rest items
        session.flush();
        session.clear();
        rebuildIndex(Product.class);
        return messages;
    }
    @Override
    public List<String> updateProductPrice(Long productId) {
        List<String> messages = new ArrayList<String>();
        String sql = "select product_id, min(price) minPrice, max(price) maxPrice, min(pricePromo) minPricePromo, max(pricePromo) maxPricePromo from product_variant where product_id = :productId and active='Y' and inventory >= 0 group by product_id";
        SQLQuery query = this.getSessionFactory().getCurrentSession().createSQLQuery(sql)
                .addScalar("product_id", StandardBasicTypes.LONG)
                .addScalar("minPrice", StandardBasicTypes.LONG)
                .addScalar("maxPrice", StandardBasicTypes.LONG)
                .addScalar("minPricePromo", StandardBasicTypes.LONG)
                .addScalar("maxPricePromo", StandardBasicTypes.LONG);
        List<Object[]> rs = (List<Object[]>) query.setParameter("productId", productId).list();

        String variantSql = "select price , pricePromo from product_variant where active='Y' and inventory >= 0 and product_id = :productId";
        SQLQuery variantQuery = this.getSessionFactory().getCurrentSession().createSQLQuery(variantSql).addScalar("price", StandardBasicTypes.LONG).addScalar("pricePromo", StandardBasicTypes.LONG);

        Product prod = this.findById(productId);
        for (Object[] obj : rs) {
            Long prodId = (Long) obj[0];
            Long minPrice = (Long) obj[1];
            Long maxPrice = (Long) obj[2];
            List<Object[]> var = (List<Object[]>) variantQuery.setParameter("productId", prodId).list();
            String displayPrice = WebUtil.generateDisplayPrice(minPrice, maxPrice);
            String displayPricePromo = WebUtil.generateDisplayPromoPrice(var);
            prod.setDisplayPrice(displayPrice);
            prod.setDisplayPricePromo(displayPricePromo);
            prod.setPriceMin(WebUtil.generatePriceMin(displayPrice, displayPricePromo));

        }
        this.getSessionFactory().getCurrentSession().merge(prod);
        return messages;
    }

    /**
     * 1. Reset pricePromote = 0 if end date was expired.
     * 2. Update priceMin, displayPrice, displayPricePromo and update into Product table. 
     * @return
     */
    @Override
    public List<String> runPromoPriceJob() {
        /*Remove promotion price if any*/
        int i = this.getSessionFactory().getCurrentSession().createQuery("update ProductVariant a set a.pricePromo = 0  where a.pricePromo > 0 and a.promoEndDate < :currentDate").
                setParameter("currentDate", new Date()).executeUpdate();
        /*set price & promotion for display in the front-end*/
        return updateProductPrice();

    }

    @Override
    public FullTextSession getFullTextSearchSession() {
        return super.getFullTextSession();
    }

}
