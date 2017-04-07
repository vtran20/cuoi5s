package com.easysoft.ecommerce.service.promotion.impl;

import com.easysoft.ecommerce.model.Promotion;
import com.easysoft.ecommerce.model.User;
import com.easysoft.ecommerce.model.session.ItemMap;
import com.easysoft.ecommerce.model.session.OrderMap;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.promotion.AwardSupport;
import com.easysoft.ecommerce.util.PromotionUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

/**
 * Award:
 * + Product
 * + $ off -> product price 50000, 30000$ off mean, the price has to pay to be 20000
 * + % off -> product price 50000, 10% off mean, the price has to pay to be 45000
 * 
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class OrderAward extends AwardSupport implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderAward.class);

    @Override
    public String execute(SessionObject sessionObject, User user, Promotion promotion) throws Exception {
        if (sessionObject != null) {

            OrderMap orderMap = sessionObject.getOrder();
            if (isValidPromoCode(sessionObject, null, user, promotion)) {
                String type = promotion.getPromoParam1();
                if (!StringUtils.isEmpty(type)) {
                    Long subTotal = orderMap.getSubPriceTotal();
                    if ("%".equals(type)) {
                        String discount = promotion.getPromoParam2();
                        if (StringUtils.isNumeric(discount)) {
                            Long lDiscount = Long.valueOf(discount);
                            Long disc = (subTotal * lDiscount) / 100;
                            orderMap.setSubPriceDiscountTotal(disc);
                        }
                    } else if ("$".equals(type)) {
                        String discount = promotion.getPromoParam2();
                        if (StringUtils.isNumeric(discount)) {
                            orderMap.setSubPriceDiscountTotal(Long.valueOf(discount));
                        }
                    }
                }
            } else {
                return "Promo is invalid";
            }
        }
        return "";
    }
}
