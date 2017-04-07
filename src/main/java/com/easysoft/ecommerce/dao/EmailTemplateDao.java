package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.EmailTemplate;


public interface EmailTemplateDao extends GenericDao<EmailTemplate, Long> {
    EmailTemplate getEmailTemplate(String templateName, String locale);
    EmailTemplate getDefaultEmailTemplate(String templateName);
}