package com.easysoft.ecommerce.service.impl;

import com.easysoft.ecommerce.dao.*;
import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.service.SearchService;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Version;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/*example http://affy.blogspot.com/2003/04/codebit-examples-for-all-of-lucenes.html*/
@Service
//@Transactional
public class SearchServiceImpl implements SearchService {

    private CategoryDao categoryDao;
    private ProductDao productDao;
    private KeywordDao keywordDao;
    private SiteDao siteDao;
    private CatalogDao catalogDao;
    private RefinementDao refinementDao;

    @Autowired
    public SearchServiceImpl(
            CategoryDao categoryDao,
            SiteDao siteDao,
            ProductDao productDao,
            KeywordDao keywordDao,
            CatalogDao catalogDao,
            RefinementDao  refinementDao) {
        super();
        this.categoryDao = categoryDao;
        this.siteDao = siteDao;
        this.productDao = productDao;
        this.keywordDao = keywordDao;
        this.catalogDao = catalogDao;
        this.refinementDao = refinementDao;
    }

    /**
     * This is used for search product.
     * <p/>
     * Note: if change this method, we need to change "count" method as well.
     *
     * @param keywords
     * @param categoryIds
     * @param refinements
     * @param sortField
     * @param reverse
     * @param startPos
     * @param maxResult
     * @return
     * @throws Exception
     */
    @Override
    public Map search(String keywords, List<Long> categoryIds, Site site, Map<String, String> refinements, int startPos, int maxResult, String sortField, boolean reverse, boolean isActive) throws Exception {

        FullTextSession indexManager = this.productDao.getFullTextSearchSession();
        BooleanQuery bq = new BooleanQuery();
        Query query;

        //search by keywords. If use enter 'discount', move to discount page
        if (!StringUtils.isEmpty(keywords)) {
            if ("discount".equalsIgnoreCase(keywords)) {
                query = new TermRangeQuery("displayPricePromo", "0", "9999999999", true, true);
                bq.add(query, BooleanClause.Occur.MUST);
            } else {
                String[] fields = new String[]{"name", "model", "keyword"};
                MultiFieldQueryParser multiFieldparser = new MultiFieldQueryParser(Version.LUCENE_30, fields, new StandardAnalyzer(Version.LUCENE_30));
                query = multiFieldparser.parse(keywords);
                bq.add(query, BooleanClause.Occur.MUST);
            }
        }
        //filter by categoryIds
        TermQuery tq = null;
        if (categoryIds != null) {
            for (Long catid : categoryIds) {
                tq = new TermQuery(new Term("categories.id", catid.toString()));
                bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));
            }
        }

        /*search by Site*/
        if (site != null) {
            tq = new TermQuery(new Term("site.id", site.getId().toString()));
            bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));
        }

        //only get active products
        if (isActive) {
            tq = new TermQuery(new Term("active", "Y"));
        } else {
            tq = new TermQuery(new Term("active", "N"));
        }
        bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));

        //Check variant is available and inventory > 0
        query = new TermRangeQuery("productVariant.inventory", "0001", "9999", true, true);
        bq.add(query, BooleanClause.Occur.MUST);

        //Filters
        if (refinements != null) {
            for (String key : refinements.keySet()) {
                String value = refinements.get(key);
                if (StringUtils.isEmpty(value)) continue;

                if (value.indexOf("-") > 0) {
                    // filter by range
                    String[] ranges = value.split("-");
                    if (ranges.length == 2) {
                        /*
                        if ("price".equalsIgnoreCase(key)) {
                            fields = new String[]{"price", "pricePromo"};
                            parser = new MultiFieldQueryParser(Version.LUCENE_30, fields, new StandardAnalyzer(Version.LUCENE_30));
                            query = parser.parse("["+paddingString(ranges[0])+" TO "+paddingString(ranges[1])+"]");
                            bq.add(query, BooleanClause.Occur.MUST);
                        } else {
                            query = new TermRangeQuery(key, paddingString(ranges[0]), paddingString(ranges[1]), false, true);
                            bq.add(query, BooleanClause.Occur.MUST);
                        }*/
                        query = new TermRangeQuery(key, paddingString(ranges[0]), paddingString(ranges[1]), true, true);
                        bq.add(query, BooleanClause.Occur.MUST);
                    } else if (ranges.length == 1) {
                        query = new TermRangeQuery(key, paddingString(""), paddingString(ranges[0]), true, true);
                        bq.add(query, BooleanClause.Occur.MUST);
                    }
                } else {
                    // filter by value
                    if ("brands".equalsIgnoreCase(key)) {
                        String[] fields = new String[]{"name", "model", "keyword"};
                        MultiFieldQueryParser multiFieldparser = new MultiFieldQueryParser(Version.LUCENE_30, fields, new StandardAnalyzer(Version.LUCENE_30));
                        query = multiFieldparser.parse(value);
                        bq.add(query, BooleanClause.Occur.MUST);
                    } else {
                        if (key.startsWith("attributeValues.key")) {
                            key = "attributeValues.key";
                        }
                        tq = new TermQuery(new Term(key, value));
                        bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));
                    }
                }
            }
        }
        //this is the second way to filter by price range
        /*fullTextQuery.enableFullTextFilter("priceRangeFilter").setParameter("fieldName", "price")
                .setParameter("lowerTerm", "0000100000").setParameter("upperTerm", "0000140000")
                .setParameter("includeLower", true).setParameter("includeUpper", true);*/

        FullTextQuery fullTextQuery = indexManager.createFullTextQuery(bq, Product.class);

        //Sort
        Sort sort = null;
        if (!StringUtils.isEmpty(sortField)) {
            if (sortField.indexOf("price") >= 0) {
                if (reverse) {
                    sort = new Sort(new SortField("price", SortField.LONG, reverse));
                } else {
                    sort = new Sort(new SortField("price", SortField.LONG, reverse));
                }
            } else {
                sort = new Sort(new SortField(sortField, SortField.STRING, reverse));
            }
        } else {
            //This is default order of search result. Sort by scoring
        }

        Map result = new HashMap();
        result.putAll(getProjectionProductIds(fullTextQuery));
        result.put("products", getProjectionProducts(fullTextQuery, startPos, maxResult, sort));
        return result;
    }

    @Override
    public Map getProductsBySubCategoryUsingLucene(String subCatIds, Site site, Map<String, String> refinements, int startPos, int maxResult, String sortField, boolean reverse, boolean isActive) throws Exception{

        FullTextSession indexManager = this.productDao.getFullTextSearchSession();
        BooleanQuery bq = new BooleanQuery();
        Query query;

        //filter by categoryIds
        TermQuery tq = new TermQuery(new Term("categories.id", subCatIds));
        bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));

        /*search by Site*/
        if (site != null) {
            tq = new TermQuery(new Term("site.id", site.getId().toString()));
            bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));
        }

        /*filter by catalog*/
//        if (site != null) {
//            QueryParser parser = new QueryParser(Version.LUCENE_30, "catalog.id", new StandardAnalyzer(Version.LUCENE_30));
//            try {
//                query = parser.parse(getCategoryTerm(site));
//                bq.add(new BooleanClause(query, BooleanClause.Occur.MUST));
//            } catch (ParseException e) {
//                throw new IllegalArgumentException("catalog.id input is incorrect for parsing");
//            }
//        }

        //only get active product
        if (isActive) {
            tq = new TermQuery(new Term("active", "Y"));
        } else {
            tq = new TermQuery(new Term("active", "N"));
        }
        bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));

        //Check variant is available and inventory > 0
        query = new TermRangeQuery("productVariant.inventory", "0001", "9999", true, true);
        bq.add(query, BooleanClause.Occur.MUST);

        //Filters
        if (refinements != null) {
            for (String key : refinements.keySet()) {
                String value = refinements.get(key);
                if (StringUtils.isEmpty(value)) continue;

                if (value.indexOf("-") > 0) {
                    // filter by range
                    String[] ranges = value.split("-");
                    if (ranges.length == 2) {
                        /*
                        if ("price".equalsIgnoreCase(key)) {
                            fields = new String[]{"price", "pricePromo"};
                            parser = new MultiFieldQueryParser(Version.LUCENE_30, fields, new StandardAnalyzer(Version.LUCENE_30));
                            query = parser.parse("["+paddingString(ranges[0])+" TO "+paddingString(ranges[1])+"]");
                            bq.add(query, BooleanClause.Occur.MUST);
                        } else {
                            query = new TermRangeQuery(key, paddingString(ranges[0]), paddingString(ranges[1]), false, true);
                            bq.add(query, BooleanClause.Occur.MUST);
                        }*/
                        query = new TermRangeQuery(key, paddingString(ranges[0]), paddingString(ranges[1]), true, true);
                        bq.add(query, BooleanClause.Occur.MUST);
                    } else if (ranges.length == 1) {
                        query = new TermRangeQuery(key, paddingString(""), paddingString(ranges[0]), true, true);
                        bq.add(query, BooleanClause.Occur.MUST);
                    }
                } else {
                    // filter by value
                    if ("brands".equalsIgnoreCase(key)) {
                        String[] fields = new String[]{"name", "model", "keyword"};
                        MultiFieldQueryParser multiFieldparser = new MultiFieldQueryParser(Version.LUCENE_30, fields, new StandardAnalyzer(Version.LUCENE_30));
                        query = multiFieldparser.parse(value);
                        bq.add(query, BooleanClause.Occur.MUST);
                    } else {
                        if (key.startsWith("attributeValues.key")) {
                            key = "attributeValues.key";
                        }
                        tq = new TermQuery(new Term(key, value));
                        bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));
                    }
                }
            }
        }
        //this is the second way to filter by price range
        /*fullTextQuery.enableFullTextFilter("priceRangeFilter").setParameter("fieldName", "price")
                .setParameter("lowerTerm", "0000100000").setParameter("upperTerm", "0000140000")
                .setParameter("includeLower", true).setParameter("includeUpper", true);*/


        FullTextQuery fullTextQuery = indexManager.createFullTextQuery(bq, Product.class);

        //Sort
        Sort sort = null;
        if (!StringUtils.isEmpty(sortField)) {
            if (sortField.indexOf("price") >= 0) {
                if (reverse) {
                    sort = new Sort(new SortField("price", SortField.LONG, reverse));
                } else {
                    sort = new Sort(new SortField("price", SortField.LONG, reverse));
                }
            } else {
                sort = new Sort(new SortField(sortField, SortField.STRING, reverse));
            }
        } else {
            sort = new Sort(new SortField("sequence", SortField.FLOAT, false));
        }

        Map result = new HashMap();
        result.put("count", fullTextQuery.getResultSize());
        result.put("products", getProjectionProducts(fullTextQuery, startPos, maxResult, sort));
        return result;
    }

    @Override
    public Map getProductsBySubCategoryUsingLucene(String subCatIds, Site site, Map<String, String> refinements, int startPos, int maxResult, String sortField, boolean reverse) throws Exception{
        return this.getProductsBySubCategoryUsingLucene(subCatIds, site, refinements, startPos, maxResult, sortField, reverse, true);
    }

    
    /**
     * This method will return list of products and used to share in search & category function.
     *
     * @param fullTextQuery
     * @param startPos
     * @param maxResult
     * @param sort
     * @return
     */
    private List<Product> getProjectionProducts(FullTextQuery fullTextQuery, int startPos, int maxResult, Sort sort) {
        /*set projection: this will get necessary data for front end - not query from database*/
        fullTextQuery.setProjection(FullTextQuery.SCORE, "id",
                "name", "model", "displayPrice", "displayPricePromo", "price", "uri", "imageUrl", "metaDescription", "metaKeyword", "newProduct");

        List<Product> products = new ArrayList<Product>();

        for (Object[] result : (List<Object[]>) fullTextQuery.setSort(sort).setFirstResult(startPos).setMaxResults(maxResult).list()) {
            Product product = new Product();
            product.setId((Long) result[1]);
            product.setName((String) result[2]);
            product.setModel((String) result[3]);
            product.setDisplayPrice((String) result[4]);
            product.setDisplayPricePromo((String) result[5]);
            product.setPriceMin((Long) result[6]);
            product.setUri((String) result[7]);
            product.setImageUrl((String) result[8]);
            product.setMetaDescription((String) result[9]);
            product.setMetaKeyword((String) result[10]);
            product.setNewProduct((String) result[11]);
            products.add(product);
        }

        return products;
    }

    private Map getProjectionProductIds(FullTextQuery fullTextQuery) {
        /*set projection: this will get necessary data for front end - not query from database*/
        fullTextQuery.setProjection(FullTextQuery.SCORE, "id");

        StringBuffer prodIds = new StringBuffer();
        List<Object[]> list = (List<Object[]>) fullTextQuery.list();
        for (Object[] result : list) {
            if (StringUtils.isEmpty(prodIds.toString())) {
                prodIds.append(result[1]);
            } else {
                prodIds.append(",").append(result[1]);
            }
        }
        Map result = new HashMap();
        result.put("productIds", prodIds.toString());
        result.put("count", list.size());
        return result;
    }

    public List<Object[]> countProductsBySubCategoryMap(String keywords, Site site, Map<String, String> refinements) throws Exception {
        List result = new ArrayList();

        List<Category> categories = categoryDao.getSubCategoriesBySite(site, true); 

        for (Category category:categories) {
            List<Long> listIds = new ArrayList<Long>();
            listIds.add(category.getId());
            Integer numberOfProd = count(keywords, listIds, site, refinements, true);
            if (numberOfProd > 0) {
                Object[] arr = new Object[2];
                arr[0] = category;
                arr[1] = numberOfProd;
                result.add(arr);
            }
        }
        return result;
    }


    /**
     * Using for show number of product available on filter function (Search).
     *
     * @param keywords
     * @param categoryIds
     * @param refinements
     * @return
     * @throws Exception
     */
    @Override
    public Map countRefinementMap(String keywords, List<Long> categoryIds, Site site, Map<String, String> refinements, boolean isActive) throws Exception {
        Map result = new HashMap();
        Map temp = this.refinementDao.getRefinements(site);
        Set refs = temp.keySet();

        for (Iterator<Refinement> it = refs.iterator(); it.hasNext(); ) {
            Refinement ref = it.next();
            List<RefinementValue> refinementValues = (List<RefinementValue>) temp.get(ref);
            Map refinementValueMap = new HashMap();
            for (RefinementValue refinementValue: refinementValues) {
                if (!refinements.containsKey(ref.getRefinementColumn())) {
                    //add refinement value
                    refinements.put(ref.getRefinementColumn(), refinementValue.getRefinementKey());
                    Integer count = count(keywords, categoryIds, site, refinements, isActive);
                    refinements.remove(ref.getRefinementColumn());
                    if (count > 0) {
                        refinementValueMap.put(refinementValue.getRefinementKey(), count);
                    }
                } else {
                    if (refinementValue.getRefinementKey().equalsIgnoreCase(refinements.get(ref.getRefinementColumn()))) {
                        Integer count = count(keywords, categoryIds, site, refinements, isActive);
                        if (count > 0) {
                            refinementValueMap.put(refinementValue.getRefinementKey(), count);
                        }
                    }
                }
            }
            result.put(ref.getRefinementColumn(), refinementValueMap);
        }
        return result;
    }
    /**
     * Using for show number of product available on filter function (subcategory).
     *
     * @param subCatIds
     * @param refinements
     * @return
     * @throws Exception
     */
    @Override
    public Map countRefinementMap(String subCatIds, Site site, Map<String, String> refinements, boolean isActive) throws Exception {
        Map result = new HashMap();
        Map temp = this.refinementDao.getRefinements(site);
        Set refs = temp.keySet();

        for (Iterator<Refinement> it = refs.iterator(); it.hasNext(); ) {
            Refinement ref = it.next();
            List<RefinementValue> refinementValues = (List<RefinementValue>) temp.get(ref);
            Map refinementValueMap = new HashMap();
            for (RefinementValue refinementValue: refinementValues) {
                if (!refinements.containsKey(ref.getRefinementColumn())) {
                    //add refinement value
                    refinements.put(ref.getRefinementColumn(), refinementValue.getRefinementKey());
                    Integer count = countProductsBySubCategoryUsingLucene(subCatIds, site, refinements, isActive);
                    refinements.remove(ref.getRefinementColumn());
                    if (count > 0) {
                        refinementValueMap.put(refinementValue.getRefinementKey(), count);
                    }
                } else {
                    if (refinementValue.getRefinementKey().equalsIgnoreCase(refinements.get(ref.getRefinementColumn()))) {
                        Integer count = countProductsBySubCategoryUsingLucene(subCatIds, site, refinements, isActive);
                        if (count > 0) {
                            refinementValueMap.put(refinementValue.getRefinementKey(), count);
                        }
                    }
                }
            }
            result.put(ref.getRefinementColumn(), refinementValueMap);
        }
        return result;
    }
    /**
     * Using for show number of product available on filter function.
     *
     * @param keywords
     * @param categoryIds
     * @param refinements
     * @return
     * @throws Exception
     */
    @Override
    public Integer count(String keywords, List<Long> categoryIds, Site site, Map<String, String> refinements, boolean isActive) throws Exception {

        FullTextSession indexManager = this.productDao.getFullTextSearchSession();

        BooleanQuery bq = new BooleanQuery();
        Query query;

        //search by keywords. If use enter 'discount', move to discount page
        if (!StringUtils.isEmpty(keywords)) {
            if ("discount".equalsIgnoreCase(keywords)) {
                query = new TermRangeQuery("displayPricePromo", "0", "9999999999", false, true);
                bq.add(query, BooleanClause.Occur.MUST);
            } else {
                String[] fields = new String[]{"name", "model", "keyword"};
                MultiFieldQueryParser multiFieldparser = new MultiFieldQueryParser(Version.LUCENE_30, fields, new StandardAnalyzer(Version.LUCENE_30));
                query = multiFieldparser.parse(keywords);
                bq.add(query, BooleanClause.Occur.MUST);
            }
        }

        //filter by categoryIds
        TermQuery tq = null;
        if (categoryIds != null) {
            for (Long catid : categoryIds) {
                tq = new TermQuery(new Term("categories.id", catid.toString()));
                bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));
            }
        }
        /*search by Site*/
        if (site != null) {
            tq = new TermQuery(new Term("site.id", site.getId().toString()));
            bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));
        }

        //only get active product
        if (isActive) {
            tq = new TermQuery(new Term("active", "Y"));
        } else {
            tq = new TermQuery(new Term("active", "N"));
        }
        bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));

        //Check variant is available and inventory > 0
        query = new TermRangeQuery("productVariant.inventory", "0001", "9999", true, true);
        bq.add(query, BooleanClause.Occur.MUST);

        //Filters
        if (refinements != null) {
            for (String key : refinements.keySet()) {
                String value = refinements.get(key);
                if (StringUtils.isEmpty(value)) continue;

                if (value.indexOf("-") > 0) {
                    // filter by range
                    String[] ranges = value.split("-");
                    if (ranges.length == 2) {
                        query = new TermRangeQuery(key, paddingString(ranges[0]), paddingString(ranges[1]), true, true);
                        bq.add(query, BooleanClause.Occur.MUST);
                        /*
                        if ("price".equalsIgnoreCase(key)) {
                            fields = new String[]{"price", "pricePromo"};
                            parser = new MultiFieldQueryParser(Version.LUCENE_30, fields, new StandardAnalyzer(Version.LUCENE_30));
                            query = parser.parse("["+paddingString(ranges[0])+" TO "+paddingString(ranges[1])+"]");
                            bq.add(query, BooleanClause.Occur.MUST);
                        } else {
                            query = new TermRangeQuery(key, paddingString(ranges[0]), paddingString(ranges[1]), false, true);
                            bq.add(query, BooleanClause.Occur.MUST);
                        }
                        */
                    } else if (ranges.length == 1) {
                        query = new TermRangeQuery(key, paddingString(""), paddingString(ranges[0]), true, true);
                        bq.add(query, BooleanClause.Occur.MUST);
                    }
                } else {
                    // filter by value
                    if ("brands".equalsIgnoreCase(key)) {
                        String[] fields = new String[]{"name", "model", "keyword"};
                        MultiFieldQueryParser multiFieldparser = new MultiFieldQueryParser(Version.LUCENE_30, fields, new StandardAnalyzer(Version.LUCENE_30));
                        query = multiFieldparser.parse(value);
                        bq.add(query, BooleanClause.Occur.MUST);
                    } else {
                        if (key.startsWith("attributeValues.key")) {
                            key = "attributeValues.key";
                        }
                        tq = new TermQuery(new Term(key, value));
                        bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));
                    }
                }
            }
        }

        FullTextQuery fullTextQuery = indexManager.createFullTextQuery(bq, Product.class);

        return fullTextQuery.getResultSize();
    }

    public Integer countProductsBySubCategoryUsingLucene(String subCatId, Site site, Map<String, String> refinements, boolean isActive) throws Exception {
        FullTextSession indexManager = this.productDao.getFullTextSearchSession();
        BooleanQuery bq = new BooleanQuery();

        //filter by categoryIds
        TermQuery tq = new TermQuery(new Term("categories.id", subCatId));
        bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));

        /*search by Site*/
        if (site != null) {
            tq = new TermQuery(new Term("site.id", site.getId().toString()));
            bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));
        }

        /*filter by catalog*/
        Query query;
//        if (site != null) {
//            QueryParser parser = new QueryParser(Version.LUCENE_30, "catalog.id", new StandardAnalyzer(Version.LUCENE_30));
//            try {
//                query = parser.parse(getCategoryTerm(site));
//                bq.add(new BooleanClause(query, BooleanClause.Occur.MUST));
//            } catch (ParseException e) {
//                throw new IllegalArgumentException("catalog.id input is incorrect for parsing");
//            }
//        }

        //only get active product
        if (isActive) {
            tq = new TermQuery(new Term("active", "Y"));
        } else {
            tq = new TermQuery(new Term("active", "N"));
        }
        bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));

        //Check variant is available and inventory > 0
        query = new TermRangeQuery("productVariant.inventory", "0001", "9999", true, true);
        bq.add(query, BooleanClause.Occur.MUST);

        //Filters
        if (refinements != null) {
            for (String key : refinements.keySet()) {
                String value = refinements.get(key);
                if (StringUtils.isEmpty(value)) continue;

                if (value.indexOf("-") > 0) {
                    // filter by range
                    String[] ranges = value.split("-");
                    if (ranges.length == 2) {
                        /*
                        if ("price".equalsIgnoreCase(key)) {
                            fields = new String[]{"price", "pricePromo"};
                            parser = new MultiFieldQueryParser(Version.LUCENE_30, fields, new StandardAnalyzer(Version.LUCENE_30));
                            query = parser.parse("["+paddingString(ranges[0])+" TO "+paddingString(ranges[1])+"]");
                            bq.add(query, BooleanClause.Occur.MUST);
                        } else {
                            query = new TermRangeQuery(key, paddingString(ranges[0]), paddingString(ranges[1]), false, true);
                            bq.add(query, BooleanClause.Occur.MUST);
                        }*/
                        query = new TermRangeQuery(key, paddingString(ranges[0]), paddingString(ranges[1]), true, true);
                        bq.add(query, BooleanClause.Occur.MUST);
                    } else if (ranges.length == 1) {
                        query = new TermRangeQuery(key, paddingString(""), paddingString(ranges[0]), true, true);
                        bq.add(query, BooleanClause.Occur.MUST);
                    }
                } else {
                    // filter by value
                    if ("brands".equalsIgnoreCase(key)) {
                        String[] fields = new String[]{"name", "model", "keyword"};
                        MultiFieldQueryParser multiFieldparser = new MultiFieldQueryParser(Version.LUCENE_30, fields, new StandardAnalyzer(Version.LUCENE_30));
                        query = multiFieldparser.parse(value);
                        bq.add(query, BooleanClause.Occur.MUST);
                    } else {
                        if (key.startsWith("attributeValues.key")) {
                            key = "attributeValues.key";
                        }
                        tq = new TermQuery(new Term(key, value));
                        bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));
                    }
                }
            }
        }
        //Note: this is the second solution to filter by price range. Compare with the previous one
        /*fullTextQuery.enableFullTextFilter("priceRangeFilter").setParameter("fieldName", "price")
                .setParameter("lowerTerm", "0000100000").setParameter("upperTerm", "0000140000")
                .setParameter("includeLower", true).setParameter("includeUpper", true);*/


        FullTextQuery fullTextQuery = indexManager.createFullTextQuery(bq, Product.class);

        return fullTextQuery.getResultSize();
    }

//    @Override
//    public List<Product> searchProductsBySubCategory(Long categoryId, Site site) throws Exception {
//        FullTextSession indexManager = this.productDao.getFullTextSearchSession();
//        String[] fields = new String[]{"id"};
//        MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_30, fields, new StandardAnalyzer(Version.LUCENE_30));
//        org.apache.lucene.search.Query query = parser.parse(categoryId.toString());
//        FullTextQuery persistenceQuery = indexManager.createFullTextQuery(query, Category.class);
//
//        List<Category> result = persistenceQuery.list();
//        if (result != null) {
//            if (result.size() == 1) {
//                return result.get(0).getProducts();
//            } else {
//                throw new IllegalArgumentException("We expect only one category should be returned: " + result.size());
//            }
//        } else {
//            return null;
//        }
//    }

    /**
     * This is used for search keyword.
     * <p/>
     * Note: if change this method, we need to change "count" method as well.
     *
     * @param keyword
     * @param startPos
     * @param maxResult
     * @return
     * @throws Exception
     */
    @Override
    public String getAutoCompleteKeywords(String keyword, Site site, int startPos, int maxResult) throws Exception {

        FullTextSession indexManager = this.productDao.getFullTextSearchSession();
        String field = "keyword";
        QueryParser fieldParser = new QueryParser(Version.LUCENE_30, field, new StandardAnalyzer(Version.LUCENE_30));
        BooleanQuery bq = new BooleanQuery();

        //search by keywords
        Query query = fieldParser.parse(keyword+"*");
        bq.add(query, BooleanClause.Occur.MUST);

        FullTextQuery fullTextQuery = indexManager.createFullTextQuery(bq, Keyword.class);

        //Sort
        Sort sort = new Sort(new SortField("count", SortField.LONG));

        fullTextQuery.setProjection(FullTextQuery.SCORE, "keyword");

        StringBuffer sb = new StringBuffer();
        for (Object[] result : (List<Object[]>) fullTextQuery.setSort(sort).setFirstResult(startPos).setMaxResults(maxResult).list()) {
            sb = sb.append(result[1]).append("\n");
        }

        return sb.toString();
    }

    private String paddingString(String value) {
        return paddingString(value, 10);
    }

    private String paddingString(String value, int padding) {
        if (padding <= 0) {
            padding = 10;
        }
        if (StringUtils.isEmpty(value)) value = "";

        if (value.length() > padding) throw new IllegalArgumentException("Try to pad on a number too big");
        StringBuilder paddedLong = new StringBuilder();
        for (int padIndex = value.length(); padIndex < padding; padIndex++) {
            paddedLong.append('0');
        }
        return paddedLong.append(value).toString();
    }

    private String getCategoryTerm(Site site) {
        String result = "";
        if (site != null) {
            List<Category> list = categoryDao.getSubCategoriesBySite(site, true);
            if (list != null && list.size() > 0) {
                result = "(";
                for (Category category: list) {
                    if ("(".equals(result)) {
                        result += category.getId();
                    } else {
                        result += " " + category.getId();
                    }
                }
                result += ")";
            }
        }
        return result;
    }


    public Map getAllProducts(List<Long> categoryIds, Site site, Map <String, String> refinements, int startPos, int maxResult, String sortField, boolean reverse, boolean isActive) throws Exception {
        FullTextSession indexManager = this.productDao.getFullTextSearchSession();
        BooleanQuery bq = new BooleanQuery();
        Query query;

        //filter by categoryIds
        TermQuery tq = null;
        if (categoryIds != null) {
            for (Long catid : categoryIds) {
                tq = new TermQuery(new Term("categories.id", catid.toString()));
                bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));
            }
        }

        /*search by Site*/
        if (site != null) {
            tq = new TermQuery(new Term("site.id", site.getId().toString()));
            bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));
        }

        //only get active products
        if (isActive) {
            tq = new TermQuery(new Term("active", "Y"));
        } else {
            tq = new TermQuery(new Term("active", "N"));
        }
        bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));

        //Check variant is available and inventory > 0
        query = new TermRangeQuery("productVariant.inventory", "0001", "9999", true, true);
        bq.add(query, BooleanClause.Occur.MUST);

        //Filters
        if (refinements != null) {
            for (String key : refinements.keySet()) {
                String value = refinements.get(key);
                if (StringUtils.isEmpty(value)) continue;

                if (value.indexOf("-") > 0) {
                    // filter by range
                    String[] ranges = value.split("-");
                    if (ranges.length == 2) {
                        /*
                        if ("price".equalsIgnoreCase(key)) {
                            fields = new String[]{"price", "pricePromo"};
                            parser = new MultiFieldQueryParser(Version.LUCENE_30, fields, new StandardAnalyzer(Version.LUCENE_30));
                            query = parser.parse("["+paddingString(ranges[0])+" TO "+paddingString(ranges[1])+"]");
                            bq.add(query, BooleanClause.Occur.MUST);
                        } else {
                            query = new TermRangeQuery(key, paddingString(ranges[0]), paddingString(ranges[1]), false, true);
                            bq.add(query, BooleanClause.Occur.MUST);
                        }*/
                        query = new TermRangeQuery(key, paddingString(ranges[0]), paddingString(ranges[1]), true, true);
                        bq.add(query, BooleanClause.Occur.MUST);
                    } else if (ranges.length == 1) {
                        query = new TermRangeQuery(key, paddingString(""), paddingString(ranges[0]), true, true);
                        bq.add(query, BooleanClause.Occur.MUST);
                    }
                } else {
                    // filter by value
                    if ("brands".equalsIgnoreCase(key)) {
                        String[] fields = new String[]{"name", "model", "keyword"};
                        MultiFieldQueryParser multiFieldparser = new MultiFieldQueryParser(Version.LUCENE_30, fields, new StandardAnalyzer(Version.LUCENE_30));
                        query = multiFieldparser.parse(value);
                        bq.add(query, BooleanClause.Occur.MUST);
                    } else {
                        if (key.startsWith("attributeValues.key")) {
                            key = "attributeValues.key";
                        }
                        tq = new TermQuery(new Term(key, value));
                        bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));
                    }
                }
            }
        }
        //this is the second way to filter by price range
        /*fullTextQuery.enableFullTextFilter("priceRangeFilter").setParameter("fieldName", "price")
                .setParameter("lowerTerm", "0000100000").setParameter("upperTerm", "0000140000")
                .setParameter("includeLower", true).setParameter("includeUpper", true);*/

        FullTextQuery fullTextQuery = indexManager.createFullTextQuery(bq, Product.class);

        //Sort
        Sort sort = null;
        if (!StringUtils.isEmpty(sortField)) {
            if (sortField.indexOf("price") >= 0) {
                if (reverse) {
                    sort = new Sort(new SortField("price", SortField.LONG, reverse));
                } else {
                    sort = new Sort(new SortField("price", SortField.LONG, reverse));
                }
            } else {
                sort = new Sort(new SortField(sortField, SortField.STRING, reverse));
            }
        } else {
            //This is default order of search result. Sort by scoring
        }

        Map result = new HashMap();
        result.putAll(getProjectionProductIds(fullTextQuery));
        result.put("products", getProjectionProducts(fullTextQuery, startPos, maxResult, sort));
        return result;
    }
    public Map countRefinementMap(List<Long> categoryIds, Site site, Map<String, String> refinements, boolean isActive) throws Exception {
        Map result = new HashMap();
        Map temp = this.refinementDao.getRefinements(site);
        Set refs = temp.keySet();

        for (Iterator<Refinement> it = refs.iterator(); it.hasNext(); ) {
            Refinement ref = it.next();
            List<RefinementValue> refinementValues = (List<RefinementValue>) temp.get(ref);
            Map refinementValueMap = new HashMap();
            for (RefinementValue refinementValue: refinementValues) {
                if (!refinements.containsKey(ref.getRefinementColumn())) {
                    //add refinement value
                    refinements.put(ref.getRefinementColumn(), refinementValue.getRefinementKey());
                    Integer count = countAllProducts(categoryIds, site, refinements, isActive);
                    refinements.remove(ref.getRefinementColumn());
                    if (count > 0) {
                        refinementValueMap.put(refinementValue.getRefinementKey(), count);
                    }
                } else {
                    if (refinementValue.getRefinementKey().equalsIgnoreCase(refinements.get(ref.getRefinementColumn()))) {
                        Integer count = countAllProducts(categoryIds, site, refinements, isActive);
                        if (count > 0) {
                            refinementValueMap.put(refinementValue.getRefinementKey(), count);
                        }
                    }
                }
            }
            result.put(ref.getRefinementColumn(), refinementValueMap);
        }
        return result;
    }

    public Integer countAllProducts(List<Long> categoryIds, Site site, Map <String, String> refinements, boolean isActive) throws Exception {

        FullTextSession indexManager = this.productDao.getFullTextSearchSession();

        BooleanQuery bq = new BooleanQuery();
        Query query;

        //filter by categoryIds
        TermQuery tq = null;
        if (categoryIds != null) {
            for (Long catid : categoryIds) {
                tq = new TermQuery(new Term("categories.id", catid.toString()));
                bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));
            }
        }

        /*search by Site*/
        if (site != null) {
            tq = new TermQuery(new Term("site.id", site.getId().toString()));
            bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));
        }

        //only get active product
        if (isActive) {
            tq = new TermQuery(new Term("active", "Y"));
        } else {
            tq = new TermQuery(new Term("active", "N"));
        }
        bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));

        //Check variant is available and inventory > 0
        query = new TermRangeQuery("productVariant.inventory", "0001", "9999", true, true);
        bq.add(query, BooleanClause.Occur.MUST);

        //Filters
        if (refinements != null) {
            for (String key : refinements.keySet()) {
                String value = refinements.get(key);
                if (StringUtils.isEmpty(value)) continue;

                if (value.indexOf("-") > 0) {
                    // filter by range
                    String[] ranges = value.split("-");
                    if (ranges.length == 2) {
                        query = new TermRangeQuery(key, paddingString(ranges[0]), paddingString(ranges[1]), true, true);
                        bq.add(query, BooleanClause.Occur.MUST);
                        /*
                        if ("price".equalsIgnoreCase(key)) {
                            fields = new String[]{"price", "pricePromo"};
                            parser = new MultiFieldQueryParser(Version.LUCENE_30, fields, new StandardAnalyzer(Version.LUCENE_30));
                            query = parser.parse("["+paddingString(ranges[0])+" TO "+paddingString(ranges[1])+"]");
                            bq.add(query, BooleanClause.Occur.MUST);
                        } else {
                            query = new TermRangeQuery(key, paddingString(ranges[0]), paddingString(ranges[1]), false, true);
                            bq.add(query, BooleanClause.Occur.MUST);
                        }
                        */
                    } else if (ranges.length == 1) {
                        query = new TermRangeQuery(key, paddingString(""), paddingString(ranges[0]), true, true);
                        bq.add(query, BooleanClause.Occur.MUST);
                    }
                } else {
                    // filter by value
                    if ("brands".equalsIgnoreCase(key)) {
                        String[] fields = new String[]{"name", "model", "keyword"};
                        MultiFieldQueryParser multiFieldparser = new MultiFieldQueryParser(Version.LUCENE_30, fields, new StandardAnalyzer(Version.LUCENE_30));
                        query = multiFieldparser.parse(value);
                        bq.add(query, BooleanClause.Occur.MUST);
                    } else {
                        if (key.startsWith("attributeValues.key")) {
                            key = "attributeValues.key";
                        }
                        tq = new TermQuery(new Term(key, value));
                        bq.add(new BooleanClause(tq, BooleanClause.Occur.MUST));
                    }
                }
            }
        }

        FullTextQuery fullTextQuery = indexManager.createFullTextQuery(bq, Product.class);

        return fullTextQuery.getResultSize();
    }

}
