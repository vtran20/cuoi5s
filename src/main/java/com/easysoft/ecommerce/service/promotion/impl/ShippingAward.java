package com.easysoft.ecommerce.service.promotion.impl;

import com.easysoft.ecommerce.model.Promotion;
import com.easysoft.ecommerce.model.PromotionClass;
import com.easysoft.ecommerce.model.User;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.condition.ConditionSupport;
import com.easysoft.ecommerce.service.promotion.AwardSupport;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Award:
 * + Free shipping
 * + $ off -> Shipping fee 50000, 30000$ off mean, the fee has to pay to be 20000
 * + % off -> Shipping fee 50000, 10% off mean, the fee has to pay to be 45000
 * 
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShippingAward extends AwardSupport implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingAward.class);

    @Override
    public String execute(SessionObject sessionObject, User user, Promotion promotion) throws Exception {
        if (sessionObject != null) {
            Long shippingPrice = sessionObject.getOrder().getShippingPrice();
            if (isValidPromoCode(sessionObject, null, user, promotion)) {
                String type = promotion.getPromoParam1();
                if (!StringUtils.isEmpty(type)) {
                    if ("FREE".equalsIgnoreCase(type)) {
                        sessionObject.getOrder().setShippingDiscountPrice(shippingPrice);
                    } else if ("%".equals(type)) {//TODO: Correct and test this case later
                        String discount = promotion.getPromoParam2();
                        if (StringUtils.isNumeric(discount)) {
                            Long lDiscount = Long.valueOf(discount);
                            Long disc = (shippingPrice * lDiscount) / 100;
                            sessionObject.getOrder().setShippingDiscountPrice(disc);
                        }
                    } else if ("$".equals(type)) {//TODO: Correct and test this case later
                        String discount = promotion.getPromoParam2();
                        if (StringUtils.isNumeric(discount)) {
                            sessionObject.getOrder().setShippingDiscountPrice(Long.valueOf(discount));
                        }
                    }
                }
            }
        }
        return "";
    }
}
