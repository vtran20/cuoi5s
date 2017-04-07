package com.easysoft.ecommerce.service.condition.impl;

import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.ProductVariant;
import com.easysoft.ecommerce.model.User;
import com.easysoft.ecommerce.model.session.ItemMap;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.service.component.CommerceSupport;
import com.easysoft.ecommerce.service.condition.ConditionSupport;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

/**
 * Expession: (SUB_TOTAL_PRICE - SUB_TOTAL_PRICE_DISCOUNT) >= 500000
 * 
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class SubOrderTotalCondition extends ConditionSupport implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubOrderTotalCondition.class);

    @Override
    public String execute(SessionObject sessionObject, ItemMap itemMap, User user, String expression) throws Exception {
        if (sessionObject != null) {
            Long subPriceTotal = sessionObject.getOrder().getSubPriceTotal();
            Long subPriceDiscountTotal = sessionObject.getOrder().getSubPriceDiscountTotal();
            if (!StringUtils.isEmpty(expression)) {
                //using replaceFirst to fix an issue duplicate SUB_TOTAL_PRICE name, ex: "(SUB_TOTAL_PRICE - SUB_TOTAL_PRICE_DISCOUNT) >= 500000"
                if (subPriceTotal == null) {
                    expression = expression.replaceFirst("SUB_TOTAL_PRICE", "0");
                } else {
                    expression = expression.replaceFirst("SUB_TOTAL_PRICE", subPriceTotal.toString());
                }
                if (subPriceDiscountTotal == null) {
                    expression = expression.replaceFirst("SUB_TOTAL_PRICE_DISCOUNT", "0");
                } else {
                    expression = expression.replaceFirst("SUB_TOTAL_PRICE_DISCOUNT", subPriceDiscountTotal.toString());
                }
            }
        }
        return expression;
    }
}
