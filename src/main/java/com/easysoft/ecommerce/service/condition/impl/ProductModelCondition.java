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
 * Expession: ('MODEL' == 'HOSTING')
 * 
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductModelCondition extends ConditionSupport implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductModelCondition.class);

    @Override
    public String execute(SessionObject sessionObject, ItemMap itemMap, User user, String expression) throws Exception {
        expression = expression.replaceFirst("INPUT_MODEL_NUMBER", itemMap.getModelNumber());
        return expression;
    }
}
