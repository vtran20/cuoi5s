package com.easysoft.ecommerce.service.promotion.impl;

import com.easysoft.ecommerce.model.ConditionClass;
import com.easysoft.ecommerce.model.Promotion;
import com.easysoft.ecommerce.model.PromotionCondition;
import com.easysoft.ecommerce.model.User;
import com.easysoft.ecommerce.model.session.ItemMap;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.service.promotion.AwardSupport;
import com.easysoft.ecommerce.util.PromotionUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

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
public class ProductAward extends AwardSupport implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductAward.class);

    @Override
    public String execute(SessionObject sessionObject, User user, Promotion promotion) throws Exception {
        if (sessionObject != null) {

            List <ItemMap> items = sessionObject.getOrder().getItems();
            for (ItemMap item: items) {
                if (isValidPromoCode(sessionObject, item, user, promotion)) {
                    String type = promotion.getPromoParam1();
                    if (!StringUtils.isEmpty(type)) {
                        Long priceItem = item.getFinalPriceItem();
                        if ("%".equals(type)) {
                            String discount = promotion.getPromoParam2();
                            if (StringUtils.isNumeric(discount)) {
                                Long lDiscount = Long.valueOf(discount);
                                Long disc = (priceItem * lDiscount) / 100;
                                item.setPriceItemPromoDiscount(disc);
                            }
                        } else if ("$".equals(type)) {
                            String discount = promotion.getPromoParam2();
                            if (StringUtils.isNumeric(discount)) {
                                item.setPriceItemPromoDiscount(Long.valueOf(discount));
                            }
                        }
                    }
                } else {
                    //do nothing
                }
            }


        }
        return "";
    }

}
