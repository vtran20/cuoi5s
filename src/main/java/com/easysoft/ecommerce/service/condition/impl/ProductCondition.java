package com.easysoft.ecommerce.service.condition.impl;

import com.easysoft.ecommerce.model.User;
import com.easysoft.ecommerce.model.session.ItemMap;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.condition.ConditionSupport;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

/**
 * Expession: SUB_TOTAL_PRICE > 500000
 * <p/>
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductCondition extends ConditionSupport implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCondition.class);

    @Override
    public String execute(SessionObject sessionObject, ItemMap itemMap, User user, String expression) throws Exception {
        String exp = "";
        if (sessionObject != null) {
            List<ItemMap> items = sessionObject.getOrder().getItems();
            for (ItemMap item : items) {
                if (!StringUtils.isEmpty(expression)) {
                    if ("".equals(exp)) {
                        if (item.getId() > 0) {
                            exp = expression.replaceAll("productId", String.valueOf(item.getId()));
                        } else {
                            //do nothing
                        }
                    } else {
                        if (item.getId() > 0) {
                            exp += " OR " + expression.replaceAll("productId", String.valueOf(item.getId()));
                        } else {
                            //do nothing
                        }
                    }
                }
            }
        }
        return exp;
    }

//    @Override
//    public String execute(ItemMap item, String expression) throws Exception {
//        String exp = "";
//        if (item.getId() > 0) {
//            exp = expression.replaceAll("productId", String.valueOf(item.getId()));
//        } else {
//            exp = "false";
//        }
//        return exp;
//    }
}
