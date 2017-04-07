package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.News;
import com.easysoft.ecommerce.model.NewsCategory;
import com.easysoft.ecommerce.model.Site;

import java.util.List;


public interface NewsDao extends GenericDao<News, Long> {
    void removeNewsNewsCategory (Long newsId);
    List<NewsCategory> findNewscategoriesByNewsId (Long newsId);
    List<News> findNewsByNewsCategory(String categoryUri, Long categoryId, Integer startPosition, Integer maxResult, String orderByAttr, Long siteId);
    Long countActiveNewsByNewsCategory(String categoryUri, Long categoryId, Long siteId);

    Long countNewsInNewsCategory(Long newsCategoryId, String active);
    List<NewsCategory> getNewsCategories(Site site, Long newsId, String active);
}