package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.MenuTemplate;
import com.easysoft.ecommerce.model.Site;

import java.util.List;


public interface MenuTemplateDao extends GenericDao<MenuTemplate, Long> {
    List<MenuTemplate> getMenuTemplatesNotUsed(Site site);
}