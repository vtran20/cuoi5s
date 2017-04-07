package com.easysoft.ecommerce.util;

import com.easysoft.ecommerce.model.ConditionClass;
import com.easysoft.ecommerce.model.ProductVariant;
import com.easysoft.ecommerce.model.Promotion;
import com.easysoft.ecommerce.model.PromotionCondition;
import com.easysoft.ecommerce.model.session.ItemMap;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.service.condition.Condition;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This is an utility class that provide utility methods for Promotion.
 * <p/>
 * User: Vu Tran
 * Date: Jul 20, 2010
 * Time: 12:05:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class PromotionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PromotionUtil.class);


    public static void removePromotionDiscount(SessionObject so) {
        so.getOrder().setPromoCode("");
        //Temporary comment this. Remove discount on sub order only. Supporting default promotion.
        // so.getOrder().setShippingDiscountPrice(0l);
        List<ItemMap> items = so.getOrder().getItems();
        for (ItemMap item : items) {
            item.setPriceItemPromoDiscount(0l);
        }
        so.getOrder().setSubPriceDiscountTotal(0l);
    }
}
