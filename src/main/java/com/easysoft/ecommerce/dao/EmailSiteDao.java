package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.EmailSite;


public interface EmailSiteDao extends GenericDao<EmailSite, Long> {
    EmailSite getEmailInfor(Long siteId, String emailTemplate);
}