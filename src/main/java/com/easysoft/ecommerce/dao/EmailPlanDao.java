package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.EmailPlan;
import com.easysoft.ecommerce.model.Site;

public interface EmailPlanDao extends GenericDao<EmailPlan, Long> {
    EmailPlan getEmailPlan(Site site);
}