package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.NewsCategory;
import com.easysoft.ecommerce.model.Site;

import java.util.List;

public interface NewsCategoryDao extends GenericDao<NewsCategory, Long> {
    List<NewsCategory> getRootNewsCategories(Site site);
    List<NewsCategory> getRootNewsCategories(Site site, String active);

    List<NewsCategory> getSubNewsCategories(Site site, NewsCategory newsCategory);
    List<NewsCategory> getSubNewsCategories(Site site, NewsCategory newsCategory, String active);

    NewsCategory getParentNewsCategory(Site site, Long newsCategoryId);
    NewsCategory getParentNewsCategory(Site site, Long newsCategoryId, String active);

    NewsCategory getNewsCategory(Site site, Long newsCategoryId);
    NewsCategory getNewsCategory(Site site, Long newsCategoryId, String active);
    NewsCategory getNewsCategory(Site site, String uri);
    NewsCategory getNewsCategory(Site site, String uri, String active);

    List<NewsCategory> findNewscategoriesHavingNews (Long siteId);
    NewsCategory findNewsCategory (Long newsId, Long newsCategoryId, Long siteId);

}
