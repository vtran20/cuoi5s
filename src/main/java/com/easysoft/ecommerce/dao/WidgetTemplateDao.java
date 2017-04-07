package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.WidgetTemplate;

import java.util.List;


public interface WidgetTemplateDao extends GenericDao<WidgetTemplate, Long> {

    List<WidgetTemplate> getWidgetTemplateByType (String type, String active);
}