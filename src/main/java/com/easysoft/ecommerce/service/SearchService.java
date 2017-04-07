package com.easysoft.ecommerce.service;

import com.easysoft.ecommerce.model.Category;
import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.Site;

import java.util.List;
import java.util.Map;


public interface SearchService {

    Map search(String keywords, List<Long> categoryIds, Site site, Map <String, String> refinements, int startPos, int maxResult, String sortField, boolean reverse, boolean isActive) throws Exception;
    Map countRefinementMap(String keywords, List<Long> categoryIds, Site site, Map<String, String> refinements, boolean isActive) throws Exception;
    Integer count(String keywords, List<Long> categoryIds, Site site, Map <String, String> refinements, boolean isActive) throws Exception;

    /**
     * These 3 methods are used to get all products and don't care about keyword or categories. It can be filtered by categories
     */
    Map getAllProducts(List<Long> categoryIds, Site site, Map <String, String> refinements, int startPos, int maxResult, String sortField, boolean reverse, boolean isActive) throws Exception;
    Map countRefinementMap(List<Long> categoryIds, Site site, Map<String, String> refinements, boolean isActive) throws Exception;
    Integer countAllProducts(List<Long> categoryIds, Site site, Map <String, String> refinements, boolean isActive) throws Exception;

    Map getProductsBySubCategoryUsingLucene(String subCatIds, Site site, Map<String, String> refinements, int startPos, int maxResult, String sortField, boolean reverse, boolean isActive) throws Exception;
    Map getProductsBySubCategoryUsingLucene(String subCatIds, Site site, Map<String, String> refinements,int startPos, int maxResult, String sortField, boolean reverse) throws Exception;
    Map countRefinementMap(String subCatId, Site site, Map<String, String> refinements, boolean isActive) throws Exception;


    Integer countProductsBySubCategoryUsingLucene(String subCatIds, Site site, Map <String, String> refinements, boolean isActive) throws Exception;

    /**
     * This method will return the list of array[Category, number of products of the category after search]
     *  
     * @param keywords
     * @param site
     * @param refinements
     * @return
     * @throws Exception
     */
    List<Object[]> countProductsBySubCategoryMap(String keywords, Site site, Map<String, String> refinements) throws Exception;

//    List<Product> searchProductsBySubCategory(Long subCategoryId, Site site) throws Exception;

    String getAutoCompleteKeywords(String keyword, Site site, int startPos, int maxResult) throws Exception;

    
}

