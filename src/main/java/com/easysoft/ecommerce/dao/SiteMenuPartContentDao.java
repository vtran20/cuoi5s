package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.*;

import java.util.List;

public interface SiteMenuPartContentDao extends GenericDao<SiteMenuPartContent, Long> {

    List<SiteMenuPartContent> getContentParts(Long rowId);
    List<SiteMenuPartContent> getContentParts(Long rowId, String active);
    List<Product> getProductContentParts(Long rowId);
    List<Product> getProductContentParts(Long rowId, String active);
    List<News> getNewsContentParts(Long rowId);
    List<News> getNewsContentParts(Long rowId, String active);
    List<Row> getMenuRows(Long menuId);
    List<Row> getMenuRows(Long menuId, String active);
    List<Row> getMenuRows(String uri, Site site);
    List<Row> getMenuRows(String uri, Site site, String active);
    WidgetTemplate getWidgetTemplate(Long rowId);
    WidgetTemplate getWidgetTemplateByPartContent(Long partContentId);

}