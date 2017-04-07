package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.EmailSiteDao;
import com.easysoft.ecommerce.model.EmailSite;
import org.springframework.stereotype.Repository;

@Repository
public class EmailSiteDaoImpl extends GenericDaoImpl<EmailSite, Long> implements EmailSiteDao {

    /**
     * TODO: Should add language base on the language of the site is setting.
     * @param siteId
     * @param emailTemplate
     * @return
     */
    @Override
    public EmailSite getEmailInfor(Long siteId, String emailTemplate) {
        EmailSite emailSite =  (EmailSite) this.getSessionFactory().getCurrentSession().createQuery("select a from EmailSite a join a.emailTemplate b where a.siteId = :siteId and b.templateName = :templateName").
                setParameter("siteId", siteId).
                setParameter("templateName", emailTemplate).uniqueResult();
        //if email site is null, get the default email.
        if (emailSite == null) {
            emailSite =  (EmailSite) this.getSessionFactory().getCurrentSession().createQuery("select a from EmailSite a join a.emailTemplate b where b.defaultEmail = 'Y' and b.templateName = :templateName").
                    setParameter("templateName", emailTemplate).uniqueResult();
        }

        return emailSite;
    }
}