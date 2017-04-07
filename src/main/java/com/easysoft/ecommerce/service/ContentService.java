package com.easysoft.ecommerce.service;

import com.easysoft.ecommerce.model.Row;
import com.easysoft.ecommerce.model.SiteMenuPartContent;

import java.util.List;
import java.util.Map;


public interface ContentService {

//    void merge(final String from, final String to, final String[] cc, final String[] bcc, final String subject, final Map map, final String emailTemplate);
    String merge (Map map, Long widgetTemplateId);
//    String merge (SiteMenuPartContent content);
    String merge(Row row);
}

