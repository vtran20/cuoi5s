package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.EmailMarketing;

import java.util.List;

public interface EmailMarketingDao extends GenericDao<EmailMarketing, Long> {

    List<EmailMarketing> getEmailMarketings (String optin, long marketingPlan, int numberRecord);

}