package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.EmailTemplateDao;
import com.easysoft.ecommerce.model.EmailTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EmailTemplateDaoImpl extends GenericDaoImpl<EmailTemplate, Long> implements EmailTemplateDao {
    @Override
    public EmailTemplate getEmailTemplate(String templateName, String locale) {
        EmailTemplate emailTemplate =  (EmailTemplate) this.getSessionFactory().getCurrentSession().createQuery("select a from EmailTemplate a where a.templateName = :templateName and a.locale = :locale").
                setParameter("templateName", templateName).
                setParameter("locale", locale).uniqueResult();
        if (emailTemplate == null) {
            emailTemplate =  (EmailTemplate) this.getSessionFactory().getCurrentSession().createQuery("select a from EmailTemplate a where a.templateName = :templateName and a.defaultEmail = 'Y'").
                    setParameter("templateName", emailTemplate).uniqueResult();
        }

        return emailTemplate;
    }

    @Override
    public EmailTemplate getDefaultEmailTemplate(String templateName) {
        return getEmailTemplate(templateName, "NO_LOCALE");
    }
}