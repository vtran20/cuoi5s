package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.WidgetTemplateDao;
import com.easysoft.ecommerce.model.WidgetTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WidgetTemplateDaoImpl extends GenericDaoImpl<WidgetTemplate, Long> implements WidgetTemplateDao {

    @Override
    public List<WidgetTemplate> getWidgetTemplateByType(String type, String active) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", type);
        map.put("active", active);
        return findByOrder(map, null, null, "sequence");
    }
}