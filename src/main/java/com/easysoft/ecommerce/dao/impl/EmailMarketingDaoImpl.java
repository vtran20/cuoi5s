package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.EmailMarketingDao;
import com.easysoft.ecommerce.model.EmailMarketing;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmailMarketingDaoImpl extends GenericDaoImpl<EmailMarketing, Long> implements EmailMarketingDao {

    @Override
    public List<EmailMarketing> getEmailMarketings(String optin, long marketingPlan, int numberRecord) {
        Query query = null;
        if (!StringUtils.isEmpty(optin)) {
            query =  getSessionFactory().getCurrentSession().createQuery(
                    "select o from EmailMarketing o where o.optin = :optin and o.marketingOrder != :marketingOrder")
                    .setParameter("optin", optin)
                    .setParameter("marketingOrder", marketingPlan);
            if (numberRecord > 0) {
                query.setMaxResults(numberRecord);
            }
        } else {
            query =  getSessionFactory().getCurrentSession().createQuery(
                    "select o from EmailMarketing o where o.marketingOrder != :marketingOrder")
                    .setParameter("marketingOrder", marketingPlan);
            if (numberRecord > 0) {
                query.setMaxResults(numberRecord);
            }
        }
        return query.list();
    }
}