package com.easysoft.ecommerce.service.condition.impl;

import com.easysoft.ecommerce.controller.SessionUtil;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.User;
import com.easysoft.ecommerce.model.session.ItemMap;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.service.condition.ConditionSupport;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Expession: (PARTNER_TYPE==1 and 'PARTNER_STATUS'=='Y')
 * 
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class PersonalPartnerCondition extends ConditionSupport implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonalPartnerCondition.class);

    @Override
    public String execute(SessionObject sessionObject, ItemMap itemMap, User user, String expression) throws Exception {
        if (sessionObject != null) {
            if (!StringUtils.isEmpty(expression) && user != null && "Y".equals(user.getPartnerStatus())) {
                expression = expression.replaceFirst("PARTNER_TYPE", user.getPartner()+"");
                expression = expression.replaceFirst("PARTNER_STATUS", user.getPartnerStatus());
            }
        }
        return expression;
    }
}
