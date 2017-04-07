package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.EmailPlanDao;
import com.easysoft.ecommerce.model.EmailPlan;
import com.easysoft.ecommerce.model.Site;
import org.hibernate.type.TimestampType;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class EmailPlanDaoImpl extends GenericDaoImpl<EmailPlan, Long> implements EmailPlanDao {

    /**
     * Get email plan that currently use for email marketing plan. It base on start and end date of marketing plan.
     *
     * @param site
     * @return
     */
    @Override
    public EmailPlan getEmailPlan(Site site) {
        List list = this.getSessionFactory().getCurrentSession()
        		.createQuery("select b from EmailPlan b where b.site.id = :siteId and ((b.startDate <= :currentDate and b.endDate >= :currentDate) or b.endDate is null) order by b.startDate desc")
                .setParameter("siteId",site.getId())
                .setParameter("currentDate", new Date(), new TimestampType()).list();

        return (list != null && list.size() >= 1)? (EmailPlan) list.get(0) : null;

    }

}